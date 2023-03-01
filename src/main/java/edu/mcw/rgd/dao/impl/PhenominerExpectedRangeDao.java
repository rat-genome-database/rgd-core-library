package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.CountQuery;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.dao.spring.phenominerExpectedRanges.PhenominerExpectedRangeQuery;
import edu.mcw.rgd.dao.spring.phenominerExpectedRanges.PhenominerRangeExpRecQuery;
import edu.mcw.rgd.dao.spring.phenominerExpectedRanges.PhenominerRangeTraitQuery;
import edu.mcw.rgd.datamodel.phenominerExpectedRange.PhenominerExpectedRange;
import edu.mcw.rgd.datamodel.phenominerExpectedRange.PhenominerRangeExperimentRec;
import edu.mcw.rgd.datamodel.phenominerExpectedRange.TraitObject;
import org.springframework.jdbc.core.SqlParameter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.*;

/**
 * Created by jthota on 4/30/2018.
 */
public class PhenominerExpectedRangeDao extends AbstractDAO{
    OntologyXDAO xdao=new OntologyXDAO();

  /*  public int insert(PhenominerExpectedRange range) throws Exception {
        String sql="insert into PHENOMINER_EXPECTED_RANGE(" +
                "EXPECTED_RANGE_ID , " +
                "EXPECTED_RANGE_NAME ," +
                "CLINICAL_MEASUREMENT_ONT_ID ," +
                "STRAIN_GROUP_ID ," +
                "AGE_DAYS_FROM_DOB_LOW_BOUND ," +
                "AGE_DAYS_FROM_DOB_HIGH_BOUND ," +
                "SEX ," +
                " TRAIT_ONT_ID, "+
                " RANGE_UNITS, "+
                "RANGE_VALUE ," +
                "RANGE_LOW ," +
                "RANGE_HIGH ," +
                "RANGE_SD, " +
                "created_date, " +
                "last_modified_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?, SYSDATE, SYSDATE)";
        return this.update(sql, new Object[]{
                range.getExpectedRangeId(),
                range.getStrainGroupName()+"_"+ range.getExpectedRangeName(),
                range.getClinicalMeasurementOntId(),
                range.getStrainGroupId(),
                range.getAgeLowBound(),
                range.getAgeHighBound(),
                range.getSex(),
                range.getTraitOntId(),
                range.getUnits(),
                range.getRangeValue(),
                range.getRangeLow(),
                range.getRangeHigh(),
                range.getRangeSD()
        });
    }*/

