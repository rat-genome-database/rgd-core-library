package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.dao.spring.phenominerExpectedRanges.PhenominerStrainGroupQuery;
import edu.mcw.rgd.datamodel.phenominerExpectedRange.PhenominerStrainGroup;

import java.util.List;

/**
 * Created by jthota on 4/30/2018.
 */
public class PhenominerStrainGroupDao extends OntologyXDAO{

    public int insertOrUpdate(PhenominerStrainGroup strainGroup) throws Exception {
        String sql= "insert into PHENOMINER_STRAIN_GROUP(strain_group_id, strain_group_name, strain_ont_id, created_date, last_modified_date) values( "+strainGroup.getId()+",'" + strainGroup.getName()+"', '"+strainGroup.getStrain_ont_id()+"' , SYSDATE, SYSDATE)";
        return this.update(sql);
    }
    public List<PhenominerStrainGroup> getAllStrainGroups() throws Exception {
        String sql="select * from phenominer_strain_group";
        PhenominerStrainGroupQuery query= new PhenominerStrainGroupQuery(this.getDataSource(), sql);
        return this.execute(query);
    }
    public List<PhenominerStrainGroup> getStrainGroupById(int strainGroupId) throws Exception {
        String sql="select * from phenominer_strain_group where strain_group_id=" +strainGroupId;
        PhenominerStrainGroupQuery query= new PhenominerStrainGroupQuery(this.getDataSource(), sql);
        return this.execute(query);
    }
    public int getStrainGroupId(String strainGroupName) throws Exception {

        String sql="select strain_group_id from phenominer_strain_group where strain_group_name=?" ;
        IntListQuery query= new IntListQuery(this.getDataSource(), sql);
        List<Integer> ids= this.execute(query, new Object[]{strainGroupName});
        return ids.size()>0?ids.get(0):0;
    }
    public List<PhenominerStrainGroup> getStrainGroupByName(String strainGroupName) throws Exception {
        String sql="select * from phenominer_strain_group where strain_group_name='" +strainGroupName+"'";
        PhenominerStrainGroupQuery query= new PhenominerStrainGroupQuery(this.getDataSource(), sql);
        return this.execute(query);
    }
    public List<Integer> getAllDistinctStrainGroupIds() throws Exception {
        String sql="select distinct(strain_group_id) from phenominer_strain_group";
        IntListQuery query= new IntListQuery(this.getDataSource(), sql);
        return execute(query);
    }
    public List<String> getAllDistinctStrainGroupNames() throws Exception {
        String sql="select distinct(strain_group_name) from phenominer_strain_group where strain_group_name not like '%NormalStrain%' order by strain_group_name";
        StringListQuery query= new StringListQuery(this.getDataSource(), sql);
        return execute(query);
    }
    public String getStrainGroupName(int strainGroupId) throws Exception {
        String sql="SELECT STRAIN_GROUP_NAME FROM PHENOMINER_STRAIN_GROUP WHERE STRAIN_GROUP_ID="+"'"+ strainGroupId+"'";
        StringListQuery query=new StringListQuery(this.getDataSource(), sql);
        return (String) query.execute().get(0);
    }
    public int getStrainGroupId(String strainGroupName, String rsId) throws Exception {
        String sql="select strain_group_id from PHENOMINER_STRAIN_GROUP where strain_group_name=? and strain_ont_id=?";
        IntListQuery query= new IntListQuery(this.getDataSource(), sql);
        List<Integer> ids=  execute(query, new Object[]{strainGroupName, rsId});
        if(ids!=null){
            if(ids.size()>0)
                return ids.get(0);
        }
        return 0;
    }
    public List<String> getStrainsOfStrainGroup(int strainGroupId) throws Exception {
        String sql="SELECT STRAIN_ONT_ID FROM PHENOMINER_STRAIN_GROUP WHERE STRAIN_GROUP_ID="+"'"+   strainGroupId+"'";
        StringListQuery query= new StringListQuery(this.getDataSource(), sql);
        return  query.execute();
    }
  /*  public int getStrainGroupIdByStrainOntId(String strainOntId) throws Exception {
        String sql="select strain_group_id from phenominer_strain_group where strain_ont_id=?";
        IntListQuery query= new IntListQuery(this.getDataSource(), sql);
        List<Integer> ids= execute(query, strainOntId);
        if(ids.size()>0){
            return ids.get(0);
        }else return 0;
}*/
  public int getStrainGroupIdByStrainOntId(String strainOntId) throws Exception {
      String sql = "select strain_group_id from phenominer_strain_group where strain_ont_id=? and strain_group_name not like 'NormalStrain%'";
      IntListQuery query = new IntListQuery(this.getDataSource(), sql);
      List ids = this.execute(query, new Object[]{strainOntId});
      return ids.size() > 0?((Integer)ids.get(0)).intValue():0;
  }
}
