//package me.ele.hackathon.example.ghost.map.parse;
//
//
//import me.ele.hackathon.example.ghost.map.parse.path.PartPath;
//import me.ele.hackathon.example.ghost.map.parse.path.Segment;
//import me.ele.hackathon.pacman.ds.Coordinate;
//
///**
// * @author: Jimu Yang
// * @date: 2019/1/27 10:40 PM
// * @descricption: want more.
// * path由segment组成 是一个双向链表
// */
//public class Path {
//
//    private Coordinate start;
//
//    private Coordinate end;
//
//    private int totalLength;
//
//    private PartPath part;
//
//    private PartPath head;
//
//    private PartPath tail;
//
//
//    public Path(Segment segment) {
//        this.start = segment.getStart();
//        this.end = segment.getEnd();
//        this.totalLength = segment.getLength();
//        this.part = new PartPath(segment);
//        this.head = this.part;
//        this.tail = this.part;
//    }
//
//    public void append(Segment segment) {
//        PartPath partPath = new PartPath(segment);
//        if (this.end.equals(segment.getStart()) || this.end.equals(segment.getEnd())) {
//            if (this.end.equals(segment.getEnd())) {
//                segment = segment.reverse();
//            }
//            this.end = segment.getEnd();
//            this.totalLength += segment.getLength();
//
//            this.tail.setNext(partPath);
//            partPath.setBefore(this.tail);
//            this.tail = partPath;
//        } else if (this.start.equals(segment.getEnd()) || this.start.equals(segment.getStart())) {
//            if (this.start.equals(segment.getStart())) {
//                segment = segment.reverse();
//            }
//            this.start = segment.getStart();
//            this.totalLength += segment.getLength();
//
//            this.head.setBefore(partPath);
//            partPath.setNext(this.head);
//            this.head = partPath;
//        }
//        throw new RuntimeException("path append failed: " + segment.toString());
//    }
//
////    public void setNext(Segment next) {
////        if (this.end.equals(next.start)) {
////            this.next = next;
////        } else if (this.end.equals(next.end)) {
////            this.next = next.reverse();
////        } else {
////            throw new RuntimeException(this.toString() + " set next failed:" + next.toString());
////        }
////    }
//
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
//    public int getTotalLength() {
//        return totalLength;
//    }
//
//    public void setTotalLength(int totalLength) {
//        this.totalLength = totalLength;
//    }
//
//    public PartPath getPart() {
//        return part;
//    }
//
//    public void setPart(PartPath part) {
//        this.part = part;
//    }
//
//    public PartPath getHead() {
//        return head;
//    }
//
//    public void setHead(PartPath head) {
//        this.head = head;
//    }
//
//    public PartPath getTail() {
//        return tail;
//    }
//
//    public void setTail(PartPath tail) {
//        this.tail = tail;
//    }
//}
