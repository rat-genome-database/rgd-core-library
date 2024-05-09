package edu.mcw.rgd.process.describe;

import edu.mcw.rgd.dao.impl.GeneDAO;
import edu.mcw.rgd.process.Utils;

import java.util.*;
import java.io.*;

public class GeneDescription {
	public String gene_id;// RGD_ID of a gene
	public String gene_name;// Gene name

    // indexes to geneContents array - order is important
    public static final int GC_MOLECULAR_FUNCTION = 0;
    public static final int GC_BIOLOGICAL_PROCESS = 1;
    public static final int GC_PATHWAY = 2;
    public static final int GC_PHENOTYPE = 3;
    public static final int GC_DISEASE = 4;
    public static final int GC_CELLULAR_COMPONENT = 5;
    public static final int GC_CHEBI = 6;
    public static final int GC_COUNT = 7;

	String geneType = "";

	// Stores annotated term's attributes if it belongs to given Ontology
    private List<AnnotationAttributes> [] geneContents = (List<AnnotationAttributes>[]) new ArrayList[GC_COUNT];

    public String description = null;// Stores the Gene Description as a String Object

	//Constructor initializes instance variables and ArrayLists
	public GeneDescription(String id, String name) throws Exception {
		setGeneId(id);
		setGeneName(name);
        for( int i=0; i<GC_COUNT; i++ ) {
		    geneContents[i] = new ArrayList<AnnotationAttributes>(3);
        }
		geneType=new GeneDAO().getGene(Integer.parseInt(gene_id)).getType();
		description = "";
	}

	//Sets gene Id as received from the Database
	public void setGeneId(String d)
	{
		gene_id = d;
	}

	//Sets gene name as received from the Database
	public void setGeneName(String nam)
	{
		gene_name = nam;
	}

	/************************************************************************************
	* If a term is annotated to this gene and the term belongs to given ontology,		*
	* then this method adds the term and its attributes to the ArrayList				*
	*************************************************************************************/
	public void generateContent(AnnotationDG a, String group, int gc)
	{
		AnnotationAttributes abutes = new AnnotationAttributes(a.getTermAcc(), a.getTerm(), group, a.getAspect(), a.getEvidence(), a.getQualifier(), a.geteQualifier());

        // do not add top-level terms or terms with unacceptable evidence code
        if( abutes.getAnnotTerm().equals("GO:0003674") //removes if the term is Molecular Function
            || abutes.getAnnotTerm().equals("GO:0008150") //removes if the term is Biological Process
            || abutes.getAnnotTerm().equals("GO:0005575") //removes if the term is cellular component
            || abutes.getAnnotTerm().equals("GO:0005515") //removes if the term is 'protein binding'
            || abutes.getEQualify() == 4 )//removes any term with evidence qualification = 4
        {
            return; // generate no content
        }

        if( geneContents[gc].size()<3 )
            geneContents[gc].add (abutes);
	}

	//returns gene Id of GeneDescription object
	public String getGeneId()
	{
		return gene_id;
	}

	//returns gene name of GeneDescription onject
	public String getGeneName()
	{
		return gene_name;
	}

    /**
     * returns String representation of ArrayList geneContents for given ontology
     * @param gc index to geneContents array (one of GC_xxx enums)
     * @return
     */
	public String getGeneContents(int gc)
	{
		return geneContents[gc].toString();
	}

	/********************************************************************************************************************************
	*Creates a String object which stores a portion of description that is derived from terms related to Molecular Function		*
	*@return a description derived only from Molecular Function terms		*
	*********************************************************************************************************************************/
	public String createDescriptionMolecularFunction()
	{
		if(Utils.stringsAreEqual(geneType,"pseudo")){
			return createDescription(GC_MOLECULAR_FUNCTION, "ENCODES a " + geneType +"gene" +" that exhibits ", 0);
		}
		if(!Utils.stringsAreEqual(geneType,"protein-coding")&&!Utils.stringsAreEqual(geneType,"pseudo")){
			return createDescription(GC_MOLECULAR_FUNCTION, "ENCODES an " + geneType + " that exhibits ", 0);
		}
        return createDescription(GC_MOLECULAR_FUNCTION, "ENCODES a protein that exhibits ", 0);
	}

	/********************************************************************************************************************************
	*Creates a String object which stores a portion of description that is derived from terms related to Biological Process		*
	*@return a String object 'descriptionStringCopy' which stores description derived only from Biological Process terms		*
	*********************************************************************************************************************************/
	public String createDescriptionBiologicalProcess()
	{
        return createDescription(GC_BIOLOGICAL_PROCESS, "INVOLVED IN ", 0);
	}

	/********************************************************************************************************************************
	*Creates a String object which stores a portion of description that is derived from terms related to Cellular Component	*
	*@return a String object 'descriptionStringCopy' which stores description derived only from Cellular Component terms		*
	*********************************************************************************************************************************/
	public String createDescriptionCellularComponent()
	{
        return createDescription(GC_CELLULAR_COMPONENT, "FOUND IN ", 0);
	}

	/********************************************************************************************************************************
	*Creates a String object which stores a portion of description that is derived from terms related to Phenotype Ontology		*
	*@return a String object 'descriptionStringCopy' which stores description derived only from Phenotype Ontology terms		*
	*********************************************************************************************************************************/
	public String createDescriptionPhenotype()
	{
        return createDescription(GC_PHENOTYPE, "ASSOCIATED WITH ", 2);
	}

	/********************************************************************************************************************************
	*Creates a String object which stores a portion of description that is derived from terms related to Disease Ontology		*
	*@return a String object 'descriptionStringCopy' which stores description derived only from Disease Ontology terms		*
	*********************************************************************************************************************************/
	public String createDescriptionDisease()
	{
        return createDescription(GC_DISEASE, "ASSOCIATED WITH ", 0);
	}

