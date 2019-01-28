package me.ele.hackathon.example.ghost.map.parse.coord;

import me.ele.hackathon.example.ghost.map.parse.path.Segment;
import me.ele.hackathon.pacman.ds.Coordinate;

import java.util.Arrays;


/**
 * @author: Jimu Yang
 * @date: 2019/1/28 2:46 PM
 * @descricption: want more.
 * 端点：包括 站点site 拐点corner 死点deadEnd
 */
public class EndPoint extends Coordinate {

    /**
     * 与点相连的线段
     */
    private Segment[] connectors;

    private int tongree = 0;

    public EndPoint(int x, int y) {
        super(x, y);
        this.connectors = new Segment[4];
    }

    public void add(Segment segment) {
        this.connectors[tongree++] = segment;
    }

    public int getTongree() {
        return tongree;
    }

    public Segment[] getConnectors() {
        return connectors;
    }

    public void setConnectors(Segment[] connectors) {
        this.connectors = connectors;
    }

    @Override
    public String toString() {
        return "EndPoint{" +
                "connectors=" + Arrays.toString(connectors) +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
