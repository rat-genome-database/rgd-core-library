package edu.mcw.rgd.reporting;

import edu.mcw.rgd.dao.impl.OntologyXDAO;
import edu.mcw.rgd.dao.impl.RGDManagementDAO;
import edu.mcw.rgd.datamodel.RgdId;
import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.datamodel.ontologyx.Term;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jul 7, 2011
 * Time: 10:01:23 AM
 */
public class Link {

    private static String GENE = "/rgdweb/report/gene/main.html?id=";
    private static String MARKER = "/rgdweb/report/marker/main.html?id=";
    private static String QTL = "/rgdweb/report/qtl/main.html?id=";
    private static String REF= "/rgdweb/report/reference/main.html?id=";
    private static String STRAIN = "/rgdweb/report/strain/main.html?id=";
    private static String VARIANT = "/rgdweb/report/variant/main.html?id=";
    private static String CELLLINE = "/rgdweb/report/cellline/main.html?id=";
    private static String VARIANTS = "/rgdweb/report/variants/main.html?id=";
    private static String ONTOLOGY_VIEW = "/rgdweb/ontology/view.html?acc_id=";
    private static String ONTOLOGY_ANNOT = "/rgdweb/ontology/annot.html?acc_id=";
    private static String PATHWAY_DIAGRAM = "/rgdweb/pathway/pathwayRecord.html?acc_id=";

    // genomic elements
    private static String GE = "/rgdweb/report/ge/main.html?id=";

    public static String it(String rgdId) throws Exception {
        return checkValue(rgdId, 0);
    }

    public static String it(String rgdId, int objectKey) throws Exception {
        return checkValue(rgdId, objectKey);
    }

    private static String checkValue(String value2Check, int objectKey) throws Exception {
        OntologyXDAO ontDao = new OntologyXDAO();
        Term ontObj = ontDao.getTermByAccId(value2Check);
         if(ontObj!=null){
             return ontAnnot(ontObj.getAccId());
         }else{
             if(value2Check.startsWith("RGD:")){
                 String[] rgdId = value2Check.split("RGD:");
                 return it(Integer.parseInt(rgdId[1]), objectKey);
             }else{
                return it(Integer.parseInt(value2Check), objectKey);
             }
         }
    }

    /**
     * generates report page relative url for rgd object given object rgd id;
     * the following object types are supported: genes, sslps, strains, qtls, references, promoters;
     * for object of any other type, rgd id is returned
     * @param rgdId object rgd id
     * @return report page relative url for rgd object, or rgd id if object type is not supported
     * @throws Exception
     */
    public static String it(int rgdId) throws Exception{
        RGDManagementDAO rdao = new RGDManagementDAO();
        RgdId id = rdao.getRgdId2(rgdId);
        if( id==null )
            return rgdId + "";

        return it(rgdId, id.getObjectKey());
    }

    /**
     * generates report page relative url for rgd object given object rgd id;
     * the following object types are supported: genes, sslps, strains, qtls, references, promoters;
     * for object of any other type, rgd id is returned
     * @param rgdId object rgd id
     * @return report page relative url for rgd object, or rgd id if object type is not supported
     * @throws Exception
     */
    public static String it(int rgdId, boolean isChinchilla) throws Exception{
        RGDManagementDAO rdao = new RGDManagementDAO();
        RgdId id = rdao.getRgdId2(rgdId);

        // chinchilla mode filter
        if( id!=null ) {
            // in chinchilla-mode, skip rat and mouse
            if( isChinchilla ) {
                if( id.getSpeciesTypeKey()==SpeciesType.RAT || id.getSpeciesTypeKey()==SpeciesType.MOUSE )
                    id = null;
            // in non-chinchilla mode, skip chinchilla!
            } else {
                if( id.getSpeciesTypeKey()==SpeciesType.CHINCHILLA )
                    id = null;
            }
        }

        if( id==null )
            return rgdId + "";

        return it(rgdId, id.getObjectKey());
    }

    /**
     * generates report page relative url for rgd object given object rgd id;
     * the following object types are supported: genes, sslps, strains, qtls, references, promoters, variants, cell lines;
     * for object of any other type, rgd id is returned
     * @param rgdId object rgd id
     * @param objectKey object key
     * @return report page relative url for rgd object, or rgd id if object type is not supported
     * @throws Exception
     */
    public static String it(int rgdId, int objectKey) throws Exception{
        switch(objectKey) {
            case RgdId.OBJECT_KEY_GENES: return gene(rgdId);
            case RgdId.OBJECT_KEY_SSLPS: return marker(rgdId);
            case RgdId.OBJECT_KEY_STRAINS: return strain(rgdId);
            case RgdId.OBJECT_KEY_QTLS: return qtl(rgdId);
            case RgdId.OBJECT_KEY_REFERENCES: return ref(rgdId);
            case RgdId.OBJECT_KEY_PROMOTERS: return ge(rgdId);
            case RgdId.OBJECT_KEY_VARIANTS: return variant(rgdId);
            case RgdId.OBJECT_KEY_VARIANT: return variants(rgdId);
            case RgdId.OBJECT_KEY_CELL_LINES: return cellLine(rgdId);
            case 0: return it(rgdId);
            default: return rgdId+"";
        }
    }

