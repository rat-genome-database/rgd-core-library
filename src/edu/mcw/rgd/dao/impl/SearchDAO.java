package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.RgdIndexQuery;
import edu.mcw.rgd.datamodel.RgdId;
import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.datamodel.search.GeneralSearchResult;
import edu.mcw.rgd.datamodel.search.RankedIndexItem;
import edu.mcw.rgd.datamodel.search.IndexRow;
import edu.mcw.rgd.process.search.SearchBean;
import org.springframework.jdbc.object.BatchSqlUpdate;

import java.sql.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 13, 2008
 * Time: 1:41:26 PM
 */
public class SearchDAO extends AbstractDAO {


    /**
     * remove all rows from RGD_INDEX table
     * @throws Exception
     */
    public void truncateIndex() throws Exception {

        String sql = "TRUNCATE TABLE rgd_index";
        update(sql);
    }

    /**
     * delete entries of given object and data type from index
     * @param objectType object type
     * @param dataType data type
     * @return count of rows deleted
     * @throws Exception if something unexpected happens in the framework
     */
    public int deleteFromIndex(String objectType, String dataType) throws Exception {

        String sql = "DELETE FROM rgd_index WHERE object_type=? AND data_type=?";
        return update(sql, objectType, dataType);
    }

    /**
     * delete entries of given object and data type from index;
     * @param objectType object type
     * @param dataType data type
     * @param batchSize batch size
     * @return count of rows deleted
     * @throws Exception if something unexpected happens in the framework
     */
    public int deleteFromIndex(String objectType, String dataType, int batchSize) throws Exception {

        String sql = "DELETE FROM rgd_index WHERE object_type=? AND data_type=? AND ROWNUM <= ?";
        int rowsTotal = 0;
        for( ;; ) {
            int rowsAffected = update(sql, objectType, dataType, batchSize);
            rowsTotal += rowsAffected;
            if( rowsAffected < batchSize ) {
                break;
            }
        }
        return rowsTotal;
    }

    private String buildSubSelect(List<String> required, List<String> negated, List<String> optional) throws Exception {

        StringBuilder sql = new StringBuilder();
        int k=0;
        if( required!=null ) {
            for(String term: required) {
                if (k > 0) {
                    sql.append("\nintersect ");
                }

                sql.append(" \nselect rgd_id from rgd_index where keyword_lc like '");

                term = handleSqlSubstitutions(term);

                boolean isInt = false;
                try {
                    Integer.parseInt(term);
                    isInt = true;
                }catch (Exception e) {
                }

                if (term.trim().contains(" ")) {
                    term = "%" + term + "%";
                }else if (!isInt) {
                    term = term + "%";
                }

                sql.append(term).append('\'');

                k++;
            }
        }

        if( optional!=null ) {
            for(String term: optional) {
                if (k > 0) {
                    sql.append("\nunion ");
                }

                sql.append(" \nselect rgd_id from rgd_index where keyword_lc like '");

                term = handleSqlSubstitutions(term);

                if (term.trim().contains(" ")) {
                    term = "%" + term + "%";
                }

                sql.append(term).append('\'');
                k++;
            }
        }

        if( negated!=null ) {
            for(String term: negated) {
                if (k > 0) {
                    sql.append("\nminus ");
                }

                sql.append(" \nselect rgd_id from rgd_index where keyword_lc like '");

                term = handleSqlSubstitutions(term);

                if (term.trim().contains(" ")) {
                    term = "%" + term + "%";
                }

                sql.append(term).append('\'');
                k++;
            }
        }

        return sql.toString();
    }

