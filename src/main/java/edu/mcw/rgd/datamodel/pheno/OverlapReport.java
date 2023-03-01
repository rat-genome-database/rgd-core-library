package edu.mcw.rgd.datamodel.pheno;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: 6/4/15
 * Time: 9:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class OverlapReport {

    private Map recordCountMap;
    private Map studyMap;
    private Map experimentMap;

    public Map getRecordCountMap() {
        return recordCountMap;
    }

    public void setRecordCountMap(Map recordCountMap) {
        this.recordCountMap = recordCountMap;
    }

    public Map getStudyMap() {
        return studyMap;
    }

    public void setStudyMap(Map studyMap) {
        this.studyMap = studyMap;
    }

    public Map getExperimentMap() {
        return experimentMap;
    }

    public void setExperimentMap(Map experimentMap) {
        this.experimentMap = experimentMap;
    }
}
