package me.ele.hackathon.example.ghost.map;

import me.ele.hackathon.pacman.ds.Coordinate;

/**
 * @author: Jimu Yang
 * @date: 2019/1/28 11:07 AM
 * @descricption: want more.
 */
public enum Direction implements IDirection {


    /**
     * y + 1
     */
    UP {
        @Override
        public Coordinate move(Coordinate from) {
            return new Coordinate(from.getX(), from.getY() + 1);
        }

        @Override
        public IDirection reverse() {
            return DOWN;
        }
    },

    /**
     * y - 1
     */
    DOWN {
        @Override
        public Coordinate move(Coordinate from) {
            return new Coordinate(from.getX(), from.getY() - 1);
        }

        @Override
        public IDirection reverse() {
            return UP;
        }
    },

    /**
     * x - 1
     */
    LEFT {
        @Override
        public Coordinate move(Coordinate from) {
            return new Coordinate(from.getX() - 1, from.getY());
        }

        @Override
        public IDirection reverse() {
            return RIGHT;
        }
    },


    /**
     * x + 1
     */
    RIGHT {
        @Override
        public Coordinate move(Coordinate from) {
            return new Coordinate(from.getX() + 1, from.getY());
        }

        @Override
        public IDirection reverse() {
            return LEFT;
        }
    },

    ;


}