    public int insert(PhenominerExpectedRange range) throws Exception {
        String sql = "insert into PHENOMINER_EXPECTED_RANGE(EXPECTED_RANGE_ID , EXPECTED_RANGE_NAME ,CLINICAL_MEASUREMENT_ONT_ID ,STRAIN_GROUP_ID ,AGE_DAYS_FROM_DOB_LOW_BOUND ,AGE_DAYS_FROM_DOB_HIGH_BOUND ,SEX , TRAIT_ONT_ID,  RANGE_UNITS, RANGE_VALUE ,RANGE_LOW ,RANGE_HIGH ,RANGE_SD, created_date, last_modified_date) values(?,?,?,?,?,?,?,?,?,?,?,?,?, SYSDATE, SYSDATE)";
        if(range.getStrainGroupName().contains("NormalStrain")){
            return this.update(sql, new Object[]{Integer.valueOf(range.getExpectedRangeId()), range.getStrainGroupName()+"_"+range.getSex() , range.getClinicalMeasurementOntId(), Integer.valueOf(range.getStrainGroupId()), Integer.valueOf(range.getAgeLowBound()), Integer.valueOf(range.getAgeHighBound()), range.getSex(), range.getTraitOntId(), range.getUnits(), Double.valueOf(range.getRangeValue()), Double.valueOf(range.getRangeLow()), Double.valueOf(range.getRangeHigh()), Double.valueOf(range.getRangeSD())});
        }else
            return this.update(sql, new Object[]{Integer.valueOf(range.getExpectedRangeId()), range.getStrainGroupName() + "_" + range.getExpectedRangeName(), range.getClinicalMeasurementOntId(), Integer.valueOf(range.getStrainGroupId()), Integer.valueOf(range.getAgeLowBound()), Integer.valueOf(range.getAgeHighBound()), range.getSex(), range.getTraitOntId(), range.getUnits(), Double.valueOf(range.getRangeValue()), Double.valueOf(range.getRangeLow()), Double.valueOf(range.getRangeHigh()), Double.valueOf(range.getRangeSD())});
    }
    public List<PhenominerExpectedRange> getNormalRangeRecordUnstratified(String phenotypeAccId) throws Exception {
        String sql="select * from phenominer_expected_range where clinical_measurement_ont_id=? and expected_range_name like 'NormalStrain%'" +
                " and  AGE_DAYS_FROM_DOB_LOW_BOUND=0 and AGE_DAYS_FROM_DOB_HIGH_BOUND=999 AND SEX='Mixed'";
        PhenominerExpectedRangeQuery q= new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
        return execute(q, phenotypeAccId);
    }
    public List<PhenominerExpectedRange> getExpectedRangeOfMixedAndAll(String phenotype, int strainGroupId, String ageLow, String ageHigh, String sex) throws Exception {
        String sql="select * from phenominer_expected_range where strain_group_id=? and AGE_DAYS_FROM_DOB_LOW_BOUND=? and AGE_DAYS_FROM_DOB_HIGH_BOUND=?  and clinical_measurement_ont_id=? and sex=?" +
                "and expected_range_name like '%Mixed%'";
        PhenominerExpectedRangeQuery query= new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
        return execute(query, new Object[]{strainGroupId, ageLow, ageHigh, phenotype, sex});
    }
    public List<PhenominerExpectedRange> getExpectedRanges(String phenotype, int strainGroupId, int ageLow, int ageHigh) throws Exception {
        String sql="select * from phenominer_expected_range where strain_group_id=? and clinical_measurement_ont_id=? and AGE_DAYS_FROM_DOB_LOW_BOUND=? and AGE_DAYS_FROM_DOB_HIGH_BOUND=? " ;
        PhenominerExpectedRangeQuery query= new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
        return execute(query, new Object[]{strainGroupId, phenotype, ageLow, ageHigh});
    }

    public int insertExpectedRangeExperiment(int expectedRangeId, int experimentRecordId) throws Exception {
        String sql="insert into PHENOMINER_RANGE_EXP_REC(expected_range_id, experiment_record_id, created_date, last_modified_date) values("+expectedRangeId+", "+experimentRecordId+", SYSDATE, SYSDATE)";
        return this.update(sql);
    }
    public List<Integer> getExperimentRecordIds(int expectedRangeId) throws Exception {
        String sql= "SELECT EXPERIMENT_RECORD_ID FROM PHENOMINER_RANGE_EXP_REC WHERE EXPECTED_RANGE_ID=? ";
        IntListQuery query=new IntListQuery(this.getDataSource(), sql);
        return execute(query, new Object[]{expectedRangeId});
    }

   public String buildIntQuery(List<Integer> values, String field){
        String sql= new String();
        if(values!=null){
            if(values.size()>0){
                sql+=" and "+field +" in (";

                boolean first=true;
                for(int i:values){
                    if(first){
                        sql+=i;
                        first=false;
                    }else{
                        sql+=","+i;
                    }


                }
                sql+=")";
            }
        }
        //    System.out.println(sql);
        return sql;
    }
    public String buildStringQuery(List<String> strs, String field){
        String sql= new String();
        if(strs!=null){
            if(strs.size()>0){
                sql+=" and "+field +" in (";
                boolean first=true;
                for(String s:strs){
                    if(first){
                        sql+="'"+s +"'";
                        first=false;
                    }else{
                        sql+=","+"'"+s+"'";
                    }
                }
                sql+=")";
            }

        }
        return sql;
    }

