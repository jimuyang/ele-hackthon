package me.ele.hackathon.example.ghost.map.parser;

import me.ele.hackathon.example.ghost.map.Direction;
import me.ele.hackathon.example.ghost.map.coord.AccessPoint;
import me.ele.hackathon.example.ghost.map.coord.Point;
import me.ele.hackathon.example.ghost.map.coord.Plot;
import me.ele.hackathon.example.ghost.path.Segment;
import me.ele.hackathon.pacman.ds.Coordinate;
import me.ele.hackathon.pacman.ds.GameConfig;
import me.ele.hackathon.pacman.ds.GameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Yang Fan
 * @date: 2019/1/25
 * @desc: map parser
 */
public class MapParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapParser.class);

    final String MAP_NAME = "/map1.txt";

    /**
     * 地图
     */
    private GameMap map;

    /**
     * 配置
     */
    private GameConfig config;


    /****************
     * 解析结果
     ****************/

    private Point[][] pointMap;

//    /**
//     * 定义通度3，4为站点
//     */
//    private List<Coordinate> sites = new ArrayList<>();
//
//    /**
//     * 拐点 转角 通度为2且不在一个方向上
//     */
//    private List<Coordinate> corners = new ArrayList<>();
//
//    /**
//     * 定义通度0，1为死点
//     */
//    private List<Coordinate> deadEnds = new ArrayList<>();

//    /**
//     * 路径线段
//     */
//    private List<Segment> segments = new ArrayList<>();

    private GameMap mapCopy;


    /**
     * pixels [ width ] [ height ]
     */

    public void parseMap() {
        Point[][] points = new Point[map.getWidth()][map.getHeight()];
        FillPass pass = new FillPass();

        pass.segLen = -1;
        pass.start = null;
        // y++ 方向遍历
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                fillEndPointMap(points, new Coordinate(i, j), pass);
            }
        }

        pass.segLen = -1;
        pass.start = null;
        // x++ 方向遍历
        for (int j = 0; j < map.getHeight(); j++) {
            for (int i = 0; i < map.getWidth(); i++) {
                fillEndPointMap(points, new Coordinate(i, j), pass);
            }
        }

        // 对EndPoint[][]进行延伸
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[0].length; j++) {
                extendEndPoint(points[i][j], points);
            }
        }

        this.pointMap = points;

        this.showEndPoints(points);
//        Arrays.stream(points).forEach(endPoints1 -> Arrays.stream(points).forEach(System.out::println));
    }

