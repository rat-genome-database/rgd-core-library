package edu.mcw.rgd.dao.spring.phenominerExpectedRanges;

import edu.mcw.rgd.dao.impl.OntologyXDAO;
import edu.mcw.rgd.datamodel.ontologyx.Term;
import edu.mcw.rgd.datamodel.phenominerExpectedRange.TraitObject;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jthota on 5/22/2018.
 */
public class PhenominerRangeTraitQuery extends MappingSqlQuery{
    public PhenominerRangeTraitQuery(DataSource ds, String query) {
        super(ds, query);
    }
    @Override
    protected Object mapRow(ResultSet rs, int i) throws SQLException {
        OntologyXDAO dao= new OntologyXDAO();
        TraitObject obj= new TraitObject();
        Term trait= new Term();
        Term subTrait=new Term();
        trait.setAccId(rs.getString("trait_ont_id"));
        try {
            trait.setTerm(dao.getTermByAccId(rs.getString("trait_ont_id")).getTerm());
            subTrait.setTerm(dao.getTermByAccId(rs.getString("subtrait_ont_id")).getTerm());
        } catch (Exception e) {
            e.printStackTrace();
        }
        subTrait.setAccId(rs.getString("subtrait_ont_id"));

        obj.setTrait(trait);
        obj.setSubTrait(subTrait);
        return obj;
    }

}
