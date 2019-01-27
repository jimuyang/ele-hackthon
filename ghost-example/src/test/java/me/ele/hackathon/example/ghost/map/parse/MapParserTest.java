package me.ele.hackathon.example.ghost.map.parse;

import me.ele.hackathon.pacman.ds.Coordinate;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author: Yang Fan
 * @date: 2019/1/25
 * @desc:
 */
public class MapParserTest {

    private MapParser mapParser;


    @Before
    public void before() {
        MapParser mapParse = new MapParser();
        mapParse.loadFromFile();
        this.mapParser = mapParse;
    }


//    @Test
//    public void testLoadMapFile() {
//        MapParser mapParse = new MapParser();
//        mapParse.loadFromFile();
//        this.mapParser = mapParse;
////        System.out.println(mapParse.getMap());
//    }


    @Test
    public void testFindSites() {
//        List<Coordinate> sites = mapParser.findSites();
//        print(sites.size());
////       sites.forEach(site -> print(site));
//        mapParser.showSites();
    }

    @Test
    public void testParseCoordinates() {
        mapParser.parseCoordinates();
        mapParser.showCoordinates();
    }


    @Test
    public void testFindSegments() {
        List<Segment> segments = mapParser.findSegments();
        segments.forEach(mapParser::showSegment);
    }


    public static void print(Object obj) {
        System.out.println(obj);
    }

}