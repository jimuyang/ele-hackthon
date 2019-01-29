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

    private Coordinate[] accessibles;

    private int[] accessCost;

    public EndPoint(int x, int y) {
        super(x, y);
        this.connectors = new Segment[4];
        this.accessibles = new Coordinate[4];
        this.accessCost = new int[4];
    }

    public void add(Segment segment) {
        this.connectors[tongree] = segment;
        this.accessibles[tongree] = segment.another(this);
        this.accessCost[tongree] = segment.getLength();
        tongree++;
    }


    public int[] getAccessCost() {
        return accessCost;
    }

    public Coordinate[] getAccessibles() {
        return accessibles;
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
        String temp = String.format("%-20s", "EndPoint (" + x + ", " + y + ") ");

        StringBuilder stringBuilder = new StringBuilder(temp + "access:");
        for (int i = 0; i < tongree; i++) {
            stringBuilder.append("(").append(accessibles[i].getX()).append(",").append(accessibles[i].getY()).append(")");
        }
        return stringBuilder.toString();
//        return temp + "seg: " + Arrays.toString(connectors);
    }
}
