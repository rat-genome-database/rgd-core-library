package edu.mcw.rgd.process;

import edu.mcw.rgd.datamodel.XDBIndex;
import edu.mcw.rgd.datamodel.ontology.Annotation;
import edu.mcw.rgd.reporting.HTMLTableReportStrategy;
import edu.mcw.rgd.reporting.Link;
import edu.mcw.rgd.reporting.Record;
import edu.mcw.rgd.reporting.Report;

import java.util.*;

public class AnnotationFormatter {
    public String buildTable(List<String> records, int columns) {
        int rowCount = (int)Math.ceil((double)(records.size() / columns)) + 1;
        StringBuilder table = new StringBuilder("<table class=\"annotationTable\" width='95%' border=0><tr>");

        for(int i = 0; i < records.size(); ++i) {
            String str = (String)records.get(i);
            if (i == 0) {
                table.append("<td valign='top'><table>");
            } else if (i % rowCount == 0) {
                table.append("</table></td><td valign='top'><table>");
            }

            table.append(str);
        }

        table.append("</table></td></tr></table>");
        return table.toString();
    }

    public String createGridFormatAnnotations(List<Annotation> annotationList, int objectId, int columns) throws Exception {
        List<String> records = new ArrayList();
        String evidence = "";
        String termAcc = "";
        String annotatedRgdId = "";
        String term = "";
        String annotUrl = null;
        Iterator var10 = annotationList.iterator();

        while(var10.hasNext()) {
            Annotation a = (Annotation)var10.next();
            if (annotUrl == null) {
                if (!a.getTermAcc().startsWith("CHEBI")) {
                    annotUrl = "/rgdweb/report/annotation/main.html";
                } else {
                    annotUrl = "/rgdweb/report/annotation/table.html";
                }
            }

            if (a.getTermAcc().equals(termAcc)) {
                if (!evidence.contains(a.getEvidence())) {
                    evidence = evidence + "," + a.getEvidence();
                }
            } else {
                if (!term.equals("")) {
                    records.add("<tr><td><img src='/rgdweb/common/images/bullet_green.png' /></td><td><a href=\"" + annotUrl + "?term=" + termAcc + "&id=" + annotatedRgdId + "\">" + term + " </a><span style=\"font-size:10px;\">&nbsp;(" + evidence + ")</span></td></tr>");
                }

                termAcc = a.getTermAcc();
                annotatedRgdId = "" + a.getAnnotatedObjectRgdId();
                term = a.getTerm();
                evidence = a.getEvidence();
            }
        }

        records.add("<tr><td><img src='/rgdweb/common/images/bullet_green.png' /></td><td><a href=\"" + annotUrl + "?term=" + termAcc + "&id=" + annotatedRgdId + "\">" + term + " </a><span style=\"font-size:10px;\">&nbsp;(" + evidence + ")</span></td></tr>");
        return this.buildTable(records, columns);
    }

    public String createGridFormatAnnotationsTable(List<Annotation> annotationList) throws Exception {
        return this.createGridFormatAnnotationsTable(annotationList, "RGD");
    }

    public String createGridFormatAnnotationsTable(List<Annotation> annotationList, String site) throws Exception {
        String annotUrl = null;
        Report report = new Report();
        Record rec = new Record();
        rec.append("Term");
        rec.append("Qualifier");
        rec.append("Evidence");
        rec.append("With");
        rec.append("Reference");
        rec.append("Notes");
        rec.append("Source");
        rec.append("Original Reference(s)");
        report.append(rec);

        for(Iterator var6 = annotationList.iterator(); var6.hasNext(); report.append(rec)) {
            Annotation a = (Annotation)var6.next();
            if (annotUrl == null) {
                if (!a.getTermAcc().startsWith("CHEBI")) {
                    annotUrl = "/rgdweb/report/annotation/main.html";
                } else {
                    annotUrl = "/rgdweb/report/annotation/table.html";
                }
            }

            rec = new Record();
            String termString = "<a href='" + annotUrl + "?term=" + a.getTermAcc() + "&id=" + a.getAnnotatedObjectRgdId() + "'>" + a.getTerm() + " </a>";
            rec.append(termString);
            if (a.getQualifier() == null) {
                rec.append("&nbsp;");
            } else {
                rec.append(a.getQualifier());
            }

            rec.append(a.getEvidence());
            if (a.getWithInfo() == null) {
                rec.append("&nbsp;");
            } else {
                int objectKey = a.getRgdObjectKey();
                if (Utils.stringsAreEqualIgnoreCase(a.getDataSrc(), "ClinVar")) {
                    if (a.getSpeciesTypeKey() == 1) {
                        objectKey = 7;
                    } else if (a.getSpeciesTypeKey() != 2 && a.getSpeciesTypeKey() != 3) {
                        objectKey = 0;
                    } else {
                        objectKey = 1;
                    }
                }

                String val = this.getLinkForWithInfo(a.getWithInfo(), objectKey);
                if (!site.equals("RGD")) {
                    val = val.replaceAll("RGD", site);
                }

                rec.append(val);
            }

            String var10001;
            if (a.getRefRgdId() != null && a.getRefRgdId() > 0) {
                var10001 = Link.ref(a.getRefRgdId());
                rec.append("<a href='" + var10001 + "' title='show reference'>" + a.getRefRgdId() + "</a>");
            } else {
                rec.append("&nbsp;");
            }

            if (a.getNotes() == null) {
                rec.append("&nbsp;");
            } else {
                int pos = a.getNotes().indexOf("; ");
                String notes;
                if (pos > 0) {
                    notes = a.getNotes().substring(0, pos);
                    notes = notes + "; " + makeGeneTermAnnotLink(a.getAnnotatedObjectRgdId(), a.getTermAcc(), "pmore");
                } else {
                    notes = a.getNotes();
                }

                rec.append(notes);
            }

            if (!site.equals("RGD")) {
                rec.append(a.getDataSrc().replaceAll("RGD", site));
            } else {
                rec.append(a.getDataSrc());
            }

            if (a.getXrefSource() == null) {
                rec.append("&nbsp;");
            } else {
                String[] refs = a.getXrefSource().split("\\|");
                if (refs.length == 1) {
                    rec.append(makeRefLink(refs[0]));
                } else if (refs.length == 2) {
                    var10001 = makeRefLink(refs[0]);
                    rec.append(var10001 + " " + makeRefLink(refs[1]));
                } else {
                    var10001 = makeRefLink(refs[0]);
                    rec.append(var10001 + makeGeneTermAnnotLink(a.getAnnotatedObjectRgdId(), a.getTermAcc(), "pmore"));
                }
            }
        }

        return (new HTMLTableReportStrategy()).format(report);
    }

