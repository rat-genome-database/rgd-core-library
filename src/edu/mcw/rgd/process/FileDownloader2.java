package edu.mcw.rgd.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

/**
 * Downloads a file from an external HTTP(S) source or a local <code>file:///</code>
 * URL and saves it locally, or downloads it into an in-memory byte array.
 * <p>
 * You can specify options so the local file copy includes a date stamp before or
 * after the file name. You can also force the local copy to be stored gzip-compressed.
 * <p>
 * On a download failure the call is retried after a sleep period. A fixed number of
 * retries is allowed; between successive retries the sleep period is doubled.
 * <p>
 * This is the JDK-only successor to {@link FileDownloader}. It replaces Apache
 * <code>httpclient</code> with the JDK <code>java.net.http.HttpClient</code> and
 * does <b>not</b> support the <code>ftp://</code> protocol &mdash; callers passing
 * an ftp URL will get an {@link UnsupportedOperationException}.
 *
 * @author mtutaj
 */
public class FileDownloader2 {

    /** url to the external file to be downloaded */
    private String externalFile;

    /** local path where the data will be stored */
    private String localFile;

    /** content of the resource downloaded into an in-memory byte array (when localFile is null) */
    private ByteArrayOutputStream inMemoryContent;

    /** if true, the local file is written to disk using gzip compression */
    private boolean useCompression;

    /**
     * if true, the local file name has the current date appended (YYYYMMDD);
     * f.e. if localFile is 'data/gene.obo', the real file name will be 'data/gene.obo_YYYYMMDD'
     */
    private boolean appendDateStamp = false;

    /**
     * if true, the local file name has the current date prepended (YYYYMMDD);
     * f.e. if localFile is 'data/gene.obo', the real file name will be 'data/YYYYMMDD_gene.obo'
     */
    private boolean prependDateStamp = false;

    /** how long to wait in seconds until the next download attempt; doubled on each retry */
    private int downloadRetryInterval = 60;

    /** how many download attempts will be made */
    private int maxRetryCount = 8;

    /** timeout for waiting for the connection to be established, in milliseconds */
    private int connectionTimeout = 20000;

    /** timeout for waiting for data, in milliseconds */
    private int soTimeout = 20000;

    private static final Logger log = LogManager.getLogger(FileDownloader2.class);

    /**
     * Build the actual local file name based on the configured base name plus any
     * date-stamp and compression options.
     */
    public String buildLocalFileName() {

        String outFileName = localFile;
        if( appendDateStamp ) {
            outFileName += "_" + new SimpleDateFormat("yyyyMMdd").format(new Date());
        }
        if( prependDateStamp ) {
            int pos = Math.max(outFileName.lastIndexOf('/'), outFileName.lastIndexOf('\\'));
            String dateAsString = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "_";
            outFileName = pos > 0
                    ? outFileName.substring(0, pos + 1) + dateAsString + outFileName.substring(pos + 1)
                    : dateAsString + outFileName;
        }
        if( useCompression && !localFile.endsWith(".gz") ) {
            outFileName += ".gz";
        }
        return outFileName;
    }

    /**
     * If the local file already exists and is non-empty, returns its name without
     * downloading anything; otherwise downloads the external file into the local file.
     *
     * @return path to local file
     * @throws Exception when unexpected things happen
     */
    public String downloadNew() throws Exception {
        if( isLocalFileUpToDate() ) {
            return buildLocalFileName();
        }
        return download();
    }

    /**
     * Downloads the external file into a local file or into an in-memory byte buffer
     * (if no local file name is configured, the resource is downloaded into memory).
     *
     * @return path to the locally created file, or the file content when downloaded into memory
     * @throws Exception when unexpected things happen
     */
    public String download() throws Exception {

        if( externalFile == null ) {
            return null;
        }

        if( externalFile.startsWith("ftp:") ) {
            throw new UnsupportedOperationException(
                    "FTP is not supported by FileDownloader2; please use FileDownloader or migrate the URL to HTTP(S)");
        }

        boolean useLocal = externalFile.startsWith("file:///");
        String outFileName = buildLocalFileName();

        int retryInterval = downloadRetryInterval;
        for( int i = 0; i < maxRetryCount; i++, retryInterval *= 2 ) {
            try {
                if( useLocal ) {
                    downloadLocal(outFileName);
                } else {
                    downloadHttp(outFileName);
                }

                return outFileName == null
                        ? inMemoryContent.toString(StandardCharsets.UTF_8)
                        : outFileName;
            } catch( Exception e ) {
                log.warn("Failed to retrieve file " + externalFile);
                log.warn(e.getMessage() + " " + e);
            }
            log.info("download will be attempted in " + retryInterval + " seconds");
            Thread.sleep(retryInterval * 1000L);
            log.info("resuming download of " + externalFile);
        }

        String msg = "reached maximum number of download retrials -- permanent error";
        log.fatal(msg);
        throw new PermanentDownloadErrorException(msg);
    }

