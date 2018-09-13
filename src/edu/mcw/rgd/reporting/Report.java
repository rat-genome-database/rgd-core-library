package edu.mcw.rgd.reporting;


import edu.mcw.rgd.process.Utils;

import java.util.*;



/**
 * Report objects are "matrix objects that contain rows and columns.  Report objects are similar to
 * Result sets, but move all data into local memory.  For display, Report objects can be formatted by ReportStrategy objects
 *
 * Example usage.
 *
 * //first lets create a record.
 * Record record = new Record()
 * record.append("hello");
 * record.append("world");
 *
 * //next lets create a report
 * Report report = new Report();
 * report.append(record);
 *
 * //We will use the delimited format for this report
 * DelimitedReportStrategy format = new DelimitedReportStrategy();
 *
 * //Print out the record in csv format
 * System.out.println(format.format(report));
 *
 * --The output would be
 *      hello,world
 *
 */

public class Report {

    /**
     * A list of records contained in the report
     */
    public ArrayList records = new ArrayList();
    private Map sortMapping = new HashMap();

    public static final int AUTO_DETECT_SORT = 0; // reads sortMapping[col]; defaults to CHARACTER_SORT if no sort mapping
    public static final int ABSOLUTE_VALUE_NUMERIC_SORT = 1;
    public static final int CHARACTER_SORT = 2;
    public static final int NUMERIC_SORT = 3;
    public static final int CHROMOSOME_SORT = 4;

    public static final int ASCENDING_SORT = 1;
    public static final int DECENDING_SORT = 2;

    protected String header = "";
    protected String footer = "";

    /**
     * Appends a record to the end of the report
     * @param record
     */
    public void append (Record record) {
        records.add(record);
    }

    public void addSortMapping(int columnIndex, int sortType) {
        sortMapping.put(columnIndex, sortType);
    }

    /**
     * Inserts a record into the report at a specified index
     * @param index
     * @param record
     */
    public void insert(int index, Record record) {
        records.add(index,record);
    }

    /**
     * Returns an iterator over the records contained in the report
     * @return
     */
    public Iterator iterator() {
        return records.iterator();
    }

    /**
     * Formats the report with the passed in ReportStrategy object and returns the results as a String
     * @param format
     * @return
     * @throws Exception
     */
    public String format(ReportStrategy format) throws Exception{
        return format.format(this);
    }

    /**
     * Returns a column of the report as a List based on the passed in column index
     * @param columnIndex
     * @return
     */
    public List getColumn(int columnIndex) {
        ArrayList columns = new ArrayList();
        Iterator it = this.iterator();
        while (it.hasNext()) {
            Record rec = (Record) it.next();
            columns.add(rec.get(columnIndex));
        }
        return columns;
    }

    public void updateRecordValue(int recordIndex, int columnIndex, String value) {
         Record r = (Record) this.records.get(recordIndex);
         r.fields.remove(columnIndex);
         r.fields.add(columnIndex, value);
    }


    /**
     * Remove a column from this report object
     *
     * @param columnIndex - the column to remove
     */
    public void removeColumn(int columnIndex) {
        Iterator it = this.iterator();
        while (it.hasNext()) {
            Record rec = (Record) it.next();
            rec.remove(columnIndex);
        }
    }

    /**
     * Returns the report header.  By default this is the empty string.  To set a report header,
     * this class must be extended and the method overriden
     * @return
     */
    public String getHeader() {
        return header;
    }

    /**
     * Sets the report header.  The header value can be retrieved via the getHeader() method.
     *
     * @param header
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * Returns the report footer.  By default this is the empty string. To set a report footer,
     * this class must be extended and the method overridden
     * @return
     */
    public String getFooter() {
        return footer;
    }

    /**
     *
     * Sets the report footer.  The footer value can be retrieved via the getFooter() method.
     *
     * @param footer
     */
    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void sort(int columnIndex, int sortOrder, boolean skipHeader) {
        this.sort(columnIndex, Report.AUTO_DETECT_SORT, sortOrder, skipHeader);
    }

    public int getIndex(String header) {
        Record rec = (Record) this.records.get(0);
        Iterator it = rec.iterator();
        int count = 0;
        while (it.hasNext()) {
            String val = (String) it.next();
            if (val.equals(header)) {
                return count;
            }
            count++;
        }
        return -1;
    }

    public void sort(int columnIndex, int sortType, int sortOrder) {
        this.sort(columnIndex, sortType, sortOrder, false);   
    }
    /**
     * Sorts the reports internal buffer based on column, sortType, and order
     * <p>
     * This method sorts the internal buffer.
     * <p>
     * @param columnIndex the index of the column to sort by
     * @param sortType  0= auto detect, 1 = absolute value numeric, 2 = character, 3 = numeric
     * @param sortOrder 1 = ascending, 2 = descending
     */
    public void sort(int columnIndex, int sortType, int sortOrder, boolean skipHeader) {
        sort(columnIndex, sortType, sortOrder, skipHeader, -1);
    }

