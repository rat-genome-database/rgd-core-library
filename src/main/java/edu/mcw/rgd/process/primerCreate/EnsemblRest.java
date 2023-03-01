package edu.mcw.rgd.process.primerCreate;

import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.process.FileDownloader;
import edu.mcw.rgd.process.Utils;

public class EnsemblRest {

    public static String REST_SERVER = "http://grch37.rest.ensembl.org";

    public String retrieveSequence(String chr, int start, int stop, String strand, int speciesKey) throws Exception{

        String species = SpeciesType.getCommonName(speciesKey);

        //String ext = "/sequence/region/human/X:1000000..1000100:1?content-type=text/plain";
        String ext = "/sequence/region/"+species+"/"+chr+":"+start+".."+stop
                +":"+strand+"?content-type=text/plain";

        FileDownloader downloader = new FileDownloader();
        downloader.setExternalFile(REST_SERVER+ext);
        downloader.setLocalFile("/tmp/"+System.currentTimeMillis());
        String localFile = downloader.download();
        String seq = Utils.readFileAsString(localFile);

        // validate sequence size
        if( seq.length()<(stop-start+1) )
            throw new Exception("Server "+REST_SERVER+" returned sequence of size "+seq.length()+"; seq size "+(stop-start+1)+" was expected");

        return seq;
    }
}
