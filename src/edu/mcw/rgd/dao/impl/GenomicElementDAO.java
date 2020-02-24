package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.ExpressionDataQuery;
import edu.mcw.rgd.dao.spring.GenomicElementQuery;
import edu.mcw.rgd.datamodel.ExpressionData;
import edu.mcw.rgd.datamodel.GenomicElement;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author mtutaj
 * @since 2/22/12
 * handles all code regarding genomic elements and their attributes
 * <blockquote>
 *     Note: for effective searches on symbol, please create lower-case index on symbol field:
 *     CREATE INDEX genomic_elements_symbol_lc_i ON genomic_elements(LOWER(symbol));
 *     <br>
 *     Then Oracle will use that index for queries like that:
 *     SELECT * FROM genomic_elements WHERE LOWER(symbol)='symbol_lc';
 * </blockquote>
 * GENOMIC_ELEMENTS_VIEW contains genes,qtls,strains and sslps not kept in GENOMIC_ELEMENTS table yet
 * <pre>
CREATE VIEW GENOMIC_ELEMENTS_VIEW AS
SELECT RGD_ID,gene_type_lc OBJECT_TYPE,gene_symbol SYMBOL,full_name NAME,gene_desc DESCRIPTION,notes,null SOURCE,'SO:0000704' SO_ACC_ID
FROM GENES
UNION ALL
SELECT RGD_ID,inheritance_type OBJECT_TYPE,qtl_symbol SYMBOL,qtl_name NAME,null DESCRIPTION,notes,source_url SOURCE,'SO:0000771' SO_ACC_ID
FROM QTLS
UNION ALL
SELECT RGD_ID,strain_type_name_lc OBJECT_TYPE,strain_symbol SYMBOL,full_name NAME,null DESCRIPTION,notes,SOURCE,null SO_ACC_ID
FROM STRAINS
UNION ALL
SELECT RGD_ID,null OBJECT_TYPE,null SYMBOL,rgd_name NAME,null DESCRIPTION,notes,null SOURCE,'SO:0000207' SO_ACC_ID
FROM SSLPS
 </pre>
 */
public class GenomicElementDAO extends AbstractDAO {

