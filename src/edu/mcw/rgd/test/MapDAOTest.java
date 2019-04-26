package edu.mcw.rgd.test;

import edu.mcw.rgd.dao.impl.MapDAO;
import edu.mcw.rgd.dao.impl.TranscriptDAO;
import edu.mcw.rgd.datamodel.Map;
import edu.mcw.rgd.datamodel.MapData;
import junit.framework.TestCase;

import java.util.*;

/**
 * @author mtutaj
 * @since Sep 13, 2010
 */
public class MapDAOTest extends TestCase {

    MapDAO dao = new MapDAO();

    public MapDAOTest(String testName) {
        super(testName);
    }

    public void testAll() throws Exception {

        //TranscriptDAO tdao = new TranscriptDAO();
        //tdao.deleteTranscript(9478523, 11460505);

        testMapDAO();

        MapData md1 = new MapData();
        md1.setMapKey(7);
        md1.setChromosome("34");
        md1.setRgdId(2004);
        md1.setStartPos(1);
        md1.setStopPos(2);
        int cnt = dao.insertMapData(md1);
        assert cnt != 0;

        md1.setSrcPipeline("TEST");
        cnt = dao.updateMapData(md1);
        assert cnt != 0;

        cnt = dao.deleteMapData(md1.getKey());
        assert cnt != 0;

        java.util.Map map = dao.getChromosomeSizes(360);
        cnt = map.size();
    }

    public void testMapDAO() throws Exception{

        // test primary reference assemblies
        List<Map> maps = dao.getPrimaryRefAssemblies();
        assert maps.size()==3;
        // there must be 3 entries, each for every species
        for( Map map: maps ) {
            Map primaryMap = dao.getPrimaryRefAssembly(map.getSpeciesTypeKey());
            assert primaryMap.getKey()==map.getKey();
            assert map.isPrimaryRefAssembly()==true;
        }
    }

    public void testGetMapData() throws Exception{

        ArrayList<Integer> rgdList = new ArrayList<Integer>();
        rgdList.add(61320);
        rgdList.add(61321);
        rgdList.add(61322);
        rgdList.add(61323);
        rgdList.add(61324);
        rgdList.add(61325);
        rgdList.add(61326);
        rgdList.add(61327);
        rgdList.add(61328);
        rgdList.add(61329);
        rgdList.add(61330);
        rgdList.add(61331);
        rgdList.add(61332);
        rgdList.add(61333);
        rgdList.add(61334);
        rgdList.add(61335);
        rgdList.add(61336);
        rgdList.add(61337);
        rgdList.add(61338);
        rgdList.add(61339);
        rgdList.add(61340);


        int mapKey = 60;
        for(int rgdId:rgdList){
        // test primary reference assemblies
            List<MapData> maps = dao.getMapData(rgdId,mapKey);

            for(int i=0; i<maps.size();i++){
                System.out.println("here:" + maps.get(i).getChromosome()+"\t"+maps.get(i).getMapsDataPositionMethodId()+
                        "\t"+maps.get(i).getStartPos()+"\t"+maps.get(i).getRgdId());
            }
        }

    }

    public void testUpdateMapData() throws Exception{

        // test primary reference assemblies
        int rgdId = 70998;
        int mapKey = 60;
        List<MapData> mds = dao.getMapData(rgdId, mapKey);
        for( MapData md: mds ) {
            // increment all positions by 1
            md.setStartPos(md.getStartPos()+1);
            md.setStopPos(md.getStopPos()+1);
        }
        dao.updateMapData(mds);

        for( MapData md: mds ) {
            // decrement all positions by 1
            md.setStartPos(md.getStartPos()-1);
            md.setStopPos(md.getStopPos()-1);
        }
        dao.updateMapData(mds);
    }

    /** test we have active maps in RGD
     *
     * @throws Exception
     */
    public void testGetActiveMaps() throws Exception {

        List<Map> maps = dao.getActiveMaps();
        assertTrue(!maps.isEmpty());
    }

    public void testGetMapDataByMapKeyChr() throws Exception{
        List<MapData> mapList = dao.getMapDataByMapKeyChr("12", 60);
        System.out.println("here!");
    }
 }
