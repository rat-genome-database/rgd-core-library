package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.spring.*;

import java.util.Date;
import java.util.List;

import edu.mcw.rgd.datamodel.skygen.*;

import javax.sql.DataSource;

/**
 * @author jdepons
 * @since 5/7/14
 */
public class SkyGenDAO extends AbstractDAO {

    public DataSource getDataSource() throws Exception{
        return DataSourceFactory.getInstance().getSkynetDataSource();
    }

    public Patient getPatient(int id) throws Exception{

        String query = "select * from skygen_patient sp, skygen_user_patient sup " +
                "where sp.patient_id = sup.patient_id and sup.patient_id=?";

        PatientQuery pq = new PatientQuery(this.getSkygenDataSource(), query);
        List patients =  execute(pq, id);

        if (patients.size() < 1) {
            throw new Exception("Patient not found");
        }

        return (Patient) patients.get(0);

    }

    public void log(String username, int patientId, String msg) throws Exception{

        String sql = "INSERT INTO skygen_log (user_name, patient_id, msg, log_date)  VALUES (?,?,?,?)";
        update(sql, username, patientId, msg, new Date());
    }

    public boolean hasPermissions (String username, int patientId) throws Exception{

        String query = "select count(*) from skygen_user_patient sup where user_name=? and patient_id=?";
        return getCount(query, username, patientId) > 0;
    }

    public List<Patient> getPatients(String username) throws Exception{

        String query = "select * from skygen_patient sp, skygen_user_patient sup " +
                "where sp.patient_id = sup.patient_id and sup.user_name=? order by lower(sp.last_name)";

        PatientQuery pq = new PatientQuery(this.getSkygenDataSource(), query);
        return execute(pq, username);
    }

    public void insertPatient(String user, Patient p) throws Exception{

        String sql = "insert into skygen_patient (mrn, first_name, last_name, dob, patient_id) " +
                " values (?,?,?,?,?) ";

        int pid = this.getNextKeyFromSequence("SKYGEN_PATIENT_SEQ");

        update(sql, p.getMrn(), p.getFirstName(), p.getLastName(), p.getDateOfBirth(), pid);


        sql = "insert into skygen_user_patient (user_name, patient_id) " +
                " values (?,?) ";
        update(sql, user, pid);
    }

    public List<String> getPatientPhenotypes(int id) throws Exception{
        String query = "select phenotype from skygen_patient_phenotype where patient_id=?";
        return StringListQuery.execute(this, query, id);
    }

    public List<String> getPatientGenes(int id) throws Exception{
        String query = "select gene_id from skygen_patient_gene where patient_id=?";
        return StringListQuery.execute(this, query, id);
    }

    public List<StringMapQuery.MapPair> getPatientPhenotypeTerms(int id) throws Exception{
        String query = "select phenotype, term from skygen_patient_phenotype, ont_terms " +
                "where patient_id=? and phenotype=term_acc";
        return StringMapQuery.execute(this, query, id);
    }

    public List<StringMapQuery.MapPair> getPatientGeneSymbols(int id) throws Exception{
        String query = "select gene_id, gene_symbol from skygen_patient_gene, genes " +
                "where patient_id=? and to_number(gene_id)=rgd_id";
        return StringMapQuery.execute(this, query, id);
    }

    public void insertPatientPhenotype(int id, String phenotype) throws Exception {
        String sql = "insert into skygen_patient_phenotype values (?,?)";
        update(sql, id, phenotype);
    }

    public void insertPatientGene(int id, String geneId) throws Exception {
        String sql = "insert into skygen_patient_gene values (?,?)";
        update(sql, id, geneId);
    }

    public void deletePatientPhenotype(int id, String phenotype) throws Exception {
        String sql = "delete from skygen_patient_phenotype where patient_id=? and phenotype=?";
        update(sql, id, phenotype);
    }

    public void deletePatientGene(int id, String geneId) throws Exception {
        String sql = "delete from skygen_patient_gene where patient_id=? and gene_id=?";
        update(sql, id, geneId);
    }

    public void deleteAllPatientPhenotypes(int id) throws Exception {
        String sql = "delete from skygen_patient_phenotype where patient_id=?";
        update(sql, id);
    }

    public void deleteAllPatientGenes(int id) throws Exception {
        String sql = "delete from skygen_patient_gene  where patient_id=?";
        update(sql, id);
    }

    public void deletePatient(int id) throws Exception {
        String sql = "delete from skygen_patient where patient_id=?";
        update(sql, id);
    }

}
