package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.spring.IntStringMapQuery;

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

        /** Fast count of active genes for a species (no maps_data join). */
        public int countActiveGenes(int speciesKey) throws Exception {
            String sql = "SELECT COUNT(*) FROM rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND r.species_type_key=? AND r.object_key=1";
            return getCount(sql, speciesKey);
        }

        /** Fast count of active genes for a species on a specific assembly (2-table, no NVL). */
        public int countActiveGenesByMapKey(int speciesKey, int mapKey) throws Exception {
            String sql = "SELECT COUNT(DISTINCT md.rgd_id) FROM maps_data md, rgd_ids r " +
                    "WHERE md.map_key=? AND md.rgd_id=r.rgd_id " +
                    "AND r.object_status='ACTIVE' AND r.species_type_key=? AND r.object_key=1";
            return getCount(sql, mapKey, speciesKey);
        }

        /** Lightweight ID+symbol fetch for a species (no assembly filter). */
        public List<IntStringMapQuery.MapPair> getActiveGeneIds(int speciesKey) throws Exception {
            String sql = "SELECT DISTINCT g.rgd_id, g.gene_symbol " +
                    "FROM genes g, rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND r.species_type_key=? " +
                    "AND NVL(gene_type_lc,'*') NOT IN ('splice','allele') " +
                    "AND g.rgd_id=r.rgd_id ORDER BY g.gene_symbol_lc";
            return IntStringMapQuery.execute(this, sql, speciesKey);
        }

        /** Lightweight ID+symbol fetch for a species on a specific assembly. */
        public List<IntStringMapQuery.MapPair> getActiveGeneIdsByMapKey(int speciesKey, int mapKey) throws Exception {
            String sql = "SELECT DISTINCT g.rgd_id, g.gene_symbol " +
                    "FROM genes g, rgd_ids r, maps_data md " +
                    "WHERE r.object_status='ACTIVE' AND r.species_type_key=? " +
                    "AND NVL(gene_type_lc,'*') NOT IN ('splice','allele') " +
                    "AND g.rgd_id=r.rgd_id AND md.rgd_id=r.rgd_id AND md.map_key=? " +
                    "ORDER BY g.gene_symbol_lc";
            return IntStringMapQuery.execute(this, sql, speciesKey, mapKey);
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

        /** Fast count of active QTLs for a species (no maps_data join). */
        public int countActiveQTLs(int speciesKey) throws Exception {
            String sql = "SELECT COUNT(*) FROM rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND r.species_type_key=? AND r.object_key=6";
            return getCount(sql, speciesKey);
        }

        /** Fast count of active QTLs for a species on a specific assembly (2-table). */
        public int countActiveQTLsByMapKey(int speciesKey, int mapKey) throws Exception {
            String sql = "SELECT COUNT(DISTINCT md.rgd_id) FROM maps_data md, rgd_ids r " +
                    "WHERE md.map_key=? AND md.rgd_id=r.rgd_id " +
                    "AND r.object_status='ACTIVE' AND r.species_type_key=? AND r.object_key=6";
            return getCount(sql, mapKey, speciesKey);
        }

        /** Lightweight ID+symbol fetch for a species (no assembly filter). */
        public List<IntStringMapQuery.MapPair> getActiveQTLIds(int speciesKey) throws Exception {
            String sql = "SELECT DISTINCT q.rgd_id, q.qtl_symbol " +
                    "FROM qtls q, rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND r.rgd_id=q.rgd_id AND r.species_type_key=? " +
                    "ORDER BY q.qtl_symbol";
            return IntStringMapQuery.execute(this, sql, speciesKey);
        }

        /** Lightweight ID+symbol fetch for a species on a specific assembly. */
        public List<IntStringMapQuery.MapPair> getActiveQTLIdsByMapKey(int speciesKey, int mapKey) throws Exception {
            String sql = "SELECT DISTINCT q.rgd_id, q.qtl_symbol " +
                    "FROM qtls q, rgd_ids r, maps_data md " +
                    "WHERE r.object_status='ACTIVE' AND r.rgd_id=q.rgd_id AND r.species_type_key=? " +
                    "AND md.rgd_id=q.rgd_id AND md.map_key=? " +
                    "ORDER BY q.qtl_symbol";
            return IntStringMapQuery.execute(this, sql, speciesKey, mapKey);
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

        /** Lightweight ID+symbol fetch for active strains. */
        public List<IntStringMapQuery.MapPair> getActiveStrainIds() throws Exception {
            String sql = "SELECT s.rgd_id, s.strain_symbol " +
                    "FROM strains s, rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND r.rgd_id=s.rgd_id " +
                    "ORDER BY s.strain_symbol";
            return IntStringMapQuery.execute(this, sql);
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

        /** Fast count of active SSLPs for a species (no maps_data join). */
        public int countActiveSSLPs(int speciesKey) throws Exception {
            String sql = "SELECT COUNT(*) FROM rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND r.species_type_key=? AND r.object_key=3";
            return getCount(sql, speciesKey);
        }

        /** Fast count of active SSLPs for a species on a specific assembly (2-table). */
        public int countActiveSSLPsByMapKey(int speciesKey, int mapKey) throws Exception {
            String sql = "SELECT COUNT(DISTINCT md.rgd_id) FROM maps_data md, rgd_ids r " +
                    "WHERE md.map_key=? AND md.rgd_id=r.rgd_id " +
                    "AND r.object_status='ACTIVE' AND r.species_type_key=? AND r.object_key=3";
            return getCount(sql, mapKey, speciesKey);
        }

        /** Lightweight ID+name fetch for a species (no assembly filter). */
        public List<IntStringMapQuery.MapPair> getActiveSSLPIds(int speciesKey) throws Exception {
            String sql = "SELECT DISTINCT s.rgd_id, s.rgd_name " +
                    "FROM sslps s, rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND r.rgd_id=s.rgd_id AND r.species_type_key=? " +
                    "ORDER BY s.rgd_name";
            return IntStringMapQuery.execute(this, sql, speciesKey);
        }

        /** Lightweight ID+name fetch for a species on a specific assembly. */
        public List<IntStringMapQuery.MapPair> getActiveSSLPIdsByMapKey(int speciesKey, int mapKey) throws Exception {
            String sql = "SELECT DISTINCT s.rgd_id, s.rgd_name " +
                    "FROM sslps s, rgd_ids r, maps_data md " +
                    "WHERE r.object_status='ACTIVE' AND r.rgd_id=s.rgd_id AND r.species_type_key=? " +
                    "AND md.rgd_id=s.rgd_id AND md.map_key=? " +
                    "ORDER BY s.rgd_name";
            return IntStringMapQuery.execute(this, sql, speciesKey, mapKey);
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

        /** Lightweight ID+title fetch for active references. */
        public List<IntStringMapQuery.MapPair> getActiveReferenceIds() throws Exception {
            String sql = "SELECT ref.rgd_id, ref.title " +
                    "FROM references ref, rgd_ids r " +
                    "WHERE r.object_status='ACTIVE' AND ref.rgd_id=r.rgd_id " +
                    "ORDER BY ref.rgd_id";
            return IntStringMapQuery.execute(this, sql);
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

        /** Lightweight ID+name fetch for all projects. */
        public List<IntStringMapQuery.MapPair> getAllProjectIds() throws Exception {
            String sql = "SELECT p.rgd_id, p.name " +
                    "FROM projects p " +
                    "ORDER BY p.name";
            return IntStringMapQuery.execute(this, sql);
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