    /**
     * run general search
     * @param sb SearchBean
     * @return GeneralSearchResult object with search results
     * @throws Exception when unexpected error in spring framework occurs
     */
    public GeneralSearchResult runGeneralSearch(SearchBean sb) throws Exception{

        try(Connection conn = this.getConnection()) {

            String sql = "select unique(rgd_id), object_type, species_type_key " +
                         "from rgd_index where rgd_id in ( " + this.buildSubSelect(sb.getRequired(), sb.getNegated(), sb.getOptional()) + "\n) ";
            if( sb.isChinchilla() ) {
                // in chinchilla mode, skip mouse and rat
                sql += " AND species_type_key NOT IN(2,3)";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            GeneralSearchResult result= new GeneralSearchResult();

            while (rs.next()) {
                String objectType = rs.getString("object_type");
                String rgdId = rs.getString("rgd_id");
                int speciesTypeKey = rs.getInt("species_type_key");

                switch (objectType) {
                    case "GENES":
                        result.incrementHitCount(RgdId.OBJECT_KEY_GENES, speciesTypeKey);
                        break;
                    case "QTLS":
                        result.incrementHitCount(RgdId.OBJECT_KEY_QTLS, speciesTypeKey);
                        break;
                    case "REFERENCES":
                        result.incrementHitCount(RgdId.OBJECT_KEY_REFERENCES, speciesTypeKey);
                        break;
                    case "STRAINS":
                        result.incrementHitCount(RgdId.OBJECT_KEY_STRAINS, speciesTypeKey);
                        break;
                    case "SSLPS":
                        result.incrementHitCount(RgdId.OBJECT_KEY_SSLPS, speciesTypeKey);
                        break;
                    case "PROMOTERS":
                        result.incrementHitCount(RgdId.OBJECT_KEY_PROMOTERS, speciesTypeKey);
                        break;
                    case "CELL_LINES":
                        result.incrementHitCount(RgdId.OBJECT_KEY_CELL_LINES, speciesTypeKey);
                        break;
                    case "VARIANTS":
                        result.incrementHitCount(RgdId.OBJECT_KEY_VARIANTS, speciesTypeKey);
                        break;
                    default:
                        if (rgdId.indexOf(':') > 0) {
                            // only ontologies have colon in the rgdId (rgdId is term accession id, actually)
                            result.ontologyTerms.put(rgdId, objectType);
                        }
                        break;
                }
            }
            return result;
        }
    }

    /**
     * same as runGeneralSearch, but it searches only through ontology terms
     * @param sb SerchBean
     * @return GeneralSearchResult object with search results
     * @throws Exception when unexpected error in spring framework occurs
     */
    public GeneralSearchResult runOntologySearch(SearchBean sb) throws Exception{

        try(Connection conn =  this.getConnection()) {

            String sql = "SELECT UNIQUE(rgd_id), object_type, species_type_key " +
                        "FROM rgd_index WHERE rgd_id IN(" + this.buildSubSelect(sb.getRequired(), sb.getNegated(), sb.getOptional()) + "\n) "+
                        "AND INSTR(rgd_id,':')>0"; // only ontologies have colon in the rgd id, like 'CHEBI:0012345'

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            GeneralSearchResult result= new GeneralSearchResult();

            while (rs.next()) {
                String objectType = rs.getString("object_type");
                result.ontologyTerms.put(rs.getString("rgd_id"), objectType);

                // increment hit count for given ontology
                Integer hitCount = result.ontTermHits.get(objectType);
                if( hitCount == null )
                    hitCount = 0;
                result.ontTermHits.put(objectType, hitCount+1);
            }
            return result;
        }
    }

    public Map<Integer, RankedIndexItem> getRankedRGDIds(SearchBean sb, String objectType) throws Exception{

        // paranoia check
        if( sb.getRequired()==null || sb.getRequired().isEmpty() ) {
            // search keyword must be set
            return new HashMap<>();
        }

        Map<Integer, RankedIndexItem> indexItems = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM rgd_index g WHERE ");

        String subSelect = this.buildSubSelect(sb.getRequired(), sb.getNegated(), sb.getOptional());
        if( !subSelect.isEmpty() ) {
            sql.append("rgd_id in ( ").append(subSelect).append(") ")
                    .append("and (");

            Iterator it = sb.getRequired().iterator();
            int i =0;
            while (it.hasNext()) {
                if (i > 0) {
                    sql.append(" or ");
                }

                String term = (String) it.next();
                term = handleSqlSubstitutions(term);

                sql.append("keyword_lc like '%").append(term).append("%'");
                i++;
            }

            if( sb.getOptional()!=null ) {
                it = sb.getOptional().iterator();
                while (it.hasNext()) {
                    if (i > 0) {
                        sql.append(" or ");
                    }

                    String term = (String) it.next();
                    term = handleSqlSubstitutions(term);

                    sql.append("keyword_lc like '%").append(term).append("%'");
                    i++;
                }
            }
            sql.append(") and ");
        }

        sql.append("object_type='").append(objectType).append("' ");

        // handle species
        if( sb.getSpeciesType()!=SpeciesType.ALL && SpeciesType.isValidSpeciesTypeKey(sb.getSpeciesType()) ) {
            sql.append("and species_type_key=").append(sb.getSpeciesType());
        }
        else if( sb.getSpeciesType()==SpeciesType.ALL ) {
         /*
            if( sb.isChinchilla() )
                sql.append("and species_type_key IN(1,4) ");
            else
                sql.append("and species_type_key<>4 ");
                */
        }

        // add ontology filter to the query
        String ontologyFilter = ReportDAO.buildOntologyFilter(sb);
        if( ontologyFilter!=null )
            sql.append(ontologyFilter);

        sql.append(" ORDER BY rank DESC");

        String lcTerm = sb.getTerm().trim().toLowerCase();

        createRankedIndexItems2(sql.toString(), lcTerm, indexItems);

        return indexItems;
    }

    // novel method
    private void createRankedIndexItems2(String sql, String lcTerm, Map<Integer, RankedIndexItem> indexItems) throws Exception {

        RgdIndexQuery q = new RgdIndexQuery(this.getDataSource(), sql);

        // sql query results are ordered by rank
        // for every rgd_id the rank is the line with highest rank
        for( IndexRow rs: (List<IndexRow>)execute(q) ) {
            String keyword = rs.getKeyword();
            if (keyword.contains(" ") && !lcTerm.contains(" ")) {
                continue;
            }
            String dataType = rs.getDataType();

            int rgdId = rs.getRgdId();
            RankedIndexItem r = indexItems.get(rgdId);
            if( r==null ) {
                // new ranked item
                r = new RankedIndexItem();
                if (indexItems.size() < 1000) {
                    indexItems.put(rgdId, r);
                }
                else
                    break;

                r.setId(rgdId);
                r.setObjectType(rs.getObjectType());
                r.setSpeciesTypeKey(rs.getSpeciesTypeKey());
            }
            r.addDataType(dataType);

            int rank = rs.getRank();
            if( keyword.equals(lcTerm) ) {
                switch (dataType) {
                    case "symbol":
                        rank += 1000;
                        break;
                    case "old_gene_symbol":
                        rank += 950;
                        break;
                    case "name":
                        rank += 900;
                        break;
                    case "old_gene_name":
                        rank += 850;
                        break;
                    default:
                        rank += 100;
                        break;
                }
			}
            if( r.getRank()<rank ) {
                r.setRank(rank);
            }
        }
    }


    public int insertIntoRgdIndex(List<Object[]> data) throws Exception{
        BatchSqlUpdate bsu = new BatchSqlUpdate(this.getDataSource(),
            "insert into rgd_index (rgd_id, keyword_lc, object_type, data_type, species_type_key, rank) " +
                    "values (?,LOWER(?),?,?,?,?)",
            new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.INTEGER},
            data.size());
        bsu.compile();

        for( Object[] row: data ) {
            bsu.update(row[0], row[1], row[2], row[3], row[4], row[5]);
        }
        return executeBatch(bsu);
    }

