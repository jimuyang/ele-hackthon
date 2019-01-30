package me.ele.hackathon.example.ghost.map.coord;

import me.ele.hackathon.example.ghost.map.Direction;
import me.ele.hackathon.example.ghost.path.Segment;
import me.ele.hackathon.pacman.ds.Coordinate;

import java.util.Objects;

/**
 * @author: Jimu Yang
 * @date: 2019/1/30 8:20 PM
 * @descricption: want more.
 */
public class AccessPoint implements Comparable<AccessPoint> {

    /**
     * 到达点
     */
    private Coordinate coord;

    /**
     * 到达总路程
     */
    private int cost;

    /**
     * 到达方向
     */
    private Direction dir;

    /**
     * 出发线段
     */
    private Segment go;

    public Segment getGo() {
        return go;
    }

    public void setGo(Segment go) {
        this.go = go;
    }

    public Coordinate getCoord() {
        return coord;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Direction getDir() {
        return dir;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }


    /**
     * 到达的点相同 到达方向相同 就认为到达相同
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessPoint that = (AccessPoint) o;
        return Objects.equals(coord, that.coord) &&
                dir == that.dir;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord, dir);
    }

    @Override
    public int compareTo(AccessPoint o) {
        if (!this.coord.equals(o.coord)) {
            return this.coord.compareTo(o.coord);
        }
        return this.dir.ordinal() - o.dir.ordinal();
    }
}
