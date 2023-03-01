package edu.mcw.rgd.process;

import org.apache.commons.net.ftp.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.zip.GZIPOutputStream;

/**
 * @author mtutaj, 12/28/2010

 * Downloads a file from an external source (through http or ftp), and saves the file locally,
 * or downloads it into a in-memory byte array.
 * <p>
 * You can specify options so local file copy could contain the date stamp in the file name,
 * either before or after the file name. Also you can force the local file to be stored compressed using gzip format.
 * <p>
 * If an exception occurs during the download, the download process is put to sleep for some time
 * and when resumed it retries the download. A fixed number of retries is allowed. Between successive retrials
 * the sleep period is doubled.
 * <p>
 * Ftp download is performed in passive mode.
 */
public class FileDownloader {

    // url to the external file to be downloaded
    private String externalFile;

    // local path where the data will be stored
    private String localFile;

    // content of the resource downloaded from external site into in memory byte array
    private ByteArrayOutputStream inMemoryContent;

    // the local copy of the file is written to the disk using compression
    private boolean useCompression;

    // if true, the local file name will contain current date at the end;
    // f.e. if localFile is 'data/gene.obo', the name of real file created will be 'data/gene.obo_YYYYMMDD'
    //      where YYYY-year, MM-month, 'DD'-day
    private boolean appendDateStamp = false;

    // if true, the local file name will contain current date at the beginning;
    // f.e. if localFile is 'data/gene.obo', the name of real file created will be 'data/YYYYMMDD_gene.obo'
    //      where YYYY-year, MM-month, 'DD'-day
    private boolean prependDateStamp = false;

    // how long to wait in seconds until next download attempt will be made
    // note: 2nd attempt uses doubled interval, 3rd attempt uses quadrupled interval, etc
    private int downloadRetryInterval = 60;

    // how many download attempts will be made
    private int maxRetryCount = 8;  // eight attempt will be made after about 2hr sleep period

    // timeout for waiting for the connection to be established
    private int connectionTimeout = 20000; // 20 sec

    // timeout for waiting for the data (in milliseconds)
    private int soTimeout = 20000; // 20 sec

    private boolean doNotUseHttpClient = false;

    // local log4j logger
    private Logger log = LogManager.getLogger(FileDownloader.class);

