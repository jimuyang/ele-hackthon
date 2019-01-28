package me.ele.hackathon.example.ghost.map.parse;

import me.ele.hackathon.pacman.ds.Coordinate;

/**
 * @author: Jimu Yang
 * @date: 2019/1/28 11:08 AM
 * @descricption: want more.
 */
public interface IDirection {

    Coordinate move(Coordinate from);

    IDirection reverse();
}
