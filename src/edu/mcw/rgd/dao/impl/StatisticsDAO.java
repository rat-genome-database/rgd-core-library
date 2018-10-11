package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.datamodel.Portal;
import edu.mcw.rgd.datamodel.PortalCat;
import edu.mcw.rgd.datamodel.RgdId;
import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.process.Utils;

import java.sql.*;
import java.util.*;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 19, 2008
 * Time: 1:31:52 PM
 */
public class StatisticsDAO extends AbstractDAO {

    PortalDAO portalDAO = new PortalDAO();

    public java.util.Map<String,String> getActiveCount(int speciesTypeKey) throws Exception {
        String sql = "SELECT count(distinct(ri.rgd_id)) as tot, ro.object_name from rgd_ids ri, rgd_objects ro " +
                "where ri.object_key = ro.object_key and ri.object_status = 'ACTIVE' ";

        if (speciesTypeKey != 0) {
            sql += "AND ri.species_type_key=? ";
        }

        sql += " GROUP BY ro.object_name ORDER BY ro.object_name";
        return statQuery(speciesTypeKey, sql);
    }

    public java.util.Map<String,String> getRetiredCount(int speciesTypeKey) throws Exception {
        String sql = "select count(distinct(ri.rgd_id)) as tot, ro.object_name from rgd_ids ri, rgd_objects ro " +
                "where ri.object_key = ro.object_key and ri.object_status = 'RETIRED' ";

        if (speciesTypeKey != 0) {
            sql += "and ri.species_type_key=? ";
        }

        sql += " group by ro.object_name order by ro.object_name";
        return statQuery(speciesTypeKey, sql);

    }

    public java.util.Map<String,String> getWithdrawnCount(int speciesTypeKey) throws Exception {
        String sql = "select count(distinct(ri.rgd_id)) as tot, ro.object_name from rgd_ids ri, rgd_objects ro " +
                "where ri.object_key = ro.object_key and ri.object_status = 'WITHDRAWN' ";

        if (speciesTypeKey != 0) {
            sql += "and ri.species_type_key=? ";
        }

        sql += " group by ro.object_name order by ro.object_name";
        return statQuery(speciesTypeKey, sql);

    }



    public java.util.Map<String,String> getObjectReferenceCount(int speciesTypeKey) throws Exception {
        String sql = "select count(distinct(rr.ref_key)) as tot, ro.object_name " +
                "from rgd_ref_rgd_id rr, rgd_ids ri, rgd_objects ro where ri.object_key=ro.object_key and ri.rgd_id = rr.RGD_ID " +
                "and ri.OBJECT_STATUS='ACTIVE' ";

        if (speciesTypeKey != 0) {
            sql += "and ri.species_type_key=? ";
        }

        sql += " and rr.ref_key in ( " +
                "select ir.ref_key from references ir, rgd_ids iri where ir.rgd_id = iri.rgd_id and iri.object_status='ACTIVE') " +
                "group by ro.object_name order by ro.object_name ";

        return statQuery(speciesTypeKey, sql);
    }