    public String buildMethodQuery(List<String> strs, String field){
        String sql= new String();
        if(strs.size()==3){
            return null;
        }

        if (strs.size() > 0 ) {

            if(strs.contains("mixed")){

                for(String s:strs){
                    if(s.equalsIgnoreCase("mixed")){

                        if(!strs.contains("vascular") && !strs.contains("tail")){
                            sql+=" and "+ field +" not like '%vascular%' ";

                            sql += " and "+ field +" not like '%tail%' ";

                        }else{
                            if(strs.contains("vascular") && !strs.contains("tail")){


                                sql += " and "+ field +" not like '%tail%' ";
                            }
                            if(strs.contains("tail") && !strs.contains("vascular")){


                                sql+=" and "+ field +" not like '%vascular%' ";
                            }

                        }

                    }
                }

            }else{
                if(strs.contains("vascular") && strs.contains("tail")) {

                    sql+= " and "+ field + " like '%vascular%' or " + field +" like '%tail%'";

                }else{
                    if(strs.contains("vascular")){
                        sql+=" and "+ field +" like '%vascular%' ";
                    }else{
                        sql+=" and "+ field +" like '%tail%' ";
                    }
                }
            }


        }

        return sql;
    }
    public List<TraitObject> getExpectedRangeTraitAncestors(PhenominerExpectedRange range, String traitOntId) throws Exception {
        String sql = "SELECT * FROM PHENOMINER_RANGE_TRAIT WHERE EXPECTED_RANGE_ID= ?" ;
        if(traitOntId!=null)
            sql+= " AND TRAIT_ONT_ID=?" ;
        PhenominerRangeTraitQuery query = new PhenominerRangeTraitQuery(this.getDataSource(), sql);
        if(traitOntId!=null)
            return execute(query, new Object[]{range.getExpectedRangeId(), traitOntId});
        else
            return execute(query, new Object[]{range.getExpectedRangeId()});
    }
    public List<String> getDistinctClinicalMeasurementOntIdsByAncestorTrait(String traitOntId) throws Exception {
        String sql = "select distinct(clinical_measurement_ont_id) from phenominer_expected_range where expected_range_id in (" +
                "SELECT expected_range_id FROM PHENOMINER_RANGE_TRAIT WHERE trait_ont_id=?)";
        StringListQuery query = new StringListQuery(this.getDataSource(), sql);

        return execute(query, traitOntId);
    }
    public List<String> getDistinctPGAMeasurements() throws Exception {
        String sql="SELECT DISTINCT(CLINICAL_MEASUREMENT_ONT_ID) FROM PHENOMINER_EXPECTED_RANGE WHERE TRAIT_ONT_ID IS NULL";
        StringListQuery query= new StringListQuery(this.getDataSource(), sql);
        return query.execute();
    }
    public List<String> getDistinctPhenotypeTraits(String phenotype) throws Exception {
        String sql="SELECT DISTINCT(TRAIT_ONT_ID) FROM PHENOMINER_EXPECTED_RANGE WHERE CLINICAL_MEASUREMENT_ONT_ID=?";
        StringListQuery query= new StringListQuery(this.getDataSource(), sql);
        return execute(query, new Object[]{phenotype});
    }
    public List<TraitObject> getDistinctPhenotypeTraitParents(String phenotype, String traitOntId) throws Exception {
        String sql = "SELECT distinct(subtrait_ont_id),  trait_ont_id FROM PHENOMINER_RANGE_TRAIT WHERE EXPECTED_RANGE_ID IN (" +
                " SELECT EXPECTED_RANGE_ID FROM PHENOMINER_EXPECTED_RANGE WHERE CLINICAL_MEASUREMENT_ONT_ID =?)";
        if(traitOntId!=null){
            sql+=" AND TRAIT_ONT_ID=?";
        }
        List<TraitObject> objects= new ArrayList<>();
        try(Connection conn=this.getDataSource().getConnection()){
            PreparedStatement stmt= conn.prepareStatement(sql);
            stmt.setString(1, phenotype);
            if(traitOntId!=null){
                stmt.setString(2, traitOntId);
            }
            ResultSet rs=  stmt.executeQuery();
            while (rs.next()){
                TraitObject obj= new TraitObject();
                obj.setSubTrait(xdao.getTerm(rs.getString("subtrait_ont_id")));
                obj.setTrait(xdao.getTerm(rs.getString("trait_ont_id")));
                objects.add(obj);
            }
        }
        return objects;
        //  PhenominerRangeTraitQuery query=new PhenominerRangeTraitQuery(this.getDataSource(), sql);
        //  return execute(query, new Object[]{phenotype});
    }
    public List<String> getDistinctStrainGroups() throws Exception {
        String sql = "select distinct(STRAIN_GROUP_ID) from phenominer_expected_range";
        StringListQuery query = new StringListQuery(this.getDataSource(), sql);
        return query.execute();
    }

