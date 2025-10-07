package edu.mcw.rgd.process;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jdepons
 * @since 11/18/11
 */
public class FastaParser {

    private int mapKey;
    private String chr;
    private String chrDir;
    private String lastError;

    private String[] chrFilePrefixes = {"chr", "Chr"};
    private String[] chrFileSuffixes = {".fa", ".fna", ".mfa"};
    private Map<String,File> chrFileMap = new HashMap<String,File>();

    public String getChrDir() {
        return chrDir;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        final String dir = "/ref/fasta/";

        String chrDir;
        switch(mapKey) {
            case 17: chrDir=dir+"hs37"; break;
            case 38: chrDir=dir+"hs38"; break;
            case 18: chrDir=dir+"mm37"; break;
            case 35: chrDir=dir+"mm38"; break;
            case 239: chrDir=dir+"mm39"; break;
            case 60: chrDir=dir+"rn3.4"; break;
            case 70: chrDir=dir+"rn5"; break;
            case 360: chrDir=dir+"rn6"; break;
            case 372: chrDir=dir+"rn7.2"; break;
            case 380: chrDir=dir+"GRCr8"; break;
            case 511: chrDir=dir+"panPan2"; break;
            case 631: chrDir=dir+"canFam3"; break;
            case 634: chrDir=dir+"ROS_Cfam_1.0"; break;
            case 910: chrDir=dir+"susScr3"; break;
            case 911: chrDir=dir+"susScr11"; break;
            case 1311: chrDir=dir+"chlSab2"; break;
            default: chrDir = null; lastError="ERROR: Unsupported mapKey="+mapKey; break;
        }
        setMapKey(mapKey, chrDir);
    }

    public void setMapKey(int mapKey, String fastaDir) {
        this.mapKey = mapKey;
        this.chrDir = fastaDir;
        chrFileMap.clear();
    }

    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        chr = chr.toUpperCase();
        if( chr.equals("M") ) {
            chr = "MT";
        }
        this.chr = chr;
    }

    public String getLastError() {
        return this.lastError;
    }

    public String getSequence(String chr, int start, int end) throws Exception{
        setChr(chr);
        return getSequence(start, end);
    }

    File openChrFile() {
        File chrFile = chrFileMap.get(chr);
        if( chrFile!=null ) {
            return chrFile;
        }

        lastError = "";

        for( String chrFilePrefix: chrFilePrefixes ) {
            for( String chrFileSuffix: chrFileSuffixes ) {
                chrFile = new File(chrDir+"/"+chrFilePrefix+chr+chrFileSuffix);
                if( chrFile.exists() ) {
                    lastError = "";
                    chrFileMap.put(chr, chrFile);
                    return chrFile;
                }
                if( lastError.isEmpty() ) {
                    lastError = "ERROR: Cannot open file "+chrFile.getAbsolutePath();
                } else {
                    lastError += " nor file "+chrFile.getAbsolutePath();
                }
            }
        }
        return null;
    }

    public String getSequence(int start, int end) throws Exception{

        File chrFile = openChrFile();
        if( chrFile==null ) {
            return null;
        }
        long len = chrFile.length();

        // validate start and stop positions
        if(start>end){
            lastError = "WARNING: swapping start and stop positions";
            int tmp = start;
            start = end;
            end = tmp;
        }

        if(start<1){
            lastError = "WARNING: start pos ["+start+"] changed to 1";
            start = 1;
        }
        if(end<1){
            lastError = "WARNING: end pos ["+end+"] changed to 1";
            end = 1;
        }
        if( start>len ) {
            lastError = "WARNING: start pos ["+start+"] changed to "+len;
            start = (int)len;
        }
        if( end>len ) {
            lastError = "WARNING: end pos ["+end+"] changed to "+len;
            end = (int)len;
        }
        if(start>end){
            int tmp = start;
            start = end;
            end = tmp;
        }


        RandomAccessFile raf = new RandomAccessFile(chrFile, "r");

        // skip first line which could be like this:
        // >Chr11
        String line = raf.readLine();
        int headerLen = line.length() + 1; // firstRow

        // we assume all data lines are of fixed length
        // therefore we need to read the second row to determine the length of data line
        line = raf.readLine();
        int dataLineLen = line.length();

        // determine offset
        int stOffset = ((int) Math.ceil(start / dataLineLen)) -1;
        start += headerLen + stOffset;
        if ((start - stOffset - headerLen) % dataLineLen == 0) {
            start--;
        }

        int eOffset = ((int) Math.ceil(end / dataLineLen)) -1;
        end += headerLen + eOffset;
        if ((end - eOffset - headerLen) % dataLineLen == 0) {
            end--;
        }

        raf.seek(start);
        byte[] b = new byte[end + 1 - start];
        raf.read(b,0,end +1-start);
        raf.close();
        return new String(b);
    }
}
