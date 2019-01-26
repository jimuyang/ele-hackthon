package me.ele.hackathon.pacman.engine;

import lombok.Builder;
import me.ele.hackathon.pacman.ds.Coordinate;
import me.ele.hackathon.pacman.ds.Direction;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lanjiangang on 2018/12/31.
 */
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerInput {
    private Map<Integer, Direction> dirs = new LinkedHashMap<>();
    private Map<Integer, Coordinate> bornPos = new LinkedHashMap<>();;

    public PlayerInput() {

    }

    public PlayerInput(Map<Integer, Direction> dirs) {
        this.dirs = dirs;
    }

    public PlayerInput(Map<Integer, Direction> dirs, Map<Integer, Coordinate> bornPos) {
        this.dirs = dirs;
        this.bornPos = bornPos;
    }

    public Map<Integer, Direction> getDirs() {
        return dirs;
    }

    public void setDirs(Map<Integer, Direction> dirs) {
        this.dirs = dirs;
    }

    public Map<Integer, Coordinate> getBornPos() {
        return bornPos;
    }

    public void setBornPos(Map<Integer, Coordinate> bornPos) {
        this.bornPos = bornPos;
    }
}