    public String buildLocalFileName() {

        // create output file name
        String outFileName = localFile;
        if( appendDateStamp ) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            outFileName += "_"+sdf.format(new java.util.Date());
        }
        if( prependDateStamp ) {
            // find the position to prepend
            int pos = outFileName.lastIndexOf('/');
            if( pos < 0 )
                pos = outFileName.lastIndexOf('\\');

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateAsString = sdf.format(new java.util.Date())+"_";
            if( pos > 0 )
                outFileName = outFileName.substring(0, pos+1) + dateAsString + outFileName.substring(pos+1);
            else
                outFileName = dateAsString + outFileName;
        }
        if( useCompression ) {
            if( !localFile.endsWith(".gz") )
                outFileName += ".gz";
        }
        return outFileName;
    }

    /**
     * if the local file does not exist, then the external file is downloaded into a local file
     * else this function does nothing
     * @return file path to local file
     * @throws Exception when unexpected things happen
     */
    public String downloadNew() throws Exception {
        if( isLocalFileUpToDate() )
            return buildLocalFileName();
        return download();
    }

    /**
     * downloads the external file into a local file or into a memory buffer
     * (if a local file name is specified, the resource is downloaded to a local file,
     *  otherwise the resource is download into an in-memory byte buffer)
     * @return file path to locally created file or file content if the file was downloaded into memory
     * @throws Exception when unexpected things happen
     */
    public String download() throws Exception {

        if( externalFile==null )
            return null;

        // shall we use ftp to download the file?
        boolean useFtp = externalFile.startsWith("ftp:");

        // handle local files
        boolean useLocal = externalFile.startsWith("file:///");

        // create output file name
        String outFileName = buildLocalFileName();

        int retryInterval = downloadRetryInterval; // double sleep interval between download attempts
        for( int i=0; i<this.maxRetryCount; i++, retryInterval *= 2 ) {
            try {
                if( useFtp ) {
                    downloadFtp(outFileName);
                } else if( useLocal ) {
                    downloadLocal(outFileName);
                } else {
                    if( isDoNotUseHttpClient() ) {
                        downloadUrl(outFileName);
                    } else {
                        downloadUrlViaHttpClient(outFileName);
                    }
                }

                if( outFileName==null ) {
                    return inMemoryContent.toString("UTF-8");
                } else {
                    return outFileName;
                }
            }
            catch(Exception e) {
                log.warn("Failed to retrieve file "+externalFile);
                log.warn(e.getMessage()+" "+e.toString());
            }
            // retry the connection
            log.info("download will be attempted in "+retryInterval+" seconds");
            Thread.sleep(retryInterval * 1000l);
            log.info("resuming download of "+externalFile);
       }

       String msg = "reached maximum number of download retrials -- permanent error";
       log.fatal(msg);
       throw new PermanentDownloadErrorException(msg);
    }

    /**
     * list files for the current working directory
     * @return list of file names
     * @throws Exception when unexpected things happen
     */
    public String[] listFiles() throws Exception {
        log.info("Listing contents of " + externalFile);

        // we must break the url into server part and the rest
        int slashPos = externalFile.indexOf('/', 6); // look for '/' pos skipping initial 'ftp://'
        if( slashPos<0 )
            throw new Exception("malformed ftp url");

        String ftpServer = externalFile.substring(6, slashPos);
        String ftpFile = externalFile.substring(slashPos);

        FTPClient client = new FTPClient();

        try {
            // try to connect and log-in as anonymous to ftp server
            doFtpConnect(client, ftpServer);

            client.setSoTimeout(soTimeout);
            client.setConnectTimeout(connectionTimeout);

            // get list of file names in the specified directory
            FTPFile[] ftpFiles = client.listFiles(ftpFile, new FTPFileFilter() {
                public boolean accept(FTPFile ftpFile) {
                    return ftpFile.isFile();
                }
            });
            String[] fileNames = new String[ftpFiles.length];
            for( int i=0; i<ftpFiles.length; i++ ) {
                fileNames[i] = ftpFiles[i].getName();
            }

            // return the list of file names
            return fileNames;
        }
        finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("ftp close/disconnect: "+e.getMessage()+" "+e.toString());
            }
        }
    }

    void downloadFtp(String outFileName) throws Exception {

        log.debug("Downloading file " + externalFile);

        // we must break the url into server part and the rest
        int slashPos = externalFile.indexOf('/', 6); // look for '/' pos skipping initial 'ftp://'
        if( slashPos<0 )
            throw new Exception("malformed ftp url");

        String ftpServer = externalFile.substring(6, slashPos);
        String ftpFile = externalFile.substring(slashPos);

        FTPClient client = new FTPClient();
        OutputStream os = null;

        try {
            // try to connect and log-in as anonymous to ftp server
            doFtpConnect(client, ftpServer);

            // create local file
            os = createOutputFile(outFileName);

            // download file from FTP server
            log.debug("Retrieving file " + ftpFile);
            if( !client.retrieveFile(ftpFile, os) ) {
                int reply = client.getReplyCode();
                if(!FTPReply.isPositiveCompletion(reply)) {
                    log.warn(client.getReplyString());
                }
                throw new Exception("Failed to retrieve file " + ftpFile);
            }
        }
        finally {
            try {
                client.disconnect();
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("ftp close/disconnect: "+e.getMessage()+" "+e.toString());
            }
        }

        log.info("Downloaded "+ftpFile+" to "+outFileName);
    }

    void downloadUrlViaHttpClient(String outFileName) throws Exception {
        log.info("Downloading file " + externalFile);

        HttpClient client = new DefaultHttpClient();
        HttpParams params = client.getParams();
        HttpConnectionParams.setSoTimeout(params, soTimeout);
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);

        OutputStream os = null;
        try {
            HttpResponse response = client.execute(new HttpGet(externalFile));
            HttpEntity entity = response.getEntity();

            os = createOutputFile(outFileName);
            entity.writeTo(os);
        }
        finally {
            try { if(os!=null) os.close(); } catch(IOException e){}
            client.getConnectionManager().shutdown();
        }

        log.info("Downloaded " + externalFile + " to " + outFileName);
    }

    void downloadUrl(String outFileName) throws Exception {
        log.info("Downloading file " + externalFile);

        try( OutputStream os = createOutputFile(outFileName) ) {

            URL url = new URL(externalFile);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (BufferedInputStream in = new BufferedInputStream(conn.getInputStream()) ) {
                copyData(in, os);
            }
        }

        log.info("Downloaded " + externalFile + " to " + outFileName);
    }

    void downloadLocal(String outFileName) throws Exception {
        log.info("Downloading file " + externalFile);

        String fileName = externalFile.substring(7); // strip 'file://' from external file path

        try( BufferedInputStream is = new BufferedInputStream(new FileInputStream(fileName));
             OutputStream os = createOutputFile(outFileName) ) {

            copyData(is, os);
        }

        log.info("Downloaded " + externalFile + " to " + outFileName);
    }

    private void copyData(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[4096];
        int wasRead;
        while( (wasRead=is.read(buf, 0, buf.length))>0 ) {
            os.write(buf, 0, wasRead);
        }
    }

    private OutputStream createOutputFile(String outFileName) throws IOException {

        OutputStream out;
        // in memory output file
        if( outFileName==null ) {
            out = inMemoryContent = new ByteArrayOutputStream();
        } else {
            // create buffered output file
            out = new BufferedOutputStream(new FileOutputStream(outFileName));
        }

        // compress the output file if necessary
        if( isUsingCompression() ) {
            // if the input file is already compressed, there will be no need to double compress it
            // we assume, if the external file contain .gz extension, it is already gzip compressed
            if( !externalFile.endsWith(".gz") )
                out = new GZIPOutputStream(out);
        }

        return out;
    }

    protected void doFtpConnect(FTPClient client, String ftpServer) throws Exception {
        
        // try to connect to ftp server
        log.debug("Connecting to server "+ftpServer);
        client.connect(ftpServer);
        log.debug("Connected to " + ftpServer);

        // verify reply code
        log.debug(client.getReplyString());
        int reply = client.getReplyCode();
        if(!FTPReply.isPositiveCompletion(reply)) {
            throw new Exception("FTP server refused connection.");
        }

        // anonymous login
        log.debug("Log in to ftp server as anonymous");
        if( client.login("anonymous", "rgddata@mcw.edu") )
            log.debug("Logged in to ftp server as anonymous");
        else {
            reply = client.getReplyCode();
            if(!FTPReply.isPositiveCompletion(reply)) {
                log.warn(client.getReplyString());
                throw new Exception("FTP server refused log in.");
            }
        }

        // enter binary mode
        client.setFileType(FTP.BINARY_FILE_TYPE);

        // enter passive mode
        client.enterLocalPassiveMode();
    }

    /**
     * convenience method: check if there is non-empty local file available in local directory
     * based on the current settings
     * @return true if there is a local file already on disk
     */
    public boolean isLocalFileUpToDate() {

        String localFileName = buildLocalFileName();
        File localFile = new File(localFileName);
        return localFile.exists() && localFile.length() != 0;
    }

    /**
     * external file to be downloaded
     */
    public String getExternalFile() {
        return externalFile;
    }

    /**
     * specification of external file to be downloaded
     * @param externalFile url (http or ftp) of the external file to be downloaded
     */
    public void setExternalFile(String externalFile) {
        this.externalFile = externalFile;
    }

    /**
     * local path where the downloaded data is stored
     */
    public String getLocalFile() {
        return localFile;
    }

    /**
     * local path where the data will be stored
     * @param localFile path to the local file
     */
    public void setLocalFile(String localFile) {
        this.localFile = localFile;
    }

    /**
     * the local copy of the file is using compression -- the file extension will be .gz
     * @return true if the locally stored file is using compression
     */
    public boolean isUsingCompression() {
        return useCompression;
    }

    /**
     * the downloaded file will be stored locally using compression
     * @param useCompression store the file contents by using compression
     */
    public void setUseCompression(boolean useCompression) {
        this.useCompression = useCompression;
    }

    /**
     * test if date stamp is appended to the local file name
     * <p>
     * if true, the local file name will contain current date at the end; <br/>
     * f.e. if localFile is 'data/gene.obo', the name of real file created will be 'data/gene.obo_YYYYMMDD',
     *      where YYYY-year, MM-month, 'DD'-day
     * @return true if date stamp is appended to the local file name
     */
    public boolean isAppendDateStamp() {
        return appendDateStamp;
    }

    /**
     * control whether date stamp is appended to the local file name
     * <p>
     * if true, the local file name will contain current date at the end; <br/>
     * f.e. if localFile is 'data/gene.obo', the name of real file created will be 'data/gene.obo_YYYYMMDD',
     *      where YYYY-year, MM-month, 'DD'-day
     * @param appendDateStamp if true, the date stamp is appended to the local file name
     */
    public void setAppendDateStamp(boolean appendDateStamp) {
        this.appendDateStamp = appendDateStamp;
    }

    /**
     * test if date stamp is prepended to the local file name
     * <p>
     * if true, the local file name will contain current date at the beginning; <br/>
     * f.e. if localFile is 'data/gene.obo', the name of real file created will be 'data/YYYYMMDD_gene.obo',
     *      where YYYY-year, MM-month, 'DD'-day
     * @return true if date stamp is prepended to the local file name
     */
    public boolean isPrependDateStamp() {
        return prependDateStamp;
    }

    /**
     * control whether date stamp is prepended to the local file name
     * <p>
     * if true, the local file name will contain current date at the beginning; <br/>
     * f.e. if localFile is 'data/gene.obo', the name of real file created will be 'data/YYYYMMDD_gene.obo',
     *      where YYYY-year, MM-month, 'DD'-day
     * @param prependDateStamp if true, the date stamp is prepended to the local file name
     */
    public void setPrependDateStamp(boolean prependDateStamp) {
        this.prependDateStamp = prependDateStamp;
    }

    /**
     * how long to wait in seconds until next download attempt will be made
     * <p>
     * The default is 60 seconds.
     * @return interval of seconds
     */
    public int getDownloadRetryInterval() {
        return downloadRetryInterval;
    }

    /**
     * how long to wait in seconds until next download attempt will be made
     * <p>
     * The default is 60 seconds.
     * <p>
     * note: 2nd attempt uses doubled interval, 3rd attempt uses quadrupled interval, etc
     * @param downloadRetryInterval interval in seconds
     */
    public void setDownloadRetryInterval(int downloadRetryInterval) {
        this.downloadRetryInterval = downloadRetryInterval;
    }

    /**
     * how many download retries will be made; between retry attempts a wait is made, with duration specified by downloadRetryInterval property
     * @return maximum count of download retries
     */
    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    /**
     * change maximum count of download retries
     * @param maxRetryCount new maximum count of download retries<p>Note: if this parameter is less than 0 or greater than 10000
     * then it won't be changed</p>
     * @return new value of maxRetryCount parameter
     */
    public int setMaxRetryCount(int maxRetryCount) {
        if( maxRetryCount >=0  && maxRetryCount<10000 )
            this.maxRetryCount = maxRetryCount;
        return maxRetryCount;
    }

    /**
     * Returns the default socket timeout (SO_TIMEOUT) in milliseconds which is the timeout for waiting for data.
     * A timeout value of zero is interpreted as an infinite timeout.
     * The default SO_TIMEOUT is 20 seconds
     * @return SO_TIMEOUT in milliseconds
     */
    public int getSoTimeout() {
        return soTimeout;
    }

    /**
     * Sets the default socket timeout (SO_TIMEOUT) in milliseconds which is the timeout for waiting for data.
     * A timeout value of zero is interpreted as an infinite timeout.
     * @param soTimeout SO_TIMEOUT in milliseconds
     */
    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public boolean isDoNotUseHttpClient() {
        return doNotUseHttpClient;
    }

    /**
     * by default HttpClient from apache commons is used to handle FTP and HTTP requests;
     * however, in some cases it does not work (f.e. when downloading files from PharmGKB site);
     * @param doNotUseHttpClient if true java URL and URLConnection classes will be used to handle HTTP requests;
     *                           otherwise HttpClient will be used
     */
    public void setDoNotUseHttpClient(boolean doNotUseHttpClient) {
        this.doNotUseHttpClient = doNotUseHttpClient;
    }

    /**
     * reached maximum number of retrials -- permanent error
     */
    public class PermanentDownloadErrorException extends Exception {

        public PermanentDownloadErrorException(String msg) {
            super(msg);
        }
    }
}
