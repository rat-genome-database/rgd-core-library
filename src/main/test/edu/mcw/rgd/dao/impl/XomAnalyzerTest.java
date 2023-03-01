package edu.mcw.rgd.test;

import junit.framework.*;
import edu.mcw.rgd.xml.*;
import nu.xom.*;
import org.jaxen.*;
import org.jaxen.xom.*;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Mar 4, 2010
 * Time: 11:02:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class XomAnalyzerTest extends TestCase {

    public XomAnalyzerTest(String testName) {
        super(testName);
    }

    public boolean runAll()
    {
        try {
            testXomTree();
            testStreaming();
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * this method of processing just loads entire XML source into XOM builder, and then
     * we can use XPath queries to extract useful information;
     * Note: XML source should be of small size
     */
    public void testXomTree() throws Exception {

        String xml = "<?xml version=\"1.0\" ?>" +
            "<eSearchResult> "+
            "  <Count>25854</Count>"+
            "  <RetMax>10</RetMax>"+
            "  <RetStart>0</RetStart>"+
            "  <QueryKey name=\"aaa\" >1</QueryKey>"+
            "</eSearchResult>";
        StringReader reader = new StringReader(xml);
        
        Builder builder = new Builder();
        Document doc = builder.build(reader);
        reader.close();

        // extract values of elements important to us (parameters to eFetch)
        XPath xpath = new XOMXPath("//Count");
        String recordCount = xpath.stringValueOf(doc);
        Assert.assertEquals("Count mismatch: ", recordCount, "25854");

        xpath = new XOMXPath("/eSearchResult/QueryKey");
        String queryKey = xpath.stringValueOf(doc);
        Assert.assertEquals("QueryKey mismatch: ", queryKey, "1");

        xpath = new XOMXPath("//QueryKey/@name");
        String name = xpath.stringValueOf(doc);
        Assert.assertEquals("QueryKey 'name' attribute mismatch: ", name, "aaa");

        xpath = new XOMXPath("/eSearchResult[contains(Count,'8')]/RetMax");
        String retMax = xpath.stringValueOf(doc);
        Assert.assertEquals("retmax mismatch: ", retMax, "10");
    }

    public void testStreaming() throws Exception {

        String xml = "<?xml version=\"1.0\" ?>" +
                "<List> "+
                "  <Id id=\"10\">"+
                "    <Field name=\"alias\">AAA</Field>" +
                "    <Field name=\"alias\">AAA</Field>" +
                "  </Id>"+
                "  <Id id=\"20\">"+
                "    <Field name=\"alias\">AAA</Field>" +
                "  </Id>"+
                "  <Id id=\"30\">"+
                "    <Field name=\"alias\">AAA</Field>" +
                "  </Id>"+
                "</List>";
        StringReader reader = new StringReader(xml);

        MyXomAnalyzer xom = new MyXomAnalyzer();
        Document doc = xom.parse(reader);
        Assert.assertEquals(xom.ids.size(), 3);
    }

    // example how to build a class to process huge XML stream of records
    class MyXomAnalyzer extends XomAnalyzer {

        // list of ids to be extracted
        public ArrayList<String> ids = new ArrayList<String>();

        public void initRecord(String name) {

            // just start a new record
            Assert.assertEquals("initRecord", name, "Id");
        }

        // entire record has been parsed
        public Element parseRecord(Element element) {

            Assert.assertEquals(element.getLocalName(), "Id");

            try {
            XOMXPath xpath = new XOMXPath("Field/@name='alias'");
            String alias = xpath.stringValueOf(element);
            Assert.assertEquals(alias, "true");

            ids.add(element.getAttributeValue("id"));
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return null; // discard the element from resulting document (a must if your XML stream is huge)
        }

        public Element parseSubrecord(Element element) {

            Assert.assertEquals(element.getLocalName(), "Field");

            String val = element.getValue();
            Assert.assertEquals(val, "AAA");
            return element;
        }
    }
}
