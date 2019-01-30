package me.ele.hackathon.example.ghost.path;

import me.ele.hackathon.pacman.ds.Coordinate;

import java.util.Objects;

/**
 * @author: Jimu Yang
 * @date: 2019/1/30 2:23 PM
 * @descricption: want more.
 * <p>
 * 路径节点 astar用
 */
public class PathNode implements Comparable<PathNode> {

    private Coordinate here;
    private PathNode from; // 链表
    private PathNode next; // 双向链表 输出路径用

    private int arriveCost;
    private int targetCost;

    private int index;

    public PathNode() {
    }

    public PathNode(Coordinate here) {
        this.here = here;
    }

    public PathNode(Coordinate here, PathNode from, int arriveCost, int targetCost) {
        this.here = here;
        this.from = from;
        this.arriveCost = arriveCost;
        this.targetCost = targetCost;
    }

    public int getCost() {
        return this.arriveCost + this.targetCost;
    }

    public Coordinate getHere() {
        return here;
    }

    public void setHere(Coordinate here) {
        this.here = here;
    }

    public int getArriveCost() {
        return arriveCost;
    }

    public void setArriveCost(int arriveCost) {
        this.arriveCost = arriveCost;
    }

    public PathNode getFrom() {
        return from;
    }

    public void setFrom(PathNode from) {
        this.from = from;
    }

    public int getTargetCost() {
        return targetCost;
    }

    public void setTargetCost(int targetCost) {
        this.targetCost = targetCost;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int compareTo(PathNode o) {
        // cost相同时 后进入open list的先出 idx越大越好
        return this.getCost() == o.getCost() ? (o.index - this.index) : (this.getCost() - o.getCost());
    }

    public PathNode getNext() {
        return next;
    }

    public void setNext(PathNode next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathNode pathNode = (PathNode) o;
        return Objects.equals(here, pathNode.here);
    }

    @Override
    public int hashCode() {
        return Objects.hash(here);
    }

}

