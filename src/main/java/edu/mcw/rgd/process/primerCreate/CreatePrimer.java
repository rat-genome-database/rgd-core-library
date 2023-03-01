package edu.mcw.rgd.process.primerCreate;

import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.reporting.Link;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: Jun 25, 2010
 * Time: 11:17:29 AM
 */
public class CreatePrimer {

    private String primerInputFile;
    private String bufferLength;
    private String usage;
    private String species;
    private String gene;
    private String geneSymbol;
    private int geneRgd;
    private String posChr;
    private int posStart;
    private int posStop;
    private String settings;

    protected final Log logger = LogFactory.getLog(getClass());

    public void runMain(String[] args,PrintWriter response) throws Exception{
        logger.debug("CreatePrimer.runMain");

        if(args.length>0){
            for(int a=0; a<args.length; a++ ){
                if(args[a].contains("-primerInput:")){
                    String obj = args[a];
                    String argArr[] = obj.split(":");
                    primerInputFile = argArr[1];
                }else if(args[a].contains("-buffer:")){
                    String obj = args[a];
                    String argArr[] = obj.split(":");
                     bufferLength = argArr[1];
                }else if(args[a].contains("-species:")){
                    String obj = args[a];
                    String argArr[] = obj.split(":");
                     species = argArr[1];
                }else if(args[a].contains("-geneName:")){
                    String obj = args[a];
                    String argArr[] = obj.split(":");
                    gene = argArr[1];
                }else if(args[a].contains("-geneSymbol:")){
                    String geneobj = args[a];
                    String argArr[] = geneobj.split(":");
                    geneSymbol = argArr[1];
                }else if(args[a].contains("-geneRgdId:")){
                    String geneRgdid = args[a];
                    String argArr[] = geneRgdid.split(":");
                    geneRgd = Integer.parseInt(argArr[1]);
                }else if(args[a].contains("-pos:")){
                    String obj = args[a];
                    String argArr[] = obj.split(":");
                    String pos[] = argArr[1].split("-");
                    posChr = pos[0];
                    posStart = Integer.parseInt(pos[1]);
                    posStop = Integer.parseInt(pos[2]);
                }else if(args[a].contains("-settings:")){
                    String input = args[a];
                    String argArr[] = input.split(":");
                    settings = argArr[1];
                }

            }

            if(primerInputFile != null && bufferLength != null && gene != null && posChr !=null && posStart != 0 && posStop !=0){
                createPrimerInputSeq(primerInputFile, bufferLength, species, gene, geneSymbol, geneRgd, posChr, posStart, posStop, settings,response);
            }else{
                throw new ArgumentsException("primerInputFile not found");
            }

        }else{
            throw new ArgumentsException("Arguments not found..this script needs parameters.\n" + getUsage());
        }
    }

    public void createPrimerInputSeq(String primerInputFile, String bufferLength, String species, String gene, String symbol, int rgdid, String posChr, int posStart, int posStop, String settings, PrintWriter response) throws Exception{

        logger.debug("CreatePrimer.createPrimerInputSeq");

        int bpBuffer = Integer.parseInt(bufferLength);
        int speciesTypeKey = SpeciesType.HUMAN;

        PrimerDAO pdao = new PrimerDAO();

        response.println("<html><link href='/common/style/rgd_styles-3.css' rel='stylesheet' type='text/css' /><body>");
        response.println("<table class='wrapperTable' cellpadding='0' cellspacing='0' border='0'><tr><td><div class='wrapper'><div id='main'>");
        response.println("<h5>"+ new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date())
                +", Ensembl Database build_assembly: "+PrimerDAO.DBVER+"</h5>");
        response.println("<h1>Primer Report</h1>");
        response.println("<table style='border:1px solid black;padding:5px;'>");

        EnsemblGene eg = pdao.getGene(gene);
        logger.debug("CreatePrimer.createPrimerInputSeq after pdao.getGene");

