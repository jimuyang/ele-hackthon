package me.ele.hackathon.pacman.ds;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by lanjiangang on 2018/12/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameInfo {
    private GameConfig config;
    private GameMap map;

    public GameInfo() {
    }

    public GameInfo(GameConfig config, GameMap map) {
        this.config = config;
        this.map = map;
    }

    public GameConfig getConfig() {
        return config;
    }

    public void setConfig(GameConfig config) {
        this.config = config;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "GameInfo{" +
                "config=" + config +
                ", map=" + map +
                '}';
    }
}
