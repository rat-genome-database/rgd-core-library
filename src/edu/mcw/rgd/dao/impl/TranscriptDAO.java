package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;
import edu.mcw.rgd.datamodel.*;
import edu.mcw.rgd.process.Utils;

import java.util.*;

/**
 * @author mtutaj
 * @since Jun 18, 2010
 * manages gene transcripts and transcripts features: exons, utrs, ...
 */
public class TranscriptDAO extends AbstractDAO {

    RGDManagementDAO rgdDAO = new RGDManagementDAO();
    MapDAO mapDAO = new MapDAO();


    /**
     * get a transcripts given its rgd id
     * @param transcriptRgdId transcript rgd id
     * @return Transcript object or null
     * @throws Exception on error in framework
     */
    public Transcript getTranscript(int transcriptRgdId) throws Exception {
        String query = "SELECT t.* FROM transcripts t WHERE t.transcript_rgd_id=?";
        List<Transcript> transcripts = executeTranscriptQuery(query, transcriptRgdId);

        return transcripts.isEmpty() ? null : transcripts.get(0);
    }

    /**
     * get list of transcripts by transcript accession id
     * @param accId transcript accession id like NM_030992
     * @return list of transcripts; could be empty list, never null
     * @throws Exception on error in framework
     */
    public List<Transcript> getTranscriptsByAccId(String accId) throws Exception {

        String query = "SELECT t.* FROM transcripts t WHERE t.acc_id=?";
        return executeTranscriptQuery(query, accId);
    }

    /**
     * get list of transcripts by refseq protein accession id
     * @param proteinAccId protein accession id like NP_030992
     * @return list of transcripts; could be empty list, never null
     * @throws Exception on error in framework
     */
    public List<Transcript> getTranscriptsByProteinAccId(String proteinAccId) throws Exception {

        String query = "SELECT t.* FROM transcripts t WHERE t.protein_acc_id=?";
        return executeTranscriptQuery(query, proteinAccId);
    }


    /**
     * get list of transcripts bound to given gene
     * @param geneRgdId gene rgd id
     * @return list of transcripts tied up to the given gene; could be empty list, never null
     * @throws Exception on error in framework
     */
    public List<Transcript> getTranscriptsForGene(int geneRgdId, int mapKey) throws Exception {
        String query = "select t.* from TRANSCRIPTS t, maps_data md where t.GENE_RGD_ID=? and md.rgd_id=t.transcript_rgd_id and md.map_key=?";
        return executeTranscriptQuery(query, geneRgdId, mapKey);
    }


    /**
     * get list of transcripts bound to given gene
     * @param geneRgdId gene rgd id
     * @return list of transcripts tied up to the given gene; could be empty list, never null
     * @throws Exception on error in framework
     */
    public List<Transcript> getTranscriptsForGene(int geneRgdId) throws Exception {
        String query = "select t.* from TRANSCRIPTS t where t.GENE_RGD_ID=?";
        return executeTranscriptQuery(query, geneRgdId);
    }

    /**
     * return coordinates for all features of given transcript
     * @param transcriptRgdId rgd id of a transcript object
     * @return list of coordinates of transcript feature(s) of given transcript
     * @throws Exception on error in framework
     */
    public List<TranscriptFeature> getFeatures(int transcriptRgdId) throws Exception {

        String query = "SELECT m.*,t.transcript_rgd_id,r.object_key " +
                "FROM maps_data m,transcript_features t,rgd_ids r " +
                "WHERE m.rgd_id(+)=t.feature_rgd_id AND t.transcript_rgd_id=? AND t.feature_rgd_id=r.rgd_id";
        TranscriptFeatureQuery q = new TranscriptFeatureQuery(this.getDataSource(), query);
        return execute(q, transcriptRgdId);
    }

    /**
     * return coordinates for all features of given kind, like exons, belonging to given transcript
     * @param rgdId rgd id of a transcript object
     * @param feature kind of feature to return, like exons
     * @return list of coordinates of transcript feature(s) of given kind
     * @throws Exception on error in framework
     */
    public List<TranscriptFeature> getFeatures(int rgdId, TranscriptFeature.FeatureType feature) throws Exception {

        int objectKey = TranscriptFeature.getObjectKey(feature);
        String query = "select m.*,t.TRANSCRIPT_RGD_ID,r.OBJECT_KEY from MAPS_DATA m,TRANSCRIPT_FEATURES t,RGD_IDS r where m.RGD_ID=t.FEATURE_RGD_ID and t.TRANSCRIPT_RGD_ID=? and t.FEATURE_RGD_ID=r.RGD_ID AND r.OBJECT_KEY=?";
        TranscriptFeatureQuery q = new TranscriptFeatureQuery(this.getDataSource(), query);
        return execute(q, rgdId, objectKey);
    }

