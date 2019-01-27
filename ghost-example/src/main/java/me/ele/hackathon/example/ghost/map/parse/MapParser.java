package me.ele.hackathon.example.ghost.map.parse;

import me.ele.hackathon.pacman.ds.Coordinate;
import me.ele.hackathon.pacman.ds.GameConfig;
import me.ele.hackathon.pacman.ds.GameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author: Yang Fan
 * @date: 2019/1/25
 * @desc: map parser
 */
public class MapParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapParser.class);

//    public static final String MAP_FILE_PATH = "/Users/muyi/learning/hackathon2018-pacman/ghost-example/src/main/resources/map1.txt";
//
//    public static final String CONFIG_FILE_PATH = "/Users/muyi/learning/hackathon2018-pacman/ghost-example/src/main/resources/config";

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


    /**
     * 定义通度3，4为站点
     */
    private List<Coordinate> sites = new ArrayList<>();

    /**
     * 拐点 转角 通度为2且不在一个方向上
     */
    private List<Coordinate> corners = new ArrayList<>();

    /**
     * 定义通度0，1为死点
     */
    private List<Coordinate> deadEnds = new ArrayList<>();

    /**
     * 路径线段
     */
    private List<Segment> segments = new ArrayList<>();

    private GameMap mapCopy;


    /**
     * pixels [ width ] [ height ]
     */


    public List<Segment> findSegments() {
        int segLen = -1;
        Coordinate start = null;
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                int t = map.getPixels()[i][j];
                if (t != Plot.BARRIER) {
                    if (segLen == -1) {
                        // 新的端点
                        segLen = 0;
                        start = new Coordinate(i, j);
                    } else {
                        segLen++;
                    }
                } else {
                    if (segLen > 0) {
                        // 找到了线段
                        segments.add(new Segment(start, new Coordinate(i, j - 1)));
                    }
                    segLen = -1;
                    start = null;
                }
            }
        }
        return this.segments;
    }


    public void parseCoordinates() {
        int height = map.getHeight(); // 每列有多少元素
        int width = map.getWidth(); // 有多少列

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Coordinate coord = new Coordinate(i, j);
                switch (judgeCoord(coord, map)) {
                    case Plot.SITE:
                        this.sites.add(coord);
                        break;
                    case Plot.CORNER:
                        this.corners.add(coord);
                        break;
                    case Plot.DEADEND:
                        this.deadEnds.add(coord);
                        break;
                    default:
                }
            }
        }
    }


    public void showCoordinates() {
        this.sites.forEach(site -> {
            mapCopy.getPixels()[site.getX()][site.getY()] = 9;
        });

        this.deadEnds.forEach(site -> {
            mapCopy.getPixels()[site.getX()][site.getY()] = 8;
        });

        this.corners.forEach(site -> {
            mapCopy.getPixels()[site.getX()][site.getY()] = 7;
        });

        showMap(mapCopy, Arrays.asList(1, 7, 8, 9));
    }

    public void showSites() {
        this.sites.forEach(site -> {
            mapCopy.getPixels()[site.getX()][site.getY()] = 9;
        });
        showMap(mapCopy, Arrays.asList(1, 9));
    }

    public void showSegment(Segment segment) {
        Coordinate start = segment.getStart();
        Coordinate end = segment.getEnd();

        if (start.getX() < end.getX()) {
            for (int i = start.getX(); i <= end.getX(); i++) {
                this.mapCopy.getPixels()[i][start.getY()] = 9;
            }
        } else {
            for (int i = start.getY(); i <= end.getY(); i++) {
                this.mapCopy.getPixels()[start.getX()][i] = 9;
            }
        }
        showMap(mapCopy, Arrays.asList(1, 9));
    }

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


    public int judgeCoord(Coordinate coord, GameMap map) {
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

            this.map = GameMap.load(this.getClass().getResourceAsStream("/map1.txt"));
            this.mapCopy = GameMap.load(this.getClass().getResourceAsStream("/map1.txt"));

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("map/config file read failed.");
        }
    }


    public GameMap getMap() {
        return map;
    }
}
