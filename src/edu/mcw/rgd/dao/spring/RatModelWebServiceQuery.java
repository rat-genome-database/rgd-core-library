package edu.mcw.rgd.dao.spring;
import edu.mcw.rgd.dao.AbstractDAO;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RatModelWebServiceQuery extends MappingSqlQuery {

    public static class test{

        public String getQualifier() {
            return qualifier;
        }

        public void setQualifier(String qualifier) {
            this.qualifier = qualifier;
        }

        public String getDiseaseOrPhenotypeTerm() {
            return diseaseOrPhenotypeTerm;
        }

        public void setDiseaseOrPhenotypeTerm(String diseaseOrPhenotypeTerm) {
            this.diseaseOrPhenotypeTerm = diseaseOrPhenotypeTerm;
        }

        public String getWithConditionsTerm() {
            return withConditionsTerm;
        }

        public void setWithConditionsTerm(String withConditionsTerm) {
            this.withConditionsTerm = withConditionsTerm;
        }

        public String getEvidence() {
            return evidence;
        }

        public void setEvidence(String evidence) {
            this.evidence = evidence;
        }

        public String getRefRgdId() {
            return refRgdId;
        }

        public void setRefRgdId(String refRgdId) {
            this.refRgdId = refRgdId;
        }

        public String getStrainType() {
            return strainType;
        }

        public void setStrainType(String strainType) {
            this.strainType = strainType;
        }

        public String getStrain() {
            return strain;
        }

        public void setStrain(String strain) {
            this.strain = strain;
        }

        private String strain;

        public int getStrainRgdId() {
            return strainRgdId;
        }

        public void setStrainRgdId(int strainRgdId) {
            this.strainRgdId = strainRgdId;
        }

        private int strainRgdId;
        private String qualifier;
        private String diseaseOrPhenotypeTerm;
        private String withConditionsTerm;
        private String evidence;
        private String refRgdId;
        private String strainType;

    }
    public RatModelWebServiceQuery(DataSource ds, String query) {
        super(ds, query);
    }

    protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

        test t= new test();
        t.setStrain(rs.getString("strain"));
        t.setStrainRgdId(rs.getInt("strain_rgd_id"));
        t.setQualifier(rs.getString("qualifier"));
        t.setDiseaseOrPhenotypeTerm(rs.getString("Disease_OR_Phenotype_Term"));
        t.setWithConditionsTerm(rs.getString("With_Conditions"));
        t.setEvidence(rs.getString("evidence"));
        t.setRefRgdId(rs.getString("ref_rgd_id"));
        try {
            t.setStrainType(rs.getString("strain_type"));
        }catch (Exception ignored) {
        }
//        try {
//            String strainType = rs.getString("strain_type");
//            if (strainType != null) {
//                t.setStrainType(strainType);
//            }
//        }catch (Exception ignored) {
//      }
        return t;
    }

    public static List<test> execute(AbstractDAO dao, String sql, Object... params) throws Exception {
        RatModelWebServiceQuery q = new RatModelWebServiceQuery(dao.getDataSource(), sql);
        return dao.execute(q, params);
    }
}