        ArrayList<EnsemblTranscript> ensTranscripts = pdao.getTranscriptsForGene(eg);
        if(ensTranscripts.size()>0){
            for(EnsemblTranscript et : ensTranscripts){
                if(et.getCcdsId().length()>0){
                    ArrayList<EnsemblExon> exonsInTranscript = pdao.getExonsForTranscript(et);
                    et.setExonList(exonsInTranscript);
                }else{
                    throw new Exception("Could not find ccds Id for transcript " + et.getEnsTranscriptName() + ". ");
                }
            }
        }else{
            throw new Exception("Could not find transcript for gene " + gene + ". ENSEMBL CCDSID not available.");
        }
        eg.setEnsemblTranscripts(ensTranscripts);
        ArrayList<EnsemblVariation> evListinGene = pdao.getGenicVariants(eg, posChr, posStart, posStop);
        String primerFile = primerInputFile+new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SS'.txt'").format(new Date());

        logger.debug("CreatePrimer.createPrimerInputSeq 22");

        PrintWriter primerWriter = new PrintWriter(primerFile);
        int count = 1;

        EnsemblVariation evNewObj = new EnsemblVariation();
        evNewObj.setVarName("NA");

        if(evListinGene.size()==0){
            evNewObj.setVarName("novel");
            evNewObj.setVstart(posStart);
            evNewObj.setVstop(posStop);
            evNewObj.setvChr(posChr);
            evNewObj.setvSeq_region_id(eg.getSeqRegionId());
            evNewObj.setvStrand(eg.geteGStrand());
            evNewObj.setConsequenceType("NA");

            evListinGene.add(evNewObj);
        }

        logger.debug("CreatePrimer.createPrimerInputSeq 3");

        response.println("<tr><td style='font-weight:700'>Settings File:</td><td>" + settings + "</td></tr>");
        response.println("<tr><td style='font-weight:700'>Ensembl Gene:</td><td><a href='http://www.ensembl.org/Homo_sapiens/Gene/Sequence?db=core;g=" + gene + "'>"+gene+"</a> (" + eg.getEnsGeneName() + ")</td></tr>");


        int rgdId = 0; // ugly hack to declare rgdId variable somehow, so the code could compile
        if (rgdId<1) {
            response.println("<tr><td style='font-weight:700'>Gene:</td><td>"+ symbol.toUpperCase() + " (Chr"+eg.getEgChr()+":"+eg.geteGStart()+".."+eg.getEgStop()+")</td></tr>");
        }else {
            response.println("<tr><td style='font-weight:700'>Gene:</td><td><a href=" + Link.it(rgdid) + ">"+ symbol.toUpperCase() + " (Chr"+eg.getEgChr()+":"+eg.geteGStart()+".."+eg.getEgStop()+")</a></td></tr>");
        }

        response.println("<tr><td style='font-weight:700'>Variant Position:</td><td><a href='http://rgd.mcw.edu/fgb2/gbrowse/human_37_1/?q=Chr" + posChr + ":" + posStart + ".." + posStop + "'>"+posChr + ":" + posStart + ".." + posStop+"</a></td></tr>");


        logger.debug("CreatePrimer.createPrimerInputSeq 4");

        for (EnsemblVariation evObj : evListinGene){
            if(evObj.getVarName().contains("novel")){
                response.println("<tr><td style='font-weight:700'>Variant ID:</td><td><a href='http://www.ensembl.org/Homo_sapiens/Gene/Sequence?db=core;g=" + gene + "'>"+evObj.getVarName()+"</a></td></tr>");
            }else{
                response.println("<tr><td style='font-weight:700'>Variant ID:</td><td><a href='http://www.ensembl.org/Homo_sapiens/Variation/Mappings?db=core;g="+eg.getEnsGeneName()+";source=dbSNP;v="+evObj.getVarName()+";vdb=variation;vf="+evObj.getVid()+"'>" + evObj.getVarName() + "</a></td></tr>");
            }

        }

