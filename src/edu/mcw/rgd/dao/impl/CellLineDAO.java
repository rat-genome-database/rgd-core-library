package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.spring.CellLineQuery;
import edu.mcw.rgd.datamodel.CellLine;

import java.util.List;

/**
 * methods specific for cell line objects
 */
public class CellLineDAO extends GenomicElementDAO {

    public List<CellLine> getActiveCellLines() throws Exception {

        String query = "SELECT g.*,r.*,c.* FROM rgd_ids r,genomic_elements g,cell_lines c "+
                "WHERE r.rgd_id=g.rgd_id AND g.rgd_id=c.rgd_id AND object_status='ACTIVE'";
        CellLineQuery q = new CellLineQuery(this.getDataSource(), query);
        return execute(q);
    }

    public List<CellLine> getActiveCellLines(String srcPipeline) throws Exception {

        String query = "SELECT g.*,r.*,c.* FROM rgd_ids r,genomic_elements g,cell_lines c "+
                "WHERE r.rgd_id=g.rgd_id AND g.rgd_id=c.rgd_id AND object_status='ACTIVE' AND src_pipeline=?";
        CellLineQuery q = new CellLineQuery(this.getDataSource(), query);
        return execute(q, srcPipeline);
    }

    public CellLine getCellLine(int rgdId) throws Exception {

        String query = "SELECT g.*,r.*,c.* FROM rgd_ids r,genomic_elements g,cell_lines c "+
                "WHERE r.rgd_id=? AND r.rgd_id=g.rgd_id AND g.rgd_id=c.rgd_id";
        CellLineQuery q = new CellLineQuery(this.getDataSource(), query);
        List<CellLine> rows = execute(q, rgdId);
        if( rows.isEmpty() ) {
            return null;
        }
        return rows.get(0);
    }

    /**
     * insert new cell line object into CELL_LINES table
     * <br>
     * Note: implicitly a row is inserted into GENOMIC_ELEMENTS table as well
     * @param obj CellLine object
     * @return count of rows affected
     * @throws Exception
     */
    public int insertCellLine(CellLine obj) throws Exception {

        // insert a row into GENOMIC_ELEMENTS table
        int r = insertElement(obj);

        // insert a row into CELL_LINE table
        String sql =
            "INSERT INTO cell_lines(availability,characteristics,gender, germline_competent,origin,phenotype,research_use,src_pipeline,rgd_id) "+
            "VALUES(?,?,?,?, ?,?,?,?,?)";

        return r + update(sql,
                obj.getAvailability(), obj.getCharacteristics(), obj.getGender(), obj.getGermlineCompetent(),
                obj.getOrigin(), obj.getPhenotype(), obj.getResearchUse(), obj.getSrcPipeline(), obj.getRgdId()
        );
    }

    /**
     * update cell line object in CELL_LINES and GENOMIC_ELEMENTS tables
     * @param obj CellLine object
     * @return count of rows affected
     * @throws Exception
     */
    public int updateCellLine(CellLine obj) throws Exception {

        // update a row in GENOMIC_ELEMENTS table
        int r = updateElement(obj);

        // update a row in CELL_LINE table
        String sql =
            "UPDATE cell_lines "+
            "SET availability=?,characteristics=?,gender=?, germline_competent=?,origin=?,phenotype=?,research_use=?,src_pipeline=? "+
            "WHERE rgd_id=?";

        return r + update(sql,
                obj.getAvailability(), obj.getCharacteristics(), obj.getGender(), obj.getGermlineCompetent(),
                obj.getOrigin(), obj.getPhenotype(), obj.getResearchUse(), obj.getSrcPipeline(), obj.getRgdId()
        );
    }

}
