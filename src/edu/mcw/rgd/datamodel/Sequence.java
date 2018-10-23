package edu.mcw.rgd.datamodel;

import edu.mcw.rgd.process.Dumper;

import java.util.Date;

/**
 * @author mtutaj
 * @since July 12, 2018
 * new Sequence object - represents greatly simplified model compared to Sequence
 */
public class Sequence implements Identifiable, Dumpable {

    private int seqKey; // unique sequence key
    private int rgdId; // rgd id of object associated with the sequence
    private String seqType; // sequence type
    private Date createdDate; // date and time the sequence was created
    private String seqMD5; // MD5 value computed on sequence data
    private String seqData; // sequence data

    public int getSeqKey() {
        return seqKey;
    }

    public void setSeqKey(int seqKey) {
        this.seqKey = seqKey;
    }

    @Override
    public int getRgdId() {
        return rgdId;
    }

    @Override
    public void setRgdId(int rgdId) {
        this.rgdId = rgdId;
    }

    public String getSeqType() {
        return seqType;
    }

    public void setSeqType(String seqType) {
        this.seqType = seqType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getSeqMD5() {
        return seqMD5;
    }

    public void setSeqMD5(String seqMD5) {
        this.seqMD5 = seqMD5;
    }

    public String getSeqData() {
        return seqData;
    }

    public void setSeqData(String seqData) {
        this.seqData = seqData;
    }

    public String dump(String delimiter) {

        // we are skipping null fields
        return new Dumper(delimiter, true, true)
                .put("KEY", seqKey)
                .put("RGDID", rgdId)
                .put("TYPE", seqType)
                .put("CREATED", createdDate)
                .put("MD5", seqMD5)
                .put("SEQ", seqData)
                .dump();
    }
}
