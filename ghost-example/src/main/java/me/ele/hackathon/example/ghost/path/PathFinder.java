package me.ele.hackathon.example.ghost.path;

import me.ele.hackathon.example.ghost.map.coord.Point;
import me.ele.hackathon.example.ghost.map.parser.MapParser;
import me.ele.hackathon.pacman.ds.Coordinate;

/**
 * @author: Jimu Yang
 * @date: 2019/1/30 12:32 AM
 * @descricption: want more.
 */
public class PathFinder {

    private MapParser mapParser;

    /**
     * 寻找从起点到终点的可选路径们
     *
     * @param start
     * @param end
     */
    public void findPaths(Coordinate start, Coordinate end) {
        Point startPoint = getPoints()[start.getX()][start.getY()];
        Point endPoint = getPoints()[end.getX()][end.getY()];

        // 起点、终点accessPoints相同(他们在同一段上)



        // 包围：从端点的所有通度方向前往端点
        if (endPoint.getLocated() != null) {
            // 终点是中途点 对每个端点做包围
            endPoint.getAccessPoints(); // 所在的线段两端点 这也是最后一段路径
        } else if (endPoint.getTongree() > 0) {
            // 这个点本身就是端点 包围
        } else {
            throw new RuntimeException("invalid end:" + end);
        }
    }


    private void findPathBetweenEndPoints(Point start, Point end) {

    }


    private Point[][] getPoints() {
        return this.mapParser.getPointMap();
    }

}
