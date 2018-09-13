package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;

import java.util.Date;

/**
 * Bean class for a protein.
 * <p>
 * Data is currently contained in the proteins table
 */
public class Protein implements Identifiable, Speciated, ObjectWithName, ObjectWithSymbol, Dumpable {

    private int rgdId;
    private String symbol;
    private String name;
    private String uniprotId;
    private int speciesTypeKey;
    private String srcPipeline;
    private Date createdDate;

    /**
     * two genes are equal if either they have same rgd_id or gene key
     * @param obj Protein object to compare
     * @return true if both genes have either the same rgd ids or gene keys
     */
    @Override
    public boolean equals(Object obj) {
        Protein g = (Protein) obj;
        return this.rgdId==g.rgdId;
    }

    /**
     * hashCode() must return consistent values: if two Gene objects are equal, their hash codes must be equal
     * @return
     */
    @Override
    public int hashCode() {
        return rgdId;
    }

    @Override
    public int getRgdId() {
        return rgdId;
    }

    @Override
    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniprotId() {
        return uniprotId;
    }

    public void setUniprotId(String uniprotId) {
        this.uniprotId = uniprotId;
    }

    @Override
    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public String getSrcPipeline() {
        return srcPipeline;
    }

    public void setSrcPipeline(String srcPipeline) {
        this.srcPipeline = srcPipeline;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String toString() {
       return "RGD:" + rgdId + ", "+uniprotId+", " + symbol + ", " + name+", "+srcPipeline;
    }

    public String dump(String delimiter) {

        return new Dumper(delimiter)
            .put("RGD_ID", rgdId)
            .put("UNIPROT_ID", uniprotId)
            .put("SYMBOL", symbol)
            .put("NAME", name)
            .put("CREATED_DATE", createdDate)
            .put("SRC_PIPELINE", srcPipeline)
                .put("SPECIES_TYPE_KEY", speciesTypeKey)
            .dump();
    }
}