    void downloadHttp(String outFileName) throws Exception {

        log.info("Downloading file " + externalFile);

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(connectionTimeout))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(externalFile))
                .timeout(Duration.ofMillis(soTimeout))
                .header("User-Agent", "Mozilla/5.0")
                .GET()
                .build();

        if( outFileName == null ) {
            // in-memory mode
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            checkStatus(response.statusCode());
            try( OutputStream os = createOutputFile(null) ) {
                os.write(response.body());
            }
        } else if( useCompression && !externalFile.endsWith(".gz") ) {
            // compress on the fly
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            checkStatus(response.statusCode());
            try( InputStream in = response.body();
                 OutputStream os = createOutputFile(outFileName) ) {
                in.transferTo(os);
            }
        } else {
            // stream directly to disk
            Path target = Path.of(outFileName);
            if( target.getParent() != null ) {
                Files.createDirectories(target.getParent());
            }
            HttpResponse<Path> response = client.send(request,
                    HttpResponse.BodyHandlers.ofFile(target,
                            StandardOpenOption.CREATE,
                            StandardOpenOption.WRITE,
                            StandardOpenOption.TRUNCATE_EXISTING));
            checkStatus(response.statusCode());
        }

        log.info("Downloaded " + externalFile + " to " + outFileName);
    }

    void downloadLocal(String outFileName) throws Exception {

        log.info("Downloading file " + externalFile);
        String fileName = externalFile.substring(7); // strip 'file://' prefix

        try( BufferedInputStream is = new BufferedInputStream(new FileInputStream(fileName));
             OutputStream os = createOutputFile(outFileName) ) {
            is.transferTo(os);
        }

        log.info("Downloaded " + externalFile + " to " + outFileName);
    }

    private void checkStatus(int statusCode) throws IOException {
        if( statusCode < 200 || statusCode >= 300 ) {
            throw new IOException("HTTP " + statusCode + " from " + externalFile);
        }
    }

    private OutputStream createOutputFile(String outFileName) throws IOException {

        OutputStream out;
        if( outFileName == null ) {
            out = inMemoryContent = new ByteArrayOutputStream();
        } else {
            out = new BufferedOutputStream(new FileOutputStream(outFileName));
        }
        // if the source is already gzip-compressed, do not double-compress it
        if( useCompression && !externalFile.endsWith(".gz") ) {
            out = new GZIPOutputStream(out);
        }
        return out;
    }

    /**
     * convenience method: check if a non-empty local file is already present based
     * on the current settings
     */
    public boolean isLocalFileUpToDate() {
        File f = new File(buildLocalFileName());
        return f.exists() && f.length() != 0;
    }

    public String getExternalFile() {
        return externalFile;
    }

    public void setExternalFile(String externalFile) {
        this.externalFile = externalFile;
    }

    public String getLocalFile() {
        return localFile;
    }

    public void setLocalFile(String localFile) {
        this.localFile = localFile;
    }

    public boolean isUsingCompression() {
        return useCompression;
    }

    public void setUseCompression(boolean useCompression) {
        this.useCompression = useCompression;
    }

    public boolean isAppendDateStamp() {
        return appendDateStamp;
    }

    public void setAppendDateStamp(boolean appendDateStamp) {
        this.appendDateStamp = appendDateStamp;
    }

    public boolean isPrependDateStamp() {
        return prependDateStamp;
    }

    public void setPrependDateStamp(boolean prependDateStamp) {
        this.prependDateStamp = prependDateStamp;
    }

    public int getDownloadRetryInterval() {
        return downloadRetryInterval;
    }

    public void setDownloadRetryInterval(int downloadRetryInterval) {
        this.downloadRetryInterval = downloadRetryInterval;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    /**
     * Change the maximum count of download retries.
     *
     * @param maxRetryCount new maximum count of download retries; ignored unless 0..9999
     * @return the supplied value (for fluent chaining), regardless of whether it was applied
     */
    public int setMaxRetryCount(int maxRetryCount) {
        if( maxRetryCount >= 0 && maxRetryCount < 10000 ) {
            this.maxRetryCount = maxRetryCount;
        }
        return maxRetryCount;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    /** Thrown when the maximum number of download retries has been exhausted. */
    public static class PermanentDownloadErrorException extends Exception {

        public PermanentDownloadErrorException(String msg) {
            super(msg);
        }
    }
}
