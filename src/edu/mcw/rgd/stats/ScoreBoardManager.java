package edu.mcw.rgd.stats;

import edu.mcw.rgd.dao.impl.StatisticsDAO;
import edu.mcw.rgd.datamodel.RgdId;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: May 4, 2009
 * Time: 11:08:13 AM
 */
public class ScoreBoardManager {

    public StatisticsDAO dao = new StatisticsDAO();

    private boolean isBefore(Date from, Date to) {
        if( from==null ) {
            return false;
        }

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(from);

        GregorianCalendar today = new GregorianCalendar();
        today.setTime(to);

        if (gc.get(GregorianCalendar.YEAR) < today.get(GregorianCalendar.YEAR)) {
            return true;
        }

        if (gc.get(GregorianCalendar.YEAR) == today.get(GregorianCalendar.YEAR) && gc.get(GregorianCalendar.DAY_OF_YEAR) < today.get(GregorianCalendar.DAY_OF_YEAR)) {
            return true;
        }

        return false;
    }

    public Map<String,String> mapSubtract(Map<String,String> minuend, Map<String,String> subtrahend) {

        if( minuend==null ) {
            return subtrahend;
        }

        Map<String,String> difference = new LinkedHashMap<>();

        for (String key : minuend.keySet()) {
            try {
                difference.put(key, Integer.toString(Integer.parseInt(minuend.get(key)) - Integer.parseInt(subtrahend.get(key))));
            } catch (Exception e) {
                //System.out.println("Error for key " + key + "minu= " + minuend.get(key) + " sub = " + subtrahend.get(key));
            }
        }
        return difference;
    }

    public Map<String,String> getRGDObjectCount(int speciesType) throws Exception{
        return dao.getRGDObjectCount(speciesType);
    }

