package edu.mcw.rgd.process;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Sep 11, 2009
 * Time: 1:20:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class PositionConverter {

    public static Connection conn = null;

       // Load the JDBC driver
       static {
           try {
               String driverName = "com.mysql.jdbc.Driver";
               Class.forName(driverName);



               conn = DriverManager.getConnection("jdbc:mysql://dillon.hmgc.mcw.edu:3306/human_362", "gbrowser", "nwgbr_06p");
           } catch (Exception e) {
               e.printStackTrace();
           }




       }

       public static void main(String[] args) throws Exception {       
            System.out.println(getAminoAcidPosition("1", 82038670, 82230695, 82159000));
       }


       public static long getAminoAcidPosition(String chr, long genomicStart, long genomicStop, long genomicPosition ) throws Exception {

           Statement s = null;
           ResultSet rs = null;

           long count = 0;
           try {

               String sql = "select * from fdata where fstart >= " + genomicStart + " and fstart <= " + genomicPosition +
                       " and ftypeid = 16 and fref='Chr" + chr + "'";

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

           return count;

       }


}
