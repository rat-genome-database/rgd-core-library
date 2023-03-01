package edu.mcw.rgd.process;

import edu.mcw.rgd.dao.impl.TranscriptDAO;
import edu.mcw.rgd.datamodel.MapData;
import edu.mcw.rgd.datamodel.Transcript;
import edu.mcw.rgd.datamodel.TranscriptFeature;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 5/29/15
 * Time: 10:19 AM
 * <p>
 * utility classes for handling DNA sequence
 */
public class SeqUtils {

    /**
     * given transcript model and a reference sequence, translate the transcript CDS into protein
     * @param transcript transcript model
     * @param mapKey map key for the assembly
     * @param useWebService if true, RGD web service is used to get the chunk of reference sequence;
     *        if false, reference sequence must be available locally in a well known location
     *        (for details, see the code of FastaParser)
     * @return SeqUtilsResult object
     * @throws Exception
     */
    static public SeqUtilsResult translateTranscriptIntoProtein(Transcript transcript, int mapKey, boolean useWebService) throws Exception {
        return new SeqUtils().translateTranscript(transcript, mapKey, useWebService);
    }

    SeqUtilsResult translateTranscript(Transcript transcript, int mapKey, boolean useWebService) throws Exception {

        SeqUtilsResult result = new SeqUtilsResult();
        result.transcript = transcript;
        result.mapKey = mapKey;

        // determine chromosome
        for(MapData md: transcript.getGenomicPositions() ) {
            if( md.getMapKey()==mapKey ) {
                result.chr = md.getChromosome();
                result.strand = md.getStrand();
            }
        }

        // Iterate over all transcripts for this gene
        TranscriptDAO dao = new TranscriptDAO();
        List<TranscriptFeature> features = dao.getFeatures(transcript.getRgdId(), mapKey);

        // See if we have a Coding region
        if( !transcript.isNonCoding() ) {
            processFeatures(features, result);
            processTranscript(result, useWebService);
        }
        return result;
    }

    void processFeatures(List<TranscriptFeature> features, SeqUtilsResult tflags) {

        // process all Transcript features for this transcript. These are Exoms, 3primeUTRs and 5PrimeUTRs
        for( TranscriptFeature tf: features ) {

            String objectName = tf.getFeatureType().name();
            System.out.println("  Found: " + objectName + " " + tf.getStartPos() + " - " + tf.getStopPos() + " (" + tflags.strand + ")");

            if (objectName.equals("UTR3") ) {
                tflags.threeUtr = tf;
            }
            else if (objectName.equals("UTR5") ) {
                tflags.fiveUtr = tf;
            }
            else if (objectName.equals("EXON") ) {
                tflags.exons.add(tf);
            }
        }
    }

    // Process the exons and UTRS creating the variant_transcript.
    void processTranscript(SeqUtilsResult tflags, boolean useWebService) throws Exception {

        if (tflags.strand != null && tflags.strand.equals("-") ) {
            System.out.print("Switching UTRs as we're dealing with - strand ... \n");
            TranscriptFeature temp = tflags.threeUtr;
            tflags.threeUtr = tflags.fiveUtr;
            tflags.fiveUtr = temp;
        }

        // OK we' have a variant in an Exom  for only plus stranded genes !!!!!!
        handleUTRs(tflags.exons, tflags.threeUtr, tflags.fiveUtr);

        StringBuffer refDna = new StringBuffer();

        // Build up DNA Sequence from features
        for (TranscriptFeature feature: tflags.exons) {
            // Skip those exons that have been removed. These have had their start / stop marked as -1
            if (feature.getStartPos() != -1) {
                String dnaChunk;
                if( useWebService )
                    dnaChunk = getDnaChunkCached(tflags, feature.getStartPos(), feature.getStopPos());
                else
                    dnaChunk = getDnaChunkDirect(tflags, feature.getStartPos(), feature.getStopPos());
                if( dnaChunk!=null )
                    refDna.append(dnaChunk);
            }
        }

        System.out.print(" RefDna length = " + refDna.length() + " mod3=" + (refDna.length() % 3) + "\n");
        System.out.print(" ENTIRE RefDna :\n" + refDna.toString() + "\n");

        if (tflags.strand.equals("-")) {
            System.out.print("		Negative Strand found reverseComplement dna \n");
            // Dealing with "-" strand , reverse the DNA
            refDna = reverseComplement(refDna);
        } else {
            System.out.print("		Positive Strand found\n");
        }

        // Check for rna evenly divisible by 3 or log as error
        if (refDna.length() % 3 != 0) {
            System.out.print(tflags.transcript.getAccId()+" RGD:"+tflags.transcript.getRgdId()+" DNALEN:"+refDna.length()+":TRIPLETERROR\n");
            tflags.tripletError = true;

            // make it divisible by 3
            refDna.replace(0, refDna.length(), refDna.substring(0, refDna.length() - (refDna.length() % 3)));
            System.out.print(" RefDna fixed div 3 length =  : " + refDna.length() + " mod " + (refDna.length() % 3) + "\n");
        }

        System.out.print("ENTIRE Ref DNA :\n" + refDna.toString() + "\n");

        // translate DNA
        tflags.translatedProtein = translate(refDna);
    }

