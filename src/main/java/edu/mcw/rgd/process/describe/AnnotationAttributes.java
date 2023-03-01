/**
 * @(#)Gene.java
 *
 *
 * @author 
 * @version 1.00 2009/7/8
 * Object of the type AnnotationAttributes holds relevant information for the
 * annotated term 
 */
package edu.mcw.rgd.process.describe;


import edu.mcw.rgd.process.Utils;

public class AnnotationAttributes
{
	public String termId; // term acc id
	public String termName;// this termName is displayed in the description
	public String ontologyGroup;
	public String goAspect;
	public String eCode;
	public int eQualify;
	public String termQualifier;
	
	// six argument constructor initializes term_id, term name, group, aspect, evidence code, and evidence qualifier
	public AnnotationAttributes(String termId, String tname, String ontogrp, String aspect, String evidenceCode, String tQualifier, int eQualify)
	{
		setAnnotTerm(termId);
		setTermName(tname);
		setOntologyGroup(ontogrp);
		setGoAspect(aspect);
		setEcode(evidenceCode);
		//setEvidenceQualification(evidenceCode);
        this.eQualify = eQualify;
		setTermQualifier(tQualifier);
	}
	
	/**
	*Sets termId
	*@param term_id RGD_ID of the annotated term as received from the Database
	*/
	public void setAnnotTerm(String term_id)
	{
		termId = term_id;
	}
	
	/**
	*Sets annotated term name to variable termName
	*@param tname1 term name of the annotated term
	*/
	public void setTermName(String tname1)
	{
		termName = tname1;
	}
	
	/**
	*Sets the type of Ontology the term belongs to. The Ontologies are
	*Gene Ontology, Phenotype Ontology, Pathway Ontology, and Disease Ontology
	*@param group group in which the term belongs to
	*/
	public void setOntologyGroup(String group)
	{
		ontologyGroup = group;
	}
	
	/********************************************************************************************************
	*Sets the Aspects of the annotated term. That is, there are six aspects:				*
	*Molecular function, Biological Process, Phenotype, Pathway, Disease, and Cellular component.		*
	*Variable goAspect might look redundant since we already have variable ontologyGroup to identify	*
	*the Ontology for a term. However, we need to set variable goAspect to distinguish what kind of Gene	*
	*Ontology a term belongs to. That is, Molecular Funtion, Biological Process, or Cellular Component	*
	*@param f go aspect
	*********************************************************************************************************/
	public void setGoAspect(String f)
	{
		goAspect = f;
	}
	
	/**
	*Sets evidence code for the annotated term
	*@param evidenceCode
	*/
	public void setEcode(String evidenceCode)
	{
		eCode = evidenceCode;
	}
	
	/****************************************************************************************
	*takes evidence code as an argument and sets weights (eQualify) to the evidence codes.		*
	*Lower the weight, better the evidence code						*
	*@param evidenceCode2 evidence code of the annotated term
	*****************************************************************************************/
	public void setEvidenceQualification(String evidenceCode2)
	{
        /*
		if ((evidenceCode2.equals("IDA")) || (evidenceCode2.equals("IGI")) ||
			(evidenceCode2.equals("EXP")) || (evidenceCode2.equals("IPI")) ||
			(evidenceCode2.equals("IMP")) || (evidenceCode2.equals("IEP")) ||
			(evidenceCode2.equals("IED")) || (evidenceCode2.equals("IPM")) ||
			(evidenceCode2.equals("IAGP")))
		{
			eQualify = 1;
		}
		else if ((evidenceCode2.equals("ISS")) || (evidenceCode2.equals("ISO")) ||
			(evidenceCode2.equals("ISA")) || (evidenceCode2.equals("ISM")))
		{
			eQualify = 2;
		}
		else if ((evidenceCode2.equals("TAS")) || (evidenceCode2.equals("IEA")) ||
			(evidenceCode2.equals("IC")) || (evidenceCode2.equals("RCA")) ||
			(evidenceCode2.equals("IGC")))
		{
			eQualify = 3;
		}
		else
			eQualify = 4;
			*/
	}

	/****************************************************************************************
	*Stores Qualifier for the annotated term as received from the Database.			*
	*Some common Qualifiers are 'Not', 'No_Association', and Null.				*
	*@param qualify Qualifier received from the database
	*****************************************************************************************/
	public void setTermQualifier(String qualify)
	{
		termQualifier = qualify;
	}
	
	//retrieves the term id
	public String getAnnotTerm()
	{
		return termId;
	}
	
	//retrieves the term name for an annotated term
	public String getTermName()
	{
		return termName;
	}
	
	//retrieves the ontologyGroup(what type of Ontology) for an annotated term
	public String getOntologyGroup()
	{
		return ontologyGroup;
	}
	
	//retrieves Aspect (Molecular function, Biological Process etc) for an annotated term
	public String getGoAspect()
	{
		return goAspect;
	}
	
	// retrieves evidence code for an annotated term
	public String getEcode()
	{
		return eCode;
	}
	
	// retrieves evidence code weight for an annotated term
	public int getEQualify()
	{
		return eQualify;
	}
	
	//retrieves Qualifier for an annotated term
	public String getTermQualifier()
	{
		return termQualifier;
	}
	
	//returns String representation of AnnotationAttributes object
	public String toString()
	{
		return String.format("%s %s %s %s %d\n", 
			termId, termName, ontologyGroup, eCode, eQualify );
	}

}