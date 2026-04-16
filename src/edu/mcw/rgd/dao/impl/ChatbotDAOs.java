package edu.mcw.rgd.dao.impl;

import javax.sql.DataSource;
import java.util.List;

/**
 * Chatbot-specific DAO wrappers.
 * Each nested class extends an existing DAO and overrides
 * {@link edu.mcw.rgd.dao.AbstractDAO#getDataSource()} to return the caller-supplied
 * DataSource. The chatbot decides per environment which Oracle DS to inject
 * (stage reed vs. env default) and passes it in the constructor.
 * A few {@code ...ByMapKey} methods are added for assembly-filtered bulk
 * loading; these do not exist in the parent DAOs.
 */
public class ChatbotDAOs {

    private ChatbotDAOs() {
        // utility holder
    }

    // ---------------------------------------------------------------- Gene
    public static class Gene extends GeneDAO {
        private final DataSource dataSource;

        public Gene(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public DataSource getDataSource() {
            return dataSource;
        }

        /**
         * Active genes for a species, restricted to a specific assembly (map_key).
         * Splices and alleles are excluded, matching {@link GeneDAO#getActiveGenes(int)}.
         */
        public List<edu.mcw.rgd.datamodel.Gene> getActiveGenesByMapKey(int speciesKey, int mapKey) throws Exception {
            String sql = "SELECT DISTINCT g.*, r.species_type_key FROM genes g, rgd_ids r, maps_data md " +
                    "WHERE r.object_status='ACTIVE' AND r.species_type_key=? " +
                    "AND NVL(gene_type_lc,'*') NOT IN ('splice','allele') " +
                    "AND r.rgd_id=g.rgd_id AND md.rgd_id=g.rgd_id AND md.map_key=? " +
                    "ORDER BY g.gene_symbol_lc";
            return executeGeneQuery(sql, speciesKey, mapKey);
        }

        /** Count of active genes for a species (matches {@link GeneDAO#getActiveGenes(int)}). */
        public int countActiveGenes(int speciesKey) throws Exception {
            String sql = "SELECT COUNT(*) FROM genes g, rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND r.species_type_key=? " +
                    "AND NVL(gene_type_lc,'*') NOT IN ('splice','allele') " +
                    "AND r.rgd_id=g.rgd_id";
            return getCount(sql, speciesKey);
        }

        /** Count of active genes for a species on a specific assembly (matches {@link #getActiveGenesByMapKey}). */
        public int countActiveGenesByMapKey(int speciesKey, int mapKey) throws Exception {
            String sql = "SELECT COUNT(DISTINCT g.rgd_id) FROM genes g, rgd_ids r, maps_data md " +
                    "WHERE r.object_status='ACTIVE' AND r.species_type_key=? " +
                    "AND NVL(gene_type_lc,'*') NOT IN ('splice','allele') " +
                    "AND r.rgd_id=g.rgd_id AND md.rgd_id=g.rgd_id AND md.map_key=?";
            return getCount(sql, speciesKey, mapKey);
        }
    }

    // ----------------------------------------------------------------- QTL
    public static class QTL extends QTLDAO {
        private final DataSource dataSource;

        public QTL(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public DataSource getDataSource() {
            return dataSource;
        }

        /** Active QTLs for a species, restricted to a specific assembly (map_key). */
        public List<edu.mcw.rgd.datamodel.QTL> getActiveQTLsByMapKey(int speciesKey, int mapKey) throws Exception {
            String sql = "SELECT DISTINCT q.*, r.species_type_key FROM qtls q, rgd_ids r, maps_data md " +
                    "WHERE r.object_status='ACTIVE' AND r.rgd_id=q.rgd_id AND r.species_type_key=? " +
                    "AND md.rgd_id=q.rgd_id AND md.map_key=? " +
                    "ORDER BY q.qtl_symbol";
            return executeQtlQuery(sql, speciesKey, mapKey);
        }

        /** Count of active QTLs for a species (matches {@link QTLDAO#getActiveQTLs(int)}). */
        public int countActiveQTLs(int speciesKey) throws Exception {
            String sql = "SELECT COUNT(*) FROM qtls q, rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND r.rgd_id=q.rgd_id AND r.species_type_key=?";
            return getCount(sql, speciesKey);
        }

        /** Count of active QTLs for a species on a specific assembly (matches {@link #getActiveQTLsByMapKey}). */
        public int countActiveQTLsByMapKey(int speciesKey, int mapKey) throws Exception {
            String sql = "SELECT COUNT(DISTINCT q.rgd_id) FROM qtls q, rgd_ids r, maps_data md " +
                    "WHERE r.object_status='ACTIVE' AND r.rgd_id=q.rgd_id AND r.species_type_key=? " +
                    "AND md.rgd_id=q.rgd_id AND md.map_key=?";
            return getCount(sql, speciesKey, mapKey);
        }
    }

