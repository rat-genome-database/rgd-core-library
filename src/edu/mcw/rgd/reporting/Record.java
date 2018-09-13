package edu.mcw.rgd.reporting;



import java.util.Iterator;
import java.util.ArrayList;



/**
 * Record objects are used to represent rows of data in Report objects.
 */
public class Record {
    ArrayList fields = new ArrayList();

    private int objectKey;

    /**
     * Appends a field to the end of the record
     * @param field field value
     * @return this object
     */
    public Record append (String field) {
        if (field == null) {
            field = "";
        }

        fields.add(field);
        return this;
    }

    /**
     * Inserts a field into the record at the specified index
     * @param index
     * @param field
     */
    public void insert(int index, String field) {
        if (field == null) {
            field = "";
        }

        fields.add(index, field);
    }
    /**
     * Returns an Iterator over the fields of the record
     * @return
     */
    public Iterator iterator() {
        return fields.iterator();
    }

    /**
     * Returns the field at the specified column index
     * @param columnIndex
     * @return
     */
    public String get(int columnIndex) {
        return (String) fields.get(columnIndex);
    }

    /**
     * Replace the field at the specified column index
     * @param columnIndex
     * @param field field value
     */
    public void set(int columnIndex, String field) {
        if (field == null) {
            field = "";
        }

        fields.set(columnIndex, field);
    }

    /**
     * Removes a field from the record.
     * 
     * @param columnIndex
     */
    public void remove(int columnIndex) {
        fields.remove(columnIndex);
    }

    public String toString() {
        String values = "";        
        for( Object field: fields ) {
            values += "\t" + field;
        }
        return values;
    }


    public int getObjectKey() {
        return objectKey;
    }

    /**
     * set object key for this row; could be used to speed up rendering
     * because if row renderer knows ahead the object key it won't have to make a costly call to database (Link.it)
     * @param objectKey
     */
    public void setObjectKey(int objectKey) {
        this.objectKey = objectKey;
    }
}