    public List<String> getDistinctPhenotypesByStrainGroupId(String strainGoupId) throws Exception {
        String sql="select distinct(clinical_measurement_ont_id) from phenominer_expected_range where strain_group_id="+strainGoupId;
        StringListQuery q= new StringListQuery(this.getDataSource(), sql);
        return q.execute();

    }
    public List<String> getDistinctPhenotypesByTrait(String strainGoupId, String traitOntId) throws Exception {
        String sql="select distinct(clinical_measurement_ont_id) from phenominer_expected_range where strain_group_id=? " ;
        if(traitOntId!=null) {
            if(!traitOntId.equalsIgnoreCase("pga"))
                sql+=   " and expected_range_id in (" +
                        "Select expected_range_id from phenominer_range_trait where trait_ont_id=?)";
            else
                sql+=" and trait_ont_id is null";
        }
        StringListQuery q= new StringListQuery(this.getDataSource(), sql);
        if(traitOntId!=null && !traitOntId.equalsIgnoreCase("pga"))
            return execute(q, new Object[]{strainGoupId, traitOntId});
        else return execute(q, new Object[]{strainGoupId});

    }
    public List<String> getDistinctTraits() throws Exception{
        String sql="SELECT DISTINCT(TRAIT_ONT_ID) FROM PHENOMINER_RANGE_TRAIT";
        StringListQuery query= new StringListQuery(this.getDataSource(), sql);
        return query.execute();
    }
    public List<String> getDistinctSubTraits(String traitOntId) throws Exception{
        String sql="SELECT DISTINCT(SUBTRAIT_ONT_ID) FROM PHENOMINER_RANGE_TRAIT   WHERE TRAIT_ONT_ID=?";
        StringListQuery query= new StringListQuery(this.getDataSource(), sql);
        return execute(query, new Object[]{traitOntId});
    }
    public List<PhenominerExpectedRange> getExpectedRangesByTraitSubTrait(String traitOntId, String subtraitOntId ) throws Exception {
        String sql="SELECT * FROM PHENOMINER_EXPECTED_RANGE WHERE EXPECTED_RANGE_ID IN (" +
                " SELECT EXPECTED_RANGE_ID FROM PHENOMINER_RANGE_TRAIT WHERE TRAIT_ONT_ID=? AND SUBTRAIT_ONT_ID=?)";
        PhenominerExpectedRangeQuery query= new PhenominerExpectedRangeQuery(this.getDataSource(),   sql);
        return execute(query, new Object[]{traitOntId, subtraitOntId});

    }
    public List<PhenominerExpectedRange> getExPectedRangesByTraitNStrainGroupId(String traitOntId, int strainGroupId, boolean isPGA) throws Exception {
        String sql=new String();

        if(!isPGA && traitOntId!=null) {
            if(!traitOntId.equals("")) {
                sql = "select * from phenominer_expected_range where strain_group_id=?" +
                        " and expected_Range_id in (select expected_range_id from phenominer_range_trait where trait_ont_id=?)";
                PhenominerExpectedRangeQuery query = new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
                return execute(query, new Object[]{strainGroupId, traitOntId});
            }else{
                sql="select * from phenominer_expected_range where strain_group_id=?";

                PhenominerExpectedRangeQuery query=new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
                return execute(query, new Object[]{strainGroupId});
            }
        }else{
            if(isPGA) {
                sql = "select * from phenominer_expected_range where strain_group_id=?" +
                        " and trait_ont_id is null";
                PhenominerExpectedRangeQuery query = new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
                return execute(query, new Object[]{strainGroupId});
            }else{
                sql="select * from phenominer_expected_range where strain_group_id=?";

                PhenominerExpectedRangeQuery query=new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
                return execute(query, new Object[]{strainGroupId});
            }
        }

    }

