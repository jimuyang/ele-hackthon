package me.ele.hackathon.example.ghost.map.parse;

import me.ele.hackathon.pacman.ds.Coordinate;
import org.junit.Test;

import java.util.List;

/**
 * @author: Yang Fan
 * @date: 2019/1/25
 * @desc:
 */
public class MapParserTest {

    private MapParser mapParser;

    @Test
    public void testLoadMapFile() {
        MapParser mapParse = new MapParser();
        mapParse.loadFromFile();
        this.mapParser = mapParse;
//        System.out.println(mapParse.getMap());
    }


    @Test
    public void testFindSites() {
       this.testLoadMapFile();
       List<Coordinate> sites = mapParser.findSites();
       print(sites.size());
//       sites.forEach(site -> print(site));
       mapParser.showSites();
    }



    public static void print(Object obj) {
        System.out.println(obj);
    }

}