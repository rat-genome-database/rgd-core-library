/**
 * @(#)DescriptionGenerator.java
 *
 *
 * @author
 * @version 1.00 2009/8/20
 */

package edu.mcw.rgd.process.describe;

import edu.mcw.rgd.dao.DataSourceFactory;
import edu.mcw.rgd.dao.impl.GeneDAO;
import edu.mcw.rgd.datamodel.Gene;
import edu.mcw.rgd.process.Utils;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


public class DescriptionGenerator {
    public static void main(String args[]) throws Exception {
        DescriptionGenerator cd3 = new DescriptionGenerator();
        System.out.println(cd3.buildDescription(2611));
    }


    // launch the application
    public String buildDescription(int rgdId) throws Exception {

        List<AnnotationDG> annotList = getAnnotationsForDescriptionGenerator(rgdId);
        filterAnnotations(annotList);
        Collections.sort(annotList, new Comparator<AnnotationDG>() {
            public int compare(AnnotationDG o1, AnnotationDG o2) {
                int r = o1.geteQualifier() - o2.geteQualifier();
                if( r!=0 )
                    return r;
                return Utils.stringsCompareToIgnoreCase(o1.getTerm(), o2.getTerm());
            }
        });
        // now we have annotations sorted by relevance; and by  term anme for same releavance

        GeneDAO gdao = new GeneDAO();
        Gene gene = gdao.getGene(rgdId);

        //creates a GeneDescription object which manipulates description for a particular gene
        GeneDescription genecription = new GeneDescription(gene.getRgdId() + "", gene.getName());

        for (AnnotationDG annot: annotList) {

            String termQualify;//holds the 'Qualifier' field from the Database
            if ((annot.getQualifier() == null)) {
                termQualify = "empty";
            } else {
                termQualify = annot.getQualifier();
            }

            //executes following statements only if the Qualifier is not 'Not' or 'No_Association'
            if (!((termQualify.equalsIgnoreCase("not")) ||
                    (termQualify.equalsIgnoreCase("no_association")))) {

                if (annot.getAspect().equals("C"))//If GO Cellular Component
                {
                    genecription.generateContent(annot, "GO", GeneDescription.GC_CELLULAR_COMPONENT);
                } else if (annot.getAspect().equals("F"))// If GO Molecular Function
                {
                    genecription.generateContent(annot, "GO", GeneDescription.GC_MOLECULAR_FUNCTION);
                } else if (annot.getAspect().equals("P")) //If GO Biological Process
                {
                    genecription.generateContent(annot, "GO", GeneDescription.GC_BIOLOGICAL_PROCESS);
                } else if (annot.getAspect().equals("N"))//If Phenotype
                {
                    genecription.generateContent(annot, "MP", GeneDescription.GC_PHENOTYPE);
                } else if (annot.getAspect().equals("W"))//If Pathway
                {
                    genecription.generateContent(annot, "PW", GeneDescription.GC_PATHWAY);
                } else if (annot.getAspect().equals("E"))//If Gene-Chemical Interaction
                {
                    genecription.generateContent(annot, "CHEBI", GeneDescription.GC_CHEBI);
                } else//If Disease
                {
                    genecription.generateContent(annot, "RDO", GeneDescription.GC_DISEASE);
                }
            }
        }

        //processes data for the last gene
        genecription.createDescription();

        return genecription.getDescription();
    }

    void filterAnnotations(List<AnnotationDG> annotList) {

        // annotations are sorted per term by evidence relevance
        // only first annotation from term annotations should be used; remaining must be deleted
        String prevAccId = "";
        Iterator<AnnotationDG> it = annotList.iterator();
        while( it.hasNext() ) {
            AnnotationDG a = it.next();
            if( a.getTermAcc().equals(prevAccId) ) {
                // duplicate annotation detected -- drop it
                it.remove();
            }
            else {
                // annotation for different term
                prevAccId = a.getTermAcc();
            }
        }
    }

    /**
     * get annotations by annotated object rgd id
     * @param rgdId annotated object rgd id
     * @return list of Annotation objects
     * @throws Exception on spring framework dao failure
     */
    public List<AnnotationDG> getAnnotationsForDescriptionGenerator(int rgdId) throws Exception {
        String query = "SELECT a.term_acc,a.term,a.qualifier,a.aspect,a.evidence,"+
                "DECODE(evidence,'IDA',1,'IGI',1,'EXP',1,'IPI',1,'IMP',1,'IEP',1,'IED',1,'IPM',1,'IAGP',1,"+
                        "'ISS',2,'ISO',2,'ISA',2,'ISM',2,"+
                        "'TAS',3,'IEA',3,'IC',3,'RCA',3,'IGC',3,"+
                        "4) equalifier "+
            "FROM full_annot a WHERE annotated_object_rgd_id=? ORDER BY term, equalifier";

        AnnotationDGQuery q = new AnnotationDGQuery(DataSourceFactory.getInstance().getDataSource(), query);
        q.declareParameter(new SqlParameter(Types.INTEGER));
        q.compile();
        return q.execute(new Object[]{rgdId});
    }

    class AnnotationDGQuery extends MappingSqlQuery {

        public AnnotationDGQuery(DataSource ds, String query) {
            super(ds, query);
        }

        protected Object mapRow(ResultSet rs, int rowNum) throws SQLException {

            AnnotationDG annot = new AnnotationDG();
            annot.setTerm(rs.getString("term"));
            annot.setAspect(rs.getString("aspect"));
            annot.setQualifier(rs.getString("qualifier"));
            annot.setEvidence(rs.getString("evidence"));
            annot.setTermAcc(rs.getString("term_acc"));
            annot.seteQualifier(rs.getInt("equalifier"));

            return annot;
        }
    }
}