    public java.util.Map<String,String> getGeneTypeCount(int speciesTypeKey) throws Exception {

        String sql = "SELECT COUNT(g.rgd_id) AS tot, gene_type_lc AS object_name "+
                "FROM genes g, rgd_ids r " +
                "WHERE r.rgd_id=g.rgd_id AND r.object_status='ACTIVE' ";
        if (speciesTypeKey != 0) {
            sql += "AND r.species_type_key=? ";
        }
        sql += "GROUP BY gene_type_lc ORDER BY gene_type_lc";

        Map types = statQuery(speciesTypeKey, sql);


        // compute counts for a subset of ncRNA genes: microRNA genes
        sql = "SELECT COUNT(g.rgd_id) AS tot, 'ncrna(microRNA)' AS object_name "+
                "FROM genes g, rgd_ids r " +
                "WHERE r.rgd_id=g.rgd_id AND r.object_status='ACTIVE' "+
                " AND gene_type_lc='ncrna' AND full_name_lc LIKE 'microrna%' ";
        if (speciesTypeKey != 0) {
            sql += "AND r.species_type_key=? ";
        }
        sql += "GROUP BY gene_type_lc ORDER BY gene_type_lc";

        types.putAll(statQuery(speciesTypeKey, sql));


        // compute counts for confirmed/predicted mirna targets
        sql = "SELECT COUNT(gene_rgd_id) AS tot, 'microRNA target '||target_type AS object_name " +
                "FROM rgd_ids r, " +
                " (SELECT DISTINCT gene_rgd_id,target_type FROM mirna_targets) t " +
                " WHERE r.rgd_id=gene_rgd_id AND r.object_status='ACTIVE' ";
        if (speciesTypeKey != 0) {
            sql += "AND r.species_type_key=? ";
        }
        sql += " GROUP BY target_type";

        types.putAll(statQuery(speciesTypeKey, sql));



        sql = "select count(*) as tot, 'protein-coding(XM Only)' as object_name from (    " +
                "    select r.rgd_id  from rgd_acc_xdb rx, rgd_ids r, genes g  " +
                "    where r.rgd_id = g.rgd_id and r.RGD_ID=rx.RGD_ID and r.OBJECT_STATUS='ACTIVE' and r.OBJECT_KEY=1  ";

        if (speciesTypeKey !=0) {
            sql += " and r.species_type_key=? ";
        }

        sql += "    and rx.XDB_KEY=1 " +
        "    and g.gene_type_lc='protein-coding' " +
        "    and substr(rx.acc_id, 0, 3) = 'XM_' " +
        "    minus " +
        "    select r.rgd_id  from rgd_acc_xdb rx , rgd_ids r, genes g " +
        "    where r.rgd_id = g.rgd_id and r.RGD_ID=rx.RGD_ID and r.OBJECT_STATUS='ACTIVE' and r.OBJECT_KEY=1  ";

        if (speciesTypeKey !=0) {
            sql += " and r.species_type_key= " + speciesTypeKey;
        }


        sql +="    and rx.XDB_KEY=1 " +
        "    and g.gene_type_lc='protein-coding' " +
        "    and substr(rx.acc_id, 0, 3) = 'NM_' " +
        ")";

        types.putAll(statQuery(speciesTypeKey, sql));


        sql = "select count(*) as tot, 'protein-coding(NM)' as object_name from (    " +
                "    select r.rgd_id  from rgd_acc_xdb rx , rgd_ids r, genes g  " +
                "    where r.rgd_id = g.rgd_id and r.RGD_ID=rx.RGD_ID and r.OBJECT_STATUS='ACTIVE' and r.OBJECT_KEY=1  ";

        if (speciesTypeKey !=0) {
            sql += " and r.species_type_key=? ";
        }

        sql +="    and rx.XDB_KEY=1 " +
        "    and g.gene_type_lc='protein-coding' " +
        "    and substr(rx.acc_id, 0, 3) = 'NM_' " +
        ")    ";

        types.putAll(statQuery(speciesTypeKey, sql));

        return types;
    }

