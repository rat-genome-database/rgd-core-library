package edu.mcw.rgd.reporting;


public class RunReport {

public static void main (String[] args) throws Exception {


  Report report = new Report();

  Record record = new Record();
  record.append("hello");
  record.append("world");
 
  report.append(record);
  
  record = new Record();
  record.append("hello2");
  record.append("world2");
  
  report.append(record);


  System.out.println("\n\n ***********COMMA Delimited************\n");
  System.out.println(new DelimitedReportStrategy().format(report));
 
  System.out.println("\n\n ***********HTML**************\n");
  System.out.println(new HTMLTableReportStrategy().format(report));

  System.out.println("\n\n **********Fixed Width*********\n");
  System.out.println(new FixedWidthReportStrategy().format(report));


 
}
}