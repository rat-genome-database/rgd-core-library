package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.dao.impl.StrainDAO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: jdepons
 * Date: May 20, 2008
 */
public class Strain implements Identifiable, ObjectWithName, ObjectWithSymbol {

    private int key;
    private String symbol;
    private String name;
    private String strain;
    private String substrain;
    private String genetics;
    private String inbredGen;
    private String origin;
    private String color;
    private String chrAltered;
    private String source;
    private String notes;
    private int rgdId;
    private String strainTypeName;
    private int speciesTypeKey;
    private String imageUrl;
    private String researchUse;
    private String geneticStatus;
    private Integer backgroundStrainRgdId; // could be NULL
    private String modificationMethod;

    private String taglessStrainSymbol;

    private String origination;

    private String description;

    private String lastStatus; // last status, determined from analysis of statusLog
    private List<Status> statusLog; // log of all strain statuses


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getResearchUse() {
        return researchUse;
    }

    public void setResearchUse(String researchUse) {
        this.researchUse = researchUse;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrain() {
        return strain;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }

    public String getSubstrain() {
        return substrain;
    }

    public void setSubstrain(String substrain) {
        this.substrain = substrain;
    }

    public String getGenetics() {
        return genetics;
    }

    public void setGenetics(String genetics) {
        this.genetics = genetics;
    }

    public String getInbredGen() {
        return inbredGen;
    }

    public void setInbredGen(String inbredGen) {
        this.inbredGen = inbredGen;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getChrAltered() {
        return chrAltered;
    }

    public void setChrAltered(String chrAltered) {
        this.chrAltered = chrAltered;
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

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getStrainTypeName() {
        return strainTypeName;
    }

    public void setStrainTypeName(String strainTypeName) {
        this.strainTypeName = strainTypeName;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public String getGeneticStatus() {
        return geneticStatus;
    }

    public void setGeneticStatus(String geneticStatus) {
        this.geneticStatus = geneticStatus;
    }

    public Integer getBackgroundStrainRgdId() {
        return backgroundStrainRgdId;
    }

    public void setBackgroundStrainRgdId(Integer backgroundStrainRgdId) {
        this.backgroundStrainRgdId = backgroundStrainRgdId;
    }

    public String getModificationMethod() {
        return modificationMethod;
    }

    public void setModificationMethod(String modificationMethod) {
        this.modificationMethod = modificationMethod;
    }

    public String getTaglessStrainSymbol() {
        return taglessStrainSymbol;
    }

    public void setTaglessStrainSymbol(String taglessStrainSymbol) {
        this.taglessStrainSymbol = taglessStrainSymbol;
    }

    public String getOrigination() {
        return origination;
    }

    public void setOrigination(String origination) {
        this.origination = origination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get list of all strain statuses
     * @return list of strain status objects
     */
    public List<Strain.Status> getStatusLog() throws Exception {
        if( statusLog == null ) {
            loadStatusLog();
        }
        return statusLog;
    }

    /**
     * strain last status, as found in STRAIN_STATUS_LOG table
     * @return strain last status object, or NULL if none
     */
    public Strain.Status getLastStatusObject() throws Exception {
        if( statusLog == null ) {
            loadStatusLog();
        }
        return statusLog.isEmpty() ? null : statusLog.get(0);
    }

    /**
     * strain last status, as found in STRAIN_STATUS_LOG table
     * @return strain last status
     */
    public String getLastStatus() throws Exception {
        if( lastStatus==null ) {
            setLastStatus();
        }
        return lastStatus;
    }

    void setLastStatus() throws Exception {

        Status status = getLastStatusObject();

        // are there any status log entries at all?
        if( status==null ) {
            lastStatus = "Unknown";
            return;
        }

        lastStatus = status.toString();
    }

    /**
     * lazy load strain status log from STRAINS table
     */
    void loadStatusLog() throws Exception {
        statusLog = new StrainDAO().getStatusLog(getRgdId());
    }

    static public SimpleDateFormat statusDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public class Status {
        public int key;
        public int strainRgdId;
        public Date statusDate;
        public Boolean liveAnimals;
        public Boolean cryopreservedEmbryo;
        public Boolean cryopreservedSperm;
        public Boolean cryorecovery;

        @Override
        public boolean equals(Object obj) {
            Status s = (Status)obj;
            return strainRgdId==s.strainRgdId &&
                    getBoolValue(liveAnimals)==getBoolValue(s.liveAnimals) &&
                    getBoolValue(cryopreservedEmbryo)==getBoolValue(s.cryopreservedEmbryo) &&
                    getBoolValue(cryopreservedSperm)==getBoolValue(s.cryopreservedSperm) &&
                    getBoolValue(cryorecovery)==getBoolValue(s.cryorecovery);
        }

        int getBoolValue(Boolean b) {
            return b==null ? -1 : b ? 1 : 0;
        }

        @Override
        public String toString() {
            String status = "";

            // first live animals
            if( liveAnimals!=null && liveAnimals ) {
                status = "Live Animals";
            } else {
                status = "";
            }

            // then cryopreserved status
            if( cryopreservedEmbryo!=null && cryopreservedEmbryo ) {
                if( status.isEmpty() ) {
                    status = "Cryopreserved Embryo";
                } else {
                    status += "; Cryopreserved Embryo";
                }
            }
            if( cryopreservedSperm!=null && cryopreservedSperm ) {
                if( status.isEmpty() ) {
                    status = "Cryopreserved Sperm";
                } else {
                    status += "; Cryopreserved Sperm";
                }
            }
            if( cryorecovery!=null && cryorecovery ) {
                if( status.isEmpty() ) {
                    status = "Cryorecovery";
                } else {
                    status += "; Cryorecovery";
                }
            }

            // handle extinct and N/A statuses
            if( status.isEmpty() ) {
                if( liveAnimals==null && cryopreservedEmbryo==null && cryopreservedSperm==null && cryorecovery==null ) {
                    status = "Unknown";
                } else {
                    status = "Extinct";
                }
            }

            // last show status date if available
            if( statusDate!=null ) {
                status += " (as of "+statusDateFormat.format(statusDate)+")";
            }
            return status;
        }

    }

    public Status createStatus() {
        return new Status();
    }

}