    /**
     * return coordinates for all features of given kind, like exons, belonging to given transcript;
     * Note: only features that are stored in the database to conserve space are returned:
     * EXONS and UTR3 and UTR5 regions, possibly spanning multiple exons; that means that for a transcript
     * there can be at most one UTR3 and/or UTR5 region
     * @param rgdId rgd id of a transcript object
     * @return list of coordinates of transcript feature(s) of given kind
     * @throws Exception on error in framework
     */
    public List<TranscriptFeature> getFeatures(int rgdId, int mapKey) throws Exception {
        String query = "select m.*,t.TRANSCRIPT_RGD_ID,r.OBJECT_KEY from MAPS_DATA m,TRANSCRIPT_FEATURES t,RGD_IDS r where m.RGD_ID=t.FEATURE_RGD_ID and t.TRANSCRIPT_RGD_ID=? and t.FEATURE_RGD_ID=r.RGD_ID and map_key=? order by m.start_pos";
        TranscriptFeatureQuery q = new TranscriptFeatureQuery(this.getDataSource(), query);
        return execute(q, rgdId, mapKey);
    }

    /**
     * for given transcript and map key, return model features: EXONS, UTR3 and UTR5 segments, CDS segments
     * @param rgdId rgd id of a transcript object
     * @return list of coordinates of transcript feature(s) of given kind
     * @throws Exception on error in framework
     */
    public List<TranscriptFeature> getModelFeatures(int rgdId, int mapKey) throws Exception {

        int transcriptHasIssues = 0;

        List<TranscriptFeature> exons = new ArrayList<>();
        List<TranscriptFeature> utr3 = new ArrayList<>();
        List<TranscriptFeature> utr5 = new ArrayList<>();
        List<TranscriptFeature> cds = new ArrayList<>();


        // get exons
        int utr3Start = 0, utr3Stop = 0;
        int utr5Start = 0, utr5Stop = 0;

        for( TranscriptFeature tf: getFeatures(rgdId, mapKey) ) {
            if( tf.getFeatureType()== TranscriptFeature.FeatureType.EXON ) {
                exons.add(tf);
            }
            else if( tf.getFeatureType()== TranscriptFeature.FeatureType.UTR3 ) {
                utr3Start = tf.getStartPos();
                utr3Stop = tf.getStopPos();
            }
            else if( tf.getFeatureType()== TranscriptFeature.FeatureType.UTR5 ) {
                utr5Start = tf.getStartPos();
                utr5Stop = tf.getStopPos();
            }
        }

        Collections.sort(exons, new Comparator<TranscriptFeature>() {
            @Override
            public int compare(TranscriptFeature o1, TranscriptFeature o2) {
                int r = o1.getChromosome().compareTo(o2.getChromosome());
                if( r!=0 ) {
                    return r;
                }
                r = o1.getStartPos() - o2.getStartPos();
                if( r!=0 ) {
                    return r;
                }
                return o1.getStopPos() - o2.getStopPos();
            }
        });

        for( TranscriptFeature exon: exons ) {
            // case 1: exon overlaps utr3 region
            if( utr3Start>0 && utr3Stop>0 ) {
                int result = handleUtr(utr3Start, utr3Stop, exon, TranscriptFeature.FeatureType.UTR3, utr3, cds);
                if( result<0 ) {
                    transcriptHasIssues++;
                }
                if( result!=0 ) {
                    continue;
                }
            }

            // case 2: exon overlaps utr5 region
            if( utr5Start>0 && utr5Stop>0 ) {
                int result = handleUtr(utr5Start, utr5Stop, exon, TranscriptFeature.FeatureType.UTR5, utr5, cds);
                if( result<0 ) {
                    transcriptHasIssues++;
                }
                if( result!=0 ) {
                    continue;
                }
            }

            TranscriptFeature tfCds = exon.clone();
            tfCds.setFeatureType(TranscriptFeature.FeatureType.CDS);
            cds.add(tfCds);
        }

        if( transcriptHasIssues>0 ) {
            System.out.println("Transcript has issues: TR_RGD_ID:"+rgdId+", MAP_KEY="+mapKey);
        }
        List<TranscriptFeature> canonicalFeatures = new ArrayList<>(exons);
        canonicalFeatures.addAll(utr3);
        canonicalFeatures.addAll(utr5);
        canonicalFeatures.addAll(cds);
        return canonicalFeatures;
    }

