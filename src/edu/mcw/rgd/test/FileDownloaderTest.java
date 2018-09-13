package edu.mcw.rgd.test;

import edu.mcw.rgd.process.FileDownloader;
import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Feb 4, 2011
 * Time: 11:28:33 AM
 */
public class FileDownloaderTest extends TestCase {

    public void testAll() throws Exception {

        testListFtpDir();
        testHttpDownload();
        testDownloadGzFile();
    }

    public void testHttpDownload() throws Exception {

        FileDownloader downloader = new FileDownloader();
        String url = "http://compbio.charite.de/hudson/job/hpo.annotations.monthly/lastStableBuild/artifact/annotation/OMIM_ALL_FREQUENCIES_diseases_to_genes_to_phenotypes.txt";
        downloader.setExternalFile(url);
        downloader.setLocalFile("/tmp/aha");
        System.out.println("downloaded to file "+downloader.download());

        downloader.setExternalFile("http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=gene&tool=rgd_eg_pipeline&email=mtutaj@mcw.edu&retmode=xml&id=362778,24223");
        downloader.setLocalFile("/tmp/aha");
        System.out.println("downloaded to file "+downloader.download());

    }

    /**
     * use FileDownloader to list files in a directory on a ftp server
     * @throws Exception
     */
    public void testListFtpDir() throws Exception {

        FileDownloader downloader = new FileDownloader();
        String url = "ftp://ftp.ncbi.nih.gov/refseq/removed/";
        downloader.setExternalFile(url);
        String[] fileNames = downloader.listFiles();

        downloader.setExternalFile("ftp://ftp.ncbi.nih.gov/repository/UniSTS/");
        fileNames = downloader.listFiles();
        int xmlFileCount = 0;
        for( String fname: fileNames ) {
            if( fname.toLowerCase().contains(".xml") ) {
                xmlFileCount++;
                System.out.println("ACCEPT: "+fname);
            }
            else
                System.out.println("IGNORE: "+fname);
        }

        assertTrue("No xml files found", xmlFileCount > 0);
    }

    public void testDownloadGzFile() throws Exception {

        FileDownloader downloader = new FileDownloader();

        downloader.setExternalFile("ftp://rgd.mcw.edu/pub/ontology/rat_strain/rat_strain.obo");
        downloader.setLocalFile("/tmp/rat_strain.obo");
        downloader.setUseCompression(true); // enable gzip compression
        downloader.setPrependDateStamp(true);
        String localFile = downloader.download();
        System.out.println("File "+downloader.getExternalFile()+" stored as "+localFile);
        // the obo file should be stored gzipped

        downloader.setExternalFile("ftp://ftp.ncbi.nih.gov/repository/UniSTS/unists.xml.59.gz");
        String fname = "/tmp/59.xml.gz";
        downloader.setLocalFile(fname);
        downloader.setUseCompression(true); // enable gzip compression
        localFile = downloader.download();
        System.out.println("File "+downloader.getExternalFile()+" stored as "+localFile);
        // you should get file copies as-is, because the input file was already compressed

    }
}
