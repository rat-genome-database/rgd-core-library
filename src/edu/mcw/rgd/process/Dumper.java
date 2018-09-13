package edu.mcw.rgd.process;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mtutaj
 * @since 4/13/12
 * utility class to dump an object state to a variety of formats;
 * currently it supports dumping to a separator delimited formats
 */
public class Dumper {

    private String delimiter = "|";
    private boolean skipNulls = true; /// by default null fields won't be dumped
    private boolean skipZeros = false; /// if true int fields with 0 value won't be dumped

    private List<String> fields = new ArrayList<>();

    public Dumper() {
    }

    public Dumper(String delimiter) {
        setDelimiter(delimiter);
    }

    public Dumper(String delimiter, boolean skipNulls) {
        setDelimiter(delimiter);
        setSkipNulls(skipNulls);
    }

    public Dumper(String delimiter, boolean skipNulls, boolean skipZeros) {
        setDelimiter(delimiter);
        setSkipNulls(skipNulls);
        setSkipZeros(skipZeros);
    }

    /**
     * add a field-value pair to a dumper
     * @param field field
     * @param value value
     * @return reference to this object
     */
    public Dumper put(String field, String value) {
        if( value!=null || !isSkipNulls() )
            fields.add(field+":"+value);
        return this;
    }

    /**
     * add a field-value pair to a dumper
     * @param field field name
     * @param value int value
     * @return reference to this object
     */
    public Dumper put(String field, Integer value) {
        String val = null;
        if( value!=null ) {
            if( value!=0 || !isSkipZeros() ) {
                val = value.toString();
            }
        }
        return put(field, val);
    }

    /**
     * add a field-value pair to a dumper
     * @param field field name
     * @param value long int value
     * @return reference to this object
     */
    public Dumper put(String field, Long value) {
        String val = null;
        if( value!=null ) {
            if( value!=0L || !isSkipZeros() ) {
                val = value.toString();
            }
        }
        return put(field, val);
    }

    /**
     * add a field-value pair to a dumper
     * @param field field
     * @param value value
     * @return reference to this object
     */
    public Dumper put(String field, Double value) {
        return put(field, value==null?null:value.toString());
    }

    /**
     * add a field-value pair to a dumper
     * @param field field
     * @param value value
     * @return reference to this object
     */
    public Dumper put(String field, java.util.Date value) {
        if( value==null ) {
            put(field, (String)null);
        } else {
            // synchronized necessary in multi-thread environment
            synchronized(_sdt) {
                put(field, _sdt.format(value));
            }
        }
        return this;
    }
    static SimpleDateFormat _sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** dump all fields to a string
     *
     * @return string
     */
    public String dump() {

        return Utils.concatenate(fields, delimiter);
    }

    /**
     * get delimiter; if not set, the default is '|'
     * @return delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * determines if fields with null values will be dumped or not; default is true, what means
     * that only non-null fields will be dumped
     * @return true if fields with null values will be dumped
     */
    public boolean isSkipNulls() {
        return skipNulls;
    }

    public void setSkipNulls(boolean skipNulls) {
        this.skipNulls = skipNulls;
    }

    /**
     * determines if int fields with 0 value will be dumped or not; default is false, what means
     * that all int fields will be dumped, regardless whether their values are 0 or not
     * @return true if int fields with 0 value will be dumped
     */
    public boolean isSkipZeros() {
        return skipZeros;
    }

    public void setSkipZeros(boolean skipZeros) {
        this.skipZeros = skipZeros;
    }
}
