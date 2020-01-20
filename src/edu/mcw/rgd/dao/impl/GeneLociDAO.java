package edu.mcw.rgd.dao.impl;

import edu.mcw.rgd.dao.AbstractDAO;
import edu.mcw.rgd.dao.spring.GeneLociQuery;
import edu.mcw.rgd.datamodel.GeneLoci;

import java.util.List;

/**
 * Created by jthota on 12/20/2019.
 */
public class GeneLociDAO extends AbstractDAO {
    public List<GeneLoci> getGeneLociByMapKeyAndChr(int mapKey, String chr) throws Exception {
        String sql="select * from gene_loci where map_key=? and chromosome=?";
        GeneLociQuery query=new GeneLociQuery(this.getDataSource(), sql);
         return execute(query, mapKey, chr);
       }
    public static void main(String[] args) throws Exception {
        GeneLociDAO dao= new GeneLociDAO();
        List<GeneLoci> loci=dao.getGeneLociByMapKeyAndChr(17, "21");
        System.out.println(loci.size());
    }
}
