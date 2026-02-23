package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.HgncFamilyQuery;
import edu.mcw.rgd.dao.spring.IntListQuery;
import edu.mcw.rgd.datamodel.HgncFamily;

import java.util.List;

/**
 * Data access for HGNC gene families.
 */
public class HgncDAO extends AbstractDAO {

    public HgncFamily getFamilyById(int familyId) throws Exception {
        String query = "SELECT * FROM hgnc_families WHERE family_id=?";
        List<HgncFamily> families = HgncFamilyQuery.execute(this, query, familyId);
        if (families.isEmpty()) {
            return null;
        }
        return families.get(0);
    }

    public List<HgncFamily> getAllFamilies() throws Exception {
        String query = "SELECT * FROM hgnc_families";
        return HgncFamilyQuery.execute(this, query);
    }

    public int insertFamily(HgncFamily f) throws Exception {
        String sql = """
            INSERT INTO hgnc_families (family_id, abbreviation, name, external_note, pubmed_ids,
              desc_comment, desc_label, desc_source, desc_go, typical_gene)
            VALUES (?,?,?,?,?,?,?,?,?,?)
            """;
        return update(sql, f.getFamilyId(), f.getAbbreviation(), f.getName(), f.getExternalNote(), f.getPubmedIds(),
                f.getDescComment(), f.getDescLabel(), f.getDescSource(), f.getDescGo(), f.getTypicalGene());
    }

    public int updateFamily(HgncFamily f) throws Exception {
        String sql = """
            UPDATE hgnc_families SET abbreviation=?, name=?, external_note=?, pubmed_ids=?,
              desc_comment=?, desc_label=?, desc_source=?, desc_go=?, typical_gene=?
            WHERE family_id=?
            """;
        return update(sql, f.getAbbreviation(), f.getName(), f.getExternalNote(), f.getPubmedIds(),
                f.getDescComment(), f.getDescLabel(), f.getDescSource(), f.getDescGo(), f.getTypicalGene(), f.getFamilyId());
    }

    public int deleteFamily(int familyId) throws Exception {
        String sql = "DELETE FROM hgnc_families WHERE family_id=?";
        return update(sql, familyId);
    }

    public List<Integer> getGeneRgdIdsForFamily(int familyId) throws Exception {
        String sql = "SELECT rgd_id FROM hgnc_family_to_genes WHERE family_id=? AND rgd_id IS NOT NULL ORDER BY rgd_id";
        return IntListQuery.execute(this, sql, familyId);
    }

    public List<HgncFamily> getGeneFamiliesByRgdId(int rgdId) throws Exception {
        String sql = """
            SELECT f.* FROM hgnc_families f
            JOIN hgnc_family_to_genes ftg ON f.family_id = ftg.family_id
            WHERE ftg.rgd_id = ?
            ORDER BY f.name
            """;
        return HgncFamilyQuery.execute(this, sql, rgdId);
    }
}