    void handleUTRs(List<TranscriptFeature> exons, TranscriptFeature threeUtr, TranscriptFeature fiveUtr) throws Exception {
        for( TranscriptFeature feature: exons ) {

            // if we have a 3'utr to deal with  at end
            if (threeUtr != null) {
                if (feature.getStopPos() < threeUtr.getStartPos()) {
                    // use entire feature
                } else if (feature.getStartPos() < threeUtr.getStartPos()) {
                    System.out.print("feature start reset to " + (threeUtr.getStartPos() - 1) + "\n");
                    feature.setStopPos(threeUtr.getStartPos() - 1);
                } else {
                    // remove all of exon
                    System.out.print("3Prime removed  feature \n");
                    feature.setStartPos(-1);
                    feature.setStopPos(-1);
                }
            }
            // if we have a 5' utr to deal with at start
            if (fiveUtr != null) {
                if (feature.getStartPos() > fiveUtr.getStopPos()) {
                    // use entire feature
                } else if (feature.getStopPos() > fiveUtr.getStopPos()) {
                    System.out.print("feature stop reset to " + (fiveUtr.getStopPos() + 1) + "\n");
                    feature.setStartPos(fiveUtr.getStopPos() + 1);
                } else {
                    // remove all of exon
                    System.out.print("5Prime removed feature" + "\n");
                    feature.setStartPos(-1);
                    feature.setStopPos(-1);
                }
            }
        }
    }

    static public StringBuffer reverseComplement(StringBuffer dna) throws Exception {
        StringBuffer buf = new StringBuffer(dna.length());
        for( int i=dna.length()-1; i>=0; i-- ) {
            char ch = dna.charAt(i);
            if( ch=='A' || ch=='a' )
                buf.append('T');
            else if( ch=='C' || ch=='c' )
                buf.append('G');
            else if( ch=='G' || ch=='g' )
                buf.append('C');
            else if( ch=='T' || ch=='t' )
                buf.append('A');
            else if( ch=='N' || ch=='n' )
                buf.append('N');
            else {
                throw new Exception("reverseComplement: unexpected nucleotide "+ch);
            }
        }
        return buf;
    }