    //
    private int handleUtr(int utrStart, int utrStop, TranscriptFeature exon, TranscriptFeature.FeatureType ftype,
                         List<TranscriptFeature> utrs, List<TranscriptFeature> cds) throws CloneNotSupportedException{

        if( utrStart==0 || utrStop==0 ) {
            return 0;
        }

        if (exon.getStopPos() >= utrStart  &&  exon.getStartPos()<= utrStop ) {
            // case 1a: utr spans over entire exon
            if( exon.getStartPos()>=utrStart && exon.getStopPos()<=utrStop ) {
                TranscriptFeature tfUtr = exon.clone();
                tfUtr.setFeatureType(ftype);
                utrs.add(tfUtr);
            }
            // case 1b: utr spans left part of an exon
            else if( exon.getStartPos()>=utrStart && exon.getStopPos()>utrStop ) {
                TranscriptFeature tfUtr = exon.clone();
                tfUtr.setFeatureType(ftype);
                tfUtr.setStopPos(utrStop);
                utrs.add(tfUtr);

                TranscriptFeature tfCds = exon.clone();
                tfCds.setFeatureType(TranscriptFeature.FeatureType.CDS);
                tfCds.setStartPos(utrStop+1);
                cds.add(tfCds);
            }
            // case 1c: utr spans right part of an exon
            else if( exon.getStartPos()<utrStart && exon.getStopPos()<=utrStop ) {
                TranscriptFeature tfUtr = exon.clone();
                tfUtr.setFeatureType(ftype);
                tfUtr.setStartPos(utrStart);
                utrs.add(tfUtr);

                TranscriptFeature tfCds = exon.clone();
                tfCds.setFeatureType(TranscriptFeature.FeatureType.CDS);
                tfCds.setStopPos(utrStart-1);
                cds.add(tfCds);
            }
            // case 1d: unexpected
            else {
                return -1;
            }
            return 1;
        }
        return 0;
    }

    /**
     * return coordinates for all features, like exons, belonging to given gene
     * @param geneRgdId rgd id of a gene
     * @return list of coordinates of transcript feature(s) of given kind
     * @throws Exception on error in spring framework
     */
    public List<TranscriptFeature> getFeaturesForGene(int geneRgdId) throws Exception {

        String query = "select m.*,t.TRANSCRIPT_RGD_ID,r.OBJECT_KEY "+
            "from MAPS_DATA m,TRANSCRIPT_FEATURES f,TRANSCRIPTS t,RGD_IDS r "+
            "where m.RGD_ID=f.FEATURE_RGD_ID and t.GENE_RGD_ID=? and t.TRANSCRIPT_RGD_ID=f.TRANSCRIPT_RGD_ID and f.FEATURE_RGD_ID=r.RGD_ID";

        TranscriptFeatureQuery q = new TranscriptFeatureQuery(this.getDataSource(), query);
        return execute(q, geneRgdId);
    }
    /**
     * get list of features tied up to given genomic position
     * @param mapKey map key for the genomic map being used
     * @param chromosome chromosome on the map
     * @param startPos start position
     * @return list of all transcript feature(s) that start at given genomic location
     * @throws Exception on error in framework
     */
    public List<MapData> getFeaturesByGenomicPos(int mapKey, String chromosome, int startPos) throws Exception {

        String query = "select * from MAPS_DATA where MAP_KEY=? and CHROMOSOME=? AND START_POS=?";
        return mapDAO.executeMapDataQuery(query, mapKey, chromosome, startPos);
    }

    /**
     * get list of features tied up to given genomic position
     * @param mapKey map key for the genomic map being used
     * @param chromosome chromosome on the map
     * @param startPos start position
     * @return list of all transcript feature(s) that start at given genomic location
     * @throws Exception on error in framework
     */
    public List<MapData> getFeaturesByGenomicPos(int mapKey, String chromosome, int startPos, int stopPos, int featureId) throws Exception {

        String query = "SELECT * FROM maps_data md, rgd_ids ri " +
                "where md.rgd_id=ri.rgd_id and ri.object_key=? and md.MAP_KEY=? " +
                "and md.CHROMOSOME=? and ri.object_status='ACTIVE' AND md.START_POS>=? " +
                "and md.stop_pos<=? order by start_pos";

        return mapDAO.executeMapDataQuery(query, featureId,mapKey, chromosome, startPos, stopPos);
    }

