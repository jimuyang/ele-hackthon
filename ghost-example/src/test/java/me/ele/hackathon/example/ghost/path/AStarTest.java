package me.ele.hackathon.example.ghost.path;

import me.ele.hackathon.example.ghost.map.coord.Point;
import me.ele.hackathon.example.ghost.map.parser.MapParser;
import me.ele.hackathon.pacman.ds.Coordinate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author: Jimu Yang
 * @date: 2019/1/30 5:24 PM
 * @descricption: want more.
 */
public class AStarTest {

//    private MapParser mapParser;

    private AStar aStar;

    @Before
    public void before() {
        MapParser mapParser = new MapParser();
        mapParser.loadFromFile();
        mapParser.parseMap();
        AStar aStar = new AStar();
        aStar.setPoints(mapParser.getPointMap());
        this.aStar = aStar;
    }


    @Test
    public void testAStar() {
        this.aStar.findPathBetweenEndPoints(new Coordinate(19, 1), new Coordinate(13, 25));
    }


}