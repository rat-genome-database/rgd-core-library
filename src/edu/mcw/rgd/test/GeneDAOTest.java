package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.*;

import edu.mcw.rgd.datamodel.*;

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

        RGDManagementDAO rdao = new RGDManagementDAO();
        RgdId id = rdao.createRgdId(RgdId.OBJECT_KEY_GENES, "ACTIVE", 3);

        Gene s = new Gene();
        s.setName("---");
        s.setSymbol("---");
        s.setRgdId(id.getRgdId());
        dao.insertGene(s);

        s.setTaglessAlleleSymbol("---");
        dao.updateGene(s);


        ProjectDAO pro = new ProjectDAO();
        List<Project> li=pro.getAllProjects();
        List<Project> t = pro.getProjectByRgdId(476081963);
        List<Integer> id2 = pro.getReferenceRgdIdsForProject(476081962);
        List<Record> rec = new PhenominerDAO().getFullRecordsForProject(69701);
        List<Record> rec1 = new PhenominerDAO().getFullRecordsForProject(476081962,"RS");
        List<Annotation> a = new AnnotationDAO().getAnnotationsByReferenceForProject(476081962);
        List<Annotation> a1 = new AnnotationDAO().getAnnotationsForProject(476081962);
        List<ProjectFile> pf1= new ProjectFileDAO().getProjectFiles(476081962);
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
