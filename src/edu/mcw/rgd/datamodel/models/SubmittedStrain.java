package edu.mcw.rgd.datamodel.models;

import java.util.Date;
import java.util.List;

/**
 * Created by jthota on 7/28/2016.
 */
public class SubmittedStrain  {
    private int submittedStrainKey;
    private String strainSymbol;
    private String strainSymbolLc;
    private String origin;
    private String source;
    private String reference;
    private String notes;
    private String strainType;
    private String researchUse;
    private String geneticStatus;
    private String backgroundStrain;
    private String method;
    private String geneSymbol;
    private String alleleSymbol;
    private int geneRgdId;
    private int alleleRgdId;
    private String lastName;
    private String firstName;
    private String email;
    private String piName;
    private String piEmail;
    private String organization;
    private Date created_date;
    private Date last_updated_date;
    private String modifiedBy;
    private String approvalStatus;
    private String displayStatus;
    private String ilarCode;
  //  private String isAvailable;
    private String availabilityContactEmail;
    private String availabilityContactUrl;
   private List<SubmittedStrainAvailabiltiy> availList;
    private String imageUrl;

   private int strainRgdId;

    public int getStrainRgdId() {
        return strainRgdId;
    }

    public void setStrainRgdId(int strainRgdId) {
        this.strainRgdId = strainRgdId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPiEmail() {
        return piEmail;
    }

    public void setPiEmail(String piEmail) {
        this.piEmail = piEmail;
    }

    public String getBackgroundStrain() {
        return backgroundStrain;
    }

    public void setBackgroundStrain(String backgroundStrain) {
        this.backgroundStrain = backgroundStrain;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getIlarCode() {
        return ilarCode;
    }

    public void setIlarCode(String ilarCode) {
        this.ilarCode = ilarCode;
    }

   // public String getIsAvailable() {
       // return isAvailable;
   // }

  //  public void setIsAvailable(String isAvailable)/ {
       // this.isAvailable = isAvailable;
   // }

    public String getAvailabilityContactEmail() {
        return availabilityContactEmail;
    }

    public void setAvailabilityContactEmail(String availabilityContactEmail) {
        this.availabilityContactEmail = availabilityContactEmail;
    }

    public String getAvailabilityContactUrl() {
        return availabilityContactUrl;
    }

    public void setAvailabilityContactUrl(String availabilityContactUrl) {
        this.availabilityContactUrl = availabilityContactUrl;
    }

    public int getSubmittedStrainKey() {
        return submittedStrainKey;
    }

    public void setSubmittedStrainKey(int submittedStrainKey) {
        this.submittedStrainKey = submittedStrainKey;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getStrainSymbol() {
        return strainSymbol;
    }

    public void setStrainSymbol(String strainSymbol) {
        this.strainSymbol = strainSymbol;
    }

    public String getStrainSymbolLc() {
        return strainSymbolLc;
    }

    public void setStrainSymbolLc(String strainSymbolLc) {
        this.strainSymbolLc = strainSymbolLc;
    }



    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStrainType() {
        return strainType;
    }

    public void setStrainType(String strainType) {
        this.strainType = strainType;
    }

    public String getResearchUse() {
        return researchUse;
    }

    public void setResearchUse(String researchUse) {
        this.researchUse = researchUse;
    }

    public String getGeneticStatus() {
        return geneticStatus;
    }

    public void setGeneticStatus(String geneticStatus) {
        this.geneticStatus = geneticStatus;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getAlleleSymbol() {
        return alleleSymbol;
    }

    public void setAlleleSymbol(String alleleSymbol) {
        this.alleleSymbol = alleleSymbol;
    }

    public int getGeneRgdId() {
        return geneRgdId;
    }

    public void setGeneRgdId(int geneRgdId) {
        this.geneRgdId = geneRgdId;
    }

    public int getAlleleRgdId() {
        return alleleRgdId;
    }

    public void setAlleleRgdId(int alleleRgdId) {
        this.alleleRgdId = alleleRgdId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPiName() {
        return piName;
    }

    public void setPiName(String piName) {
        this.piName = piName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getLast_updated_date() {
        return last_updated_date;
    }

    public void setLast_updated_date(Date last_updated_date) {
        this.last_updated_date = last_updated_date;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<SubmittedStrainAvailabiltiy> getAvailList() {
        return availList;
    }

    public void setAvailList(List<SubmittedStrainAvailabiltiy> availList) {
        this.availList = availList;
    }




}