    public List<PhenominerExpectedRange> getNormalRangesByCMId(String clinicalId) throws Exception {
        String sql= "select * from phenominer_expected_range where clinical_measurement_ont_id = ? and expected_range_name like 'Normal%'";
        PhenominerExpectedRangeQuery query = new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
        return execute(query, new Object[]{clinicalId});

    }
    public List<PhenominerExpectedRange> getExpectedRanges(List<String> clinicalMeasurementOntIds, List<Integer> strainGroupIds, List<String> sex, List<Integer> ageLow, List<Integer> ageHigh, List<String> selectedMethods, boolean isPGA, String traitOntId) throws Exception {
        String sql = "SELECT * FROM PHENOMINER_EXPECTED_RANGE WHERE CLINICAL_MEASUREMENT_ONT_ID in ( ";
        boolean first=true;
        for(String m:clinicalMeasurementOntIds){
            if(first) {
                sql += "'"+ m+ "'";
                first=false;
            }
            else sql+=" , '" + m+"'";
        }
        sql+=")";
        if(strainGroupIds != null) {
            sql = sql + this.buildIntQuery(strainGroupIds, "strain_group_id");
        }

        if(ageLow != null) {
            sql = sql + this.buildIntQuery(ageLow, "AGE_DAYS_FROM_DOB_LOW_BOUND");
        }

        if(ageHigh != null) {
            sql = sql + this.buildIntQuery(ageHigh, "AGE_DAYS_FROM_DOB_HIGH_BOUND");
        }

        if(sex != null ) {
            if(sex.size()>0)
                sql = sql + this.buildStringQuery(sex, "SEX");
        }

        if(selectedMethods != null) {
            String query = this.buildMethodQuery(selectedMethods, "expected_range_name");
            if(query != null) {
                sql = sql + query;
            }
        }
        if(traitOntId!=null){
            sql = sql + " AND TRAIT_ONT_ID=? ";
        }
        if(isPGA) {
            sql = sql + " AND TRAIT_ONT_ID IS NULL ";
        }
  //      System.out.println(sql);
        PhenominerExpectedRangeQuery query1 = new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
        if(traitOntId!=null)
            return this.execute(query1, new Object[]{traitOntId});
        else
            return query1.execute();
    }
  /*  public List<PhenominerExpectedRange> getExpectedRangesByParentTrait(String clinicalMeasurementOntId,  boolean isPGA, String subTraitOntId) throws Exception {
        String sql = "SELECT * FROM PHENOMINER_EXPECTED_RANGE WHERE CLINICAL_MEASUREMENT_ONT_ID=? " +
                " AND EXPECTED_RANGE_ID in ( SELECT EXPECTED_RANGE_ID FROM PHENOMINER_RANGE_TRAIT WHERE" +
                " SUBTRAIT_ONT_ID=? )";
        PhenominerExpectedRangeQuery query1 = new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
        return this.execute(query1, new Object[]{clinicalMeasurementOntId, subTraitOntId});
    }
*/
    public List<String> getDistinctPhenotypes(String trait) throws Exception {
        String sql = "select distinct(CLINICAL_MEASUREMENT_ONT_ID) from PHENOMINER_EXPECTED_RANGE";
        if(trait!=null){
            sql+=" where trait_ont_id=?";
        }
        StringListQuery query = new StringListQuery(this.getDataSource(), sql);
        if(trait!=null){
            return execute(query, new Object[]{trait});
        }else
            return query.execute();
    }