    public java.util.Map<String,String> getObjectWithReferenceSequenceCount(int speciesTypeKey) throws Exception {
        String sql = "select count(*) as tot, substr(rx.acc_id, 0, 3) as object_name  from rgd_acc_xdb rx " +
                "join rgd_ids r on r.RGD_ID=rx.RGD_ID " +
                "where r.OBJECT_STATUS='ACTIVE' " +
                "and r.OBJECT_KEY=1 ";
        if (speciesTypeKey != 0) {
            sql += "and r.species_type_key=? ";
        }

        sql += "and rx.XDB_KEY=1 " +
                "and rx.ACC_ID like '%~_%'  ESCAPE '~' " +
                "group by substr(rx.acc_id, 0, 3) ";

        Map first =  statQuery(speciesTypeKey, sql);

        sql = "        select count(*) as tot, 'XM_ Only' as object_name from (" +
                "        select r.rgd_id  from rgd_acc_xdb rx join rgd_ids r " +
                "        on r.RGD_ID=rx.RGD_ID where r.OBJECT_STATUS='ACTIVE' and r.OBJECT_KEY=1";

                if (speciesTypeKey !=0) {
                    sql += " and r.species_type_key=? ";
                }

                sql +="        and rx.XDB_KEY=1 " +
                "        and substr(rx.acc_id, 0, 3) = 'XM_' " +
                "        minus " +
                "        select r.rgd_id  from rgd_acc_xdb rx join rgd_ids r  " +
                "        on r.RGD_ID=rx.RGD_ID where r.OBJECT_STATUS='ACTIVE' and r.OBJECT_KEY=1";

                if (speciesTypeKey !=0) {
                    sql += " and r.species_type_key= " + speciesTypeKey;
                }

                sql += "        and rx.XDB_KEY=1 " +
                "        and substr(rx.acc_id, 0, 3) = 'NM_' " +
                "        )";

         first.putAll(statQuery(speciesTypeKey, sql));
         return first;

    }

    public java.util.Map<String,String> getRGDObjectCount(int speciesTypeKey) throws Exception {
        String sql = "SELECT COUNT(*) tot, object_status AS object_name FROM rgd_ids ";

        if (speciesTypeKey != 0) {
            sql += "WHERE species_type_key=? ";
        }

        sql += "GROUP BY object_status ORDER BY object_status";
        return statQuery(speciesTypeKey, sql);
    }

    public java.util.Map<String,String> getProteinInteractionCount(int speciesTypeKey) throws Exception {

        String sql = "SELECT COUNT(*) tot, 'protein interactions' AS object_name FROM interactions \n" +
                "WHERE EXISTS (SELECT 1 FROM rgd_ids r WHERE (rgd_id_1=rgd_id OR rgd_id_2=rgd_id) AND object_status='ACTIVE' %SPECIES%)\n" +
                "UNION ALL\n" +
                "SELECT COUNT(*) tot, 'genes with protein interactions' AS object_name FROM genes g,rgd_ids r \n" +
                "WHERE g.rgd_id=r.rgd_id AND object_status='ACTIVE' %SPECIES%\n" +
                " AND EXISTS (SELECT 1 FROM interactions i,rgd_associations a WHERE g.rgd_id=detail_rgd_id \n" +
                "    AND assoc_type='protein_to_gene' AND (rgd_id_1=master_rgd_id OR rgd_id_2=master_rgd_id))";

        String speciesPhrase = speciesTypeKey==0 ? "" : "AND species_type_key=?";
        sql = sql.replace("%SPECIES%", speciesPhrase);

        return statQuery(speciesTypeKey, sql);
    }

    public java.util.Map<String,String> getObjectsWithXDBsCount(int speciesTypeKey, int objectKey) throws Exception {
        String sql = "SELECT COUNT(DISTINCT(r.rgd_id)) tot, rx.xdb_name object_name FROM rgd_acc_xdb ra, rgd_ids r, rgd_xdb rx " +
                     "WHERE ra.xdb_key=rx.xdb_key AND r.rgd_id=ra.rgd_id ";

        if (speciesTypeKey != 0) {
            sql += "AND r.species_type_key=? ";
        }
        if (objectKey != 0) {
            sql += "AND r.object_key=? ";
        }

        sql += "AND r.object_status='ACTIVE' " +
                "GROUP BY rx.xdb_name ";

        // artificially compute counts for objects having any of xdb ids
        sql += " UNION ALL ";
        sql += "SELECT COUNT(DISTINCT(r.rgd_id)) tot, 'Any XDB' object_name FROM rgd_acc_xdb ra, rgd_ids r " +
                "WHERE r.rgd_id=ra.rgd_id ";

        if (speciesTypeKey != 0) {
            sql += "AND r.species_type_key=? ";
        }
        if (objectKey != 0) {
            sql += "AND r.object_key=? ";
        }

        sql += "AND r.object_status='ACTIVE' " +
                "ORDER BY object_name";

        return statQuery(speciesTypeKey, objectKey, sql);
    }