        if (evListinGene.size()>0) {

            EnsemblVariation evObj = evListinGene.get(0);

            int leftBuffer = bpBuffer;
            int rightBuffer = bpBuffer;

            int newStart = evObj.getVstart()-leftBuffer;
            int newStop = evObj.getVstop()+rightBuffer;

            int relativeSeqIncludedStart=440;
            int relativeSeqIncludedLength=120+(evObj.getVstop()-evObj.getVstart());
            String sequenceIncludedTag = relativeSeqIncludedStart+","+relativeSeqIncludedLength;

            response.println("<tr><td style='font-weight:700'>Variant Location:</td><td>");

            ArrayList<EnsemblExon> exonCollIngene = new ArrayList<EnsemblExon>();

            if(eg.getEnsemblTranscripts().size()>0){

                for(EnsemblTranscript etObj : eg.getEnsemblTranscripts()){
                    for(EnsemblExon ex : etObj.getExonList()){

                        int exonExistsInExonArray = checkExonExistingInArray(ex, exonCollIngene);
                        if(exonExistsInExonArray==0){
                            exonCollIngene.add(ex);
                        }
                    }
                }


                int flagInExon=0;
                EnsemblExon exObj = new EnsemblExon();
                ArrayList<EnsemblExon> varInExonArray = new ArrayList<EnsemblExon>();
                for(EnsemblExon exonObj : exonCollIngene){
                    if(evObj.getVstart()<=exonObj.getEnsExonStop() && evObj.getVstop()>=exonObj.getEnsExonStart()){
                        varInExonArray.add(exonObj);
                        if(varInExonArray.size()==1){
                            exObj=exonObj;
                            flagInExon=1;
                        }else if(varInExonArray.size()>1){
                            exObj=getLargerExon(varInExonArray);
                            flagInExon=1;
                        }else{
                            flagInExon=0;
                        }
                    }
                }

                if(flagInExon==1){
                    if(evObj.getVstart()<=exObj.getEnsExonStop() && evObj.getVstop()>=exObj.getEnsExonStart()){

                        flagInExon=1;
                        evObj.setInExonNumber("E"+exObj.getExonNumber());
                        if(exObj.getEqualExon().length()==0){
                            response.println("exonic " + evObj.getInExonNumber() + " (<a href='http://www.ensembl.org/Homo_sapiens/Transcript/Exons?db=core;g=" + eg.getEnsGeneName() + ";t=" + exObj.getExonParentTranscriptAccId() + "'>" + exObj.getEnsExonName() + "</a>)</td></tr>");
                        }else if(exObj.getEqualExon().length()>0){
                            response.print("exonic " + evObj.getInExonNumber() + " (<a href='http://www.ensembl.org/Homo_sapiens/Transcript/Exons?db=core;g=" + eg.getEnsGeneName() + ";t=" + exObj.getExonParentTranscriptAccId() + "'>" + exObj.getEnsExonName() + " ALSO in ");
                            String[] eqArr = exObj.getEqualExon().split("_");
                            for(int i=0; i<eqArr.length; i++){
                                response.print(eqArr[i]+" ");
                            }
                            response.print("</a>)</td></tr>");
                        }

                        if(exObj.getEnsExonStop()-exObj.getEnsExonStart()<=450){

                            if((evObj.getVstart()>=exObj.getEnsExonStart())||(exObj.getEnsExonStart()-evObj.getVstart())<=12){
                                response.println("<tr><td style='font-weight:700'>Exon size:</td><td>" + (exObj.getEnsExonStop() - exObj.getEnsExonStart()) + "</td></tr>");
                                response.println("<tr><td style='font-weight:700'>Exon location:</td><td><a href='http://rgd.mcw.edu/fgb2/gbrowse/human_37_1/?q=Chr" + posChr + ":" + exObj.getEnsExonStart() + ".." + exObj.getEnsExonStop() + "'>"+posChr + ":" + exObj.getEnsExonStart() + ".." + exObj.getEnsExonStop()+"</a></td></tr>");
                                response.println("<tr><td style='font-weight:700'>Variant relative position in exon:</td><td>" + (evObj.getVstart()-exObj.getEnsExonStart()) + "</td></tr>");
                                response.println("</table><br><h1>Method</h1><hr><table>");
                                response.println("<tr><td><li>Variant start relative to exon start: "+(evObj.getVstart()-exObj.getEnsExonStart())+"</td></tr>");

                                if(evObj.getVstart()>=exObj.getEnsExonStart()){
                                    if(250 >= (evObj.getVstart()-exObj.getEnsExonStart())){
                                        leftBuffer = 200+60+(evObj.getVstart()-exObj.getEnsExonStart());
                                        relativeSeqIncludedStart=200;
                                    }else if((evObj.getVstart()-exObj.getEnsExonStart())>250){
                                        leftBuffer = 200+(evObj.getVstart()-exObj.getEnsExonStart());
                                        relativeSeqIncludedStart=200+190;
                                    }
                                }else if((exObj.getEnsExonStart()>evObj.getVstart()) && (exObj.getEnsExonStart()-evObj.getVstart()<=12)){
                                        leftBuffer = 200+60+(exObj.getEnsExonStart()-evObj.getVstart());
                                        relativeSeqIncludedStart=200;
                                }

                            }


                            if((evObj.getVstop()<=exObj.getEnsExonStop())||(evObj.getVstop()-exObj.getEnsExonStop()<=12)){
                                response.println("<tr><td><li>Variant stop position relative to exon stop position: "+(exObj.getEnsExonStop()-evObj.getVstop())+"</td></tr>");

                                if(exObj.getEnsExonStop()>evObj.getVstop()){
                                    if((250 >= (exObj.getEnsExonStop()-evObj.getVstop()))){
                                        rightBuffer = 60+200+(exObj.getEnsExonStop()-evObj.getVstop());
                                        relativeSeqIncludedLength=120+(leftBuffer-relativeSeqIncludedStart)+(evObj.getVstop()-evObj.getVstart())+(exObj.getEnsExonStop()-evObj.getVstop());

                                    }else if((exObj.getEnsExonStop()-evObj.getVstop())>250){
                                        rightBuffer = 500+(exObj.getEnsExonStop()-evObj.getVstop());
                                        relativeSeqIncludedLength=(exObj.getEnsExonStop()-evObj.getVstop())+(evObj.getVstop()-evObj.getVstart())+(leftBuffer-relativeSeqIncludedStart)+60;
                                    }
                                }else if((evObj.getVstop()>exObj.getEnsExonStop()) && (evObj.getVstop()-exObj.getEnsExonStop()<=12)){
                                    rightBuffer = 60+200+(evObj.getVstop()-exObj.getEnsExonStop());
                                    relativeSeqIncludedLength=120+leftBuffer-relativeSeqIncludedStart+(evObj.getVstop()-evObj.getVstart()+(evObj.getVstop()-exObj.getEnsExonStop()));
                                }

                            }
                        }else{

                            if((evObj.getVstart()>=exObj.getEnsExonStart())||(exObj.getEnsExonStart()-evObj.getVstart())<=12){
                                response.println("<tr><td style='font-weight:700'>Exon size:</td><td>" + (exObj.getEnsExonStop() - exObj.getEnsExonStart()) + "</td></tr>");
                                response.println("<tr><td style='font-weight:700'>Exon location:</td><td><a href='http://rgd.mcw.edu/fgb2/gbrowse/human_37_1/?q=Chr" + posChr + ":" + exObj.getEnsExonStart() + ".." + exObj.getEnsExonStop() + "'>"+posChr + ":" + exObj.getEnsExonStart() + ".." + exObj.getEnsExonStop()+"</a></td></tr>");
                                response.println("<tr><td style='font-weight:700'>Variant relative position in exon:</td><td>" + (evObj.getVstart()-exObj.getEnsExonStart()) + "</td></tr>");
                                response.println("</table><br><h1>Method</h1><hr><table>");
                                response.println("<tr><td><li>Variant start relative to exon start: "+(evObj.getVstart()-exObj.getEnsExonStart())+"</td></tr>");

                                if(evObj.getVstart()>=exObj.getEnsExonStart()){
                                    if((evObj.getVstart()-exObj.getEnsExonStart())<=60){
                                        leftBuffer = (evObj.getVstart()-exObj.getEnsExonStart())+60+200;
                                        relativeSeqIncludedStart=200;
                                    }else{
                                        leftBuffer=500;
                                        relativeSeqIncludedStart=440;
                                    }
                                }else if((exObj.getEnsExonStart()>evObj.getVstart()) && (exObj.getEnsExonStart()-evObj.getVstart()<=12)){
                                        leftBuffer = 200+60+(exObj.getEnsExonStart()-evObj.getVstart());
                                        relativeSeqIncludedStart=200;
                                }
                            }

                            if((evObj.getVstop()<=exObj.getEnsExonStop())|| (evObj.getVstop()-exObj.getEnsExonStop()<=12)){
                                response.println("<tr><td><li>Variant stop position relative to exon stop position: "+(exObj.getEnsExonStop()-evObj.getVstop())+"</td></tr>");

                                if(exObj.getEnsExonStop()>evObj.getVstop()){
                                    if((exObj.getEnsExonStop()-evObj.getVstop())<=60){
                                        rightBuffer = (exObj.getEnsExonStop()-evObj.getVstop())+60+200;
                                        if(relativeSeqIncludedStart==200){
                                            relativeSeqIncludedLength=60+(evObj.getVstart()-exObj.getEnsExonStart())+(exObj.getEnsExonStop()-evObj.getVstop());
                                        }else if(relativeSeqIncludedStart==440){
                                            relativeSeqIncludedLength=60+(exObj.getEnsExonStop()-evObj.getVstop());
                                        }
                                    }else{
                                        rightBuffer=500;
                                        relativeSeqIncludedLength=120+(evObj.getVstop()-evObj.getVstart());
                                    }
                                }else if((evObj.getVstop()>exObj.getEnsExonStop()) && (evObj.getVstop()-exObj.getEnsExonStop()<=12)){
                                    rightBuffer = 60+200+(evObj.getVstop()-exObj.getEnsExonStop());
                                    if(relativeSeqIncludedStart==200){
                                        relativeSeqIncludedLength=60+leftBuffer-relativeSeqIncludedStart+(evObj.getVstop()-exObj.getEnsExonStop());
                                    }else if(relativeSeqIncludedStart==440){
                                        relativeSeqIncludedLength=60+(evObj.getVstop()-exObj.getEnsExonStop());
                                    }
                                }
                            }
                        }

                        response.println("<tr><td><li>Buffer region upstream of variant set to "+leftBuffer+"</td></tr>");
                        response.println("<tr><td><li>Target sub-sequence position relative to sequence start position: "+relativeSeqIncludedStart+"</td></tr>");
                        response.println("<tr><td><li>Buffer region downstream of variant set to "+rightBuffer+"</td></tr>");
                        response.println("<tr><td><li>Target sub-sequence length: "+relativeSeqIncludedLength+"</td></tr>");


                        newStart = evObj.getVstart()-leftBuffer;
                        newStop = evObj.getVstop()+rightBuffer;
                        sequenceIncludedTag = relativeSeqIncludedStart+","+relativeSeqIncludedLength;
                    }
                }

                if(flagInExon==0){
                    evObj.setInExonNumber("intronic");
                    response.print("intronic</td></tr>");
                    response.println("</table><br><h1>Method</h1><hr><table>");

                    writeMethodForIntronicVariant(newStart, newStop, leftBuffer, rightBuffer, evObj, relativeSeqIncludedStart, relativeSeqIncludedLength, sequenceIncludedTag, response);

                }

            }else{
                evObj.setInExonNumber("genic_no_transcripts");
                response.print("genic ( no transcripts detected ) </td></tr>");
                response.println("</table><br><h1>Method</h1><hr><table >");
                response.println("<tr><td><li>No Transcripts found for gene</td></tr>");

                writeMethodForIntronicVariant(newStart, newStop, leftBuffer, rightBuffer, evObj, relativeSeqIncludedStart, relativeSeqIncludedLength, sequenceIncludedTag, response);

            }


            response.println("</table><br>");
            response.println("<h1>Sequence</h1><hr>");
            EnsemblRest rest = new EnsemblRest();
            String seq = rest.retrieveSequence(evObj.getvChr(), newStart, newStop, eg.geteGStrand(),speciesTypeKey);

            response.print("<table><tr><td><b>Sequence location:</b>&nbsp;&nbsp;");
            if(eg.geteGStrand().equals("-1")){
                response.print(newStop + " to " + newStart);
            }else{
                response.print(newStart + " to " + newStop);
            }

            if(eg.geteGStrand().equals("-1")){
                seq = new StringBuilder(seq).reverse().toString();
            }


            System.out.println("is it novel?");
            if(evNewObj.getVarName().equals("novel")){
                evObj = evNewObj;
            }

            logger.debug("masking variants.. ");
            int varrelativeStart = newStart;
            int variantRelStart = evObj.getVstart()-varrelativeStart;
            int variantRelStop = evObj.getVstop()-varrelativeStart+1;
            String variantRefInSeq = seq.substring(variantRelStart, variantRelStop);
            String variantMaskedSeq = variantRefInSeq.replaceAll("[ATCGatgc]","*");
            String originalSeq = seq;
            seq = seq.substring(0, variantRelStart)+variantMaskedSeq+seq.substring(variantRelStop);


            if(eg.geteGStrand().equals("-1")){
                seq = new StringBuilder(seq).reverse().toString();
                originalSeq = new StringBuilder(originalSeq).reverse().toString();
                logger.debug("old var rel start is:"+variantRelStart+" and old var rel stop now is:"+variantRelStop);
                int oldStart=variantRelStart;
                int oldRelStart = relativeSeqIncludedStart+relativeSeqIncludedLength;
                int oldStop=variantRelStop;
                variantRelStop=seq.length()-oldStart;
                variantRelStart=seq.length()-oldStop;
                relativeSeqIncludedStart=seq.length()-oldRelStart;
                logger.debug("var rel start is:"+variantRelStart+" and var rel stop now is:"+variantRelStop + " sub-sequence length is:"+seq.length());
            }


            response.println("</td></tr><tr><td><br>Sequence prior to masking:</td></tr></table><pre>");
            for (int i=1; i<= originalSeq.length(); i++) {
                String value = originalSeq.substring(i-1, i);
                /*
                if(value.contains("*")){
                    value = "<span style='color:red;background-color:#02599C; font-weight:700;'>" + variantRefInSeq + "</span>";
                }
                */
                if((i-1)>=variantRelStart && i<=variantRelStop){
                    value = "<span style='color:red;background-color:#02599C; font-weight:700;'>" + value + "</span>";
                }

                response.print(value);
                if (i % 60 == 0) {
                    response.print("\n");
                }

            }
            response.println("</pre>");



            if(eg.geteGStrand().equals("-1")){
                seq = new StringBuilder(seq).reverse().toString();
            }




            ArrayList<EnsemblVariation> evListInBuffer = pdao.getOtherVariantsWithinBuffer(evObj, newStart, newStop);

            //the masker wasnt making the novel variant.. hence added the novel variant position in also.
            if(evNewObj.getVarName().equals("novel")){
                logger.debug(evNewObj.getVarName());
                evListInBuffer.add(evNewObj);
            }

            for(EnsemblVariation evInBuffer : evListInBuffer){

                int relativeStart = newStart;
                int varRelStart = evInBuffer.getVstart()-relativeStart;
                int varRelStop = evInBuffer.getVstop()-relativeStart+1;
                String varRefInSeq = seq.substring(varRelStart, varRelStop);
                String varMaskedSeq = varRefInSeq.replaceAll("[ATCGatgc]","X");
                if((evInBuffer.getVarName().equalsIgnoreCase(evObj.getVarName())) || ((evInBuffer.getVarName().equalsIgnoreCase(evNewObj.getVarName())))){
                    varMaskedSeq = varRefInSeq.replaceAll("[ATCGatgcXx]","*");
                }

                seq = seq.substring(0, varRelStart)+varMaskedSeq+seq.substring(varRelStop);
            }

            if(eg.geteGStrand().equals("-1")){
                seq = new StringBuilder(seq).reverse().toString();
            }


            response.println("<table><tr><td>Sequence after masking <b>" + evListInBuffer.size() + "</b> known ENSEMBL variants</td></tr></table><pre>");

            for (int i=1; i<=seq.length(); i++) {
                String value = seq.substring(i-1, i);
                boolean  foundInExon=false;

                for(EnsemblExon exObj : exonCollIngene){

                    if(eg.geteGStrand().equals("1")){
                        int relativeExonStart = exObj.getEnsExonStart()-newStart+1;
                        int relativeExonEnd = exObj.getEnsExonStop()-newStart+2;
                        if (i>=relativeExonStart && i<relativeExonEnd) {
                            value = highlightVariantsAndExons(value);
                            foundInExon=true;
                        }
                    }else if(eg.geteGStrand().equals("-1")){
                        int relativeExonStart = newStop-exObj.getEnsExonStart()+2;
                        int relativeExonEnd = newStop-exObj.getEnsExonStop()+1;
                        if (i>=relativeExonEnd && i<relativeExonStart) {
                            value=highlightVariantsAndExons(value);
                            foundInExon=true;
                        }
                    }

                }

                if (!foundInExon) {
                    if (value.toLowerCase().equals("x")){
                        value = "<span style='color:white;background-color:#02599C; font-weight:700;'>" + value + "</span>";
                    }else
                    if(value.toLowerCase().equals("*")){
                        value = "<span style='color:red;background-color:#02599C; font-weight:700;'>" + value + "</span>";
                    }
                }

                response.print(value);
                if (i % 60 == 0) {
                    response.print("\n");
                }
            }

            response.println("</pre>");

            sequenceIncludedTag = relativeSeqIncludedStart+","+relativeSeqIncludedLength;
            //logger.debug("SEQUENCE_ID="+gene+"_"+evObj.getInExonNumber()+"\nSEQUENCE_TEMPLATE="+seq+"\nSEQUENCE_TARGET="+sequenceIncludedTag+"\n=");
            primerWriter.println("SEQUENCE_ID="+gene+"_"+evObj.getInExonNumber()+"\nSEQUENCE_TEMPLATE="+seq+"\nSEQUENCE_TARGET="+sequenceIncludedTag+"\n=");
            count+=1;
            primerWriter.flush();
        }