  /*  public int getExpectedRangeId(PhenominerExpectedRange range) throws Exception {
        String sql="select expected_range_id from phenominer_expected_range where expected_range_name=? and " +
                " clinical_measurement_ont_id=? and " +
                " strain_group_id=? and " +
                " AGE_DAYS_FROM_DOB_LOW_BOUND=? and " +
                " AGE_DAYS_FROM_DOB_HIGH_BOUND=? and " +
                " sex=?";
        if(range.getTraitOntId()!=null && !Objects.equals(range.getTraitOntId(), "")){
            sql+=  " and trait_ont_id=?  " ;
        }
        IntListQuery query= new IntListQuery(this.getDataSource(), sql);
        List ids=new ArrayList<>();
        if(range.getTraitOntId()!=null && !Objects.equals(range.getTraitOntId(), "")) {
            ids = execute(query, range.getStrainGroupName() + "_" + range.getExpectedRangeName(), range.getClinicalMeasurementOntId(), range.getStrainGroupId(),
                    range.getAgeLowBound(), range.getAgeHighBound(), range.getSex(),range.getTraitOntId() );
        }else{
            ids = execute(query, range.getStrainGroupName() + "_" + range.getExpectedRangeName(), range.getClinicalMeasurementOntId(), range.getStrainGroupId(),
                    range.getAgeLowBound(), range.getAgeHighBound(), range.getSex());
}
        return ids.size()>0? (int) ids.get(0) :0;
    }*/

