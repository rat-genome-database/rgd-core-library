package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.RgdVariantQuery;
import edu.mcw.rgd.dao.spring.StringListQuery;
import edu.mcw.rgd.dao.spring.StringMapQuery;
import edu.mcw.rgd.datamodel.RgdVariant;

import java.util.*;

import edu.mcw.rgd.datamodel.RgdId;

/**
 * Created by IntelliJ IDEA.
 * User: hsnalabolu
 * Date: Dec 17, 2018
 * Time: 10:14:52 AM
 * <p>
 * API to manipulate data in Variants table
 */
public class GeneEnrichmentDAO extends AbstractDAO {

    /**
     * Returns a count of all active genes in rgd
     *
     * @param speciesKey species type key
     * @return count of active genes
     * @throws Exception when unexpected error in spring framework occurs
     */
    public int getReferenceGeneCount(int speciesKey) throws Exception {

        String query = "SELECT COUNT(*) FROM genes g, rgd_ids r " +
                "WHERE r.object_status='ACTIVE' AND r.species_type_key=? AND NVL(gene_type_lc,'*') NOT IN('splice','allele') " +
                " AND r.rgd_id=g.rgd_id ";

        return getCount(query, speciesKey);
    }

}