    public java.util.Map<String,String> getXDBsCount(int speciesTypeKey) throws Exception {
        String sql = "select count(rx.xdb_key) as tot, rx.xdb_name as object_name from rgd_acc_xdb ra, rgd_ids r, rgd_xdb rx " +
                     "where ra.xdb_key = rx.xdb_key and r.RGD_ID=ra.RGD_ID ";

        if (speciesTypeKey != 0) {
            sql += "and r.species_type_key=? ";
        }

        sql += "and r.OBJECT_STATUS='ACTIVE' " +
                "and r.OBJECT_KEY=1 " +
                "group by rx.xdb_name order by rx.xdb_name";


        return statQuery(speciesTypeKey, sql);

    }

    public java.util.Map<String,String> getStrainTypeCount(int speciesTypeKey) throws Exception {
        String sql = "select count(s.RGD_ID) as tot, strain_type_name_lc as object_name from strains s " +
                "join rgd_ids r on r.RGD_ID=s.RGD_ID " +
                "where r.OBJECT_STATUS='ACTIVE' ";

        if (speciesTypeKey != 0) {
            sql += "and r.species_type_key=? ";
        }

        sql += "group by strain_type_name_lc order by strain_type_name_lc   ";
        return statQuery(speciesTypeKey, sql);
    }

    public java.util.Map<String,String> getQTLInheritanceTypeCount(int speciesTypeKey) throws Exception {

        String sql = "select count(q.RGD_ID) as tot, q.inheritance_type as object_name FROM qtls q " +
                "JOIN rgd_ids r ON r.rgd_id=q.rgd_id " +
                "WHERE r.object_status='ACTIVE' ";
        if (speciesTypeKey != 0) {
            sql += "AND r.species_type_key=? ";
        }
        sql += "GROUP BY q.inheritance_type ORDER BY q.inheritance_type";

        return statQuery(speciesTypeKey, sql);
    }

    /**
     * return counts of unique references with annotations, per ontologies
     * @return map of counts of unique references with annotations, per ontology
     * @throws Exception
     */
    public java.util.Map<String,String> getAnnotatedReferencesCount(int speciesTypeKey) throws Exception {

        String sql = "SELECT COUNT(DISTINCT ref_rgd_id) tot, o.ont_name object_name \n" +
                "  FROM full_annot a,ont_terms t,ontologies o,rgd_ids r \n" +
                "  WHERE a.term_acc=t.term_acc AND t.ont_id=o.ont_id AND t.is_obsolete=0 \n" +
                "    AND annotated_object_rgd_id=r.rgd_id AND r.object_status='ACTIVE' ";
        if (speciesTypeKey != 0) {
            sql += "AND r.species_type_key=? ";
        }
        sql += "  GROUP BY ont_name \n" +
               "UNION ALL \n" +
               "SELECT COUNT(DISTINCT ref_rgd_id) tot, 'All Ontologies' object_name \n" +
               "  FROM full_annot a,ont_terms t,rgd_ids r \n" +
               "  WHERE a.term_acc=t.term_acc  AND t.is_obsolete=0 \n" +
               "    AND annotated_object_rgd_id=r.rgd_id AND r.object_status='ACTIVE' ";
        if (speciesTypeKey != 0) {
            sql += "AND r.species_type_key=? ";
        }

        return statQuery(speciesTypeKey, sql);
    }

