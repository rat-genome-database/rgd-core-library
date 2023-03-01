package edu.mcw.rgd.datamodel.ontologyx;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 4/11/12
 * Time: 10:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class Aspect {

    public static final String PATHWAY="W";
    public static final String DISEASE="D";
    public static final String CELLULAR_COMPONENT="C";
    public static final String MOLECULAR_FUNCTION="F";
    public static final String BIOLOGICAL_PROCESS="P";
    public static final String MAMMALIAN_PHENOTYPE="N";
    public static final String HUMAN_PHENOTYPE="H";
    public static final String NEURO="B";
    public static final String CHEBI="E";
    public static final String RAT_STRAIN="S";
    public static final String MEASUREMENT_METHOD="M";
    public static final String CLINICAL_MEASUREMENT="L";
    public static final String EXPERIMENTAL_CONDITION="X";

    public static String getFriendlyName(String aspect) {
        if (aspect.toUpperCase().equals("D")) {
            return "Disease";
        }else if (aspect.toUpperCase().equals("W")) {
            return "Pathway";
        }else if (aspect.toUpperCase().equals("C")) {
            return "GO: Cellular Component";
        }else if (aspect.toUpperCase().equals("F")) {
            return "GO: Molecular Function";
        }else if (aspect.toUpperCase().equals("P")) {
            return "GO: Biological Process";
        }else if (aspect.toUpperCase().equals("N")) {
            return "Mammalian Phenotype";
        }else if (aspect.toUpperCase().equals("H")) {
            return "Human Phenotype";
        }else if (aspect.toUpperCase().equals("N")) {
            return "Neuro";
        }else if (aspect.toUpperCase().equals("E")) {
            return "ChEBI";
        }else if (aspect.toUpperCase().equals("S")) {
            return "Rat Strain";
        }else if (aspect.toUpperCase().equals("M")) {
            return "Measurement Method";
        }else if (aspect.toUpperCase().equals("L")) {
            return "Clinical Measurement";
        }else if (aspect.toUpperCase().equals("X")) {
            return "Experimental Condition";
        }

        return "";
    }

}
