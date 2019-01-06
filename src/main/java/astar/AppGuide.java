package astar;

import sun.plugin.dom.core.CoreConstants;

import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * @author: Jimu Yang
 * @date: 2019/1/6 5:23 PM
 * @descricption: A*算法的guide和demo
 */
public class AppGuide {

    /**
     * 这里简化为上下左右四个方向 所以一次移动的花费是1
     *
     * A*: F = G + H
     * G: 从起点沿着产生的路径，移动到网格上指定方格的移动耗费
     * H: 从指定点到达终点的预计花费
     */

    /**
     * 这个demo中使用的游戏区域 其中0代表空地 1代表墙体
     * 在这幅简图中 *:不可穿越（边界或者墙体） S:起始位置 E:目标位置
     * * * * * * * * * *
     * *               *
     * *       *       *
     * *   S   *   E   *
     * *       *       *
     * *               *
     * * * * * * * * * *
     */
    private int[][] area = {
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
    };
    private int areaXLength = 7;
    private int areaYLength = 5;

    private Coord start = new Coord(1, 2);

    private Coord target = new Coord(5, 2);

    /**
     * open list 应该使用优先级队列 但当权重相同时 应该后进先出
     */
    private PriorityQueue<NodeScore> openQueue = new PriorityQueue<NodeScore>();
    private int indexInOpen = 0;

    /**
     * close list
     */
    private List<NodeScore> closeList = new ArrayList<NodeScore>();


    /**
     * now we start
     */
    public void run() {
        /**
         * 把start添加到open list
         */
        NodeScore startNode = new NodeScore(start, 0, null, 0);
        this.addToOpenList(startNode);

        /**
         * 寻路
         */
        boolean findPathSucc = this.findPath();

        /**
         * 保存路径。从目标格开始，沿着每一格的父节点移动直到回到起始格。这就是你的路径。
         */
        if (findPathSucc) {
            NodeScore node = this.closeList.get(this.closeList.size() - 1);
            StringBuilder sb = new StringBuilder(node.getHere().toString());
            while (node.getFrom() != null) {
                sb.append("<==").append(node.getFrom().getHere().toString());
                node = node.getFrom();
            }
            System.out.println(sb.toString());
        } else {
            System.out.println("没能找到路");
        }
    }

    public static void main(String[] args) {
        new AppGuide().run();
    }

    private boolean findPath() {
        /**
         * 重复如下的工作：
         *       a) 寻找开启列表中F值最低的格子。我们称它为当前格。
         *       b) 把它切换到关闭列表。
         *       c) 对相邻的8格中的每一个？
         *           * 如果它不可通过或者已经在关闭列表中，略过它。反之如下。
         *           * 如果它不在开启列表中，把它添加进去。把当前格作为这一格的父节点。记录这一格的F,G,和H值。
         *           * 如果它已经在开启列表中，用G值为参考检查新的路径是否更好。更低的G值意味着更好的路径。
         *           如果是这样，就把这一格的父节点改成当前格，并且重新计算这一格的G和F值。如果你保持你的开启列表按F值排序，改变之后你可能需要重新对开启列表排序。
         *       d) 停止，当你
         *           * 把目标格添加进了关闭列表(注解)，这时候路径被找到，或者
         *           * 没有找到目标格，开启列表已经空了。这时候，路径不存在。
         */
        while (true) {
            NodeScore currentNode = this.openQueue.poll();
            if (currentNode == null) {
                return false;
            }
            this.closeList.add(currentNode);
            if (currentNode.getHere().equals(target)) {
                // find the path
                return true;
            }
            // 处理它可到达的格子
            List<Coord> accessibleCoords = this.getAccessibleCoords(currentNode.getHere());
            for (Coord coord : accessibleCoords) {
                NodeScore newNode = new NodeScore(coord);
                newNode.setFrom(currentNode);
                newNode.setArriveCost(currentNode.getArriveCost() + 1);
                newNode.setToTargetCost(this.calcManhattanDistance(coord, target));
                // 已经在关闭列表中 略过它
                if (this.inCloseList(newNode)) {
                    continue;
                }
                // 开启列表中有吗
                NodeScore nodeInOpen = this.findInOpenList(coord);
                if (nodeInOpen == null) {
                    this.addToOpenList(newNode);
                } else {
                    if (nodeInOpen.getArriveCost() > newNode.getArriveCost()) {
                        this.openQueue.remove(nodeInOpen);
                        this.addToOpenList(newNode);
                    }
                }
            }
        }
    }

    private void addToOpenList(NodeScore nodeScore) {
        nodeScore.setIdxInOpen(indexInOpen++);
        this.openQueue.add(nodeScore);
    }

    private NodeScore findInOpenList(Coord coord) {
        for (NodeScore nodeScore : this.openQueue) {
            if (nodeScore.getHere().equals(coord)) {
                return nodeScore;
            }
        }
        return null;
    }

    private boolean inCloseList(NodeScore nodeScore) {
        return this.closeList.contains(nodeScore);
    }

    private boolean inCloseList(Coord coord) {
        return this.closeList.contains(new NodeScore(coord));
    }

    private List<Coord> getAccessibleCoords(Coord coord) {
        Coord[] coords = new Coord[4];
        coords[0] = new Coord(coord.getX() - 1, coord.getY());
        coords[1] = new Coord(coord.getX() + 1, coord.getY());
        coords[2] = new Coord(coord.getX(), coord.getY() - 1);
        coords[3] = new Coord(coord.getX(), coord.getY() + 1);
//        Coord left = new Coord(coord.getX() - 1, coord.getY());
//        Coord right = new Coord(coord.getX() + 1, coord.getY());
//        Coord up = new Coord(coord.getX(), coord.getY() - 1);
//        Coord down = new Coord(coord.getX(), coord.getY() + 1);

        List<Coord> neighbours = new ArrayList<>(4);
        for (Coord maybe : coords) {
            if (whatAtCoord(maybe) == 0) {
                neighbours.add(maybe);
            }
        }
        return neighbours;
    }

    private boolean checkCoord(Coord coord) {
        // 0 <= x < 7 && 0 <= y < 5
        return coord.getX() > -1 && coord.getX() < areaXLength
                && coord.getY() > -1 && coord.getY() < areaYLength;

    }

    private int whatAtCoord(Coord coord) {
        return checkCoord(coord) ? area[coord.getY()][coord.getX()] : -1;
    }

    private int calcManhattanDistance(Coord from, Coord to) {
        return Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY());
    }


}

