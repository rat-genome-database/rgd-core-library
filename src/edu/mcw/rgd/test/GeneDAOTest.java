package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.AnnotationDAO;
import edu.mcw.rgd.dao.impl.GeneDAO;

import edu.mcw.rgd.dao.impl.PhenominerDAO;
import edu.mcw.rgd.dao.impl.ProjectDAO;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.Project;

import edu.mcw.rgd.datamodel.ontology.Annotation;
import edu.mcw.rgd.datamodel.pheno.Record;
import junit.framework.TestCase;


import java.util.ArrayList;
import java.util.List;

/**
 * @author mtutaj
 * @since Sep 13, 2010
 */
public class GeneDAOTest extends TestCase {

    GeneDAO dao = new GeneDAO();

    public GeneDAOTest(String testName) {
        super(testName);
    }

    public void testAll() throws Exception {
        ProjectDAO pro = new ProjectDAO();
        List<Project> li=pro.getAllProjects();
        List<Project> t = pro.getProjectByRgdId(476081963);
        List<Integer> id = pro.getReferenceRgdIdsForProject(476081962);
        List<Record> rec = new PhenominerDAO().getFullRecordsForProject(69701);
        List<Record> rec1 = new PhenominerDAO().getFullRecordsForProject(476081962,"RS");
        List<Annotation> a = new AnnotationDAO().getAnnotationsByReferenceForProject(476081962);
        List<Annotation> a1 = new AnnotationDAO().getAnnotationsForProject(476081962);
        int c = new AnnotationDAO().getPhenoAnnotationsCountByReferenceForProject(476081962);
        int rgdId = 13838876;
        List<Gene> genes = dao.getGenesForProteinDomain(rgdId);
        assert genes.size() > 0;

        List<Integer> speciesTypeKeys = new ArrayList<>();
        speciesTypeKeys.add(1);
        speciesTypeKeys.add(2);
        speciesTypeKeys.add(3);
        List<String> evidenceCodes = new ArrayList<>();
        evidenceCodes.add("IAGP");
        evidenceCodes.add("IDA");
        genes = dao.getAnnotatedGenesAndOrthologs("DOID:14221", speciesTypeKeys, evidenceCodes);
        assert genes.size() > 0;
    }

 }