    /**
     * create a new transcript object and assign genomic coordinates
     * @param tr transcript object
     * @param speciesTypeKey species type key
     * @return number of rows affected
     * @throws Exception on error in spring framework
     */
    public int createTranscript(Transcript tr, int speciesTypeKey) throws Exception {

        // currently transcript objects are NOT annotated; if a transcript object does not have a rows
        // in TRANSCRIPTS table, it will never be found
        // create rgd id for transcript object
        RgdId rgdId = rgdDAO.createRgdId(RgdId.OBJECT_KEY_TRANSCRIPTS, "ACTIVE", "", speciesTypeKey);
        tr.setRgdId(rgdId.getRgdId());

        // add an entry to transcripts table
        String query = "INSERT INTO transcripts (transcript_rgd_id,gene_rgd_id,acc_id,is_non_coding_ind,refseq_status,"+
            "protein_acc_id,peptide_label,biotype) VALUES(?,?,?,?,?,?,?,?)";
        int rowsAffected = update(query, rgdId.getRgdId(), tr.getGeneRgdId(), tr.getAccId(), tr.isNonCoding()?"Y":"N",
                tr.getRefSeqStatus(), tr.getProteinAccId(), tr.getPeptideLabel(), tr.getType());

        tr.setDateCreated(new Date());

        // now add genomic coordinates for transcript
        for( MapData md: tr.getGenomicPositions() ) {
            md.setRgdId(rgdId.getRgdId());
        }
        rowsAffected += mapDAO.insertMapData(tr.getGenomicPositions());
        
        return rowsAffected;
    }

    /**
     * update transcript object; TRANSCRIPT_RGD_ID will be used to identify the transcript
     * @param tr transcript object
     * @return number of rows affected
     * @throws Exception on error in spring framework
     */
    public int updateTranscript(Transcript tr) throws Exception {

        // update a row in transcripts table
        String query = "UPDATE transcripts "+
                "SET gene_rgd_id=?,acc_id=?,is_non_coding_ind=?,refseq_status=?,protein_acc_id=?,peptide_label=?,biotype=? "+
                "WHERE transcript_rgd_id=?";

        return update(query, tr.getGeneRgdId(), tr.getAccId(), tr.isNonCoding()?"Y":"N",
                tr.getRefSeqStatus(), tr.getProteinAccId(), tr.getPeptideLabel(), tr.getType(), tr.getRgdId());
    }

    /**
     * delete transcript together with its genomic positions and list of transcript features;
     * the transcript rgd id itself is never deleted - it is withdrawn (if mergedTrRgdId is 0) or retired (if mergedTrRgdId is non zero)
     * @param transcriptRgdId transcript rgd id
     * @param mergedTrRgdId merged transcript rgd id
     * @return nr of rows affected
     * @throws Exception
     */
    public int deleteTranscript(int transcriptRgdId, int mergedTrRgdId) throws Exception {

        int rowsAffected = 0;

        // delete maps positions for transcript
        String query = "delete from MAPS_DATA where RGD_ID=?";
        rowsAffected += update(query, transcriptRgdId);

        // delete transcript features
        query = "delete from TRANSCRIPT_FEATURES where TRANSCRIPT_RGD_ID=?";
        rowsAffected += update(query, transcriptRgdId);

        // delete the transcript itself
        query = "delete from TRANSCRIPTS where TRANSCRIPT_RGD_ID=?";
        rowsAffected += update(query, transcriptRgdId);

        // if this transcript was merged, retire it
        RgdId rgdId = rgdDAO.getRgdId2(transcriptRgdId);
        if( mergedTrRgdId!=0 ) {
            rgdDAO.recordIdHistory(transcriptRgdId, mergedTrRgdId);
            rgdId.setObjectStatus("RETIRED");
            String notes = "merged with transcript rgd id "+mergedTrRgdId;
            if( rgdId.getNotes()==null )
                rgdId.setNotes(notes);
            else
                rgdId.setNotes(rgdId.getNotes()+"; "+notes);
        }
        else {
            // otherwise withdraw it
            rgdId.setObjectStatus("WITHDRAWN");
        }
        rgdId.setLastModifiedDate(new Date());
        rgdDAO.updateRgdId(rgdId);
        rowsAffected++;

        return rowsAffected;
    }

