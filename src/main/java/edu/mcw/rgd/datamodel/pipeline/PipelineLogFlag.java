package edu.mcw.rgd.datamodel.pipeline;

import java.util.Date;

/**
 * Created by IntelliJ IDEA. <br>
 * User: mtutaj <br>
 * Date: 9/12/11 <br>
 * Time: 2:26 PM <br>
 * <p/>
 *
 */
public class PipelineLogFlag {

    int pipelineLogKey;
    int pipelineFlagId;
    int pipelineLogRecNo;
    Date creationTime;

    public int getPipelineLogKey() {
        return pipelineLogKey;
    }

    public void setPipelineLogKey(int pipelineLogKey) {
        this.pipelineLogKey = pipelineLogKey;
    }

    public int getPipelineFlagId() {
        return pipelineFlagId;
    }

    public void setPipelineFlagId(int pipelineFlagId) {
        this.pipelineFlagId = pipelineFlagId;
    }

    public int getPipelineLogRecNo() {
        return pipelineLogRecNo;
    }

    public void setPipelineLogRecNo(int pipelineLogRecNo) {
        this.pipelineLogRecNo = pipelineLogRecNo;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
}
