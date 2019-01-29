package me.ele.hackathon.example.ghost.map.coord;

/**
 * @author: Yang Fan
 * @date: 2019/1/25
 * @desc:
 */
public interface Plot {

    /**
     * 空地
     */
    int EMPTY = 0;

    /**
     * 墙壁
     */
    int BARRIER = 1;

    /**
     * 豆子
     */
    int PACDOT = 2;

    /**
     * 大力豆
     */
    int POWER_PELLET = 3;

    /**
     * pacman
     */
    int PACMAN = 4;

    /**
     * ghost
     */
    int GHOST = 5;


    int DEADEND = 11;

    int CORNER = 12;

    int SITE = 13;

    int COMMEN = 10;
}