//    public void findPath2EndPoint(Coordinate start, Point endPoint) {
//
//
//    }


    private void showEndPoints(Point[][] points) {
        // show
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[0].length; j++) {
//                if (points[i][j] != null) {
//                    String str = String.format("%-30s", "located: " + points[i][j].getLocated());
//                    System.out.println(points[i][j].toString() + " " + str);
//                } else {
//                    System.out.println("WALL (" + i + ", " + j + ")");
//                }

//                if (points[i][j] != null && points[i][j].getTongree() != 2 && points[i][j].getTongree() != 0) {
                if (points[i][j] != null) {
                    Point site = points[i][j];

                    mapCopy.getPixels()[site.getX()][site.getY()] = 9;
                    for (int k = 0; k < site.getAccessNum(); k++) {
                        mapCopy.getPixels()[site.getAccessPoints()[k].getCoord().getX()][site.getAccessPoints()[k].getCoord().getY()] = 8;
                    }
                    showMap(mapCopy, Arrays.asList(1, 8, 9));

                    mapCopy.getPixels()[site.getX()][site.getY()] = 0;
                    for (int k = 0; k < site.getAccessNum(); k++) {
                        mapCopy.getPixels()[site.getAccessPoints()[k].getCoord().getX()][site.getAccessPoints()[k].getCoord().getY()] = 0;
                    }
                }
            }
        }
    }


    private void extendEndPoint(Point start, Point[][] points) {
        if (start == null)
            return;
        start.parseLocated();

        Coordinate temp;
        Coordinate accCoord;
        Point toPoint;
        Point fromPoint;
        int length;

//        int accessNum = 0;
//        // 解决located != null的延长
//        if (start.getLocated() != null) {
//            accessNum = 2;
//        }
//        // 解决corner site deadEnd的延长
//        if (start.getTongree() > 0) {
//            accessNum = start.getTongree();
//        }

        for (int i = 0; i < start.getAccessNum(); i++) {
            if (start.getLocated() != null) {
                // 解决中途点的延长
                accCoord = start.getAccessPoints()[1 - i].getCoord();
                fromPoint = points[accCoord.getX()][accCoord.getY()];
            } else {
                // 解决corner site deadEnd的延长
                fromPoint = start;
            }
            accCoord = start.getAccessPoints()[i].getCoord();
            toPoint = points[accCoord.getX()][accCoord.getY()];
            length = start.getAccessPoints()[i].getCost();
            if (toPoint == null) {
                throw new RuntimeException("accessible endpoint invalid");
            }

            int count = 0;
            while (toPoint.getTongree() == 2) {
                count++;
//                Segment[] connectors = toPoint.getConnectors();
                AccessPoint[] accessPoints = toPoint.getAccessPoints();
                if (accessPoints[0].getGo().have(fromPoint)) {
                    temp = accessPoints[1].getGo().another(toPoint);
                    fromPoint = toPoint;
                    toPoint = points[temp.getX()][temp.getY()];
                    length += accessPoints[1].getGo().getLength();
                } else {
                    temp = accessPoints[0].getGo().another(toPoint);
                    fromPoint = toPoint;
                    toPoint = points[temp.getX()][temp.getY()];
                    length += accessPoints[0].getGo().getLength();
                }

                if (count > 5) {
//                    throw new RuntimeException("wireless loop");
                    System.out.println("wireless loop");
                }
            }
            AccessPoint accessPoint = start.getAccessPoints()[i];
            accessPoint.setCoord(toPoint);
            accessPoint.setDir(Direction.get(fromPoint, toPoint));
            accessPoint.setCost(length);
//            start.getAccessibles()[i] = toPoint;
//            start.getAccessCost()[i] = length;
//            start.getAccessDir()[i] = Direction.get(fromPoint, toPoint);
        }
    }

    private class FillPass {
        int segLen;
        Coordinate start;
        Segment find;
    }

    private void fillEndPointMap(Point[][] points, Coordinate current, FillPass pass) {
        int type = judgeCoord(current);
        switch (type) {
            case Plot.DEADEND:
            case Plot.CORNER:
            case Plot.SITE:
                if (pass.segLen == -1) {
                    pass.segLen = 0;
                    pass.start = current;
                    pass.find = new Segment(current);
//                    points[current.getX()][current.getY()].add(pass.find);
                } else {
                    // find segment
//                    Segment find = new Segment(pass.start, current);
                    pass.find.add(current);
                    if (points[current.getX()][current.getY()] == null) {
                        points[current.getX()][current.getY()] = new Point(current.getX(), current.getY());
                    }
                    points[current.getX()][current.getY()].add(pass.find);

                    if (points[pass.start.getX()][pass.start.getY()] == null) {
                        points[pass.start.getX()][pass.start.getY()] = new Point(pass.start.getX(), pass.start.getY());
                    }
                    points[pass.start.getX()][pass.start.getY()].add(pass.find);
                    pass.segLen = 0;
                    pass.start = current;
                    pass.find = new Segment(current);
//                    points[current.getX()][current.getY()].add(pass.find);
                }
                break;
            case Plot.COMMEN:
                pass.segLen++;
                if (points[current.getX()][current.getY()] == null) {
                    points[current.getX()][current.getY()] = new Point(current.getX(), current.getY());
                }
                points[current.getX()][current.getY()].setLocated(pass.find);
                break;
            case Plot.BARRIER:
                pass.segLen = -1;
                pass.start = null;
                pass.find = null;
                break;
            default:
        }

    }


//    public List<Segment> findSegments() {
//        int segLen = -1;
//        Coordinate start = null;
//        for (int i = 0; i < map.getWidth(); i++) {
//            for (int j = 0; j < map.getHeight(); j++) {
//                int t = map.getPixels()[i][j];
//                if (t != Plot.BARRIER) {
//                    if (segLen == -1) {
//                        // 新的端点
//                        segLen = 0;
//                        start = new Coordinate(i, j);
//                    } else {
//                        segLen++;
//                    }
//                } else {
//                    if (segLen > 0) {
//                        // 找到了线段
//                        segments.add(new Segment(start, new Coordinate(i, j - 1)));
//                    }
//                    segLen = -1;
//                    start = null;
//                }
//            }
//        }
//        return this.segments;
//    }