    /**
     * return counts of active terms within ontologies
     * @return map of counts of active terms within an ontology
     * @throws Exception
     */
    public java.util.Map<String,String> getOntologyTermCount() throws Exception {

        String sql = "SELECT COUNT(*) tot, o.ont_name object_name "+
            "FROM ont_terms t,ontologies o WHERE t.ont_id=o.ont_id AND t.is_obsolete=0 "+
            "GROUP BY ont_name "+
            "UNION ALL "+
            "SELECT COUNT(*) tot, 'RDO_custom' object_name "+
            "FROM ont_terms t "+
            "WHERE t.term_acc LIKE 'DOID:90_____' AND t.is_obsolete=0";

        return statQuery(SpeciesType.ALL, sql);
    }

    /**
     * return counts of active and annotated terms per ontology
     * @param speciesTypeKey species type key
     * @return map of counts of active and annotated terms within an ontology
     * @throws Exception
     */
    public java.util.Map<String,String> getOntologyAnnotatedTermCount(int speciesTypeKey) throws Exception {

        String sqlAnnot;
        if( speciesTypeKey==SpeciesType.ALL )
            sqlAnnot = " AND EXISTS (SELECT 1 FROM full_annot a WHERE a.term_acc=t.term_acc) ";
        else
            sqlAnnot = " AND EXISTS (SELECT 1 FROM full_annot a,rgd_ids r WHERE a.term_acc=t.term_acc AND a.annotated_object_rgd_id=r.rgd_id AND species_type_key=?) ";

        String sql = "SELECT COUNT(*) tot, o.ont_name object_name "+
            "FROM ont_terms t,ontologies o WHERE t.ont_id=o.ont_id AND t.is_obsolete=0 "+
            sqlAnnot+
            "GROUP BY ont_name "+
            "UNION ALL "+
            "SELECT COUNT(*) tot, 'RDO_custom' object_name "+
            "FROM ont_terms t "+
            "WHERE t.term_acc LIKE 'DOID:90_____' AND t.is_obsolete=0 " +
            sqlAnnot;

        return statQuery(speciesTypeKey, sql);
    }

    /**
     * return counts of active annotations per ontology, species and rgd object key
     * @param speciesTypeKey species type key
     * @param objectKey RGD object key; '1'-genes, '5'-strains, '6'-qtls, etc; if 0, object of any type will match
     * @return map of counts of active annotations to terms of given ontology
     * @throws Exception
     */
    public java.util.Map<String,String> getOntologyAnnotationCount(int speciesTypeKey, int objectKey) throws Exception {

        return getOntologyAnnotationCount(speciesTypeKey, objectKey, false);
    }

    /**
     * return counts of active manual annotations per ontology, species and rgd object key
     * @param speciesTypeKey species type key
     * @param objectKey RGD object key; '1'-genes, '5'-strains, '6'-qtls, etc; if 0, object of any type will match
     * @return map of counts of active annotations to terms of given ontology
     * @throws Exception
     */
    public java.util.Map<String,String> getOntologyManualAnnotationCount(int speciesTypeKey, int objectKey) throws Exception {

        return getOntologyAnnotationCount(speciesTypeKey, objectKey, true);
    }

    private java.util.Map<String,String> getOntologyAnnotationCount(int speciesTypeKey, int objectKey, boolean manual) throws Exception {

        String sql = "SELECT COUNT(*) tot, ont_name object_name "+
            "FROM full_annot a,rgd_ids r,( "+
              "SELECT t.term_acc,o.ont_name FROM ont_terms t,ontologies o "+
              "WHERE t.ont_id=o.ont_id AND t.is_obsolete=0 "+
              "UNION  ALL "+
              "SELECT t.term_acc, 'RDO_custom' ont_name FROM ont_terms t "+
              "WHERE t.term_acc LIKE 'DOID:90_____' AND t.is_obsolete=0 "+
              ") x "+
            "WHERE x.term_acc=a.term_acc "+
            "  AND a.annotated_object_rgd_id=r.rgd_id AND r.object_status='ACTIVE' ";

        if (speciesTypeKey != 0) {
            sql += "AND r.species_type_key=? ";
        }
        if (objectKey != 0) {
            sql += "AND object_key=? ";
        }
        if( manual ) {
            // exclude evidence codes for automatic annotations
            sql += "AND evidence NOT IN('IEA','ISO','ISM','ISA','ISS','IGC','IBA','IBD','IKR','IRD','RCA') " +
            // include annotations originating in RGD
                   " AND data_src='RGD'";
        }
        sql += "GROUP BY ont_name";

        return statQuery(speciesTypeKey, objectKey, sql);
    }

