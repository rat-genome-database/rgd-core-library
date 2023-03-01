package edu.mcw.rgd.dao.spring;

import edu.mcw.rgd.datamodel.Strain;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * helper for querying STRAIN_STATUS_LOG table
 */
public class StrainStatusQuery extends MappingSqlQuery {

    public StrainStatusQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        Strain.Status st = new Strain().createStatus();

        st.key = rs.getInt("strain_status_log_key");
        st.strainRgdId = rs.getInt("strain_rgd_id");
        st.statusDate = rs.getDate("status_date");

        String yesNo = rs.getString("live_animals_y_n");
        if( yesNo!=null ) {
            st.liveAnimals = yesNo.equals("Y");
        }

        yesNo = rs.getString("cryopreserved_embryo_y_n");
        if( yesNo!=null ) {
            st.cryopreservedEmbryo = yesNo.equals("Y");
        }

        yesNo = rs.getString("cryopreserved_sperm_y_n");
        if( yesNo!=null ) {
            st.cryopreservedSperm = yesNo.equals("Y");
        }

        yesNo = rs.getString("cryorecovery_y_n");
        if( yesNo!=null ) {
            st.cryorecovery = yesNo.equals("Y");
        }

        return st;
    }

}
