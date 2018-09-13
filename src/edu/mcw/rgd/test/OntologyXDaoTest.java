package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.OntologyXDAO;
import edu.mcw.rgd.datamodel.SpeciesType;
import edu.mcw.rgd.datamodel.ontologyx.*;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: May 4, 2011
 * Time: 1:43:45 PM
 * junit test for ontology dao
 */
public class OntologyXDaoTest extends TestCase {

    OntologyXDAO dao = new OntologyXDAO();

    public OntologyXDaoTest(String testName) {
        super(testName);
    }

    public void testAll() throws Exception {
        // test for term having both ancestor and descendant terms
        String accId = "RDO:0005134";
        TermWithStats t = dao.getTermWithStats(accId, null, accId);
        assertNotNull(t);

        testIsDescendantOf();
        testGetOntology();
        testGetRelationships();
        testTermSynonyms();
        testTermCounts();
        testGetActiveSynonymsByType();
        testInsertDag();
        testGetTermsBySynonym();
        testGetPathsToRoot();
        testGetRgdIdForStrainOntId();
        testGetActiveChildTerms();
        testGetRootTerm();
        testGetTermByTermName();
    }

    public void testTermSynonyms() throws Exception {

        String termAcc = "RDO:0005869"; // hypotension

        List<TermSynonym> syns = dao.getTermSynonyms(termAcc);
        Assert.assertTrue(syns.size()>0);
        syns = dao.getTermSynonyms(termAcc, "OBO");
        Assert.assertTrue(syns.size()>0);
        syns = dao.getTermSynonymsModifiedBefore("RDO", "XXX", new Date());
        Assert.assertTrue(syns.isEmpty());

        termAcc = "RDO:0007933";

        TermSynonym syn1 = new TermSynonym();
        syn1.setTermAcc(termAcc);
        syn1.setType("primary_id");
        syn1.setName("OMIM:000000");

        Assert.assertTrue(dao.insertTermSynonym(syn1)!=0);

        TermSynonym syn2 = new TermSynonym();
        syn2.setTermAcc(termAcc);
        syn2.setType("primary_id");
        syn2.setName("MESH:C00000");

        Assert.assertEquals(1, dao.updateTermSynonym(syn1, syn2));

        Assert.assertEquals(1, dao.dropTermSynonym(syn2));
    }

    public void testTermCounts() throws Exception {

        // test for term having both ancestor and descendant terms
        String accId = "MP:0001194";
        TermWithStats t = dao.getTermWithStats(accId, null);
        assertNotNull(t);

        // test for term having both ancestor and descendant terms
        accId = "MP:0004947";
        System.out.println(accId+": ancestors="+dao.getCountOfAncestors(accId)+", descendants="+dao.getCountOfDescendants(accId));

        // test for root term, having no ancestors
        accId = "MP:0000001";
        System.out.println(accId+": ancestors="+dao.getCountOfAncestors(accId)+", descendants="+dao.getCountOfDescendants(accId));

        // test for leaf term, having no descendants
        accId = "MP:0012572";
        System.out.println(accId+": ancestors="+dao.getCountOfAncestors(accId)+", descendants="+dao.getCountOfDescendants(accId));
    }

    public void testGetActiveSynonymsByType() throws Exception {

        String ontId = "CHEBI";
        String synonymType = "xref_casrn";
        List<TermSynonym> synonyms = dao.getActiveSynonymsByType(ontId, synonymType);
        System.out.println("There are "+synonyms.size()+" synonyms of type "+synonymType+" within ontology "+ontId);
    }

    public void testIsDescendantOf() throws Exception {

        // ensure you cannot insert a dag edge where both parent and child acc ids are the same
        String accId = "RS:0001481"; // SS/JrHsd
        String ancestorAccId = "RS:0000765"; // inbred strain
        boolean isDescendant = dao.isDescendantOf(accId, ancestorAccId);
        assertTrue(isDescendant);
        isDescendant = dao.isDescendantOf(ancestorAccId, accId);
        assertFalse(isDescendant);
    }

    public void testInsertDag() throws Exception {

        // ensure you cannot insert a dag edge where both parent and child acc ids are the same
        String accId = "RDO:0005452";
        String relId = "IA"; // is-a
        try {
            dao.upsertDag(accId, accId, relId);
        }
        catch( OntologyXDAO.OntologyXDAOException e ) {
            System.out.println(e.getMessage());
        }
    }

    public void testGetTermByTermName() throws Exception{

        String term = "androgen signaling pathway";
        String ont_id = "PW";
        Term pwTerm = dao.getTermByTermName(term, ont_id);
        System.out.println("here is the acc_id" + pwTerm.getAccId());
    }

    public void testGetRelationships() throws Exception {

        String accId = "PW:0000564";
        List<TermDagEdge> edges = dao.getAllParentEdges(accId);
        for( TermDagEdge edge: edges ) {
            System.out.println(edge.getParentTermAcc()+" <-- "+edge.getChildTermAcc());
        }

        edges = dao.getAllChildEdges(accId);
        for( TermDagEdge edge: edges ) {
            System.out.println(edge.getParentTermAcc()+" <-- "+edge.getChildTermAcc());
        }

        List<TermDagEdgeCustom> customEdges = dao.getCustomTermRelationships("RDO");
        for( TermDagEdgeCustom edge: customEdges ) {
            System.out.println(edge.getParentTermAcc()+" <-- "+edge.getChildTermAcc());
        }
    }

