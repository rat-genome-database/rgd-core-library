package edu.mcw.rgd.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class PositionConverter {

    public Connection conn = null;
    
    public PositionConverter()  {
    }


       public static void main(String[] args) throws Exception {
           PositionConverter pc = new PositionConverter();
           pc.open();

           //	IFI44L
          // System.out.println(pc.getAminoAcidPosition("1", 78858741, 78880657, 78880657));

           //	IFI44L CDS
           System.out.println(pc.getAminoAcidPosition("1", 78880048, 78880082, 78880082));


           //a2m System.out.println(pc.getAminoAcidPosition("12", 9111571, 9159825, 9159825));

          //System.out.println(pc.getAminoAcidPosition("1", 82038670, 82230695, 82230695));


           pc.close();
       }


       public void open() throws Exception{
           String driverName = "com.mysql.jdbc.Driver";
           Class.forName(driverName);
           conn = DriverManager.getConnection("jdbc:mysql://dillon.hmgc.mcw.edu:3306/human_362", "gbrowser", "nwgbr_06p");;
       }

       public void close() {
           try {
               conn.close();
           }catch (Exception ignored) {
               
           }
       }


       public long getAminoAcidPosition(String chr, long genomicStart, long genomicStop, long genomicPosition ) throws Exception {

           if (genomicPosition > genomicStop) {
               throw new Exception("Genomic position is > than gene stop position");
           }


           if (conn == null) {
               throw new Exception("Connection is not open.  You must call PositionConverter.open()");
           }

           Statement s = null;
           ResultSet rs = null;

           long count = 0;
           try {

               String sql = "select * from fdata where fstart >= " + genomicStart + " and fstart <= " + genomicPosition +
                       " and ftypeid = 16 and fref='Chr" + chr + "' order by fstart" ;

               System.out.println(sql);

               s = conn.createStatement();
               rs = s.executeQuery(sql);


               while (rs.next()) {
                   System.out.println("exon: " + rs.getString("fstart") + ".." + rs.getString("fstop") + " Len: " + (rs.getLong("fstop") - rs.getLong("fstart")));

                   if (rs.getLong("fstop") > genomicPosition) {
                       count = count + (genomicPosition - rs.getLong("fstart"));
                   }else {
                       count = count + (rs.getLong("fstop") - rs.getLong("fstart"));
                   }
               }

           } finally {

               try {
                   rs.close();
                   s.close();
               }catch (Exception ignored) {

               }

           }

           return count / 3;

       }

       protected void finalize() throws Throwable {


       }



}