    /**
     * return counts of annotated objects per ontology
     * @param speciesTypeKey species type key
     * @param objectKey object key; if 0, return count of annotated objects of any type
     * @return map of counts of active annotations to terms of given ontology
     * @throws Exception
     */
    public java.util.Map<String,String> getOntologyAnnotatedObjectCount(int speciesTypeKey, int objectKey) throws Exception {

        return getOntologyAnnotatedObjectCount(speciesTypeKey, objectKey, false);
    }

    /**
     * return counts of manually annotated objects per ontology
     * @param speciesTypeKey species type key
     * @param objectKey object key; if 0, return count of annotated objects of any type
     * @return map of counts of active annotations to terms of given ontology
     * @throws Exception
     */
    public java.util.Map<String,String> getOntologyManuallyAnnotatedObjectCount(int speciesTypeKey, int objectKey) throws Exception {

        return getOntologyAnnotatedObjectCount(speciesTypeKey, objectKey, true);
    }

    private java.util.Map<String,String> getOntologyAnnotatedObjectCount(int speciesTypeKey, int objectKey, boolean manual) throws Exception {

        String sql = "SELECT COUNT(DISTINCT rgd_id) tot, x.ont_name object_name "+
            "FROM full_annot a,rgd_ids r,( "+
              "SELECT t.term_acc,o.ont_name FROM ont_terms t,ontologies o "+
              "WHERE t.ont_id=o.ont_id AND t.is_obsolete=0 "+
              "UNION ALL "+
              "SELECT t.term_acc, 'RDO_custom' ont_name FROM ont_terms t "+
              "WHERE t.term_acc LIKE 'DOID:90_____' AND t.is_obsolete=0 "+
              ") x "+
            "WHERE x.term_acc=a.term_acc "+
            "  AND a.annotated_object_rgd_id=r.rgd_id AND r.object_status='ACTIVE' ";

        if (speciesTypeKey != 0) {
            sql += "AND r.species_type_key=? ";
        }
        if (objectKey != 0) {
            sql += "AND object_key=? ";
        }
        if( manual ) {
            // exclude evidence codes for automatic annotations
            sql += "AND evidence NOT IN('IEA','ISO','ISM','ISA','ISS','IGC','IBA','IBD','IKR','IRD','RCA') " +
            // include annotations originating in RGD
                   " AND data_src='RGD' ";
        }
        sql += "GROUP BY ont_name ";

        // UNION WITH TOTAL OBJECTS FOR ALL ONTOLOGIES
        sql += "UNION ALL ";

        sql += "SELECT COUNT(DISTINCT rgd_id) tot, 'All Ontologies' object_name "+
            "FROM full_annot a,ont_terms t,rgd_ids r "+
            "WHERE t.is_obsolete=0 AND t.term_acc=a.term_acc "+
            "  AND a.annotated_object_rgd_id=r.rgd_id AND r.object_status='ACTIVE' ";

        if (speciesTypeKey != 0) {
            sql += "AND r.species_type_key=? ";
        }
        if (objectKey != 0) {
            sql += "AND object_key=? ";
        }
        if( manual ) {
            // exclude evidence codes for automatic annotations
            sql += "AND evidence NOT IN('IEA','ISO','ISM','ISA','ISS','IGC','IBA','IBD','IKR','IRD','RCA') " +
            // include annotations originating in RGD
                   " AND data_src='RGD' ";
        }

        return statQuery(speciesTypeKey, objectKey, sql);
    }