    /**
     * Sorts the reports internal buffer based on column, sortType, and order
     * <p>
     * This method sorts the internal buffer.
     * <p>
     * @param columnIndex the index of the column to sort by
     * @param sortType  0= auto detect, 1 = absolute value numeric, 2 = character, 3 = numeric
     * @param sortOrder 1= ascending, 2 = descending
     * @param columnIndex2 the index of the secondary column to sort by (always sorted alphabetically)
     */
    public void sort(int columnIndex, int sortType, int sortOrder, boolean skipHeader, final int columnIndex2) {

        if( sortType == AUTO_DETECT_SORT ) {
            Integer sort = (Integer) this.sortMapping.get(columnIndex);
            if (sort == null)
                sortType = Report.CHARACTER_SORT;
            else
                sortType = sort;
        }

        /**
         * Comparitor used for sort.  Implemented as an inner class since it is
         * specific to this sort method.
         */
        class ColumnComparitor implements Comparator{

            private int columnIndex = 0;
            private int sortType = 0;
            private int sortOrder = 0;

            public ColumnComparitor(int columnIndex, int sortType, int sortOrder) {
                this.columnIndex = columnIndex;
                this.sortType = sortType;
                this.sortOrder = sortOrder;
            }

            public int compare(Object o1, Object o2) {
                Record rec = (Record) o1;
                Record rec2 = (Record) o2;

                String val1 = rec.get(columnIndex);
                String val2 = rec2.get(columnIndex);

                if (this.sortType == Report.ABSOLUTE_VALUE_NUMERIC_SORT) {

                    if (val1 == null || val1.equals("")) {
                        val1 = Double.MAX_VALUE + "";
                    }

                    if (val2 == null || val2.equals("")) {
                        val2 = Double.MAX_VALUE + "";
                    }

                    if (Math.abs(Double.parseDouble(val1)) > Math.abs(Double.parseDouble(val2))) {
                        if (sortOrder == Report.ASCENDING_SORT) {
                            return 1;
                        }else {
                            return -1;
                        }
                    }else if (Math.abs(Double.parseDouble(val1)) < Math.abs(Double.parseDouble(val2))) {
                        if (sortOrder == Report.ASCENDING_SORT) {
                            return -1;
                        }else {
                            return 1;
                        }
                    } else {
                        return handleColumnIndex2(rec, rec2);
                    }
                }else if (this.sortType==Report.NUMERIC_SORT) {
                    if (val1 == null || val1.equals("")) {
                        val1 = Double.MAX_VALUE + "";
                    }

                    if (val2 == null || val2.equals("")) {
                        val2 = Double.MAX_VALUE + "";
                    }


                    if (Double.parseDouble(val1) > Double.parseDouble(val2)) {
                        if (sortOrder == Report.ASCENDING_SORT) {
                            return 1;
                        }else {
                            return -1;
                        }
                    }else if (Double.parseDouble(val1) < Double.parseDouble(val2)) {
                        if (sortOrder == Report.ASCENDING_SORT) {
                            return -1;
                        }else {
                            return 1;
                        }
                    } else {
                        return handleColumnIndex2(rec, rec2);
                    }
                }else if (this.sortType==Report.CHROMOSOME_SORT) {

                    if (val1 == null || val1.trim().equals("")) {
                        val1 = Integer.MAX_VALUE + "";
                    }

                    if (val2 == null || val2.trim().equals("")) {
                        val2 = Integer.MAX_VALUE + "";
                    }

                    if (val1.toLowerCase().equals("x")) {
                        val1 = "98";
                    }

                    if (val1.toLowerCase().equals("y")) {
                        val1 = "99";
                    }

                    if (val2.toLowerCase().equals("x")) {
                        val2 = "98";
                    }

                    if (val2.toLowerCase().equals("y")) {
                        val2 = "99";
                    }

                    String start1 = rec.get(columnIndex + 1);
                    String start2 = rec2.get(columnIndex + 1);                

                    if (start1 == null || start1.trim().equals("")) {
                        start1=Double.MAX_VALUE + "";
                    }

                    if (start2 == null || start2.trim().equals("")) {
                        start2=Double.MAX_VALUE + "";
                    }

                    try {
                    if (Double.parseDouble(val1) > Double.parseDouble(val2)) {
                        if (sortOrder == Report.ASCENDING_SORT) {
                            return 1;
                        }else {
                            return -1;
                        }
                    }else if (Double.parseDouble(val1) < Double.parseDouble(val2)) {
                        if (sortOrder == Report.ASCENDING_SORT) {
                            return -1;
                        }else {
                            return 1;
                        }
                    }else if (Double.parseDouble(start1) > Double.parseDouble(start2)) {
                        if (sortOrder == Report.ASCENDING_SORT) {
                            return 1;
                        }else {
                            return -1;
                        }
                    }else if (Double.parseDouble(start1) < Double.parseDouble(start2)) {
                        if (sortOrder == Report.ASCENDING_SORT) {
                            return -1;
                        }else {
                            return 1;
                        }
                    } else {
                        return handleColumnIndex2(rec, rec2);
                    }
                    } catch (Exception e) {
                        return handleColumnIndex2(rec, rec2);
                    }


                }else {
                    if (val1.toLowerCase().compareTo(val2.toLowerCase()) > 0) {
                        if (sortOrder == Report.ASCENDING_SORT) {
                            return 1;
                        }else {
                            return -1;
                        }
                    }else if (val1.toLowerCase().compareTo(val2.toLowerCase()) < 0) {
                        if (sortOrder == Report.ASCENDING_SORT) {
                            return -1;
                        }else {
                            return 1;
                        }
                    } else {
                        return handleColumnIndex2(rec, rec2);
                    }
                }
            }

            // sort by secondary column if required
            int handleColumnIndex2(Record rec, Record rec2) {
                if( columnIndex2>=0 ) {
                    String val1 = rec.get(columnIndex2);
                    String val2 = rec2.get(columnIndex2);
                    return Utils.stringsCompareToIgnoreCase(val1, val2);
                }
                return 0; // no sort by secondary column
            }
        }

        Record record = null;
        if (skipHeader) {
            record = (Record) records.get(0);
            records.remove(0);
        }

        Collections.sort(records,new ColumnComparitor(columnIndex, sortType, sortOrder));

        if (skipHeader) {
            records.add(0, record);
        }
    }
}
