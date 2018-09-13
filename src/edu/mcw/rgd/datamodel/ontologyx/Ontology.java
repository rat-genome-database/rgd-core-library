package edu.mcw.rgd.datamodel.ontologyx;

/**
 * Created by IntelliJ IDEA.<br>
 * User: jdepons<br>
 * Date: Feb 17, 2011<br>
 * Time: 2:29:16 PM<br>
 * <p>
 * Class used to represent an Ontology. Based on fields from ONTOLOGIES table.
 */
public class Ontology {

    public static final String CLINICAL_MEASUREMENT= "CMO";
    public static final String STRAIN = "RS";
    public static final String EXPERIMENTAL_CONDITION="XCO";
    public static final String MEASUREMENT_MEATHOD="MMO";

    private String id;          // ONTOLOGIES.ONT_ID
    private String name;        // ONTOLOGIES.ONT_NAME ontology namespace
    private String description; // optional description
    private String aspect; // term aspect, as used by curators
    private boolean isPublic; // ontology is public
    private String oboHeader;
    private String homePage; // ontology home page
    private String logoUrl; // url of the ontology logo (url to picture)
    private String rootTermAcc; // accession id of the ontology root term

    /**
     * check whether given ontology is an external ontology, or internal ontology;
     * internal ontology is an ontology that was developed entirely, or in collaboration, by RGD team
     * @return true if an ontology is an internal ontology
     */
    public boolean isInternal() {
        return id.equals("CMO") || id.equals("MMO") || id.equals("CMO") ||
                id.equals("RS") || id.equals("XCO") || id.equals("VT");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getOboHeader() {
        return oboHeader;
    }

    public void setOboHeader(String oboHeader) {
        this.oboHeader = oboHeader;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getRootTermAcc() {
        return rootTermAcc;
    }

    public void setRootTermAcc(String rootTermAcc) {
        this.rootTermAcc = rootTermAcc;
    }

    // temporary until ont_ids in phenominer tables will be fixed
    public static String zeroPadId(int id) {

        return zeroPadId(id + "");
    }

    /**
     * left pad idString with '0' characters until length of the string is 7
     * @param idString id part of term accession id
     * @return id left padded with zeros until id length is 7
     */
    public static String zeroPadId(String idString) {

        while (idString.length() < 7) {
            idString = "0" + idString;
        }
        return idString;
    }

    // temporary until ont_ids in phenominer tables will be fixed
    public static String buildId(String ontologyId, int id) {

        return ontologyId + ":" + zeroPadId(id);
    }

    public static String buildId(String ontologyId, String id) {

        return ontologyId + ":" + zeroPadId(id);
    }

    /*
     * normalize term acc id; accession id should be a few letters, followed by colon
     * and then by numeric id padded left with zeros up to 7 digits
     * like that: 'RS:0000001'
     * @param accId term accession id to be normalized
     * @return normalized term accession id
     */
    public static String formatId(String accId) {
        // handle null accId
        if( accId==null )
            return null;
        // handle accId without ontology prefix, or with missing ':'
        int colonPos = accId.indexOf(':');
        if( colonPos <=0 )
            return accId;
        // handle accId that is already normalized
        if( accId.length() - colonPos == 7+1 )
            return accId;

        // normalize acc id
        return buildId(accId.substring(0, colonPos), accId.substring(colonPos+1));
    }
}
