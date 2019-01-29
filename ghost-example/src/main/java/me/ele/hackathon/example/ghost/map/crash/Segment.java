//package me.ele.hackathon.example.ghost.map.crash;
//
//import me.ele.hackathon.example.ghost.map.Direction;
//import me.ele.hackathon.example.ghost.map.IDirection;
//import me.ele.hackathon.pacman.ds.Coordinate;
//
///**
// * @author: Jimu Yang
// * @date: 2019/1/27 3:09 PM
// * @descricption: want more.
// * 有向线段 或者说：射线？
// */
//public class Segment {
//
//    private Coordinate start;
//
//    private Coordinate end;
//
//    private IDirection dir;
//
//    private int length;
//
//    private Coordinate[] coords;
//
//
//    public Segment(Coordinate start, Coordinate end) {
//        this.start = start;
//        this.end = end;
//        this.coords = new Coordinate[]{start, end};
//        if (start.getY() == end.getY() && start.getX() == end.getX()) {
//            this.dir = null;
//            this.length = 0;
//            return;
//        }
//
//        if (start.getX() == end.getX()) {
//            if (start.getY() > end.getY()) {
//                this.length = start.getY() - end.getY();
//                this.dir = Direction.DOWN;
//            } else {
//                this.length = end.getY() - start.getY();
//                this.dir = Direction.UP;
//            }
//
//        } else if (start.getY() == end.getY()) {
//            if (start.getX() > end.getX()) {
//                this.length = start.getX() - end.getX();
//                this.dir = Direction.LEFT;
//            } else {
//                this.length = end.getX() - start.getX();
//                this.dir = Direction.RIGHT;
//            }
//
//        } else {
//            throw new RuntimeException("invalid path");
//        }
//    }
//
//
//    /**
//     * reverse the path (direct update)
//     */
//    public Segment reverse() {
//        Coordinate temp = this.start;
//        this.start = this.end;
//        this.end = temp;
//        this.dir = this.dir.reverse();
//        return this;
//    }
//
//    public Coordinate getStart() {
//        return start;
//    }
//
//    public void setStart(Coordinate start) {
//        this.start = start;
//    }
//
//    public Coordinate getEnd() {
//        return end;
//    }
//
//    public void setEnd(Coordinate end) {
//        this.end = end;
//    }
//
//    public Coordinate[] getCoords() {
//        return coords;
//    }
//
//    public void setCoords(Coordinate[] coords) {
//        this.coords = coords;
//    }
//
//    public int getLength() {
//        return length;
//    }
//
//    @Override
//    public String toString() {
//        return start.toString() + "-->" + start.toString();
//    }
//
//}