    /**
     * return counts of annotated objects per portal
     * @param speciesTypeKey species type key
     * @param objectKey object key; if 0, return count of annotated objects of any type
     * @return map of counts of active annotated objects to portal
     * @throws Exception
     */
    public java.util.Map<String,String> getPortalAnnotatedObjectCount(int speciesTypeKey, int objectKey) throws Exception {

        TreeMap sb = new TreeMap();
        for(Portal portal: portalDAO.getPortals() ) {
            // process only 'Standard' portals
            if( !portal.getPortalType().equals("Standard") )
                continue;

            PortalCat cat = portalDAO.getSummaryForPortal(portal.getKey());
            if( cat!=null ) {
                sb.put(portal.getUrlName(), getPortalValue(speciesTypeKey, objectKey, cat.getSummaryTableHtml()));
            }
        }
        return sb;
    }

    /**
     * return name of the portal stat given species and object key
     * @param speciesTypeKey species type key
     * @param objectKey object key; if 0, return count of annotated objects of any type
     * @return name of the portal stat
     * @throws Exception
     */
    static public String getPortalStatName(int speciesTypeKey, int objectKey) {

        String name = "Portal ";
        name += SpeciesType.getCommonName(speciesTypeKey) + " ";
        if( objectKey!=0 )
            name += RgdId.getObjectTypeName(objectKey);
        else
            name += "Object";
        name += " Count";

        return name;
    }

    private String getPortalValue(int speciesTypeKey, int objectKey, String summaryTable) {

        int sum = 0;

        int ratGenesSum = extractValue(summaryTable, "genes-sum");
        int humanGenesSum = extractValue(summaryTable, "human-genes-sum");
        int mouseGenesSum = extractValue(summaryTable, "mouse-genes-sum");
        int ratQtlsSum = extractValue(summaryTable, "qtls-sum");
        int humanQtlsSum = extractValue(summaryTable, "human-qtls-sum");
        int mouseQtlsSum = extractValue(summaryTable, "mouse-qtls-sum");
        int ratStrainsSum = extractValue(summaryTable, "strains-sum");
        int humanStrainsSum = extractValue(summaryTable, "human-strains-sum");
        int mouseStrainsSum = extractValue(summaryTable, "mouse-strains-sum");

        // handle rats
        if( speciesTypeKey==SpeciesType.RAT ) {
            if( objectKey==RgdId.OBJECT_KEY_GENES )
                sum = ratGenesSum;
            else if( objectKey==RgdId.OBJECT_KEY_QTLS )
                sum = ratQtlsSum;
            else if( objectKey==RgdId.OBJECT_KEY_STRAINS )
                sum = ratStrainsSum;
            else
                sum = ratGenesSum + ratQtlsSum + ratStrainsSum;
        }
        // handle human
        else if( speciesTypeKey==SpeciesType.HUMAN ) {
            if( objectKey==RgdId.OBJECT_KEY_GENES )
                sum = humanGenesSum;
            else if( objectKey==RgdId.OBJECT_KEY_QTLS )
                sum = humanQtlsSum;
            else if( objectKey==RgdId.OBJECT_KEY_STRAINS )
                sum = humanStrainsSum;
            else
                sum = humanGenesSum + humanQtlsSum + humanStrainsSum;
        }
        // handle mouse
        else if( speciesTypeKey==SpeciesType.MOUSE ) {
            if( objectKey==RgdId.OBJECT_KEY_GENES )
                sum = mouseGenesSum;
            else if( objectKey==RgdId.OBJECT_KEY_QTLS )
                sum = mouseQtlsSum;
            else if( objectKey==RgdId.OBJECT_KEY_STRAINS )
                sum = mouseStrainsSum;
            else
                sum = mouseGenesSum + mouseQtlsSum + mouseStrainsSum;
        }
        // handle all species
        else if( speciesTypeKey==SpeciesType.ALL ){
            if( objectKey==RgdId.OBJECT_KEY_GENES )
                sum = ratGenesSum + humanGenesSum + mouseGenesSum;
            else if( objectKey==RgdId.OBJECT_KEY_QTLS )
                sum = ratQtlsSum + humanQtlsSum + mouseQtlsSum;
            else if( objectKey==RgdId.OBJECT_KEY_STRAINS )
                sum = ratStrainsSum + humanStrainsSum + mouseStrainsSum;
            else
                sum = ratGenesSum + humanGenesSum + mouseGenesSum
                    + ratQtlsSum + humanQtlsSum + mouseQtlsSum
                    + ratStrainsSum + humanStrainsSum + mouseStrainsSum;
        }
        return Integer.toString(sum);
    }

