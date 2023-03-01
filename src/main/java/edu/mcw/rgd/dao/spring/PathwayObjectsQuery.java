package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.PathwayObject;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: Mar 31, 2011
 * Time: 3:31:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class PathwayObjectsQuery extends MappingSqlQuery {
    public PathwayObjectsQuery(DataSource ds, String query){
        super(ds, query);
    }



    protected Object mapRow(ResultSet resultSet, int i) throws SQLException {
        
        PathwayObject po = new PathwayObject();
        po.setAccId(resultSet.getString("object_acc_id"));
        po.setId(resultSet.getString("term_acc"));
        po.setTypeId(resultSet.getInt("object_type_id"));

        po.setXdb_key(resultSet.getInt("xdb_key"));
        if(resultSet.wasNull())
            po.setXdb_key(null);

        po.setTypeName(resultSet.getString("object_type_name"));
        po.setUrl(resultSet.getString("pathway_objects_url"));
        po.setObjName(resultSet.getString("object_name"));
        po.setObjDesc(resultSet.getString("object_description"));

        return po;
    }
}