    public Map<String,String> getRGDObjectCount(int speciesType, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            return dao.getStatMap("RGD Object", speciesType, date);
        } else {
            return this.getRGDObjectCount(speciesType);
        }
    }

    public Map<String,String> diffRGDObjectCount(int speciesType, Date from, Date to) throws Exception {
        return this.mapSubtract(this.getRGDObjectCount(speciesType, to), this.getRGDObjectCount(speciesType, from));
    }


    /// PROTEIN INTERACTION COUNTS
    public Map<String,String> getProteinInteractionCount(int speciesType) throws Exception{
        return dao.getProteinInteractionCount(speciesType);
    }

    public Map<String,String> getProteinInteractionCount(int speciesType, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            return dao.getStatMap("Protein Interaction", speciesType, date);
        } else {
            return this.getProteinInteractionCount(speciesType);
        }
    }

    public Map<String,String> diffProteinInteractionCount(int speciesType, Date from, Date to) throws Exception {
        return this.mapSubtract(this.getProteinInteractionCount(speciesType, to), this.getProteinInteractionCount(speciesType, from));
    }



    public Map<String,String> getWithdrawnCount(int speciesType) throws Exception{
        return dao.getWithdrawnCount(speciesType);
    }

    public Map<String,String> getWithdrawnCount(int speciesType, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            return dao.getStatMap("Withdrawn Object", speciesType, date);
        } else {
            return this.getWithdrawnCount(speciesType);
        }
    }

    public Map<String,String> diffWithdrawnCount(int speciesType, Date from, Date to) throws Exception{
        return this.mapSubtract(this.getWithdrawnCount(speciesType, to), this.getWithdrawnCount(speciesType, from));
    }


    public Map<String,String> getRetiredCount(int speciesType) throws Exception{
        return dao.getRetiredCount(speciesType);
    }

    public Map<String,String> getRetiredCount(int speciesType, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            return dao.getStatMap("Retired Object", speciesType, date);
        } else {
            return this.getRetiredCount(speciesType);
        }
    }

    public Map<String,String> diffRetiredCount(int speciesType, Date from, Date to) throws Exception{
        return this.mapSubtract(this.getRetiredCount(speciesType, to), this.getRetiredCount(speciesType, from));
    }



    public Map<String,String> getActiveCount(int speciesType) throws Exception{
        return dao.getActiveCount(speciesType);
    }

    public Map<String,String> getActiveCount(int speciesType, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            return dao.getStatMap("Active Object", speciesType, date);
        } else {
            return this.getActiveCount(speciesType);
        }
    }

    public Map<String,String> diffActiveCount(int speciesType, Date from, Date to) throws Exception{
        return this.mapSubtract(this.getActiveCount(speciesType, to), this.getActiveCount(speciesType, from));
    }


    public Map<String,String> getGeneTypeCount(int speciesType) throws Exception{
        return dao.getGeneTypeCount(speciesType);
    }

    public Map<String,String> getGeneTypeCount(int speciesType, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            return dao.getStatMap("Gene Type", speciesType, date);
        } else {
            return this.getGeneTypeCount(speciesType);
        }
    }

    public Map<String,String> diffGeneTypeCount(int speciesType, Date from, Date to) throws Exception {
        return this.mapSubtract(this.getGeneTypeCount(speciesType, to), this.getGeneTypeCount(speciesType, from));
    }


    public Map<String,String> getStrainTypeCount(int speciesType) throws Exception{
        return dao.getStrainTypeCount(speciesType);
    }

    public Map<String,String> getStrainTypeCount(int speciesType, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            return dao.getStatMap("Strain Type", speciesType, date);
        } else {
            return this.getStrainTypeCount(speciesType);
        }
    }

    public Map<String,String> diffStrainTypeCount(int speciesType, Date from, Date to) throws Exception{
         return this.mapSubtract(this.getStrainTypeCount(speciesType, to), this.getStrainTypeCount(speciesType, from));
    }


    public Map<String,String> getQTLInheritanceTypeCount(int speciesType) throws Exception{
        return dao.getQTLInheritanceTypeCount(speciesType);
    }

    public Map<String,String> getQTLInheritanceTypeCount(int speciesType, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            return dao.getStatMap("QTL Inheritance Type", speciesType, date);
        } else {
            return this.getQTLInheritanceTypeCount(speciesType);
        }
    }

    public Map<String,String> diffQTLInheritanceTypeCount(int speciesType, Date from, Date to) throws Exception {
         return this.mapSubtract(this.getQTLInheritanceTypeCount(speciesType, to), this.getQTLInheritanceTypeCount(speciesType, from));
    }

    public Map<String,String> getObjectReferenceCount(int speciesType) throws Exception{
        return dao.getObjectReferenceCount(speciesType);
    }

    public Map<String,String> getObjectReferenceCount(int speciesType, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            return dao.getStatMap("Objects With Reference", speciesType, date);
        } else {
            return this.getObjectReferenceCount(speciesType);
        }
    }

    public Map<String,String> diffObjectReferenceCount(int speciesType, Date from, Date to) throws Exception{
         return this.mapSubtract(this.getObjectReferenceCount(speciesType, to), this.getObjectReferenceCount(speciesType, from));
    }

    public Map<String,String> getObjectWithReferenceSequenceCount(int speciesType) throws Exception{
        return dao.getObjectWithReferenceSequenceCount(speciesType);
    }

    public Map<String,String> getObjectWithReferenceSequenceCount(int speciesType, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            return dao.getStatMap("Objects With Reference Sequence", speciesType, date);
        } else {
            return this.getObjectWithReferenceSequenceCount(speciesType);
        }
    }

    public Map<String,String> diffObjectWithReferenceSequenceCount(int speciesType, Date from, Date to) throws Exception {
         return this.mapSubtract(this.getObjectWithReferenceSequenceCount(speciesType, to), this.getObjectWithReferenceSequenceCount(speciesType, from));
    }


    public Map<String,String> getObjectsWithXDBsCount(int speciesType, int objectKey) throws Exception{
        return dao.getObjectsWithXDBsCount(speciesType, objectKey);
    }

    public Map<String,String> getObjectsWithXDBsCount(int speciesType, int objectKey, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            String objName = objectKey>0 ? RgdId.getObjectTypeName(objectKey) : "Objects";
            String statType = (objectKey==0?"Objects":objName+"s")+" With XDB";
            return dao.getStatMap(statType, speciesType, date);
        } else {
            return this.getObjectsWithXDBsCount(speciesType, objectKey);
        }
    }

    public Map<String,String> diffObjectsWithXDBsCount(int speciesType, int objectKey, Date from, Date to) throws Exception {
         return this.mapSubtract(this.getObjectsWithXDBsCount(speciesType, objectKey, to),
                 this.getObjectsWithXDBsCount(speciesType, objectKey, from));
    }


    public Map<String,String> getAnnotatedReferencesCount(int speciesType) throws Exception{
        return dao.getAnnotatedReferencesCount(speciesType);
    }

    public Map<String,String> getAnnotatedReferencesCount(int speciesType, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            return dao.getStatMap("References with Annotations", speciesType, date);
        } else {
            return this.getAnnotatedReferencesCount(speciesType);
        }
    }

    public Map<String,String> diffAnnotatedReferencesCount(int speciesType, Date from, Date to) throws Exception {
        return this.mapSubtract(this.getAnnotatedReferencesCount(speciesType, to),
                this.getAnnotatedReferencesCount(speciesType, from));
    }


    public Map<String,String> getXDBsCount(int speciesType) throws Exception{
        return dao.getXDBsCount(speciesType);
    }

    public Map<String,String> getXDBsCount(int speciesType, Date date) throws Exception {
        if (this.isBefore(date, new Date())) {
            return dao.getStatMap("XDB Count", speciesType, date);
        } else {
            return this.getXDBsCount(speciesType);
        }
    }

    public Map<String,String> diffXDBsCount(int speciesType, Date from, Date to) throws Exception {
         return this.mapSubtract(this.getXDBsCount(speciesType, to), this.getXDBsCount(speciesType, from));
    }


    /**
     * get number of active terms within an ontology
     * @param date if not null, count of terms on given date
     * @return map
     * @throws Exception
     */
    public Map<String,String> getOntologyTermCount(Date date) throws Exception {
        if( this.isBefore(date, new Date()) ) {
            return dao.getStatMap("Ontology Terms", 0, date);
        } else {
            return dao.getOntologyTermCount();
        }
    }

    /**
     * get number of active terms within an ontology that have annotations for given species
     * @param date if not null, count of terms on given date
     * @return map
     * @throws Exception
     */
    public Map<String,String> getAnnotatedOntologyTermCount(int speciesType, Date date) throws Exception {
        if( this.isBefore(date, new Date()) ) {
            return dao.getStatMap("Ontology Annotated Terms", speciesType, date);
        } else {
            return dao.getOntologyAnnotatedTermCount(speciesType);
        }
    }

    /**
     * get count of annotations for given species, per ontology
     * @param speciesType species type key
     * @param objectKey object key
     * @param date if not null, count of terms on given date
     * @return map
     * @throws Exception
     */
    public Map<String,String> getOntologyAnnotationCount(int speciesType, int objectKey, Date date) throws Exception {

        if( this.isBefore(date, new Date()) ) {
            String objName = objectKey>0 ? (RgdId.getObjectTypeName(objectKey)+" ") : "";
            return dao.getStatMap("Ontology "+objName+"Annotations", speciesType, date);
        } else {
            return dao.getOntologyAnnotationCount(speciesType, objectKey);
        }
    }

    /**
     * get count of manual annotations for given species, per ontology
     * @param speciesType species type key
     * @param objectKey object key
     * @param date if not null, count of terms on given date
     * @return map
     * @throws Exception
     */
    public Map<String,String> getOntologyManualAnnotationCount(int speciesType, int objectKey, Date date) throws Exception {

        if( this.isBefore(date, new Date()) ) {
            String objName = objectKey>0 ? RgdId.getObjectTypeName(objectKey) : "";
            return dao.getStatMap("Ontology "+objName+" Manual Annotations", speciesType, date);
        } else {
            return dao.getOntologyManualAnnotationCount(speciesType, objectKey);
        }
    }

    /**
     * get count of annotated objects for given species, per ontology
     * @param speciesType species type key
     * @param objectKey object key
     * @param date if not null, count of terms on given date
     * @return map
     * @throws Exception
     */
    public Map<String,String> getOntologyAnnotatedObjectCount(int speciesType, int objectKey, Date date) throws Exception {

        if( this.isBefore(date, new Date()) ) {
            String objName = objectKey>0 ? RgdId.getObjectTypeName(objectKey) : "Object";
            return dao.getStatMap("Ontology "+objName+"s Annotated", speciesType, date);
        } else {
            return dao.getOntologyAnnotatedObjectCount(speciesType, objectKey);
        }
    }

    /**
     * get count of manually annotated objects for given species, per ontology
     * @param speciesType species type key
     * @param objectKey object key
     * @param date if not null, count of terms on given date
     * @return map
     * @throws Exception
     */
    public Map<String,String> getOntologyManualAnnotatedObjectCount(int speciesType, int objectKey, Date date) throws Exception {

        if( this.isBefore(date, new Date()) ) {
            String objName = objectKey>0 ? RgdId.getObjectTypeName(objectKey) : "Object";
            return dao.getStatMap("Ontology "+objName+"s Manually Annotated", speciesType, date);
        } else {
            return dao.getOntologyManuallyAnnotatedObjectCount(speciesType, objectKey);
        }
    }

    /**
     * get count of annotated objects for given species, per portal
     * @param speciesType species type key
     * @param objectKey object key
     * @param date if not null, count of terms on given date
     * @return map
     * @throws Exception
     */
    public Map<String,String> getPortalAnnotatedObjectCount(int speciesType, int objectKey, Date date) throws Exception {

        if( this.isBefore(date, new Date()) ) {

            String name = StatisticsDAO.getPortalStatName(speciesType, objectKey);
            return dao.getStatMap(name, speciesType, date);
        } else {
            return dao.getPortalAnnotatedObjectCount(speciesType, objectKey);
        }
    }

    /**
     * get list of stat archive dates, string in 'YYYY-MM-DD' format, in descending sort order
     * @return list of strings, stat archive dates
     * @throws Exception
     */
    public List<String> getStatArchiveDates() throws Exception{
        return dao.getStatArchiveDates();
    }
}
