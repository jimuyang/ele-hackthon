package me.ele.hackathon.pacman;

import me.ele.hackathon.pacman.ds.GameConfig;
import me.ele.hackathon.pacman.engine.GameEngine;
import me.ele.hackathon.pacman.ds.GameMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AppTest {

    private GameEngine engine;
    private GameConfig config;

    private static final Logger logger = LoggerFactory.getLogger(AppTest.class);

    public static void main(String[] args) throws Exception {
        new AppTest().start(args);
    }

    private void start(String[] args) throws Exception {
        loadConfig("/Users/yujie/Documents/project/hackathon2018/pacman/src/test/resources/txt/test.txt");
        initGameEngine("/Users/yujie/Documents/project/hackathon2018/pacman/src/main/resources/map1.txt");
    }

    private void loadConfig(String path) throws IOException {
        config = new GameConfig();
        config.loadFromFile(path);
    }

    private void initGameEngine(String path) throws IOException {
        GameMap map = GameMap.load(new FileInputStream(new File(path)));
        engine = new GameEngine(map, config);
    }

}
