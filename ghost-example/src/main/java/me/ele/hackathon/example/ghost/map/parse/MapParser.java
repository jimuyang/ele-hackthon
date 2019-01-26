package me.ele.hackathon.example.ghost.map.parse;

import me.ele.hackathon.pacman.ds.Coordinate;
import me.ele.hackathon.pacman.ds.GameConfig;
import me.ele.hackathon.pacman.ds.GameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Yang Fan
 * @date: 2019/1/25
 * @desc: map parser
 */
public class MapParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapParser.class);

    public static final String MAP_FILE_PATH = "/Users/muyi/learning/hackathon2018-pacman/ghost-example/src/main/resources/map1.txt";

    public static final String CONFIG_FILE_PATH = "/Users/muyi/learning/hackathon2018-pacman/ghost-example/src/main/resources/config";

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

    private GameMap mapCopy;

    /**
     * 站点
     */
    private List<Coordinate> sites = new ArrayList<>();


    public List<Coordinate> findSites() {
        int height = map.getHeight(); // 每列有多少元素
        int width = map.getWidth(); // 有多少列

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // 计算点的通度(四周有几个空白点)
                int tongree = this.countTongree(i, j, map);
                if (tongree > 2) {
                    // 认为这是一个site
                    sites.add(new Coordinate(i, j));
                }
            }
        }
        return this.sites;
    }

    public void showSites() {
        this.sites.forEach(site -> {
            mapCopy.getPixels()[site.getX()][site.getY()] = 9;
        });
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
