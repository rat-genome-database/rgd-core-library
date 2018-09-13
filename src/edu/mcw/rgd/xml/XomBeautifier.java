package edu.mcw.rgd.xml;

import nu.xom.*;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: mtutaj
 * Date: Mar 19, 2010
 * Time: 1:09:28 PM
 * converts an XML record into HTML code, so XML record could be more human-readable;
 * the current implementation only performs element indentation,
 * which is quite useful;
 * TODO: add coloring of tags, attributes and comments
 */
public class XomBeautifier {

    private StringBuilder _output = new StringBuilder();

    // return CSS code needed to be included at top of web page
    // note: not completed
    public String getCss() {
        return
            "<style type=\"text/css\">" +
            " span.bea_name { color:brown; } " +
            " span.bea_tag { color:blue; } " +
            " span.bea_text { color:black; } " +
            " span.bea_attr { color:green; } " +
            " span.bea_comment { color:darkgray; } " +
            "</style>";
    }


    // convert xml source in HTML source
    public String convert(String xml) throws Exception {

        // instantiate a XOM builder
        Builder builder = new Builder();

        // parse all elements
        StringReader reader = new StringReader(xml);
        Document doc = builder.build(reader);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Serializer ser = new Serializer(out);
        ser.setIndent(2);
        ser.setMaxLength(90);
        ser.setLineSeparator("\n");
        ser.write(doc);

        // fast and dirty replacement of HTML entities
        String buf = out.toString();
        buf = buf.replace("&", "&amp;");
        buf = buf.replace("<", "&lt;").replace(">", "&gt;");

        // initialize output buffer
        _output.setLength(0);
        _output.append("<pre class=\"bea\">");
        _output.append(buf);
        _output.append("</pre>");

        // return the results
        return _output.toString();
    }

}