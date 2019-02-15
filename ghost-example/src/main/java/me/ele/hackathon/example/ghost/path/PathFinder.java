package me.ele.hackathon.example.ghost.path;

import me.ele.hackathon.example.ghost.map.Direction;
import me.ele.hackathon.example.ghost.map.coord.AccessPoint;
import me.ele.hackathon.example.ghost.map.coord.Point;
import me.ele.hackathon.example.ghost.map.parser.MapParser;
import me.ele.hackathon.pacman.ds.Coordinate;

import java.util.*;

/**
 * @author: Jimu Yang
 * @date: 2019/1/30 12:32 AM
 * @descricption: want more.
 */
public class PathFinder {

    private MapParser mapParser;

    private AStar aStar;


    public void init(MapParser mapParser) {
        if (aStar == null) {
            this.aStar = new AStar();
        }
        this.mapParser = mapParser;
        aStar.setPoints(mapParser.getPointMap());
    }

    /**
     * 寻找从起点到终点的最短路径
     *
     * @param start
     * @param end
     * @return
     */
    public Path findShortestPath(Coordinate start, Coordinate end) {
        if (start.equals(end)) {
            // 起点终点是同一点
            return new Path(start, end);
        }
        Point startPoint = this.getPoints()[start.getX()][start.getY()];
        Point endPoint = this.getPoints()[end.getX()][end.getY()];

        if (sameAccessPoints(startPoint, endPoint)) {
            // 拥有相同的accessPoints说明是在同一段上


        }

        return null;
    }


    /**
     * 寻找从起点到终点的可选路径们
     *
     * @param start
     * @param end
     */
    public List<Path> findPaths(Coordinate start, Coordinate end) {

        Point startPoint = getPoints()[start.getX()][start.getY()];
        Point endPoint = getPoints()[end.getX()][end.getY()];

        // 起点、终点accessPoints相同(他们在同一段上) 试试directGo
        Path directPath = this.directGo(startPoint, endPoint);
        if (directPath != null)
            return Collections.singletonList(directPath);

        List<Path> allPath = new ArrayList<>();
        return allPath;

//        // 包围：从端点的所有通度方向前往端点
//        if (endPoint.getLocated() != null) {
//            // 终点是中途点 对每个端点做包围
//            endPoint.getAccessPoints(); // 所在的线段两端点 这也是最后一段路径
//        } else if (endPoint.getTongree() > 0) {
//            // 这个点本身就是端点 包围
//            allPath.addAll(this.besiegeEndPoint(startPoint, endPoint));
//        } else {
//            throw new RuntimeException("invalid end:" + end);
//        }
    }


    private void findPathBetweenEndPoints(Point start, Point end) {

    }


    private Point[][] getPoints() {
        return this.mapParser.getPointMap();
    }


    /**
     * end是一个端点时，从各个方向围攻
     * end是中途点时，对两个端点围攻
     */
    private List<Path> besiegeEndPoint(Point start, Point end) {
        List<Path> paths = new ArrayList<>();
        if (end.getLocated() != null) {
            return paths;
        }

        AccessPoint[] endAccPoints = end.getAccessPoints();
        for (AccessPoint accessPoint : endAccPoints) {
            if (accessPoint == null)
                continue;
            Path path = aStar.findPath(start, accessPoint.getCoord());
            if (path != null) {
                // 追加最后一段
                path.setEnd(end);
                path.setLength(path.getLength() + accessPoint.getCost());
                path.setArriveDir(Direction.get(accessPoint.getGo().another(end), end));

                PathNode tailNode = path.getTail();
                PathNode extraNode = new PathNode(end);
                extraNode.setFrom(tailNode);
                tailNode.setNext(extraNode);
                extraNode.setArriveCost(tailNode.getArriveCost() + accessPoint.getCost());
                path.setTail(extraNode);
                paths.add(path);
            }
        }
        return paths;
    }


    private static boolean sameAccessPoints(Point p1, Point p2) {
        if (p1.getAccessNum() != p2.getAccessNum()) {
            return false;
        }
        Arrays.sort(p1.getAccessPoints(), AccessPoint.comparator);
        Arrays.sort(p2.getAccessPoints(), AccessPoint.comparator);
        for (int i = 0; i < p1.getAccessNum(); i++) {
            if (!p1.getAccessPoints()[i].equals(p2.getAccessPoints()[i]))
                return false;
        }
        return true;
    }

    public Path directGo(Point start, Point end) {
        if (!sameAccessPoints(start, end)) {
            return null;
        }

        for (int i = 0; i < start.getAccessNum(); i++) {
            if (start.getAccessPoints()[i].equals(end.getAccessPoints()[i])
                    && start.getAccessPoints()[i].getCost() > end.getAccessPoints()[i].getCost()) {
                AccessPoint accessPoint = start.getAccessPoints()[i];
                // 前往这个accessPoint即可
                Path directPath = new Path();
                directPath.setStart(start);
                directPath.setEnd(end);
                directPath.setLength(accessPoint.getCost() - end.getAccessPoints()[i].getCost());
                directPath.setGoDir(Direction.get(start, accessPoint.getGo().another(start)));
                directPath.setArriveDir(accessPoint.getDir());

                PathNode startNode = new PathNode(start, null, 0);
                PathNode endNode = new PathNode(end, startNode, directPath.getLength());
                startNode.setNext(endNode);

                directPath.setHead(startNode);
                directPath.setTail(endNode);
                return directPath;
            }

        }
        return null;
    }


}