    /**
     * get list of active genomic elements of given object key
     * @param objectKey object key
     * @return List of GenomicElement objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<GenomicElement> getActiveElements(int objectKey) throws Exception {

        String sql = "SELECT ge.*,r.species_type_key,r.object_status,r.object_key "+
                "FROM genomic_elements ge, rgd_ids r "+
                "WHERE r.object_key=? AND ge.rgd_id=r.rgd_id AND r.object_status='ACTIVE'";

        GenomicElementQuery q = new GenomicElementQuery(this.getDataSource(), sql);
        return execute(q, objectKey);
    }

    /**
     * get GenomicElement object given its rgd id
     * @param rgdId rgd id of genomic element
     * @return GenomicElement object or null if there is no row with given rgd_id in GENOMIC_ELEMENTS table
     * @throws Exception when unexpected error in spring framework occurs
     */
    public GenomicElement getElement(int rgdId) throws Exception {

        String sql = "SELECT ge.*,r.species_type_key,r.object_status,r.object_key "+
                "FROM genomic_elements ge, rgd_ids r "+
                "WHERE ge.rgd_id=? AND ge.rgd_id=r.rgd_id";

        GenomicElementQuery q = new GenomicElementQuery(this.getDataSource(), sql);
        List<GenomicElement> list = execute(q, rgdId);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * get GenomicElement object given its symbol and object type
     * @param symbol symbol of genomic element
     * @param objectKey object key
     * @return List of GenomicElements object matching the symbol and object key
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<GenomicElement> getElementsBySymbol(String symbol, int objectKey) throws Exception {

        String sql = "SELECT ge.*,r.species_type_key,r.object_status,r.object_key "+
                "FROM genomic_elements ge, rgd_ids r "+
                "WHERE LOWER(ge.symbol)=LOWER(?) AND ge.rgd_id=r.rgd_id AND r.object_key=?";

        GenomicElementQuery q = new GenomicElementQuery(this.getDataSource(), sql);
        return execute(q, symbol, objectKey);
    }

    /**
     * get GenomicElement object given its symbol and object type
     *
     * @param accId accession id of genomic element
     * @param xdbKey external database id for the accession id
     * @param objectKey object key
     * @param speciesTypeKey species type key
     * @return List of GenomicElements object matching the symbol and object key
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<GenomicElement> getElementsByAccId(String accId, int xdbKey, int objectKey, int speciesTypeKey) throws Exception {

        String sql = "SELECT ge.*,r.species_type_key,r.object_status,r.object_key \n" +
                "FROM rgd_acc_xdb x,rgd_ids r,genomic_elements_view ge \n" +
                "WHERE x.rgd_id=r.rgd_id AND r.rgd_id=ge.rgd_id AND x.acc_id=? \n" +
                " AND r.object_key=? AND x.xdb_key=? AND r.species_type_key=?";

        GenomicElementQuery q = new GenomicElementQuery(this.getDataSource(), sql);
        return execute(q, accId, objectKey, xdbKey, speciesTypeKey);
    }

    /**
     * insert new genomic element into table GENOMIC_ELEMENTS
     *
     * @param ge GenomicElement object
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int insertElement(GenomicElement ge) throws Exception{

        List<GenomicElement> elements = new ArrayList<>(1);
        elements.add(ge);
        return insertElements(elements);
    }

    /**
     * insert new genomic element into table GENOMIC_ELEMENTS
     *
     * @param elements list of GenomicElement objects
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int insertElements(List<GenomicElement> elements) throws Exception{

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(),
                "INSERT INTO genomic_elements(symbol,name,description,source, so_acc_id,rgd_id,object_type,notes,genomic_alteration) "+
                "VALUES(?,?,?,?, ?,?,?,?,?)",
                new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                        Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});

        su.compile();

        for( GenomicElement ge: elements ) {

            su.update(ge.getSymbol(), ge.getName(), ge.getDescription(), ge.getSource(),
                    ge.getSoAccId(), ge.getRgdId(), ge.getObjectType(), ge.getNotes(), ge.getGenomicAlteration());
        }
        return executeBatch(su);
    }

    /**
     * Update genomic element in table GENOMIC_ELEMENTS given rgdID
     *
     * @param ge GenomicElement object
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int updateElement(GenomicElement ge) throws Exception{

        String sql = "UPDATE genomic_elements SET symbol=?, name=?, description=?, source=?, "+
                "so_acc_id=?, object_type=?, notes=?, genomic_alteration=? WHERE rgd_id=?";

        return update(sql, ge.getSymbol(), ge.getName(), ge.getDescription(), ge.getSource(),
                ge.getSoAccId(), ge.getObjectType(), ge.getNotes(), ge.getGenomicAlteration(), ge.getRgdId());
    }

    /**
     * get expression data for given genomic element
     * @param rgdId rgd id of genomic element
     * @return List of ExpressionData objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<ExpressionData> getExpressionData(int rgdId) throws Exception {

        String sql = "SELECT e.* FROM expression_data e WHERE rgd_id=? ";

        ExpressionDataQuery q = new ExpressionDataQuery(this.getDataSource(), sql);
        return execute(q, rgdId);
    }

    /**
     * get expression data for given source
     * @param source source name
     * @return List of ExpressionData objects
     * @throws Exception when unexpected error in spring framework occurs
     */
    public List<ExpressionData> getExpressionDataForSource(String source) throws Exception {

        String sql = "SELECT e.* FROM expression_data e WHERE source=?";

        ExpressionDataQuery q = new ExpressionDataQuery(this.getDataSource(), sql);
        return execute(q, source);
    }

    /**
     * insert a list of expression data objects
     * @param list list of expression data objects
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int insertExpressionData(Collection<ExpressionData> list) throws Exception {

        String sql = "INSERT INTO expression_data "+
                "(rgd_id, tissue, transcripts, chip_seq_read_density, experiment_methods, regulation, tissue_term_acc," +
                "strain_term_acc, source, notes, expression_data_key) "+
                "VALUES(?,?,?,?,?,?,?,?,?,?,expression_data_seq.nextval)";

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), sql,
            new int[]{Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.DOUBLE, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR
                    , Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
        su.compile();

        for( ExpressionData ee: list ) {

            su.update(ee.getRgdId(), ee.getTissue(), ee.getTranscripts(), ee.getChipSeqReadDensity(), ee.getExperimentMethods(),
                    ee.getRegulation(), ee.getTissueTermAcc(), ee.getStrainTermAcc(), ee.getSource(), ee.getNotes());
        }

        return executeBatch(su);
    }

    /**
     * update a list of expression data objects
     * @param list list of expression data objects
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int updateExpressionData(Collection<ExpressionData> list) throws Exception {

        String sql = "UPDATE expression_data SET transcripts=?, chip_seq_read_density=?, tissue=?, rgd_id=?, " +
                "experiment_methods=?, regulation=?, tissue_term_acc=?, strain_term_acc=?, source=?, notes=? "+
                "WHERE expression_data_key=?";

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), sql,
            new int[]{Types.VARCHAR, Types.VARCHAR, Types.DOUBLE, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
                    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER});
        su.compile();

        for( ExpressionData ee: list ) {

            su.update(ee.getTissue(), ee.getTranscripts(), ee.getChipSeqReadDensity(), ee.getRgdId(), ee.getExperimentMethods(),
                    ee.getRegulation(), ee.getTissueTermAcc(), ee.getStrainTermAcc(), ee.getSource(), ee.getNotes(), ee.getKey());
        }

        return executeBatch(su);
    }

    /**
     * update a list of expression data objects
     * @param list list of expression data objects
     * @return count of rows affected
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int deleteExpressionData(Collection<ExpressionData> list) throws Exception {

        String sql = "DELETE FROM expression_data WHERE expression_data_key=?";

        BatchSqlUpdate su = new BatchSqlUpdate(this.getDataSource(), sql, new int[]{Types.INTEGER});
        su.compile();

        for( ExpressionData ee: list ) {
            su.update(ee.getKey());
        }

        return executeBatch(su);
    }

    /// get list of protein domains associated with a given gene (possibly empty)
    public List<GenomicElement> getProteinDomainsForGene(int geneRgdId) throws Exception {

        String sql = "SELECT ge.*,i.species_type_key,i.object_status,i.object_key \n" +
                "FROM genomic_elements ge,rgd_ids i \n" +
                "WHERE i.rgd_id=ge.rgd_id AND ge.rgd_id IN(\n" +
                "  SELECT a1.detail_rgd_id FROM rgd_associations a1,rgd_associations a2\n" +
                "  WHERE a1.assoc_type='protein_to_domain' AND a2.assoc_type='protein_to_gene' AND a1.master_rgd_id=a2.master_rgd_id AND a2.detail_rgd_id=?" +
                ") ORDER BY LOWER(symbol)";

        GenomicElementQuery q = new GenomicElementQuery(this.getDataSource(), sql);
        return execute(q, geneRgdId);
    }
}
