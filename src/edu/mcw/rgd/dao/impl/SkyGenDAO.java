package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.*;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import edu.mcw.rgd.datamodel.skygen.*;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import javax.print.attribute.standard.DateTimeAtCompleted;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 5/7/14
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SkyGenDAO extends AbstractDAO {


    public Patient getPatient(int id) throws Exception{

        String query = "select * from skygen_patient sp, skygen_user_patient sup " +
                "where sp.patient_id = sup.patient_id and sup.patient_id=?";

        PatientQuery pq = new PatientQuery(this.getSkygenDataSource(), query);
        pq.declareParameter(new SqlParameter(Types.NUMERIC));
        pq.compile();
        //return pq.execute(new Object[]{mrn});

        List patients =  pq.execute(new Object[]{id});

        if (patients.size() < 1) {
            throw new Exception("Patient not found");
        }

        Patient p = (Patient) patients.get(0);
        return p;

    }

    public void log(String username, int patientId, String msg) throws Exception{
        SqlUpdate su = new SqlUpdate(this.getSkygenDataSource(), "insert into skygen_log (user_name, patient_id, msg, log_date) " +
                " values (?,?,?,?) ");

        su.declareParameter(new SqlParameter(Types.VARCHAR)); // GENE_SYMBOL
        su.declareParameter(new SqlParameter(Types.NUMERIC)); // GENE_SYMBOL_LC
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // FULL_NAME
        su.declareParameter(new SqlParameter(Types.TIMESTAMP)); // FULL_NAME

        su.compile();

        Object[] oa = new Object[]{username, patientId,msg,new java.sql.Date(new Date().getTime())};

        su.update(oa);


    }

    public boolean hasPermissions (String username, int patientId) throws Exception{
        String query = "select count(*) from skygen_user_patient sup where user_name='" + username + "' and patient_id=" + patientId;

        CountQuery cq = new CountQuery(this.getSkygenDataSource(),query);

        List l = cq.execute();

        Integer i = (Integer) l.get(0);

        if (i > 0) {
            return true;
        }else {
            return false;
        }

    }



    public List<Patient> getPatients(String username) throws Exception{

        String query = "select * from skygen_patient sp, skygen_user_patient sup " +
                "where sp.patient_id = sup.patient_id and sup.user_name=? order by lower(sp.last_name)";

        System.out.println("query = " + query);
        PatientQuery pq = new PatientQuery(this.getSkygenDataSource(), query);
        pq.declareParameter(new SqlParameter(Types.VARCHAR));
        pq.compile();
        return pq.execute(new Object[]{username});
    }

    public void insertPatient(String user, Patient p) throws Exception{

        SqlUpdate su = new SqlUpdate(this.getSkygenDataSource(), "insert into skygen_patient (mrn, first_name, last_name, dob, patient_id) " +
                " values (?,?,?,?,?) ");

        su.declareParameter(new SqlParameter(Types.VARCHAR)); // GENE_SYMBOL
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // GENE_SYMBOL_LC
        su.declareParameter(new SqlParameter(Types.VARCHAR)); // FULL_NAME
        su.declareParameter(new SqlParameter(Types.DATE)); // NOMEN_REVIEW_DATE
        su.declareParameter(new SqlParameter(Types.NUMERIC)); // NOMEN_REVIEW_DATE


        su.compile();

        int pid = this.getNextKeyFromSequence("SKYGEN_PATIENT_SEQ");

        Object[] oa = new Object[]{p.getMrn(),p.getFirstName(),p.getLastName(),p.getDateOfBirth(), pid};

        su.update(oa);

        System.out.println("inserting " + user + " - " + pid);

        su = new SqlUpdate(this.getSkygenDataSource(), "insert into skygen_user_patient (user_name, patient_id) " +
                " values (?,?) ");

        su.declareParameter(new SqlParameter(Types.VARCHAR)); // GENE_SYMBOL
        su.declareParameter(new SqlParameter(Types.NUMERIC)); // GENE_SYMBOL_LC

        su.compile();
        oa = new Object[]{user,pid};

        su.update(oa);


    }

    public List<String> getPatientPhenotypes(int id) throws Exception{
        String queryString = "select phenotype from skygen_patient_phenotype where patient_id=?";
        StringListQuery phenotypesQ = new StringListQuery(this.getSkygenDataSource(), queryString);
        phenotypesQ.declareParameter(new SqlParameter(Types.NUMERIC));
        phenotypesQ.compile();
        return phenotypesQ.execute(new Object[]{id});
    }

    public List<String> getPatientGenes(int id) throws Exception{
        String queryString = "select gene_id from skygen_patient_gene where patient_id=?";
        StringListQuery genesQ = new StringListQuery(this.getSkygenDataSource(), queryString);
        genesQ.declareParameter(new SqlParameter(Types.NUMERIC));
        genesQ.compile();
        return genesQ.execute(new Object[]{id});
    }

    public List<StringMapQuery.MapPair> getPatientPhenotypeTerms(int id) throws Exception{
        String queryString = "select phenotype, term from skygen_patient_phenotype, ont_terms " +
                "where patient_id=? and phenotype=term_acc";
        StringMapQuery phenotypesQ = new StringMapQuery(this.getSkygenDataSource(), queryString);
        phenotypesQ.declareParameter(new SqlParameter(Types.NUMERIC));
        phenotypesQ.compile();
        return phenotypesQ.execute(new Object[]{id});
    }

    public List<StringMapQuery.MapPair> getPatientGeneSymbols(int id) throws Exception{
        String queryString = "select gene_id, gene_symbol from skygen_patient_gene, genes " +
                "where patient_id=? and to_number(gene_id)=rgd_id";
        StringMapQuery genesQ = new StringMapQuery(this.getSkygenDataSource(), queryString);
        genesQ.declareParameter(new SqlParameter(Types.NUMERIC));
        genesQ.compile();
        return genesQ.execute(new Object[]{id});
    }

    public void insertPatientPhenotype(int id, String phenotype) throws Exception {
        SqlUpdate phenotypeUpdate = new SqlUpdate(this.getSkygenDataSource(), "insert into skygen_patient_phenotype"
        + " values (?,?)");
        phenotypeUpdate.declareParameter(new SqlParameter(Types.NUMERIC));
        phenotypeUpdate.declareParameter(new SqlParameter(Types.VARCHAR));
        phenotypeUpdate.compile();
        phenotypeUpdate.update(new Object[]{id, phenotype});
    }

    public void insertPatientGene(int id, String geneId) throws Exception {
        SqlUpdate geneUpdate = new SqlUpdate(this.getSkygenDataSource(), "insert into skygen_patient_gene"
        + " values (?,?)");
        geneUpdate.declareParameter(new SqlParameter(Types.NUMERIC));
        geneUpdate.declareParameter(new SqlParameter(Types.VARCHAR));
        geneUpdate.compile();
        geneUpdate.update(new Object[]{id, geneId});
    }


    public void deletePatientPhenotype(int id, String phenotype) throws Exception {
        SqlUpdate phenotypeUpdate = new SqlUpdate(this.getSkygenDataSource(), "delete from skygen_patient_phenotype"
        + " where patient_id=? and phenotype=?");
        phenotypeUpdate.declareParameter(new SqlParameter(Types.NUMERIC));
        phenotypeUpdate.declareParameter(new SqlParameter(Types.VARCHAR));
        phenotypeUpdate.compile();
        phenotypeUpdate.update(new Object[]{id, phenotype});
    }

    public void deletePatientGene(int id, String geneId) throws Exception {
        SqlUpdate geneUpdate = new SqlUpdate(this.getSkygenDataSource(), "delete from skygen_patient_gene"
        + " where patient_id=? and gene_id=?");
        geneUpdate.declareParameter(new SqlParameter(Types.NUMERIC));
        geneUpdate.declareParameter(new SqlParameter(Types.VARCHAR));
        geneUpdate.compile();
        geneUpdate.update(new Object[]{id, geneId});
    }


    public void deleteAllPatientPhenotypes(int id) throws Exception {
        SqlUpdate phenotypeUpdate = new SqlUpdate(this.getSkygenDataSource(), "delete from skygen_patient_phenotype"
        + " where patient_id=?");
        phenotypeUpdate.declareParameter(new SqlParameter(Types.NUMERIC));
        phenotypeUpdate.compile();
        phenotypeUpdate.update(new Object[]{id});
    }

    public void deleteAllPatientGenes(int id) throws Exception {
        SqlUpdate geneUpdate = new SqlUpdate(this.getSkygenDataSource(), "delete from skygen_patient_gene"
        + " where patient_id=?");
        geneUpdate.declareParameter(new SqlParameter(Types.NUMERIC));
        geneUpdate.compile();
        geneUpdate.update(new Object[]{id});
    }

    public void deletePatient(int id) throws Exception {
        SqlUpdate geneUpdate = new SqlUpdate(this.getSkygenDataSource(), "delete from skygen_patient where patient_id=?");
        geneUpdate.declareParameter(new SqlParameter(Types.NUMERIC));
        geneUpdate.compile();
        geneUpdate.update(new Object[]{id});
    }



}