//    public void parseCoordinates() {
//        int height = map.getHeight(); // 每列有多少元素
//        int width = map.getWidth(); // 有多少列
//
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                Coordinate coord = new Coordinate(i, j);
//                switch (judgeCoord(coord)) {
//                    case Plot.SITE:
//                        this.sites.add(coord);
//                        break;
//                    case Plot.CORNER:
//                        this.corners.add(coord);
//                        break;
//                    case Plot.DEADEND:
//                        this.deadEnds.add(coord);
//                        break;
//                    default:
//                }
//            }
//        }
//    }

//
//    public void showCoordinates() {
//        this.sites.forEach(site -> {
//            mapCopy.getPixels()[site.getX()][site.getY()] = 9;
//        });
//
//        this.deadEnds.forEach(site -> {
//            mapCopy.getPixels()[site.getX()][site.getY()] = 8;
//        });
//
//        this.corners.forEach(site -> {
//            mapCopy.getPixels()[site.getX()][site.getY()] = 7;
//        });
//
//        showMap(mapCopy, Arrays.asList(1, 7, 8, 9));
//    }
//
//    public void showSites() {
//        this.sites.forEach(site -> {
//            mapCopy.getPixels()[site.getX()][site.getY()] = 9;
//        });
//        showMap(mapCopy, Arrays.asList(1, 9));
//    }

//    public void showSegment(Segment segment) {
//        Coordinate start = segment.getStart();
//        Coordinate end = segment.getEnd();
//
//        if (start.getX() < end.getX()) {
//            for (int i = start.getX(); i <= end.getX(); i++) {
//                this.mapCopy.getPixels()[i][start.getY()] = 9;
//            }
//        } else {
//            for (int i = start.getY(); i <= end.getY(); i++) {
//                this.mapCopy.getPixels()[start.getX()][i] = 9;
//            }
//        }
//        showMap(mapCopy, Arrays.asList(1, 9));
//    }

    public static void showMap(GameMap map, List<Integer> shown) {
        // 加载地图时 每次加载一行 现在也每次print一行
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                if (shown.contains(map.getPixels()[i][j])) {
                    stringBuilder.append(map.getPixels()[i][j]);
                } else {
                    stringBuilder.append(" ");
                }
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }
        LOGGER.info("map is : \n{}", stringBuilder.toString());
    }

    public int countTongree(int x, int y, GameMap map) {
        int[][] pixels = map.getPixels();
        if (pixels[x][y] == Plot.BARRIER) {
            return 0;
        }
        int n = 0;
        n += x - 1 >= 0 && pixels[x - 1][y] != Plot.BARRIER ? 1 : 0;
        n += x + 1 < map.getWidth() && pixels[x + 1][y] != Plot.BARRIER ? 1 : 0;
        n += y - 1 >= 0 && pixels[x][y - 1] != Plot.BARRIER ? 1 : 0;
        n += y + 1 < map.getHeight() && pixels[x][y + 1] != Plot.BARRIER ? 1 : 0;
        return n;
    }


    public int judgeCoord(Coordinate coord) {
        GameMap map = this.map;
        int x = coord.getX(), y = coord.getY();
        int[][] pixels = map.getPixels();
        if (pixels[x][y] == Plot.BARRIER) {
            return Plot.BARRIER;
        }

        int left = x - 1 >= 0 && pixels[x - 1][y] != Plot.BARRIER ? 1 : 0;
        int right = x + 1 < map.getWidth() && pixels[x + 1][y] != Plot.BARRIER ? 1 : 0;
        int down = y - 1 >= 0 && pixels[x][y - 1] != Plot.BARRIER ? 1 : 0;
        int up = y + 1 < map.getHeight() && pixels[x][y + 1] != Plot.BARRIER ? 1 : 0;

        int n = left + right + up + down;

        if (n < 2) {
            return Plot.DEADEND;
        } else if (n > 2) {
            return Plot.SITE;
        } else {
            if (left + right == 2 || up + down == 2) {
                return Plot.COMMEN;
            } else {
                return Plot.CORNER;
            }
        }
    }


    public void loadFromFile() {
        try {
            GameConfig config = new GameConfig();
            config.loadFromInputStream(this.getClass().getResourceAsStream("/config"));
            this.config = config;

            this.map = GameMap.load(this.getClass().getResourceAsStream(MAP_NAME));
            this.mapCopy = GameMap.load(this.getClass().getResourceAsStream(MAP_NAME));

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("map/config file read failed.");
        }
    }


    public GameMap getMap() {
        return map;
    }

    public Point[][] getPointMap() {
        return pointMap;
    }
//    public int compare(int a, int b) {
////        if (a >= b) {
////            return a;
////        } else {
////            return b;
////        }
//
//        return a >= b ? a : b;
//    }
}