    // --------------------------------------------------------------- Strain
    public static class Strain extends StrainDAO {
        private final DataSource dataSource;

        public Strain(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public DataSource getDataSource() {
            return dataSource;
        }

        /** Count of active strains (matches {@link StrainDAO#getActiveStrains()}). */
        public int countActiveStrains() throws Exception {
            String sql = "SELECT COUNT(*) FROM strains s, rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND r.rgd_id=s.rgd_id";
            return getCount(sql);
        }
    }

    // --------------------------------------------------------- SSLP / Marker
    public static class SSLP extends SSLPDAO {
        private final DataSource dataSource;

        public SSLP(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public DataSource getDataSource() {
            return dataSource;
        }

        /** Active SSLPs for a species, restricted to a specific assembly (map_key). */
        public List<edu.mcw.rgd.datamodel.SSLP> getActiveSSLPsByMapKey(int speciesKey, int mapKey) throws Exception {
            String sql = "SELECT DISTINCT s.*, r.species_type_key FROM sslps s, rgd_ids r, maps_data md " +
                    "WHERE r.object_status='ACTIVE' AND r.rgd_id=s.rgd_id AND r.species_type_key=? " +
                    "AND md.rgd_id=s.rgd_id AND md.map_key=? " +
                    "ORDER BY s.rgd_name";
            return executeSSLPQuery(sql, speciesKey, mapKey);
        }

        /** Count of active SSLPs for a species (matches {@link SSLPDAO#getActiveSSLPs(int)}). */
        public int countActiveSSLPs(int speciesKey) throws Exception {
            String sql = "SELECT COUNT(*) FROM sslps s, rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND r.rgd_id=s.rgd_id AND r.species_type_key=?";
            return getCount(sql, speciesKey);
        }

        /** Count of active SSLPs for a species on a specific assembly (matches {@link #getActiveSSLPsByMapKey}). */
        public int countActiveSSLPsByMapKey(int speciesKey, int mapKey) throws Exception {
            String sql = "SELECT COUNT(DISTINCT s.rgd_id) FROM sslps s, rgd_ids r, maps_data md " +
                    "WHERE r.object_status='ACTIVE' AND r.rgd_id=s.rgd_id AND r.species_type_key=? " +
                    "AND md.rgd_id=s.rgd_id AND md.map_key=?";
            return getCount(sql, speciesKey, mapKey);
        }
    }

    // ------------------------------------------------------------ Reference
    public static class Reference extends ReferenceDAO {
        private final DataSource dataSource;

        public Reference(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public DataSource getDataSource() {
            return dataSource;
        }

        /** Count of active references (matches {@link ReferenceDAO#getActiveReferences()}). */
        public int countActiveReferences() throws Exception {
            String sql = "SELECT COUNT(*) FROM references ref, rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND ref.rgd_id=r.rgd_id";
            return getCount(sql);
        }
    }

    // -------------------------------------------------------------- Project
    public static class Project extends ProjectDAO {
        private final DataSource dataSource;

        public Project(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public DataSource getDataSource() {
            return dataSource;
        }

        /** Count of all projects (matches {@link ProjectDAO#getAllProjects()}). */
        public int countAllProjects() throws Exception {
            String sql = "SELECT COUNT(*) FROM projects";
            return getCount(sql);
        }
    }

    // ------------------------------------------------------ Map (assemblies)
    public static class Map extends MapDAO {
        private final DataSource dataSource;

        public Map(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public DataSource getDataSource() {
            return dataSource;
        }
    }
}