    /**
     * create a new transcript feature object
     * @param tf transcript feature
     * @param speciesTypeKey species type key
     * @return number of rows affected
     * @throws Exception on error in framework
     */
    public int createFeature(TranscriptFeature tf, int speciesTypeKey) throws Exception {

        int affectedRows = 0;

        // create rgd id for transcript feature object
        RgdId rgdId = rgdDAO.createRgdId(TranscriptFeature.getObjectKey(tf.getFeatureType()), "ACTIVE", "", speciesTypeKey);
        tf.setRgdId(rgdId.getRgdId());
        affectedRows++;

        // add an entry to transcripts table
        bindFeatureToTranscript(tf.getTranscriptRgdId(), rgdId.getRgdId());
        affectedRows++;

        // create a mapping for transcript
        mapDAO.insertMapData(tf);    
        affectedRows++;

        return affectedRows;
    }

    /**
     * bind existing feature object to a transcript
     * @param transcriptRgdId transcript rgd id
     * @param featureRgdId rgd id of transcript feature
     * @throws Exception on error in framework
     */
    public void bindFeatureToTranscript(int transcriptRgdId, int featureRgdId) throws Exception {

        // add an entry to transcripts table
        String query = "insert into TRANSCRIPT_FEATURES (TRANSCRIPT_RGD_ID,FEATURE_RGD_ID) VALUES(?,?)";
        update(query, transcriptRgdId, featureRgdId);
    }

    /**
     * unbind feature object from a transcript
     * @param transcriptRgdId transcript rgd id
     * @param featureRgdId rgd id of transcript feature
     * @throws Exception on error in framework
     */
    public int unbindFeatureFromTranscript(int transcriptRgdId, int featureRgdId) throws Exception {

        String query = "DELETE FROM transcript_features WHERE transcript_rgd_id=? AND feature_rgd_id=?";
        return update(query, transcriptRgdId, featureRgdId);
    }

    /**
     * delete feature and map positions from rgd
     * @param featureRgdId feature rgd id
     * @return number of rows affected
     * @throws Exception on error in framework
     */
    public int deleteFeature(int featureRgdId) throws Exception {

        int rowsAffected = 0;

        // delete maps position for transcript feature
        String query = "delete from MAPS_DATA where RGD_ID=?";
        Object[] oa = new Object[]{featureRgdId};
        rowsAffected += update(query, oa);

        // delete the transcript feature itself
        query = "delete from TRANSCRIPT_FEATURES where FEATURE_RGD_ID=?";
        rowsAffected += update(query, oa);

        // delete the transcript rgd_id
        query = "delete from RGD_IDS where RGD_ID=?";
        rowsAffected += update(query, oa);

        return rowsAffected;
    }


    public List<Transcript> getTranscripts(int mapKey) throws Exception {
        String query = "SELECT t.* " +
                "FROM transcripts t, genes g, maps_data md "+
                "WHERE g.rgd_id = t.gene_rgd_id AND md.rgd_id=g.rgd_id AND map_key=?";
        return executeTranscriptQuery(query, mapKey);
    }

    /**
     * detach a transcript from gene, by removing a row from TRANSCRIPTS table;
     * if the transcript is associated with only one gene, the transcript features must be detached too
     *
     * @param transcriptRgdId transcript rgd id
     * @param geneRgdId gene rgd id
     * @return number of rows affected
     * @throws Exception on error in framework
     */
    public int detachTranscriptFromGene(int transcriptRgdId, int geneRgdId) throws Exception {

        String sql = "SELECT COUNT(DISTINCT gene_rgd_id) FROM transcripts "+
                "WHERE transcript_rgd_id=? AND gene_rgd_id<>?";
        int countOfOtherGenesForThisTranscript = getCount(sql, transcriptRgdId, geneRgdId);
        if( countOfOtherGenesForThisTranscript==0 ) {
            // delete transcript features
            sql = "DELETE FROM transcript_features WHERE transcript_rgd_id=?";
            update(sql, transcriptRgdId);
        }

        sql = "DELETE FROM transcripts WHERE transcript_rgd_id=? AND gene_rgd_id=?";
        return update(sql, transcriptRgdId, geneRgdId);
    }

    /// Transcript query implementation helper
    public List<Transcript> executeTranscriptQuery(String query, Object ... params) throws Exception {
        TranscriptQuery q = new TranscriptQuery(this.getDataSource(), query);
        List<Transcript> transcripts = execute(q, params);

        // now load genomic positions
        if( transcripts!=null ) {
            for( Transcript transcript: transcripts ) {
                transcript.setGenomicPositions(mapDAO.getMapData(transcript.getRgdId()));
            }
        }
        return transcripts;
    }
}