    String getLinkForWithInfo(String withInfo, int objectKey) throws Exception {
        try {
            if (!withInfo.contains("|")) {
                String var10000 = this.getLinkForWithInfoEx(withInfo, objectKey);
                return "<a href='" + var10000 + "'>" + withInfo + "</a>";
            } else {
                String[] multipleInfos = withInfo.split("\\|");
                String withInfoField = "";
                String[] var5 = multipleInfos;
                int var6 = multipleInfos.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    String info = var5[var7];
                    withInfoField = withInfoField + "<a href='" + this.getLinkForWithInfoEx(info, objectKey) + "'>" + info + "</a> ";
                }

                return withInfoField;
            }
        } catch (Exception var9) {
            return withInfo;
        }
    }

    String getLinkForWithInfoEx(String withInfo, int objectKey) throws Exception {
        if (withInfo.startsWith("RGD:")) {
            return objectKey != 0 ? Link.it(Integer.parseInt(withInfo.substring(4)), objectKey) : Link.it(Integer.parseInt(withInfo.substring(4)));
        } else {
            return Link.it(withInfo);
        }
    }

    public String createGridFormatAnnotatedObjects(List<Annotation> annotationList, int columns) throws Exception {
        List<String> records = new ArrayList();
        Iterator var4 = annotationList.iterator();

        while(var4.hasNext()) {
            Annotation a = (Annotation)var4.next();
            String objSymbol = Utils.NVL(a.getObjectSymbol(), "NA");
            String objName = Utils.NVL(a.getObjectName(), "NA");
            String var10001 = Link.it(a.getAnnotatedObjectRgdId(), a.getRgdObjectKey());
            records.add("<tr><td><img src='/rgdweb/common/images/bullet_green.png' /></td><td><a href=\"" + var10001 + "\" class='geneList" + a.getSpeciesTypeKey() + "'>" + objSymbol + " </a><span style=\"font-size:10px;\">&nbsp;(" + objName + ")</span></td></tr>");
        }

        return this.buildTable(records, columns);
    }

    public static String makeRefLink(String id) throws Exception {
        if (id.startsWith("PMID:")) {
            return XDBIndex.getInstance().getXDB(2).getALink(id.substring(5), id);
        } else if (id.startsWith("REF_RGD_ID:")) {
            String var10000 = Link.ref(Integer.parseInt(id.substring(11)));
            return "<a href=\"" + var10000 + "\">" + id + "</a>";
        } else {
            return id;
        }
    }

    static String makeGeneTermAnnotLink(int rgdId, String termAcc, String aclass) {
        String text = aclass.equals("imore") ? "&nbsp;&nbsp;&nbsp;" : "more ...";
        String str = " <a class=\"" + aclass + "\" href=\"/rgdweb/report/annotation/table.html?id=" + rgdId;
        str = str + "&term=" + termAcc + "\" title=\"see all interactions and original references for this gene and chemical\">" + text + "</a>";
        return str;
    }

    public List<Annotation> filterList(List<Annotation> annotationList, String aspect) {
        List<Annotation> returnList = new ArrayList();
        Iterator var4 = annotationList.iterator();

        while(var4.hasNext()) {
            Annotation annot = (Annotation)var4.next();
            if (annot.getAspect().equalsIgnoreCase(aspect)) {
                returnList.add(annot);
            }
        }

        Collections.sort(returnList, new Comparator<Annotation>() {
            public int compare(Annotation o1, Annotation o2) {
                return Utils.stringsCompareToIgnoreCase(o1.getTerm(), o2.getTerm());
            }
        });
        return returnList;
    }
}