    public int getExpectedRangeId(PhenominerExpectedRange range) throws Exception {
        String sql = "select expected_range_id from phenominer_expected_range where expected_range_name=? and  clinical_measurement_ont_id=? and  strain_group_id=? and  AGE_DAYS_FROM_DOB_LOW_BOUND=? and  AGE_DAYS_FROM_DOB_HIGH_BOUND=? and  sex=?";
        if(range.getTraitOntId() != null && !Objects.equals(range.getTraitOntId(), "")) {
            sql = sql + " and trait_ont_id=?  ";
        }

        IntListQuery query = new IntListQuery(this.getDataSource(), sql);
        new ArrayList();
        List ids;
        //     System.out.println(range.getExpectedRangeName()+"\t"+ range.getClinicalMeasurementOntId()+"\t"+ range.getStrainGroupId()+"\t"+range.getTraitOntId());
        if(range.getTraitOntId() != null && !Objects.equals(range.getTraitOntId(), "")) {
            if(range.getExpectedRangeName().contains("NormalStrain")){
                ids = this.execute(query, new Object[]{range.getExpectedRangeName(), range.getClinicalMeasurementOntId(), Integer.valueOf(range.getStrainGroupId()), Integer.valueOf(range.getAgeLowBound()), Integer.valueOf(range.getAgeHighBound()), range.getSex(), range.getTraitOntId()});
            }else
                ids = this.execute(query, new Object[]{range.getStrainGroupName() + "_" + range.getExpectedRangeName(), range.getClinicalMeasurementOntId(), Integer.valueOf(range.getStrainGroupId()), Integer.valueOf(range.getAgeLowBound()), Integer.valueOf(range.getAgeHighBound()), range.getSex(), range.getTraitOntId()});
        } else {
            if(range.getExpectedRangeName().contains("NormalStrain")){
                ids = this.execute(query, new Object[]{range.getExpectedRangeName(), range.getClinicalMeasurementOntId(), Integer.valueOf(range.getStrainGroupId()), Integer.valueOf(range.getAgeLowBound()), Integer.valueOf(range.getAgeHighBound()), range.getSex()});
            }else
                ids = this.execute(query, new Object[]{range.getStrainGroupName() + "_" + range.getExpectedRangeName(), range.getClinicalMeasurementOntId(), Integer.valueOf(range.getStrainGroupId()), Integer.valueOf(range.getAgeLowBound()), Integer.valueOf(range.getAgeHighBound()), range.getSex()});
        }

        return ids.size() > 0?((Integer)ids.get(0)).intValue():0;
    }
    public int updateExpectedRange(PhenominerExpectedRange range, int expectedRangeId) throws Exception {
        String sql="update phenominer_expected_range set RANGE_VALUE=?, RANGE_LOW=?, RANGE_HIGH=?, RANGE_SD=?, last_modified_date=SYSDATE "+
                "where expected_range_id=?";
        return update(sql, range.getRangeValue(), range.getRangeLow(), range.getRangeHigh(), range.getRangeSD(), expectedRangeId);

    }
    public int insertRangeTrait(TraitObject o, int expectedRangeId) throws Exception {
        String sql = "INSERT INTO PHENOMINER_RANGE_TRAIT(EXPECTED_RANGE_ID,TRAIT_ONT_ID,SUBTRAIT_ONT_ID) VALUES (?,?,?) ";
        String traitOntId= o.getTrait()!=null?o.getTrait().getAccId():"";
        String subtraitOntId=o.getSubTrait()!=null?o.getSubTrait().getAccId():"";

        return this.update(sql, valueOf(expectedRangeId), traitOntId, subtraitOntId);
    }
    public List<TraitObject> getPhenominerRangeTraitAncestors(TraitObject o,int expectedRangeId) throws Exception {
        String sql="select * from phenominer_range_trait where expected_range_id=? and " +
                " trait_ont_id=? and subtrait_ont_id=?";
        PhenominerRangeTraitQuery query=new PhenominerRangeTraitQuery(this.getDataSource(), sql);
        return execute(query, new Object[]{expectedRangeId, o.getTrait().getAccId(),o.getSubTrait().getAccId()});
    }
    public int updateRangeTrait(TraitObject o, int expectedRangeId) throws Exception {
        String sql="update phenominer_range_trait set trait_ont_id=?, subtrait_ont_id=? where expected_range_id=?";
        return   update(sql, o.getTrait().getAccId(), o.getSubTrait().getAccId(), expectedRangeId);
    }
    public List<PhenominerExpectedRange> getExpectedRangesByParentTrait(String clinicalMeasurementOntId, boolean isPGA, String subTraitOntId) throws Exception {
        String sql = "SELECT * FROM PHENOMINER_EXPECTED_RANGE WHERE CLINICAL_MEASUREMENT_ONT_ID=?  AND   EXPECTED_RANGE_ID in ( SELECT EXPECTED_RANGE_ID FROM PHENOMINER_RANGE_TRAIT WHERE SUBTRAIT_ONT_ID=? )";
        if(isPGA) {
            sql = sql + " OR EXPECTED_RANGE_ID IN (SELECT EXPECTED_RANGE_ID FROM PHENOMINER_EXPECTED_RANGE WHERE CLINICAL_MEASUREMENT_ONT_ID=? AND TRAIT_ONT_ID IS NULL)";
        }

        PhenominerExpectedRangeQuery query1 = new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
        if(isPGA)
            return this.execute(query1, new Object[]{clinicalMeasurementOntId, subTraitOntId, clinicalMeasurementOntId});
        else
            return this.execute(query1, new Object[]{clinicalMeasurementOntId, subTraitOntId});
    }
    public int getPhenominerRangeExperimentRecCount(int experimentRecordId, int expectedRangeId) throws Exception {
        String sql="select * from phenominer_range_exp_rec where experiment_record_id=? and expected_range_id=?";
        PhenominerRangeExpRecQuery query= new PhenominerRangeExpRecQuery(getDataSource(), sql);
        List<PhenominerRangeExperimentRec> recs= execute(query, experimentRecordId, expectedRangeId);
        return recs.size();
    }


