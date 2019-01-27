package me.ele.hackathon.example.ghost.map.parse;

import me.ele.hackathon.pacman.ds.Coordinate;

/**
 * @author: Jimu Yang
 * @date: 2019/1/27 3:09 PM
 * @descricption: want more.
 * 有向线段 或者说：射线？
 */
public class Segment {


    private Coordinate start;

    private Coordinate end;

    private int length;

    private Coordinate[] coords;


    public Segment(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
        this.coords = new Coordinate[]{start, end};

        if (start.getX() == end.getX()) {
            this.length = Math.abs(start.getY() - end.getY());
        } else if (start.getY() == end.getY()) {
            this.length = Math.abs(start.getX() - end.getX());
        } else {
            throw new RuntimeException("invalid segment");
        }

    }

    public Coordinate getStart() {
        return start;
    }

    public void setStart(Coordinate start) {
        this.start = start;
    }

    public Coordinate getEnd() {
        return end;
    }

    public void setEnd(Coordinate end) {
        this.end = end;
    }

    public Coordinate[] getCoords() {
        return coords;
    }

    public void setCoords(Coordinate[] coords) {
        this.coords = coords;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return start.toString() + "-->" + start.toString();
    }
}
