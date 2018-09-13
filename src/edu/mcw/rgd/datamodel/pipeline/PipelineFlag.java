package edu.mcw.rgd.datamodel.pipeline;

/**
 * Created by IntelliJ IDEA. <br>
 * User: mtutaj <br>
 * Date: 9/12/11 <br>
 * Time: 2:21 PM
 * <p/>
 * a flag assigned by a QC module to a pipeline record
 */
public class PipelineFlag {

    private int id;
    private String symbol;
    private String description;
    private String pipeline;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPipeline() {
        return pipeline;
    }

    public void setPipeline(String pipeline) {
        this.pipeline = pipeline;
    }
}

    /*
create table pipeline_flags (
pipeline_flag_id number(10),
pipeline_flag_symbol varchar2(40),
pipeline_flag_description varchar2(1000),
pipeline_name varchar2(80)
);
    */