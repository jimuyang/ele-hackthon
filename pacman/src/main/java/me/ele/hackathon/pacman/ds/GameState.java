package me.ele.hackathon.pacman.ds;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/**
 * Created by lanjiangang on 2018/12/26.
 */
@Data
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameState {
    //coordinates of left pac-dots
    private List<Coordinate> pacDots;
    //coordinates of left Power Pellets
    private List<Coordinate> powerPellets;
    //pacman's coordinate
    private Map<Integer, Coordinate> pacman;
    //ghosts' coordinates
    private Map<Integer, Coordinate> ghosts;
    //pacman's feast
    private Map<Integer, Boolean> pacmanFeast;

    public GameState(List<Coordinate> pacDots, List<Coordinate> powerPellets, Map<Integer, Coordinate> pacman, Map<Integer, Coordinate> ghosts, Map<Integer, Boolean> pacmanFeast) {
        this.pacDots = pacDots;
        this.powerPellets = powerPellets;
        this.pacman = pacman;
        this.ghosts = ghosts;
        this.pacmanFeast = pacmanFeast;
    }
}
