package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.AnnotationDAO;
import edu.mcw.rgd.datamodel.EvidenceCode;
import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.datamodel.ontology.Annotation;
import junit.framework.TestCase;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: Jun 1, 2011
 * Time: 10:30:19 AM
 */
public class AnnotationDaoTest extends TestCase {

    AnnotationDAO annDao = new AnnotationDAO();

    public AnnotationDaoTest(String testName) {
        super(testName);
    }

    public void testAll() throws Exception{

        List<Annotation> annots = annDao.getAnnotationsGroupedByGene("RDO:0013332",false,3,1000,1);
        testGetObjectAnnotationbyTermAndSpecies();

        testEvidenceCodes();

        testStaleAnnotations();
        testGetAnnotations();
        testInsertUpdateDelete();
        testGetAnnotationsByRef();
        testGetAnnotationsBySpecies();

    }

    public void testEvidenceCodes() throws Exception {

        String evidence = "ISO";
        System.out.println(evidence+": "+EvidenceCode.getName(evidence));
        System.out.println("  "+evidence+": is manual = "+EvidenceCode.isManual(evidence));
        System.out.println("  "+evidence+": is manual in same species = "+EvidenceCode.isManualInSameSpecies(evidence));
        System.out.println("  "+evidence+": is manual in other species = "+EvidenceCode.isManualInOtherSpecies(evidence));

        evidence = "IEP";
        System.out.println(evidence+": "+EvidenceCode.getName(evidence));
        System.out.println("  "+evidence+": is manual = "+EvidenceCode.isManual(evidence));
        System.out.println("  "+evidence+": is manual in same species = "+EvidenceCode.isManualInSameSpecies(evidence));
        System.out.println("  "+evidence+": is manual in other species = "+EvidenceCode.isManualInOtherSpecies(evidence));

        evidence = "IBD";
        System.out.println(evidence+": "+EvidenceCode.getName(evidence));
        System.out.println("  "+evidence+": is manual = "+EvidenceCode.isManual(evidence));
        System.out.println("  "+evidence+": is manual in same species = "+EvidenceCode.isManualInSameSpecies(evidence));
        System.out.println("  "+evidence+": is manual in other species = "+EvidenceCode.isManualInOtherSpecies(evidence));

        evidence = "TEST";
        System.out.println(evidence+": "+EvidenceCode.getName(evidence));
        System.out.println("  "+evidence+": is manual = "+EvidenceCode.isManual(evidence));
        System.out.println("  "+evidence+": is manual in same species = "+EvidenceCode.isManualInSameSpecies(evidence));
        System.out.println("  "+evidence+": is manual in other species = "+EvidenceCode.isManualInOtherSpecies(evidence));
    }

    public void testStaleAnnotations() throws Exception {

        List<Annotation> annots = annDao.getAnnotationsWithStaleTermNames();
        System.out.println("Count of annotations with stale term names: "+annots.size());
    }

    public void testInsertUpdateDelete() throws Exception {

        // create new annotation -- set only non-null fields
        Annotation a = new Annotation();
        a.setTerm("TEST TEST TEST");
        a.setTermAcc("CHEBI:41500");
        a.setAnnotatedObjectRgdId(1024);
        a.setRgdObjectKey(1);
        a.setDataSrc("TEST");
        a.setEvidence("ISO");
        a.setAspect("E");
        a.setRefRgdId(6480464);

        Annotation b = annDao.getAnnotation(a);

        if( b==null ) {
            annDao.insertAnnotation(a);
            System.out.println("annotation inserted");
        }

        a.setNotes("TEST TEST");
        annDao.updateAnnotation(a);
        System.out.println("annotation updated");

        b = annDao.getAnnotation(a);
        assertTrue(b.getKey().equals(a.getKey()));

        annDao.deleteAnnotation(b.getKey());
        System.out.println("annotation deleted");
    }

    public void testGetAnnotationsByRef() throws Exception {
        int refRgdId = 2290271; // RGD_PIPELINES: GOA_HUMAN
        System.out.println(new Date());
        int count = annDao.getCountOfAnnotationsByReference(refRgdId);
        System.out.println("Count of annotations from human goa pipeline: "+count);
        System.out.println(new Date());
        count = annDao.getAnnotationsByReference(refRgdId).size();
        System.out.println("Annotations from human goa pipeline loaded: "+count);
        System.out.println(new Date());
    }

    public void testGetAnnotations() throws Exception {

        String accId = "CHEBI:36342"; // has over 400,000 annotations
        int maxRows = 1000;
        int speciesTypeKey = SpeciesType.RAT;
        boolean withChildren = true;

        List<Annotation> annots = annDao.getAnnotations(accId, withChildren, speciesTypeKey, maxRows);
        System.out.println("CHEBI:36342 for rat, with children="+annots.size());

        withChildren = false;
        annots = annDao.getAnnotations(accId, withChildren, speciesTypeKey, maxRows);
        System.out.println("CHEBI:36342 for rat, without children="+annots.size());

        speciesTypeKey = SpeciesType.ALL;
        annots = annDao.getAnnotations(accId, withChildren, speciesTypeKey, maxRows);
        System.out.println("CHEBI:36342 for all, without children="+annots.size());

        withChildren = true;
        annots = annDao.getAnnotations(accId, withChildren, speciesTypeKey, maxRows);
        System.out.println("CHEBI:36342 for all, with children="+annots.size());
    }

    public void testGetAnnotationsBySpecies() throws Exception {
        int speciesTypeKey = 3;
        List<Annotation> annots = annDao.getAnnotationsBySpecies(speciesTypeKey);
        System.out.println("count of rat annotations: "+annots.size());
    }

    public void testGetObjectAnnotationbyTermAndSpecies() throws Exception {
        List<Annotation> diseaseAnn;
        int rgdid = 1308314;
        String accId = "RDO:9000013";
        int speciesTypeKey = 3;
        diseaseAnn = annDao.getObjectAnnotationbyTermAndSpecies(rgdid, accId, speciesTypeKey);
        System.out.println(diseaseAnn.size());
    }

}
