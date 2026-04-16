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
