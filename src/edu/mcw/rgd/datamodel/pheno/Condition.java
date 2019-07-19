package edu.mcw.rgd.datamodel.pheno;

import edu.mcw.rgd.dao.impl.OntologyXDAO;

import java.text.DecimalFormat;

/**
 * @author jdepons
 * @since 2/7/11
 * Represents a row from EXPERIMENT_CONDITION table.
 */
public class Condition {

    private int id; // unique condition id, from Oracle sequence EXPERIMENT_CONDITION_SEQ

    // mutually exclusive: experimentRecordId, geneExpressionRecordId
    private int experimentRecordId;
    private int geneExpressionRecordId;

    private Integer ordinality;
    private String units;
    private String valueMin;
    private String valueMax;

    private double durationLowerBound;
    private double durationUpperBound;
    private String notes;
    private String ontologyId;
    private String applicationMethod;

    private String conditionDescription;

    public double getDurationLowerBound() {
        return durationLowerBound;
    }

    public void setDurationLowerBound(double durationLowerBound) {
        this.durationLowerBound = durationLowerBound;
    }

    public double getDurationUpperBound() {
        return durationUpperBound;
    }

    public void setDurationUpperBound(double durationUpperBound) {
        this.durationUpperBound = durationUpperBound;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExperimentRecordId() {
        return experimentRecordId;
    }

    public void setExperimentRecordId(int experimentRecordId) {
        this.experimentRecordId = experimentRecordId;
    }

    public Integer getOrdinality() {
        return ordinality;
    }

    public void setOrdinality(Integer ordinality) {
        this.ordinality = ordinality;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getValueMin() {
        return valueMin;
    }

    public void setValueMin(String valueMin) {
        this.valueMin = valueMin;
    }

    public String getValueMax() {
        return valueMax;
    }

    public void setValueMax(String valueMax) {
        this.valueMax = valueMax;
    }

    public String getValue() {
        return (valueMin == null || valueMax == null || valueMin.equals(valueMax)) ? valueMin : valueMin + "~" + valueMax;
    }

    public void setValue(String value) {
        valueMin = value;
        valueMax = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOntologyId() {
        return ontologyId;
    }

    public void setOntologyId(String ontologyId) {
        this.ontologyId = ontologyId;
    }

    public String getApplicationMethod() {
        return applicationMethod;
    }

    public void setApplicationMethod(String applicationMethod) {
        this.applicationMethod = applicationMethod;
    }

    public int getGeneExpressionRecordId() {
        return geneExpressionRecordId;
    }

    public void setGeneExpressionRecordId(int geneExpressionRecordId) {
        this.geneExpressionRecordId = geneExpressionRecordId;
    }

    public String getConditionDescription() {
        return conditionDescription;
    }

    public void setConditionDescription(String conditionDescription) {
        this.conditionDescription = conditionDescription;
    }

    // if 'conditionDescription' is null, generate it and return it
    public String getConditionDescription2() {
        if( conditionDescription==null ) {
            conditionDescription = generateConditionDescription();
        }
        return conditionDescription;
    }

    public String generateConditionDescription() throws Exception {
        OntologyXDAO xdao = new OntologyXDAO();

        String desc = xdao.getTerm(getOntologyId()).getTerm();

        if (getValue() != null) {
            desc += " (" + getValue() + " " + getUnits() + ") ";
        }

        DecimalFormat df = new DecimalFormat("###.#");

        if (getDurationLowerBound() > 0 || getDurationUpperBound() > 0) {
            if (getDurationLowerBound() == getDurationUpperBound()) {

                if ((getDurationLowerBound() / 86400) < 1) {
                    desc += " (for " + df.format(getDurationLowerBound() / 3600) + " hours)";
                } else {
                    desc += " (for " + df.format(getDurationLowerBound() / 86400) + " days)";
                }
            } else {
                if ((getDurationLowerBound() / 86400) < 1 || (getDurationUpperBound() / 86400) < 1) {
                    desc += " (between " + df.format(getDurationLowerBound() / 3600) + " and " + df.format(getDurationUpperBound() / 3600) + " hours)";
                } else {
                    desc += " (between " + df.format(getDurationLowerBound() / 86400) + " and " + df.format(getDurationUpperBound() / 86400) + " days)";
                }
            }
        }
        return desc;
    }

    // String array for special values of DurationBounds
    // Get the string with SPECIAL_VALUE_STRINGS[- value]
    static final String[] SPECIAL_VALUE_STRINGS = {"", "exhaustion", "death", "end of the experiment"};

    static public String convertDurationBoundToString(double value_number) {
        if (value_number > 0) {
            DecimalFormat d_f = new DecimalFormat("0.###");
            return d_f.format(value_number);
        }

        // Return string of special value
        try {
            return SPECIAL_VALUE_STRINGS[- (int)value_number];
        } catch (Exception e) {
            return "undefined value";
        }
    }

    static public double convertStringToDurationBound(String value_str) {
        for (int i = 0; i < SPECIAL_VALUE_STRINGS.length; i++) {
            if (value_str.equals(SPECIAL_VALUE_STRINGS[i])) return (long) -i;
        }
        return 0l;
    }
}
