package me.ele.hackathon.example.ghost.path;

import me.ele.hackathon.example.ghost.map.coord.Point;
import me.ele.hackathon.pacman.ds.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author: Jimu Yang
 * @date: 2019/1/30 2:48 PM
 * @descricption: want more.
 * 用于Point[][]的astar
 */
public class AStar {

    private Point[][] points;

    private int index = 0;
    private PriorityQueue<PathNode> openQueue = new PriorityQueue<>();
    private List<PathNode> closeList = new ArrayList<>();

    public void findPathBetweenEndPoints(Coordinate start, Coordinate end) {
        this.init();

        boolean succ = this.astarRun(start, end);
        if (succ) {
            this.showPath();
        } else {
            System.out.println("cannot find path");
        }
    }


    private boolean astarRun(Coordinate start, Coordinate end) {
        PathNode startNode = new PathNode(start, null, 0, 0);
        this.addToOpenQueue(startNode);

        while (true) {
            PathNode current = this.openQueue.poll();
            if (current == null)
                return false;
            this.closeList.add(current);

            if (current.getHere().equals(end))
                // find the path successfully
                return true;

            // 处理current可达的格子
            Point currentPoint = points[current.getHere().getX()][current.getHere().getY()];
//            Coordinate[] accessibles = currentPoint.getAccessibles();
//            for (Coordinate coord: accessibles) {
            for (int i = 0; i < currentPoint.getAccessNum(); i++) {
                Coordinate access = currentPoint.getAccessPoints()[i].getCoord();
                // exist in close list: ignore it
                if (this.findInCloseList(access) != null) {
                    continue;
                }

                PathNode newNode = new PathNode(access);
                newNode.setFrom(current);
                newNode.setArriveCost(current.getArriveCost() + currentPoint.getAccessPoints()[i].getCost());
                newNode.setTargetCost(access.mhdDistance(end));

                PathNode existInOpen = this.findInOpenQueue(access);
                if (existInOpen == null) {
                    this.addToOpenQueue(newNode);
                } else {
                    if (existInOpen.getArriveCost() > newNode.getArriveCost()) {
                        this.openQueue.remove(existInOpen);
                        this.addToOpenQueue(newNode);
                    }
                }
            }
        }
    }

    private void init() {
        this.index = 0;
        this.openQueue.clear();
        this.closeList.clear();
    }

    public PathNode buildPath() {
        PathNode node;
        for (node = this.closeList.get(this.closeList.size() - 1);
             node.getFrom() != null;
             node.getFrom().setNext(node), node = node.getFrom())
            ;
        return node;
    }

    private PathNode showPath() {
        PathNode node = this.closeList.get(this.closeList.size() - 1);
        StringBuilder sb = new StringBuilder(node.getHere().toString());
        while (node.getFrom() != null) {
            sb.append("<==").append(node.getFrom().getHere().toString());
            node = node.getFrom();
        }
        System.out.println(sb.toString());
        return node;
    }

    public void addToOpenQueue(PathNode pathNode) {
        pathNode.setIndex(index++);
        this.openQueue.add(pathNode);
    }

    public void addToCloseList(PathNode pathNode) {
        this.closeList.add(pathNode);
    }

    public PathNode findInOpenQueue(Coordinate coord) {
        for (PathNode pathNode : this.openQueue) {
            if (pathNode.getHere().equals(coord))
                return pathNode;
        }
        return null;
    }

    public PathNode findInCloseList(Coordinate coord) {
        for (PathNode pathNode : this.closeList) {
            if (pathNode.getHere().equals(coord))
                return pathNode;
        }
        return null;
    }

    public void setPoints(Point[][] points) {
        this.points = points;
    }
}
