package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.SubmittedStrainQuery;
import edu.mcw.rgd.datamodel.models.SubmittedStrain;


import javax.sql.DataSource;
import java.util.List;

/**
 * Created by jthota on 7/28/2016.
 */
public class SubmittedStrainDao extends AbstractDAO {

    public DataSource getDataSource() throws Exception{
        return DataSourceFactory.getInstance().getCurationDataSource();
    }

    public int insert(SubmittedStrain s) throws Exception{

    String sql="insert into submitted_strains (submitted_strain_key, strain_symbol, strain_symbol_lc,strain_type, genetic_status, background_strain, modification_method,  origin, source,"+
       "reference_id, display_status, notes, research_use, ilar_code,  AVAILABILTIY_CONTACT_EMAIL, AVAILABILTTY_CONTACT_URL, gene_symbol,GENE_RGD_ID, allele_symbol,ALLELE_RGD_ID,"+
               "last_name, first_name, email, pi, organization, created_date, last_updated_date, modified_by, approval_status,pi_email,image_url,strain_rgd_id)"+
        "values (?, ?,?, ?,?, ?, ?, ?, ?,?, ?,?, ?,?, ?, "+
               " ?, ?,?,?,?, ?, ?, ?,?,?,  SYSDATE, SYSDATE, ?, ?,?,?,?)";
        int submittedStrainKey= s.getSubmittedStrainKey();
        String strainSymbol= s.getStrainSymbol();
        String strain_symbol_lc= s.getStrainSymbolLc();
        String strain_type=s.getStrainType();
        String origin=s.getOrigin();
        String source=s.getSource();
        String reference=s.getReference();
        String notes=s.getNotes();
        String researchUse=s.getResearchUse();
        String geneticStatus=s.getGeneticStatus();
        String backgroundStrain=s.getBackgroundStrain();
        String method=s.getMethod();
        String geneSymbol=s.getGeneSymbol();
        String alleleSymbol=s.getAlleleSymbol();
        String ilarCode=s.getIlarCode();
      //  String isAvailable=s.getIsAvailable();
        String availabilityContactEmail=s.getAvailabilityContactEmail();
        String availabilityContactUrl=s.getAvailabilityContactUrl();
        int geneRgdId=0;
        if(s.getGeneRgdId()!=0){
            geneRgdId=s.getGeneRgdId();
        }
         int alleleRgdId=0;
                 if(s.getAlleleRgdId()!=0){
                     alleleRgdId=s.getAlleleRgdId();
                 }
         String lastName=s.getLastName();
         String firstName=s.getFirstName();
         String email=s.getEmail();
         String piName=s.getPiName();
         String piEmail=s.getPiEmail();
         String organization=s.getOrganization();
         String modifiedBy=s.getModifiedBy();
         String approvalStatus=s.getApprovalStatus();
         String displayStatus=s.getDisplayStatus();
         String imageUrl= s.getImageUrl();
        int strainRgdId=s.getStrainRgdId();

       return update(sql, submittedStrainKey, strainSymbol,strain_symbol_lc,strain_type,geneticStatus, backgroundStrain, method, origin,  source, reference, displayStatus, notes, researchUse,ilarCode, availabilityContactEmail, availabilityContactUrl,  geneSymbol,geneRgdId, alleleSymbol,alleleRgdId, lastName, firstName, email, piName, organization, modifiedBy, approvalStatus,piEmail, imageUrl,strainRgdId );

    }

    public int updateStrainRgdId(int submissionKey, int strainRgdId) throws Exception{
        String sql="update submitted_strains set strain_rgd_id=?,last_updated_date=SYSDATE where submitted_strain_key=?";

        return update(sql,strainRgdId,submissionKey);
    }

    public int updateGeneRgdId(int submissionKey, int geneRgdId) throws Exception{
        String sql="update submitted_strains set gene_rgd_id=?,last_updated_date=SYSDATE where submitted_strain_key=?";

        return update(sql,geneRgdId,submissionKey);
    }
    public int updateAlleleRgdId(int submissionKey, int alleleRgdId) throws Exception{
        String sql="update submitted_strains set allele_rgd_id=?,last_updated_date=SYSDATE  where submitted_strain_key=?";
        return update(sql,alleleRgdId, submissionKey);
    }
    public int updateApprovalStatus(int submissionKey, String status)throws Exception{
        String sql="update submitted_strains set approval_status=?, last_updated_date=SYSDATE where submitted_strain_key=?";
        return update(sql, status, submissionKey);
    }
    public int updateImageUrl(int key, String imageUrl) throws Exception{
        String sql="update submitted_strains set image_url=?, last_updated_date=SYSDATE where submitted_strain_key=?";
        return update(sql, imageUrl, key);

    }
    public int delete(int submissionKey) throws Exception{
        String sql="delete submitted_strains where SUBMITTED_STRAIN_KEY=?";
        return update(sql, submissionKey);

    }
    public List<SubmittedStrain> getAllSubmittedStrains() throws Exception{
        String sql= "select * from submitted_strains";
        SubmittedStrainQuery q= new SubmittedStrainQuery(this.getDataSource(), sql);
        return execute(q);
    }
    public List<SubmittedStrain> getSubmittedStrainByStrainSymbolLC(String strainSymbolLc) throws Exception{
        String sql= "select * from submitted_strains where strain_symbol_lc=?";
        SubmittedStrainQuery q= new SubmittedStrainQuery(this.getDataSource(), sql);
         return execute(q,strainSymbolLc);
    }

    public List<SubmittedStrain> getInProcessStrains() throws Exception{
        String sql="select * from submitted_strains where approval_status in ('incomplete','submitted') order by last_updated_date desc";
        SubmittedStrainQuery q= new SubmittedStrainQuery(this.getDataSource(),sql);
        List<SubmittedStrain> strains= execute(q);
        return strains;
    }
    public List<SubmittedStrain> getCompletedStrains() throws Exception{
        String sql="select * from submitted_strains where approval_status in ('complete', 'denied') order by last_updated_date desc ";
        SubmittedStrainQuery q= new SubmittedStrainQuery(this.getDataSource(),sql);
        List<SubmittedStrain> strains= execute(q);
        return strains;
    }
    public SubmittedStrain getSubmittedStrainBySubmissionKey(int submissionKey) throws Exception {
        String sql="select * from submitted_strains where submitted_strain_key=?";
        SubmittedStrainQuery q= new SubmittedStrainQuery(this.getDataSource(), sql);
        List rows=execute(q, submissionKey);
        if(rows!=null){
        return (SubmittedStrain) rows.get(0);}
        else return null;
    }
    public int updateSubmittedStrain(String strainSymbolLc, String status) throws Exception{
        String sql= "update submitted_strains set approval_status=? , last_updated_date = SYSDATE where strain_symbol_lc=?";
        return update(sql,status,strainSymbolLc);
    }

    public int getNextKey() throws Exception {
        return getNextKey("submitted_strain_seq");
    }

}