    static public String translate(StringBuffer dna) {

        StringBuilder out = new StringBuilder(dna.length() / 3);
        for( int i=0; i<dna.length(); i+=3 ) {
            char c1 = Character.toUpperCase(dna.charAt(i + 0));
            char c2 = Character.toUpperCase(dna.charAt(i + 1));
            char c3 = Character.toUpperCase(dna.charAt(i + 2));

            if( c1=='C') { // QUARTER C
                if( c2=='A' ) {
                    if( c3=='T' || c3=='C' ) {
                        out.append("H"); // histodine
                    }
                    else if( c3=='A' || c3=='G' ) {
                        out.append("Q"); // glutamine
                    }
                    else
                        out.append("X"); // unknown
                }
                else if( c2=='C' ) {
                    out.append("P"); // proline
                }
                else if( c2=='G' ) {
                    out.append("R"); // arginine
                }
                else if( c2=='T' ) {
                    out.append("L"); // leucine
                }
                else
                    out.append("X"); // unknown
            }

            else if( c1=='G' ) { // QUARTER G
                if( c2=='A' ) {
                    if( c3=='T' || c3=='C' ) {
                        out.append("D"); // aspartic acid
                    }
                    else if( c3=='A' || c3=='G' ) {
                        out.append("E"); // glutamic acid
                    }
                    else
                        out.append("X"); // unknown
                }
                else if( c2=='C' ) {
                    out.append("A"); // alanine
                }
                else if( c2=='G' ) {
                    out.append("G"); // glycine
                }
                else if( c2=='T' ) {
                    out.append("V"); // valine
                }
                else
                    out.append("X"); // unknown
            }

            else if( c1=='A' ) { // QUARTER A
                if( c2=='A' ) {
                    if( c3=='T' || c3=='C' ) {
                        out.append("N"); // asparagine
                    }
                    else if( c3=='A' || c3=='G' ) {
                        out.append("K"); // lysine
                    }
                    else
                        out.append("X"); // unknown
                }
                else if( c2=='C' ) {
                    out.append("T"); // threonine
                }
                else if( c2=='G' ) {
                    if( c3=='T' || c3=='C' ) {
                        out.append("S"); // serine
                    }
                    else if( c3=='A' || c3=='G' ) {
                        out.append("R"); // arginine
                    }
                    else
                        out.append("X"); // unknown
                }
                else if( c2=='T' ) {
                    if( c3=='T' || c3=='C' || c3=='A' ) {
                        out.append("I"); // isoleucine
                    }
                    else if( c3=='G' ) {
                        out.append("M"); // methionine
                    }
                    else
                        out.append("X"); // unknown
                }
                else
                    out.append("X"); // unknown
            }

            else if( c1=='T' ) { // QUARTER T
                if( c2=='A' ) {
                    if( c3=='T' || c3=='C' ) {
                        out.append("Y"); // tyrosine
                    }
                    else if( c3=='A' || c3=='G' ) {
                        out.append("*"); // STOP
                    }
                    else
                        out.append("X"); // unknown
                }
                else if( c2=='C' ) {
                    out.append("S"); // serine
                }
                else if( c2=='G' ) {
                    if( c3=='T' || c3=='C' ) {
                        out.append("C"); // cysteine
                    }
                    else if( c3=='A' ) {
                        out.append("*"); // STOP
                    }
                    else if( c3=='G' ) {
                        out.append("W"); // tryptophan
                    }
                    else
                        out.append("X"); // unknown
                }
                else if( c2=='T' ) {
                    if( c3=='T' || c3=='C' ) {
                        out.append("F"); // phenylalanine
                    }
                    else if( c3=='A' || c3=='G' ) {
                        out.append("L"); // leucine
                    }
                    else
                        out.append("X"); // unknown
                }
                else
                    out.append("X"); // unknown
            }

            else { // QUARTER N
                out.append("X"); // unknown
            }
        }

        return out.toString();
    }


    String getDnaChunkDirect(SeqUtilsResult info, int startPos, int stopPos) throws Exception {

        _fastaParser.setMapKey(info.mapKey);
        String seq = _fastaParser.getSequence(info.chr, startPos, stopPos);
        if( seq!=null )
            seq = seq.replaceAll("[\\s\\00]","");
        return seq;
    }
    static FastaParser _fastaParser = new FastaParser();

    String getDnaChunk(SeqUtilsResult info, int startPos, int stopPos) throws Exception {
        String url = "http://dev.rgd.mcw.edu/rgdweb/seqretrieve/retrieve.html?mapKey="+info.mapKey+
                "&chr="+info.chr+"&startPos="+startPos+"&stopPos="+stopPos+"&format=text";
        return getDnaChunk(url);
    }

    String getDnaChunk(String url) throws Exception {
        FileDownloader downloader = new FileDownloader();
        downloader.setExternalFile(url);
        downloader.setLocalFile("/tmp/dna"+System.currentTimeMillis());
        String localFile = downloader.download();
        String dnaChunk = Utils.readFileAsString(localFile);
        new File(localFile).delete();

        Thread.sleep(50);
        return dnaChunk;
    }

    String getDnaChunkCached(SeqUtilsResult info, int startPos, int stopPos) throws Exception {
        String url = "http://dev.rgd.mcw.edu/rgdweb/seqretrieve/retrieve.html?mapKey="+info.mapKey+
                "&chr="+info.chr+"&startPos="+startPos+"&stopPos="+stopPos+"&format=text";

        String dnaChunk;
        synchronized(_cacheOfDnaChunks) {
            dnaChunk = _cacheOfDnaChunks.get(url);
            if( dnaChunk==null ) {
                // clear the cache if it is too big
                if( _cacheOfDnaChunks.size()>=100000 ) {
                    _cacheOfDnaChunks.clear();
                }
                dnaChunk = getDnaChunk(url);
                _cacheOfDnaChunks.put(url, dnaChunk);
            }
        }
        return dnaChunk;
    }
    static private Map<String,String> _cacheOfDnaChunks = new HashMap<String, String>();

    public class SeqUtilsResult {

        // input information
        public Transcript transcript;
        public int mapKey;

        // intermediate information
        public String chr;
        public String strand = null;
        public TranscriptFeature threeUtr = null;
        public TranscriptFeature fiveUtr = null;
        public List<TranscriptFeature> exons = new ArrayList<TranscriptFeature>();

        // results
        public String translatedProtein;
        public boolean tripletError; // true if length of coding nucleotides is not divisible by 3
    }
}
