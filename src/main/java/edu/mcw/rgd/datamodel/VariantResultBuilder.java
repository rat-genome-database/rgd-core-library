package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.prediction.PolyPhenPrediction;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author jdepons
 * @since 10/13/11
 */
public class VariantResultBuilder {

    private VariantResult vr = new VariantResult();

    public VariantResult getVariantResult() {
        return this.vr;
    }

    public void mapVariant(ResultSet rs) throws Exception {
        Variant v = VariantMapper.mapRow(rs);
        if (vr.getVariant() == null || (!vr.getVariant().variantNucleotide.equals(v.getVariantNucleotide()))) {
            vr.setVariant(v);
        }
    }

    private Object getLast(List list) {
        if (list.size() > 0) {
            return list.get(list.size() - 1);
        } else {
            return null;
        }
    }

    public void mapConservation(ResultSet rs) throws Exception {
        ConservationScore cs = (ConservationScore) this.getLast(vr.getVariant().getConservationScore());
        if( cs == null ) {
            BigDecimal score = rs.getBigDecimal("SCORE");
            if ( !rs.wasNull() ) {
                cs = new ConservationScore();
                cs.setScore(score);
                cs.setChromosome(rs.getString("CHR"));
                cs.setPosition(rs.getInt("START_POS")); // We cannot use POSITION on this result set as there are 2
                cs.setNuc(rs.getString("REF_NUC"));

                vr.getVariant().conservationScore.add(cs);
            }
        }

    }

    public void mapTranscript(ResultSet rs) throws Exception {
        TranscriptResult tr = (TranscriptResult) this.getLast(vr.getTranscriptResults());
        String id = rs.getString("VARIANT_TRANSCRIPT_ID");

        if (!rs.wasNull()) {
            if (tr == null || !tr.getTranscriptId().equals(id)) {
                tr = new TranscriptResult();

                tr.setTranscriptId(id);

                AminoAcidVariantMapper aavm = new AminoAcidVariantMapper();
                AminoAcidVariant aav = aavm.mapRow(rs, 1);

                tr.setAminoAcidVariant(aav);

                vr.transcriptResults.add(tr);
            }
        }
    }


    public void mapPolyphen(ResultSet rs) throws Exception {

        TranscriptResult tr = (TranscriptResult) this.getLast(vr.transcriptResults);
        PolyPhenPrediction ppp = null;

        if (tr != null) {
            ppp = (PolyPhenPrediction) this.getLast(tr.polyPhenPrediction);
        }

        long id = rs.getLong("POLYPHEN_ID");

        if (!rs.wasNull()) {
            if (ppp == null || ppp.getId() != id) {
                PolyPhenMapper pm = new PolyPhenMapper();
                tr.polyPhenPrediction.add(pm.mapRow(rs, 1));
            }
        }
    }

    public void mapClinVar(ResultSet rs) throws Exception {
        int rgdId = rs.getInt("RGD_ID");
        if( rgdId!=0 ) {
            VariantInfo clinVarInfo = new VariantInfo();
            VariantQuery.mapVariants(rs, clinVarInfo);
            vr.setClinvarInfo(clinVarInfo);
        }
    }

    /**
     * Map the fields from the DB_SNP table into the Variant Result requires
     *
     * @param rs
     * @throws Exception
     */
    public void mapDBSNP(ResultSet rs, VariantSearchBean vsb) throws Exception {

        String snp = rs.getString("SNP_NAME");

        if (snp != null) {
            // filter out duplicate DB_SNP_ID's
            List<String> snpList = vr.getDbSNPIds();
            if (snpList.contains(snp)) {
                return;
            }


            vr.dbSNPIds.add(snp);

            Float f = rs.getFloat("MAF_FREQUENCY");
            vr.setDbSnpMAFFrequence(f);

            Integer i = rs.getInt("MAF_SAMPLE_SIZE");
            vr.setDbSnpMAFSampleSize(i);

            vr.setDbSnpAllele(rs.getString("ALLELE"));
            vr.setDbSnpMAFAllele(rs.getString("MAF_ALLELE"));
            vr.setDbSnpStdError(rs.getFloat("STD_ERROR"));
            vr.setDbSnpAvgHetroScore(rs.getFloat("AVG_HETRO_SCORE"));

            try {
                String questionable = rs.getString("QUESTIONABLE_ALLELE");

                if (questionable != null && questionable.equals("Y")) {
                    System.out.print("questionable dbSnp found = " + questionable);
                    vr.setDbSnpQuestionable("Allele is questionable");
                }
            } catch(Exception e) {

            }

        }

    }


}
