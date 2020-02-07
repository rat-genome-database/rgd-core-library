package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.StringMapQuery;
import edu.mcw.rgd.process.Dumper;

import java.util.*;
import java.util.Map;

/**
 * @author jdepons
 * @since Dec 19, 2007
 * Bean class for an RGD gene.
 * <p>
 * Data is currently contained in the genes table
 */

//@ApiModel
public class Gene implements Identifiable, Speciated, ObjectWithName, ObjectWithSymbol, Dumpable {

    private int key; // gene key
    private String symbol;
    private String name;
    private String description;
    private String notes;
    private int rgdId;
    private String type;
    private Date nomenReviewDate;
    private int speciesTypeKey;
    private String refSeqStatus;
    private String NcbiAnnotStatus;
    private String soAccId;
    private String agrDescription;
    private String mergedDescription;
    private String geneSource;
    private String ensemblGeneSymbol;
    private String ensemblGeneType;
    private String ensemblFullName;
    private String nomenSource;

    /**
     * two genes are equal if either they have same rgd_id or gene key
     * @param obj Gene object to compare
     * @return true if both genes have either the same rgd ids or gene keys
     */
    @Override
    public boolean equals(Object obj) {
        Gene g = (Gene) obj;
        return this.rgdId==g.rgdId || this.key==g.key;
    }

    /**
     * hashCode() must return consistent values: if two Gene objects are equal, their hash codes must be equal
     * @return
     */
    @Override
    public int hashCode() {
        return rgdId>0 ? rgdId : -key;
    }

    /**
     * return true if gene is one of variant types: either splice or allele
     * @return true if gene is one of variant types: either splice or allele; false otherwise
     */
    public boolean isVariant() {
        return getType()!=null && (getType().equals("splice") || getType().equals("allele"));
    }

    /**
     * get species type key: 0-undefined, 1-human, 2-mouse, 3-rat
     * @see SpeciesType
     * @return species type key
     */
   // @ApiModelProperty(position = 1, required = true, value="Returns species type key for gene")
    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
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

    public int getRgdId() {
        return rgdId;
    }

    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int jkey) {
        this.key = jkey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getNomenReviewDate() {
        return nomenReviewDate;
    }

    public void setNomenReviewDate(Date nomenReviewDate) {
        this.nomenReviewDate = nomenReviewDate;
    }

    public String getRefSeqStatus() {
        return refSeqStatus;
    }

    public void setRefSeqStatus(String refSeqStatus) {
        this.refSeqStatus = refSeqStatus;
    }

    /**
     * NCBI genome annotation status; if present (not null) it usually denotes known annotation issues
     * for this gene
     * @return NCBI annotation status or null if not available
     */
    public String getNcbiAnnotStatus() {
        return NcbiAnnotStatus;
    }

    public void setNcbiAnnotStatus(String ncbiAnnotStatus) {
        NcbiAnnotStatus = ncbiAnnotStatus;
    }

    public String getSoAccId() {
        if( soAccId==null ) {
            // load acc id from database
            setSoAccId(getSoAccId(getType()));
        }
        return soAccId;
    }

    public void setSoAccId(String soAccId) {
        this.soAccId = soAccId;
    }

    public String getAgrDescription() {
        return agrDescription;
    }

    public void setAgrDescription(String agrDescription) {
        this.agrDescription = agrDescription;
    }

    public String getMergedDescription() {
        return mergedDescription;
    }

    public void setMergedDescription(String mergedDescription) {
        this.mergedDescription = mergedDescription;
    }

    public void setGeneSource(String geneSource){this.geneSource=geneSource;}
    public String getGeneSource(){return geneSource;}

    public void setEnsemblGeneSymbol(String ensemblGeneSymbol){this.ensemblGeneSymbol=ensemblGeneSymbol;}
    public String getEnsemblGeneSymbol(){return ensemblGeneSymbol;}

    public void setEnsemblGeneType(String ensemblGeneType){this.ensemblGeneType=ensemblGeneType;}
    public String getEnsemblGeneType(){return ensemblGeneType;}

    public void setEnsemblFullName(String ensemblFullName){this.ensemblFullName=ensemblFullName;}
    public String getEnsemblFullName(){return ensemblFullName;}

    public void setNomenSource(String nomenSource){this.nomenSource=nomenSource;}
    public String getNomenSource(){return nomenSource;}

    public String toString() {
       return "RGD:" + rgdId + ", " + symbol + ", " + name;
    }

    public String dump(String delimiter) {

        return new Dumper(delimiter)
            .put("GENE_KEY", key)
            .put("GENE_SYMBOL", symbol)
            .put("FULL_NAME", name)
            .put("GENE_DESC", description)
            .put("NOTES", notes)
            .put("RGD_ID", rgdId)
            .put("GENE_TYPE_LC", type)
            .put("NOMEN_REVIEW_DATE", nomenReviewDate)
            .put("REFSEQ_STATUS", refSeqStatus)
            .put("NCBI_ANNOT_STATUS", NcbiAnnotStatus)
            .put("SPECIES_TYPE_KEY", speciesTypeKey)
            .put("SO_ACC_ID", soAccId)
            .dump();
    }


    static String getSoAccId(String geneType) {
        if( geneType2SoAccIdMap!=null ) {
            return geneType2SoAccIdMap.get(geneType);
        }
        synchronized (Gene.class) {
            geneType2SoAccIdMap = new HashMap<>();
            String sql = "SELECT gene_type_lc,so_acc_id FROM gene_types";
            try {
                for (StringMapQuery.MapPair pair : StringMapQuery.execute(new AbstractDAO(), sql)) {
                    geneType2SoAccIdMap.put(pair.keyValue, pair.stringValue);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return geneType2SoAccIdMap.get(geneType);
    }
    static private Map<String, String> geneType2SoAccIdMap;
}
