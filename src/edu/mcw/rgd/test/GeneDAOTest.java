package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.GeneDAO;
import edu.mcw.rgd.dao.impl.MapDAO;
import edu.mcw.rgd.dao.impl.ProjectDAO;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.datamodel.Project;
import edu.mcw.rgd.datamodel.Map;
import edu.mcw.rgd.datamodel.MapData;
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
