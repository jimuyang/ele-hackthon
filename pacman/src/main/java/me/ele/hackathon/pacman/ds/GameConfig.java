package me.ele.hackathon.pacman.ds;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lanjiangang on 2018/12/26.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameConfig {
    private Integer round;
    private Integer feastTime;
    private Integer pacDotPoint;
    private Integer powerPelletPoint;
    private Integer pacmanPoint;
    private Integer ghostPoint;
    private int timeoutInMs;

    public void loadFromFile(String path) throws IOException {
        this.loadFromInputStream(new FileInputStream(new File(path)));
    }

    public void loadFromInputStream(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.load(inputStream);

        this.round = Integer.valueOf(props.getProperty("round"));
        this.feastTime = Integer.valueOf(props.getProperty("feastTime"));
        this.pacDotPoint = Integer.valueOf(props.getProperty("pacDotPoint"));
        this.powerPelletPoint = Integer.valueOf(props.getProperty("powerPelletPoint"));
        this.pacmanPoint = Integer.valueOf(props.getProperty("pacmanPoint"));
        this.ghostPoint = Integer.valueOf(props.getProperty("ghostPoint"));
        this.timeoutInMs = Integer.valueOf(props.getProperty("timeoutInMs"));
    }

    public Integer round() {
        return round;
    }

    public Integer feastTime() {
        return feastTime;
    }

    public Integer pacDotPoint() {
        return pacDotPoint;
    }

    public Integer powerPelletPoint() {
        return powerPelletPoint;
    }

    public Integer pacmanPoint() {
        return pacmanPoint;
    }

    public Integer ghostPoint() {
        return ghostPoint;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Integer getFeastTime() {
        return feastTime;
    }

    public void setFeastTime(Integer feastTime) {
        this.feastTime = feastTime;
    }

    public Integer getPacDotPoint() {
        return pacDotPoint;
    }

    public void setPacDotPoint(Integer pacDotPoint) {
        this.pacDotPoint = pacDotPoint;
    }

    public Integer getPowerPelletPoint() {
        return powerPelletPoint;
    }

    public void setPowerPelletPoint(Integer powerPelletPoint) {
        this.powerPelletPoint = powerPelletPoint;
    }

    public Integer getPacmanPoint() {
        return pacmanPoint;
    }

    public void setPacmanPoint(Integer pacmanPoint) {
        this.pacmanPoint = pacmanPoint;
    }

    public Integer getGhostPoint() {
        return ghostPoint;
    }

    public void setGhostPoint(Integer ghostPoint) {
        this.ghostPoint = ghostPoint;
    }

    public int getTimeoutInMs() {
        return timeoutInMs;
    }

    @Override
    public String toString() {
        return "GameConfig{" +
                "round=" + round +
                ", feastTime=" + feastTime +
                ", pacDotPoint=" + pacDotPoint +
                ", powerPelletPoint=" + powerPelletPoint +
                ", pacmanPoint=" + pacmanPoint +
                ", ghostPoint=" + ghostPoint +
                '}';
    }
}
