package astar;

import javax.xml.soap.Node;
import java.util.Objects;

/**
 * @author: Jimu Yang
 * @date: 2019/1/6 5:49 PM
 * @descricption: want more.
 */
public class NodeScore implements Comparable<NodeScore> {

    /**
     * 本点
     */
    private Coord here;

    /**
     * 从起点到达这里的花费（已知路径中的最小花费）G
     */
    private int arriveCost;

    /**
     * 上一个点
     */
    private NodeScore from;

    /**
     * 从这里到达终点的预计花费（估计）对应H
     */
    private int toTargetCost;

    /**
     * 被添加到open list的先后
     */
    private int idxInOpen;

    /**
     * 总花费 对应F
     */
    public int getCost() {
        return arriveCost + toTargetCost;
    }

    public NodeScore() {
    }

    public NodeScore(Coord here, int arriveCost, NodeScore from, int toTargetCost, int idxInOpen) {
        this.here = here;
        this.arriveCost = arriveCost;
        this.from = from;
        this.toTargetCost = toTargetCost;
        this.idxInOpen = idxInOpen;
    }
    public NodeScore(Coord here) {
        this.here = here;
    }

    public NodeScore(Coord here, int arriveCost, NodeScore from, int toTargetCost) {
        this(here, arriveCost, from, toTargetCost, 0);
    }

    public NodeScore getFrom() {
        return from;
    }

    public void setFrom(NodeScore from) {
        this.from = from;
    }

    public Coord getHere() {
        return here;
    }

    public void setHere(Coord here) {
        this.here = here;
    }

    public int getArriveCost() {
        return arriveCost;
    }

    public void setArriveCost(int arriveCost) {
        this.arriveCost = arriveCost;
    }


    public int getToTargetCost() {
        return toTargetCost;
    }

    public void setToTargetCost(int toTargetCost) {
        this.toTargetCost = toTargetCost;
    }

    public int getIdxInOpen() {
        return idxInOpen;
    }

    public void setIdxInOpen(int idxInOpen) {
        this.idxInOpen = idxInOpen;
    }

    public int compareTo(NodeScore o) {
        // cost相同时 后进入open list的先出 idx越大越好
        return this.getCost() == o.getCost() ? (o.getIdxInOpen() - this.getIdxInOpen()) : (this.getCost() - o.getCost());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeScore nodeScore = (NodeScore) o;
        return Objects.equals(here, nodeScore.here);
    }

    @Override
    public int hashCode() {
        return Objects.hash(here);
    }
}