        primerWriter.close();

        logger.debug("CreatePrimer.createPrimerInputSeq 5");
    }

    private int checkExonExistingInArray(EnsemblExon ex, ArrayList<EnsemblExon> exonCollIngene) {

        for(EnsemblExon exonObj : exonCollIngene){
            if(ex.getEnsExonId().equals(exonObj.getEnsExonId())){
                return 1;
            }
        }

        return 0;
    }

    public EnsemblExon getLargerExon(ArrayList<EnsemblExon> exObjArr){
        EnsemblExon ex = exObjArr.get(0);
        for(EnsemblExon e : exObjArr){
            if(!e.getEnsExonName().equalsIgnoreCase(ex.getEnsExonName())){
                if((e.getEnsExonStop()-e.getEnsExonStart())>(ex.getEnsExonStop()-ex.getEnsExonStart())){
                    ex = e;
                }else if((e.getEnsExonStop()-e.getEnsExonStart())==(ex.getEnsExonStop()-ex.getEnsExonStart())){
                    String equalExon = ex.getEqualExon();
                    if(equalExon.length()>0){
                        equalExon+="_"+e.getEnsExonName();
                    }else{
                        equalExon=e.getEnsExonName();
                    }
                    ex.setEqualExon(equalExon);
                }
            }
        }
        return ex;
    }

    private void writeMethodForIntronicVariant(int newStart, int newStop, int leftBuffer, int rightBuffer, EnsemblVariation evObj, int relativeSeqIncludedStart, int relativeSeqIncludedLength, String sequenceIncludedTag, PrintWriter response){
        //newStart = evObj.getVstart()-leftBuffer;
        //newStop = evObj.getVstop()+rightBuffer;
        evObj.setInExonNumber("intronic");
        relativeSeqIncludedStart=440;
        relativeSeqIncludedLength=120+(evObj.getVstop()-evObj.getVstart());

        response.println("<tr><td><li>Buffer region upstream AND downstream of variant set to: "+leftBuffer+"</td></tr>");
        response.println("<tr><td><li>Target sequence relative start position: "+relativeSeqIncludedStart+"</td></tr>");
        response.println("<tr><td><li>Sequence length: "+relativeSeqIncludedLength+"</td></tr>");


        //sequenceIncludedTag = relativeSeqIncludedStart+","+relativeSeqIncludedLength;
    }


    public String highlightVariantsAndExons(String value){
        if (value.toLowerCase().equals("x")) {
            value = "<span style='color:white;background-color:#02599C; font-weight:700;'>" + value + "</span>";
        }else if(value.toLowerCase().equals("*")){
            value = "<span style='color:red;background-color:#02599C; font-weight:700;'>" + value + "</span>";
        }
        else {
            value = "<span style='background-color:#FFEBCD; color:#C52700;'>" + value + "</span>";
        }

        return value;
    }




    public class ArgumentsException extends Exception{
        public ArgumentsException(String msg){
            super(msg);
        }
    }



    public int getPosStop() {
        return posStop;
    }

    public void setPosStop(int posStop) {
        this.posStop = posStop;
    }

    public String getPosChr() {
        return posChr;
    }

    public void setPosChr(String posChr) {
        this.posChr = posChr;
    }

    public int getPosStart() {
        return posStart;
    }

    public void setPosStart(int posStart) {
        this.posStart = posStart;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getPrimerInputFile() {
        return primerInputFile;
    }

    public void setPrimerInputFile(String primerInputFile) {
        this.primerInputFile = primerInputFile;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getBufferLength() {
        return bufferLength;
    }

    public void setBufferLength(String bufferLength) {
        this.bufferLength = bufferLength;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public int getGeneRgd() {
        return geneRgd;
    }

    public void setGeneRgd(int geneRgd) {
        this.geneRgd = geneRgd;
    }
}