    public boolean isInIndex(String rgdId, String value, String objectType, String fieldType, int speciesTypeKey) throws Exception{
            return false;
    }

    public List<IndexRow> findCitation(String term) throws Exception{
        String sql = "SELECT DISTINCT ri.rgd_id, ri.object_type, ri.data_type, ri.species_type_key, " +
                "ri.rank, r.citation as keyword_lc from rgd_index ri, references r " +
                "WHERE ri.keyword_lc like ? and ri.data_type='citation' and ri.species_type_key=3 and r.rgd_id=ri.rgd_id order by abs(ri.rgd_id) desc";
        RgdIndexQuery q = new RgdIndexQuery(this.getDataSource(), sql);
        return execute(q, term);
    }

    // note: used by popups in curation edit tool
    public List<IndexRow> findSymbol(String symbol) throws Exception{
        String sql = "SELECT DISTINCT * FROM rgd_index WHERE keyword_lc LIKE ? AND data_type='symbol' ORDER BY ABS(rgd_id) DESC";
        RgdIndexQuery q = new RgdIndexQuery(this.getDataSource(), sql);
        return execute(q, symbol+"%");
    }

    // note: used by popups in curation edit tool
    public List<IndexRow> findSymbol(String symbol, int speciesTypeKey) throws Exception{

        if( !SpeciesType.isValidSpeciesTypeKey(speciesTypeKey) ) {
            return findSymbol(symbol);
        }

        String sql = "SELECT DISTINCT * FROM rgd_index "+
                "WHERE (keyword_lc LIKE ? OR rgd_id=?) AND data_type='symbol' AND species_type_key=? "+
                "ORDER BY ABS(rgd_id) DESC";
        RgdIndexQuery q = new RgdIndexQuery(this.getDataSource(), sql);
        return execute(q, symbol+"%", symbol, speciesTypeKey);
    }

