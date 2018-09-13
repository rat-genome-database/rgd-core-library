package edu.mcw.rgd.xml;

import nu.xom.*;
import org.jaxen.XPath;
import org.jaxen.xom.XOMXPath;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Mar 2, 2010
 * Time: 8:54:47 AM
 *
 * analyze a stream of XML records using XOM streaming model: parse one record at a time;
 * (XomFactory is a XOM builder that allows conveniently to detect record boundaries,
 * then XOMXPath objects could be used to extract useful data using XPath queries);
 * if XML records are huge, it makes sense to parse immediate childs (subrecords) to speed up XPath queries
 *
 * usage:
 * - override XomAnalyzer adding code for record parsing in initRecord(), parseRecord(), parseSubrecord()
 * - call parse() method
 *
 * Example XML document:
 * <root>
 *   <record>      // initRecord()
 *     <subrecord-1>
 *     </subrecord-1>     // parseSubrecord() called here
 *     <subrecord-2>
 *     </subrecord-2>     // parseSubrecord() called here
 *     ...
 *     <subrecord-n>
 *     </subrecord-n>    // parseSubrecord() called here
 *   </record>    // parseRecord() called here
 *   <record>     // initRecord()
 *     <subrecord-1>
 *     </subrecord-1>     // parseSubrecord() called here
 *     <subrecord-2>
 *     </subrecord-2>     // parseSubrecord() called here
 *     ...
 *     <subrecord-n>
 *     </subrecord-n>    // parseSubrecord() called here
 *   </record>    // parseRecord() called here
 *   ...
 * </root-element>
 *
 * libraries required: xom-1.2.4.jar; jaxen-1.1.1.jar
 */
public class XomAnalyzer {

    // if true XML parser must be validating; false otherwise
    private boolean validate = true;

    // record depth: depth at which stream of records is found; default is 1 (root is at 0 depth)
    private int recordDepth = 1;


    /**
     * find out if the xml parser is validating
     * @return true if xml parser is validating
     */
    public boolean isValidate() {
        return validate;
    }

    /**
     * configure the xml parser to be validating or not validating
     * @param validate
     */
    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    /**
     * get record depth: depth at which stream of records is found; default is 1 (root is at 0 depth);
     * Example:<pre>
     *
     * 0: <miRGate>
     * 1:   <search id=" MI0000862">
     * 2:    <results>
     * 3:      <result id="12586224">
     * 4:        <method>Miranda</method>
     * 3:      </result>
     * 3:      <result id="12586225">
     * 4:        <method>Miranda</method>
     * 3:      </result>
     * </pre>
     * In the above example, 'result' element is at the depth of 3
     * @return record depth
     */
    public int getRecordDepth() {
        return recordDepth;
    }

    public void setRecordDepth(int recordDepth) {
        if( recordDepth<0 )
            throw new IllegalArgumentException("record depth must not be negative");
        this.recordDepth = recordDepth;
    }

    /**
     * parse an xml file identified by File object; if the file name ends with .gz,
     * it is assumed a GZip-ped XML file is to be used and XomAnalyzer will try to decompress it
     * @param file valid File object to .xml or .xml.gz file
     * @return xom Document object
     * @throws Exception
     */
    public Document parse(File file) throws Exception {

        // instantiate a XOM builder
        Builder builder = new Builder(this.validate, new XomFactory(this));
        // and pass path to our bulk xml file to the builder

        Document doc;
        // handle gzipped xml files
        if( file.getName().toLowerCase().endsWith(".gz") ) {

            // GZipInputStream internally buffers its input, but not output
            BufferedInputStream inStream = new BufferedInputStream(new GZIPInputStream(new FileInputStream(file)));
            doc = builder.build(inStream);
            inStream.close();
        }
        else
            doc = builder.build(file);

        // now XomFactory is in control of the flow ;-)
        return doc;
    }

    public Document parse(Reader reader) throws Exception {

        // instantiate a XOM builder
        Builder builder = new Builder(this.validate, new XomFactory(this));
        // and pass path to our bulk xml file to the builder
        Document doc = builder.build(reader);
        // now XomFactory is in control of the flow ;-)
        return doc;
    }

    // called by XOM builder when a new record is started; element name is available
    public void initRecord(String name) {

    }

    // called by XOM builder when a subrecord is constructed;
    // you can parse it using XOMXPath queries here
    // return null if you want to discard the current subrecord in the parent record, or return 'element' to include it
    public Element parseSubrecord(Element element) {
        return element;
    }

    // called by XOM builder when a record is constructed;
    // you can parse it using XOMXPath queries here
    // return null if you want to discard the current record in the document, or return 'element' to include it
    public Element parseRecord(Element element) {
        return element;
    }


    // xom node factory implementation
    class XomFactory extends NodeFactory {

        // current element depth:
        //  0:root element
        //  1:record element
        //  2:subrecord element
        // ...
        int depth = 0;

        // to callback parent methods when subrecord / record is ready
        XomAnalyzer parent;

        // shortcut object to signal that current element should be discarded from document being built
        Nodes nullNodeList = new Nodes();

        public XomFactory(XomAnalyzer parent) {
            this.parent = parent;
        }

        // overridden to keep track of current element depth
        public Element startMakingElement(String name, String namespace) {
            depth++;

            // new record
            if( depth==parent.getRecordDepth()+1 ) {
                parent.initRecord(name);
            }
            return super.startMakingElement(name, namespace);
        }

        // overridden to keep track of current element depth
        // and to process elements one-by-one
        public Nodes finishMakingElement(Element element) {
            depth--;

            if( depth==parent.getRecordDepth()+1) {

                Element el = parent.parseSubrecord(element);
                if( el == null ) {
                    // discard the current element (subrecord) from document being built
                    return nullNodeList;
                }
            }
            else if( depth==parent.getRecordDepth() ) {

                Element el = parent.parseRecord(element);
                if( el == null ) {
                    // discard the current element (record) from document being built
                    return nullNodeList;
                }
            }

            // all elements of level deeper than 2 are fully constructed, including root element
            return super.finishMakingElement(element);
        }
    }
}