    public List<PhenominerExpectedRange> getExpectedRanges(String clinicalMeasurementOntId, List<Integer> strainGroupIds, List<String> sex, List<Integer> ageLow, List<Integer> ageHigh, List<String> selectedMethods, boolean isPGA) throws Exception {
        String sql = "SELECT * FROM PHENOMINER_EXPECTED_RANGE WHERE CLINICAL_MEASUREMENT_ONT_ID=? ";
        if(strainGroupIds != null && strainGroupIds.size()>0) {
            sql = sql + this.buildIntQuery(strainGroupIds, "strain_group_id");
        }

        if(ageLow != null && ageLow.size()>0) {
            sql = sql + this.buildIntQuery(ageLow, "AGE_DAYS_FROM_DOB_LOW_BOUND");
        }

        if(ageHigh != null && ageHigh.size()>0) {
            sql = sql + this.buildIntQuery(ageHigh, "AGE_DAYS_FROM_DOB_HIGH_BOUND");
        }

        if(sex != null && sex.size()>0) {
            sql = sql + this.buildStringQuery(sex, "SEX");
        }

        if(selectedMethods != null && selectedMethods.size()>0) {
            String query1 = this.buildMethodQuery(selectedMethods, "expected_range_name");
            if(query1 != null) {
                sql = sql + query1;
            }
        }

        if(isPGA) {
            sql = sql + " AND TRAIT_ONT_ID IS NULL ";
        }else{
            sql=sql + " AND TRAIT_ONT_ID is not null";
        }

        PhenominerExpectedRangeQuery query11 = new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
        return this.execute(query11, new Object[]{clinicalMeasurementOntId});
    }
    public List<PhenominerExpectedRange> getExpectedRangesOfPhenotype(String clinicalMeasurementOntId, List<Integer> strainGroupIds, List<String> sex, List<Integer> ageLow, List<Integer> ageHigh, List<String> selectedMethods, boolean isPGA) throws Exception {
        String sql = "SELECT * FROM PHENOMINER_EXPECTED_RANGE WHERE CLINICAL_MEASUREMENT_ONT_ID=? ";
        if(strainGroupIds != null) {
            sql = sql + this.buildIntQuery(strainGroupIds, "strain_group_id");
        }

        if(ageLow != null) {
            sql = sql + this.buildIntQuery(ageLow, "AGE_DAYS_FROM_DOB_LOW_BOUND");
        }

        if(ageHigh != null) {
            sql = sql + this.buildIntQuery(ageHigh, "AGE_DAYS_FROM_DOB_HIGH_BOUND");
        }

        if(sex != null) {
            sql = sql + this.buildStringQuery(sex, "SEX");
        }

        if(selectedMethods != null) {
            String query1 = this.buildMethodQuery(selectedMethods, "expected_range_name");
            if(query1 != null) {
                sql = sql + query1;
            }
        }

        if(isPGA) {
            sql = sql + " AND TRAIT_ONT_ID IS NULL ";
        }

        PhenominerExpectedRangeQuery query11 = new PhenominerExpectedRangeQuery(this.getDataSource(), sql);
        return this.execute(query11, new Object[]{clinicalMeasurementOntId});
    }
    public int getExpectedRangesByTrait(String traitOntId) throws Exception {
        String sql = "SELECT Count(distinct(clinical_measurement_ont_id)) FROM PHENOMINER_EXPECTED_RANGE WHERE EXPECTED_RANGE_ID IN ( SELECT EXPECTED_RANGE_ID FROM PHENOMINER_RANGE_TRAIT WHERE TRAIT_ONT_ID= ? )";
        CountQuery query = new CountQuery(this.getDataSource(), sql);
        //  return execute(query, new Object[]{traitOntId});
        query.declareParameter(new SqlParameter(Types.VARCHAR));
        return query.getCount(new Object[]{traitOntId});
    }

    public static void main(String[] args) throws Exception {
        PhenominerExpectedRangeDao dao=new PhenominerExpectedRangeDao();
        List<PhenominerExpectedRange> records = dao.getExpectedRanges("CMO:0000038", null, null, null, null, null, false);
        System.out.println("RECORDS SIZE:"+ records.size());
    }
}
