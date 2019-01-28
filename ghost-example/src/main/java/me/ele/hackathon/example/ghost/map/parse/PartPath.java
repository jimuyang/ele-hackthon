package me.ele.hackathon.example.ghost.map.parse;

/**
 * @author: Jimu Yang
 * @date: 2019/1/28 11:39 AM
 * @descricption: want more.
 */
public class PartPath {

    private Segment part;

    private PartPath next;

    private PartPath before;

    public PartPath(Segment segment) {
        this.part = segment;
        this.next = null;
        this.before = null;
    }

    public Segment getPart() {
        return part;
    }

    public void setPart(Segment part) {
        this.part = part;
    }

    public PartPath getNext() {
        return next;
    }

    public void setNext(PartPath next) {
        this.next = next;
    }

    public PartPath getBefore() {
        return before;
    }

    public void setBefore(PartPath before) {
        this.before = before;
    }
}