    // note: used by popups in curation edit tool
    public List<IndexRow> findSymbol(String symbol, int speciesTypeKey, String objectType) throws Exception{

        if (objectType == null) {
            return findSymbol(symbol, speciesTypeKey);
        }

        String sql = "";

        switch (objectType) {
            case "STRAINS":
                sql = "SELECT distinct ri.rgd_id, ri.object_type, ri.data_type, ri.species_type_key, ri.rank, s.strain_symbol_lc as keyword_lc "
                        + "FROM rgd_index ri, strains s "
                        + "WHERE ri.keyword_lc like ? and ri.data_type='symbol' and ri.species_type_key=? and s.rgd_id = ri.rgd_id order by abs(ri.rgd_id) desc";
                break;
            case "GENES":
                sql = "SELECT distinct ri.rgd_id, ri.object_type, ri.data_type, ri.species_type_key, ri.rank, g.gene_symbol_lc as keyword_lc "
                        + "FROM rgd_index ri, genes g "
                        + "WHERE ri.keyword_lc like ? and ri.data_type='symbol' and ri.species_type_key=? and g.rgd_id = ri.rgd_id order by abs(ri.rgd_id) desc";
                break;
            case "QTLS":
                sql = "SELECT distinct ri.rgd_id, ri.object_type, ri.data_type, ri.species_type_key, ri.rank, q.qtl_symbol_lc as keyword_lc "
                        + "FROM rgd_index ri, qtls q "
                        + "WHERE ri.keyword_lc like ? and ri.data_type='symbol' and ri.species_type_key=? and q.rgd_id = ri.rgd_id order by abs(ri.rgd_id) desc";
                break;
            case "REFERENCES":
                sql = "SELECT distinct ri.rgd_id, ri.object_type, ri.data_type, ri.species_type_key, ri.rank, r.citation as keyword_lc "
                        + "FROM rgd_index ri, references r "
                        + "WHERE ri.keyword_lc like ? and ri.data_type='citation' and ri.species_type_key=? and r.rgd_id = ri.rgd_id order by abs(ri.rgd_id) desc";
                break;
            case "SSLPS":
                sql = "SELECT distinct ri.rgd_id, ri.object_type, ri.data_type, ri.species_type_key, ri.rank, s.rgd_name_lc as keyword_lc "
                        + "FROM rgd_index ri, sslps s "
                        + "WHERE ri.keyword_lc like ? and ri.data_type='symbol' and ri.species_type_key=? and s.rgd_id = ri.rgd_id order by abs(ri.rgd_id) desc";
                break;
            case "PROMOTERS":
                sql = "SELECT distinct ri.rgd_id, ri.object_type, ri.data_type, ri.species_type_key, ri.rank, LOWER(ge.symbol) as keyword_lc "
                        + "FROM rgd_index ri, genomic_elements ge "
                        + "WHERE ri.keyword_lc like ? and ri.data_type='symbol' and ri.species_type_key=? and g.rgd_id = ri.rgd_id order by abs(ri.rgd_id) desc";
                break;
            default:
                System.out.println("ERROR: findSymbol(,,): unsupported object type: " + objectType);
                return null;
        }

        RgdIndexQuery q = new RgdIndexQuery(this.getDataSource(), sql);
        return execute(q, symbol+"%", speciesTypeKey);
    }

    public List<IndexRow> findGenomicObject(String symbol, int speciesTypeKey) throws Exception{

        String sql="SELECT * FROM rgd_index WHERE keyword_lc=? AND "+
                "(   (data_type IN('UniProt', 'UniGene', 'GenBank Nucleotide','GenBank Protein','Ensembl Genes','Ensembl Protein','old_gene_symbol') AND object_type='GENES')" +
                " OR (data_type='symbol' AND object_type='GENES') "+
                " OR (data_type LIKE 'array_id%')" +
                ") AND species_type_key=? ORDER BY rank DESC";

        logger.debug("symbol = " + symbol);
        logger.debug("speciesTypeKey = " + speciesTypeKey);
        logger.debug("query " + sql);

        RgdIndexQuery q = new RgdIndexQuery(this.getDataSource(), sql);
        return execute(q, symbol.toLowerCase(), speciesTypeKey);
    }

    String handleSqlSubstitutions(String term) {

        int pos = term.indexOf('*');
        if( pos >= 0 ) {
            term = term.replace('*', '%');
        }

        pos = term.indexOf('\'');
        if( pos >= 0 ) {
            term = term.replace("'", "''");
        }
        return term;
    }
}

