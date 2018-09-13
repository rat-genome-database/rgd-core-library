package edu.mcw.rgd.datamodel.ontologyx;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: 4/14/15
 * Time: 1:37 PM
 * <p>
 * a custom edge in a DAG (directed acyclic graph) between two ontology terms;
 * custom edges (relations) are defined by RGD staff, currently only for RDO ontology, to be loaded
 * in addition to relations defined in external .obo files
 * <p>
 * corresponds to a row in ONT_DAG_CUSTOM table
 */
public class TermDagEdgeCustom extends TermDagEdge {

    private String operation;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