    private int extractValue(String html, String pattern) {

        String tagStart = "<span id=\""+pattern+"\">";
        String tagEnd = "</span>";
        int pos = html.indexOf(tagStart)+tagStart.length();
        String value = html.substring( pos, html.indexOf(tagEnd, pos) );
        return value.isEmpty() ? 0 : Integer.parseInt(value);
    }

    private java.util.Map<String,String> statQuery(int speciesTypeKey, String sql) throws Exception {

        return statQuery(speciesTypeKey, 0, sql);
    }

    private java.util.Map<String,String> statQuery(int speciesTypeKey, int objectKey, String sql) throws Exception {
        Connection conn = null;
        TreeMap<String,String> sb = new TreeMap<>();

        try {

            conn = this.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            int field = 0;
            if (speciesTypeKey !=0 ) {
                stmt.setInt(++field, speciesTypeKey);
            }
            if (objectKey !=0 ) {
                stmt.setInt(++field, objectKey);
            }

            // setup parameters for right part of UNION ALL clause
            if( sql.contains("UNION ALL") ) {
                if (speciesTypeKey !=0 ) {
                    stmt.setInt(++field, speciesTypeKey);
                }
                if (objectKey !=0 ) {
                    stmt.setInt(++field, objectKey);
                }
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sb.put(Utils.NVL(rs.getString("object_name"), "null"), rs.getString("tot" ));
            }

        } finally {
            try {
                conn.close();
            } catch (Exception ignored) {
            }
        }

        return sb;
    }

    public void persistStatMap(String type, int speciesTypeKey, Map<String,String> map) throws Exception {

        String sql = "INSERT INTO object_value_history " +
            "(object_value_history_id, object_type, object_name, object_value, creation_date, species_type_key) " +
            "VALUES(object_value_history_seq.nextval,?,?,?,TRUNC(SYSDATE),?)";

        for (Object o : map.keySet()) {
            String key = (String) o;

            update(sql, type, key, map.get(key), speciesTypeKey);
        }
    }

    public java.util.Map<String,String> getStatMap(String type, int speciesTypeKey, Date date) throws Exception {
        Connection conn = null;
        TreeMap<String,String> sb = new TreeMap<>();

        String sql = "SELECT * FROM object_value_history " +
                "WHERE species_type_key=? AND object_type=? AND creation_date=? ORDER BY object_value";

        try {
            conn = this.getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,speciesTypeKey);
            stmt.setString(2, type);
            stmt.setDate(3, new java.sql.Date(date.getTime()));

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                sb.put(Utils.NVL(rs.getString("object_name"), "null"), rs.getString("object_value"));
            }

        } finally {
            try {
                conn.close();
            } catch (Exception ignored) {
            }
        }

        return sb;
    }

    /**
     * get list of stat archive dates; sorted from most recent to the least
     * @return list of strings: stat archive dates; in format 'YYYY-MM-DD'
     * @throws Exception
     */
    public List<String> getStatArchiveDates() throws Exception {
        String sql = "SELECT DISTINCT(TO_CHAR(creation_date,'YYYY-MM-DD')) FROM object_value_history " +
                "ORDER BY TO_CHAR(creation_date,'YYYY-MM-DD') DESC";
        return StringListQuery.execute(this, sql);
    }
}