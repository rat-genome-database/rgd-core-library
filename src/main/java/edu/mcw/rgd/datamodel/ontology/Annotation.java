package edu.mcw.rgd.datamodel.ontology;

import edu.mcw.rgd.datamodel.Dumpable;
import edu.mcw.rgd.process.Dumper;

import java.util.Date;

/**
 * @author jdepons
 * @since Mar 20, 2009
 * Bean class for an ontology annotation
 */
public class Annotation implements Cloneable, Dumpable {
    private Integer key;
    private String term;
    private Integer annotatedObjectRgdId;
    private Integer rgdObjectKey;
    private String dataSrc;
    private String objectSymbol;
    private Integer refRgdId;
    private String evidence;
    private String withInfo;
    private String aspect;
    private String objectName;
    private String notes;
    private String qualifier;
    private String relativeTo;
    private Date createdDate;
    private Date lastModifiedDate;
    private String termAcc;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private String xrefSource;
    private int speciesTypeKey; // optional: if 0, it is undefined
    private String annotationExtension;
    private String geneProductFormId;
    private Date originalCreatedDate;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getAnnotatedObjectRgdId() {
        return annotatedObjectRgdId;
    }

    public void setAnnotatedObjectRgdId(Integer annotatedObjectRgdId) {
        this.annotatedObjectRgdId = annotatedObjectRgdId;
    }

    public Integer getRgdObjectKey() {
        return rgdObjectKey;
    }

    public void setRgdObjectKey(Integer rgdObjectKey) {
        this.rgdObjectKey = rgdObjectKey;
    }

    public String getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(String dataSrc) {
        this.dataSrc = dataSrc;
    }

    public String getObjectSymbol() {
        return objectSymbol;
    }

    public void setObjectSymbol(String objectSymbol) {
        this.objectSymbol = objectSymbol;
    }

    public Integer getRefRgdId() {
        return refRgdId;
    }

    public void setRefRgdId(Integer refRgdId) {
        this.refRgdId = refRgdId;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getWithInfo() {
        return withInfo;
    }

    public void setWithInfo(String withInfo) {
        this.withInfo = withInfo;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getRelativeTo() {
        return relativeTo;
    }

    public void setRelativeTo(String relativeTo) {
        this.relativeTo = relativeTo;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getTermAcc() {
        return termAcc;
    }

    public void setTermAcc(String termAcc) {
        this.termAcc = termAcc;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Integer lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getXrefSource() {
        return xrefSource;
    }

    public void setXrefSource(String xrefSource) {
        this.xrefSource = xrefSource;
    }

    public int getSpeciesTypeKey() {
        return speciesTypeKey;
    }

    public void setSpeciesTypeKey(int speciesTypeKey) {
        this.speciesTypeKey = speciesTypeKey;
    }

    public String getAnnotationExtension() {
        return annotationExtension;
    }

    public void setAnnotationExtension(String annotationExtension) {
        this.annotationExtension = annotationExtension;
    }

    public String getGeneProductFormId() {
        return geneProductFormId;
    }

    public void setGeneProductFormId(String geneProductFormId) {
        this.geneProductFormId = geneProductFormId;
    }

    public Date getOriginalCreatedDate() {
        return originalCreatedDate;
    }

    public void setOriginalCreatedDate(Date originalCreatedDate) {
        this.originalCreatedDate = originalCreatedDate;
    }

    public String dump(String delimiter) {

        return new Dumper(delimiter, true, true)
            .put("FULL_ANNOT_KEY", key)
            .put("RGD_ID", annotatedObjectRgdId)
            .put("OBJ_KEY", rgdObjectKey)
            .put("OBJ_SYMBOL", objectSymbol)
            .put("OBJ_NAME", objectName)
            .put("TERM_ACC", termAcc)
            .put("TERM", term)
            .put("EVIDENCE", evidence)
            .put("WITH_INFO", withInfo)
            .put("ASPECT", aspect)
            .put("QUALIFIER", qualifier)
            .put("DATA_SRC", dataSrc)
            .put("REF_RGD_ID", refRgdId)
            .put("XREF_SOURCE", xrefSource)
            .put("NOTES", notes)
            .put("CREATED_DATE", createdDate)
            .put("LAST_MOD_DATE", lastModifiedDate)
            .put("CREATED_BY", createdBy)
            .put("LAST_MOD_BY", lastModifiedBy)
            .put("RELATIVE_TO", relativeTo)
            .put("SPECIES_TYPE_KEY", speciesTypeKey)
            .put("ANNOT_EXT", annotationExtension)
            .put("PRODUCT_ID", geneProductFormId)
            .put("ORIG_CREATED_DATE", originalCreatedDate)
            .dump();
    }
}