	/********************************************************************************************************************************
	*Creates a String object which stores a portion of description that is derived from terms related to Pathway			*
	*@return a String object 'descriptionStringCopy' which stores description derived only from Pathway Ontology terms		*
	*********************************************************************************************************************************/
	public String createDescriptionPathway()
	{
        // option 1:
		//no need to indicate "inferred" or "ortholog" for Pathway Ontology terms
        return createDescription(GC_PATHWAY, "PARTICIPATES IN ", 1);
	}

    /********************************************************************************************************************************
    *Creates a String object which stores a portion of description that is derived from terms related to Chebi Ontology		*
    *@return a String object 'descriptionStringCopy' which stores description derived only from Chebi Ontology terms		*
    *********************************************************************************************************************************/
    public String createDescriptionChebi() {

        return createDescription(GC_CHEBI, "INTERACTS WITH ", 0);
    }

     /********************************************************************************************************************
     * Creates a String object which stores a portion of description that is derived from terms related to Ontology		*
     * @param gc index to geneContents array; one of GC_xxx enums
      * @param label label
      * @param option 0 - generate (inferred) and (ortholog) info   <br>
      *        option 1 - generate no (inferred) and (ortholog) info <br>
      *        option 2 - generate no (inferred) and (ortholog) info; also append term name if and only if eQualify==1 <br>
    * @return a String object
    *********************************************************************************************************************************/
    public String createDescription(int gc, String label, int option)
    {
        StringBuilder desc = new StringBuilder();// a String object to store the description is created
        int nonInferredCounter = 0;// counts the number of annotated terms with non-inferred evidence codes (like IDA, IEP etc)
        // that are stored in descriptionStringCopy . We use this variable to analyze when to display terms with Inferred evidence codes
        // (like IEA, TAS, IGC etc)
        int commaSeparatorCounter = 0;// each term is separated by a semi-colon
        // this counter is used to analyze any redundant semicolon occurrences
        List<AnnotationAttributes> contents = geneContents[gc];
        if (!contents.isEmpty()) {

            // append a semicolon, if other preceding ontologies generate some description
            // therefore, semicolon is displayed only if this term is not the first term
            boolean isFirstTerm = true;
            for( int j=0; j<gc; j++ ) {
                if( !geneContents[j].isEmpty() ) {
                    isFirstTerm = false;
                    break;
                }
            }
            if( !isFirstTerm ) {
                desc.append("; ");
            }

            desc.append(label);

            for (AnnotationAttributes aa : contents) {

                if (option == 0) {
                    // if the term does not have inferred evidence code then increase counters
                    if (!(aa.eQualify == 3)) {
                        nonInferredCounter++;
                    }

                    // if terms with non-inferred evidence codes are already used in the description and the next term has
                    // an inferred evidence code then we end this method. Terms with inferred evidence codes are used
                    // in the description only when non-inferred evidence codes are absent
                    if ((aa.eQualify == 3) && (nonInferredCounter > 0)) {
                        return desc.toString();
                    }
                }

                if (option == 2) {
                    if (aa.eQualify == 1)//This is redundant but necessary to check everytime,
                    //if the evidence code is only for rat
                    {
                        if (commaSeparatorCounter++ > 0)
                            desc.append("; ");
                        desc.append(aa.termName);
                    } else {
                        continue;
                    }
                } else {
                    if (commaSeparatorCounter++ > 0)
                        desc.append("; ");
                    desc.append(aa.termName);
                }

                if (option == 0) {
                    //if the term is annotated with inferred evidence codes, displays 'inferred' after the term
                    if (aa.eQualify == 3) {
                        desc.append(" (inferred)");
                    }

                    // if the term is annotated with evidence qualification 2 (like ISS, ISO), displays 'ortholog' after the term
                    else if (aa.eQualify == 2) {
                        desc.append(" (ortholog)");
                    }
                }
            }
        }
        return desc.length()>label.length()+2 ? desc.toString() : "";
    }

    /********************************************************************************************************
	* Generates the Gene description and stores it into String object description				*					*
	* of the element is removed										*
	*********************************************************************************************************/

	public void createDescription(){
		//output.format("%n%n%s%s%s%s%s%n", "Description for gene: \"", getGeneId(), "-", getGeneName(), "\" is:");
        StringBuilder buf = new StringBuilder(description);
		buf.append(createDescriptionMolecularFunction());
		buf.append(createDescriptionBiologicalProcess());
		buf.append(createDescriptionPathway());
		buf.append(createDescriptionPhenotype());
		buf.append(createDescriptionDisease());
		buf.append(createDescriptionCellularComponent());
        buf.append(createDescriptionChebi());
        description = buf.toString();
	}

	//returns size for ArrayList geneContents
	public int getGeneContentSize(int gc)
	{
		return geneContents[gc].size();
	}

	//returns the gene description
	public String getDescription()
	{
		return description;
	}

	public void getAuxiliaryInfo(int max, int count, PrintStream output, int len500)
	{
		output.format("%n%n%s%d", "The maximum length of the description is: " , max);
		output.format("%n%n%s%d", "The total number of \"Active\" rat genes that are annotated is: ", count);
		output.format("%n%n%s%d", "Number of rat genes which have descriptions longer or equal to 500 characters is: ", len500);
	}

	// Constructor with no arguments
	public GeneDescription() throws Exception {
        for( int i=0; i<GC_COUNT; i++ ) {
		    geneContents[i] = new ArrayList<AnnotationAttributes>(3);
        }
	}
}