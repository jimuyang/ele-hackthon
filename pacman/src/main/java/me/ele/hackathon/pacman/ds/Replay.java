package me.ele.hackathon.pacman.ds;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Replay implements Serializable {

    private static final long serialVersionUID = 1L;
    // 移动后pacman当前位置
    private Map<String, Coordinate> pacMan;
    // 移动后ghosts当前位置
    private Map<String, Coordinate> ghosts;
    // pacman移动方向
    private Map<String, Direction> pacManDirs;
    // ghosts移动方向
    private Map<String, Direction> ghostsDirs;
    // 豆子当前数量与位置
    private List<Coordinate> pacDots;
    // 大力丸当前数量与位置
    private List<Coordinate> powerPellets;
    // pacman上一轮吃掉的位置
    private Map<String, Coordinate> pacManEaten;
    // pacman分数
    private Integer pacManScore;
    // ghosts分数
    private Integer ghostsScore;
    // 剩余回合数
    private Integer round;
    // 剩余生命
    private Integer lives;
    // 游戏状态
    private Boolean gameOver;
    // pacman变身状态
    private Map<String, Boolean> pacmanFeast;
    // 地图
    private int[][] map;
    // 生命加成分数
    private Integer lifeExtra;
}
