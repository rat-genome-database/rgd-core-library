package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.GenomicElement;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author mtutaj
 * @since 2/22/12
 * mapping sql query to work with lists of GenomicElement objects
 */
public class GenomicElementQuery extends MappingSqlQuery {

   public GenomicElementQuery(DataSource ds, String query) {
       super(ds, query);
   }

   protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

       GenomicElement obj = new GenomicElement();
       mapRowForGenomicElement(obj, rs);
       return obj;
   }

    protected void mapRowForGenomicElement(GenomicElement obj, ResultSet rs) throws SQLException {

        obj.setRgdId(rs.getInt("rgd_id"));
        obj.setObjectType(rs.getString("object_type"));
        obj.setSymbol(rs.getString("symbol"));
        obj.setName(rs.getString("name"));
        obj.setDescription(rs.getString("description"));
        obj.setSource(rs.getString("source"));
        obj.setSoAccId(rs.getString("so_acc_id"));
        obj.setNotes(rs.getString("notes"));

        try { obj.setGenomicAlteration(rs.getString("genomic_alteration")); }catch (Exception ignored) { }
        try { obj.setSpeciesTypeKey(rs.getInt("species_type_key")); }catch (Exception ignored) { }
        try { obj.setObjectStatus(rs.getString("object_status")); }catch (Exception ignored) { }
        try { obj.setObjectKey(rs.getInt("object_key")); }catch (Exception ignored) { }

    }
}
