package me.ele.hackathon.pacman.ds;

import lombok.Data;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

/**
 * Created by lanjiangang on 2018/11/1.
 */
@Data
public class Coordinate implements Comparable<Coordinate> { // 坐标
    protected int x;
    protected int y;

    public Coordinate() {
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Boolean coordinateEquals(Coordinate coordinate1, Coordinate coordinate2) {
        return coordinate1.getX() == coordinate2.getX() && coordinate1.getY() == coordinate2.getY();
    }

    public static Coordinate move(Direction direction, Coordinate coordinate) {
        return coordinate.move(direction);
    }

    public static Boolean moveAble(Direction direction, GameMap map, Coordinate coordinate) {
        if (direction.equals(Direction.UP) && map.getPixels()[coordinate.getX()][coordinate.getY() + 1] == 0)
            return true;
        else if (direction.equals(Direction.RIGHT) && map.getPixels()[coordinate.getX() + 1][coordinate.getY()] == 0)
            return true;
        else if (direction.equals(Direction.DOWN) && map.getPixels()[coordinate.getX()][coordinate.getY() - 1] == 0)
            return true;
        else
            return direction.equals(Direction.LEFT) && map.getPixels()[coordinate.getX() - 1][coordinate.getY()] == 0;
    }

    public Coordinate move(Direction direction) {
        Coordinate coordinate = this;
        if (direction.equals(Direction.UP))
            return new Coordinate(coordinate.getX(), coordinate.getY() + 1);
        else if (direction.equals(Direction.RIGHT))
            return new Coordinate(coordinate.getX() + 1, coordinate.getY());
        else if (direction.equals(Direction.DOWN))
            return new Coordinate(coordinate.getX(), coordinate.getY() - 1);
        else if (direction.equals(Direction.LEFT))
            return new Coordinate(coordinate.getX() - 1, coordinate.getY());
        else
            return coordinate;

    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate))
            return false;
        Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int mhdDistance(Coordinate another) {
        return Math.abs(x - another.x) + Math.abs(y - another.y);
    }

    @Override
    public int compareTo(Coordinate o) {
        if (this.x != o.x) {
            return this.x - o.x;
        }
        return this.y - o.y;
    }
}