    public static String gene(int rgdId) {
        return GENE + rgdId;

    }
    public static String qtl(int rgdId) {
        return QTL + rgdId;

    }
    public static String strain(int rgdId) {
        return STRAIN + rgdId;

    }
    public static String marker(int rgdId) {
        return MARKER + rgdId;

    }
    public static String ref(int rgdId) {
        return REF + rgdId;
    }

    /**
     * return a link to genomic elements report page
     * @param rgdId rgd id
     * @return relative link to entry point of genomic elements report page
     */
    public static String ge(int rgdId) {
        return GE + rgdId;
    }

    /**
     * return a link to variants report page
     * @param rgdId rgd id
     * @return relative link to entry point of variants report page
     */
    public static String variants(int rgdId) {
        return VARIANTS + rgdId;
    }

    /**
     * return a link to variant report page
     * @param rgdId rgd id
     * @return relative link to entry point of variant report page
     */
    public static String variant(int rgdId) {
        return VARIANT + rgdId;
    }
    /**
     * return a link to cell line report page
     * @param rgdId rgd id
     * @return relative link to entry point of cell line report page
     */
    public static String cellLine(int rgdId) {
        return CELLLINE + rgdId;
    }

    /**
     * given ontology accession id, like RS:0000009, returns the link to ontology term viewer/browser page
     * @param ontAccId ontology accession id, like RS:0000009
     * @return url relative to current server, for page with ontology term browser
     */
    public static String ontView(String ontAccId) {
        return ONTOLOGY_VIEW + ontAccId;
    }

    /**
     * given ontology accession id, like RS:0000009, returns the link to ontology term annotations page
     * @param ontAccId ontology accession id, like RS:0000009
     * @return url relative to current server, for page with ontology term annotation
     */
    public static String ontAnnot(String ontAccId) {
        return ONTOLOGY_ANNOT + ontAccId;
    }

    /**
     * given pathway ontology accession id, like PW:0000394, returns the link to pathway diagram report page
     * @param pwAccId pathway ontology accession id, like PW:0000394
     * @return url relative to current server, for page with pathway diagram report page
     */
    public static String pathwayDiagram(String pwAccId) {
        return PATHWAY_DIAGRAM + pwAccId;
    }

    public static String gaTool(int speciesTypeKey, List ontologies, List xdbs, List orthologs, List geneSymbols, int mapKey) {
            return gaTool(speciesTypeKey, ontologies, xdbs, orthologs, geneSymbols, mapKey, "", "", "");
    }

    public static String gaTool(int speciesTypeKey, List ontologies, List xdbs, List orthologs,
                                List symbols, int mapKey , String chr, String start, String stop) {
        //"/rgdweb/ga/ui.html?species=3&rgdId=&ontology=W&xdb=23&xdb=17&ortholog=1&ortholog=2
        // &genes=a2m%0D%0Alepr&chr=1&start=&stop=&mapKey=60

        StringBuilder url = new StringBuilder(100);
        url.append("/rgdweb/ga/ui.html?species=").append(speciesTypeKey).append("&mapKey=").append(mapKey)
                .append("&chr=").append(chr).append("&start=").append(start).append("&stop=").append(stop);

        Iterator it = ontologies.iterator();
        while (it.hasNext()) {
            String ont = (String) it.next();
            url.append("&ontology=").append(ont.trim());
        }

        it = xdbs.iterator();
        while (it.hasNext()) {
            String xdb = (String) it.next();
            url.append("&xdb=").append(xdb.trim());
        }

        it = orthologs.iterator();
        while (it.hasNext()) {
            String ortho = (String) it.next();
            url.append("&ortholog=").append(ortho.trim());
        }

        url.append("&genes=");

        boolean first = true;
        it = symbols.iterator();
        while (it.hasNext()) {
            String symbol = (String) it.next();
            if (first) {
                first = false;
            }else {
                url.append(",");
            }
            url.append(symbol.trim());
        }
        System.out.println(url);

        return url.toString();
    }
}