    public void testGetTermsBySynonym() throws Exception {

        String ontId = "BP"; // GO biological process
        String synonymToSearch = "ribosomal";
        String matchType = "partial";
        List<Term> terms = dao.getTermsBySynonym(ontId, synonymToSearch, matchType);
        Assert.assertFalse(terms.isEmpty());

        ontId = "BP"; // GO biological process
        synonymToSearch = "citrulline metabolism";
        matchType = "exact";
        terms = dao.getTermsBySynonym(ontId, synonymToSearch, matchType);
        Assert.assertFalse(terms.isEmpty());
    }

    public void testGetOntology() throws Exception {

        // test valid ontology id
        Ontology o = dao.getOntology("MP");
        Assert.assertNotNull(o);
        // test invalid ontology id
        o = dao.getOntology("INVALID");
        Assert.assertNull(o);

        o = dao.getOntologyFromAccId("RDO:0000175");
        Assert.assertNotNull(o);

        o = dao.getOntologyFromAspect("D");
        Assert.assertNotNull(o);

        List<Ontology> os = dao.getOntologies();
        System.out.println("all ontologies: "+os.size());
        os = dao.getPublicOntologies();
        System.out.println("public ontologies: "+os.size());
    }

    public void testGetPathsToRoot() throws Exception {

        String accId = "GO:0043583";
        int speciesTypeKey = SpeciesType.ALL;
        System.out.println("PATH_OPTION_ONE_SHORTEST\n-----------------");
        List<List<TermWithStats>> paths = dao.getPathsToRoot(accId, OntologyXDAO.PATH_OPTION_ONE_SHORTEST);
        dumpPaths(paths);

        System.out.println("PATH_OPTION_ONE_LONGEST\n-----------------");
        paths = dao.getPathsToRoot(accId, OntologyXDAO.PATH_OPTION_ONE_LONGEST);
        dumpPaths(paths);

        System.out.println("PATH_OPTION_ALL_SHORTEST\n-----------------");
        paths = dao.getPathsToRoot(accId, OntologyXDAO.PATH_OPTION_ALL_SHORTEST);
        dumpPaths(paths);

        System.out.println("PATH_OPTION_ALL_LONGEST\n-----------------");
        paths = dao.getPathsToRoot(accId, OntologyXDAO.PATH_OPTION_ALL_LONGEST);
        dumpPaths(paths);

        System.out.println("PATH_OPTION_ALL\n-----------------");
        paths = dao.getPathsToRoot(accId, OntologyXDAO.PATH_OPTION_ALL);
        dumpPaths(paths);

        System.out.println("PATH_OPTION_DEFAULT\n-----------------");
        paths = dao.getPathsToRoot(accId, OntologyXDAO.PATH_OPTION_DEFAULT);
        dumpPaths(paths);
        System.out.println("DONE!\n-----------------");
    }

    private void dumpPaths(List<List<TermWithStats>> paths ) {
        for( List<TermWithStats> path: paths ) {
            for( TermWithStats term: path ) {
                System.out.print(term.getAccId()+"("+term.getRatGeneCountForTermAndChildren()+")");
                if( term.getRel()!=null )
                    System.out.print(" "+term.getRel()+" ");
            }
            System.out.println();
        }
    }

    public void testGetRgdIdForStrainOntId() throws Exception {

        // test root term of strain ontology
        String accId = "RS:0000457";
        int rgdId = dao.getRgdIdForStrainOntId(accId);
        Assert.assertEquals(0, rgdId);

        // test a term known to have s strain rgd id
        accId = "RS:0001275";
        rgdId = dao.getRgdIdForStrainOntId(accId);
        Assert.assertEquals(2290147, rgdId);

        // test a term from different ontology
        accId = "GO:0001976";
        rgdId = dao.getRgdIdForStrainOntId(accId);
        Assert.assertEquals(0, rgdId);
    }

    public void testGetRootTerm() throws Exception {

        // test known ontology : non-null term expected
        String ontId = "RS";
        String rootAccId = dao.getRootTerm(ontId);
        Assert.assertEquals("RS:0000457", rootAccId);

        // test unknown ontology : 'null' expected
        ontId = "RR";
        rootAccId = dao.getRootTerm(ontId);
        Assert.assertNotNull(rootAccId, "there is a root term for ontology RR!!!");
    }

    public void testGetActiveChildTerms() throws Exception {

        // test of a term with known active child terms
        int speciesTypeKey = 0;
        List<TermWithStats> children = dao.getActiveChildTerms("GO:0001976", speciesTypeKey);
        Assert.assertFalse(children.isEmpty());

        // test unknown term acc id
        children = dao.getActiveChildTerms("GO:000197600", speciesTypeKey);
        Assert.assertEquals(children.size(), 0);

        // test of known term with no known children
        children = dao.getActiveChildTerms("GO:0003063", speciesTypeKey);
        Assert.assertEquals(children.size(), 0);
    }
}
