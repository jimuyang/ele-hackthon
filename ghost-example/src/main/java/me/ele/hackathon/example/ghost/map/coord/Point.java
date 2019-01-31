package me.ele.hackathon.example.ghost.map.coord;

import me.ele.hackathon.example.ghost.map.Direction;
import me.ele.hackathon.example.ghost.path.Segment;
import me.ele.hackathon.pacman.ds.Coordinate;


/**
 * @author: Jimu Yang
 * @date: 2019/1/28 2:46 PM
 * @descricption: want more.
 */
public class Point extends Coordinate {

    /**
     * 所处的线段 中途点才有
     */
    private Segment located;

    /**
     * 通度 端点才有
     */
    private int tongree;

    /**
     * 可访问的点数量
     * 对EndPoint来说是连接数
     * 对中途点来说是2
     */
    private int accessNum;

    /**
     * 对下面几个的封装
     */
    private AccessPoint[] accessPoints;

//    /**
//     * 与点相连的线段
//     * 对中途点来说 到达端点的线段
//     */
//    private Segment[] connectors;
//
//    /**
//     * 可访问的点 对应connectors
//     */
//    private Coordinate[] accessibles;
//
//    /**
//     * 访问代价 对应accessibles
//     */
//    private int[] accessCost;
//
//    /**
//     * 到达时的方向 对应accessibles
//     */
//    private Direction[] accessDir;


    public Point(int x, int y) {
        super(x, y);
//        this.connectors = new Segment[4];
        this.accessNum = 0;
        this.accessPoints = new AccessPoint[4];
        this.tongree = 0;

//        this.accessibles = new Coordinate[4];
//        this.accessCost = new int[4];
//        this.accessDir = new Direction[4];
    }

    public void add(Segment segment) {
//        this.connectors[tongree] = segment;
        AccessPoint accessPoint = new AccessPoint();
        accessPoint.setGo(segment);
        accessPoint.setCoord(segment.another(this));
        accessPoint.setCost(segment.getLength());
        accessPoint.setDir(Direction.get(this, segment.another(this)));
        this.accessPoints[tongree] = accessPoint;
//        this.accessibles[tongree] = segment.another(this);
//        this.accessCost[tongree] = segment.getLength();
//        this.accessDir[tongree] = Direction.get(this, segment.another(this));
        tongree++;
        accessNum++;
    }

    public void setLocated(Segment located) {
        if (this.located == null) {
            this.located = located;
        }
    }

    public void parseLocated() {
        if (this.located == null)
            return;
        this.tongree = 0;
        this.accessNum = 2;

        AccessPoint accessPoint0 = new AccessPoint();
        accessPoint0.setCoord(located.getCoord1());
        accessPoint0.setCost(located.getCoord1().mhdDistance(this));
        accessPoint0.setDir(Direction.get(this, located.getCoord1()));
        accessPoint0.setGo(new Segment(this, located.getCoord1()));
        this.accessPoints[0] = accessPoint0;
//        this.connectors[0] = new Segment(this, located.getCoord1());

        AccessPoint accessPoint1 = new AccessPoint();
        accessPoint1.setCoord(located.getCoord2());
        accessPoint1.setCost(located.getCoord2().mhdDistance(this));
        accessPoint1.setDir(Direction.get(this, located.getCoord2()));
        accessPoint1.setGo(new Segment(this, located.getCoord2()));
        this.accessPoints[1] = accessPoint1;
//        this.connectors[1] = new Segment(this, located.getCoord2());

//        this.accessibles = located.getCoords();
//        this.accessCost = new int[]{accessibles[0].mhdDistance(this), accessibles[1].mhdDistance(this)};
//        this.accessDir = new Direction[]{Direction.get(this, accessibles[0]), Direction.get(this, accessibles[1])};
//        this.connectors = new Segment[]{new Segment(this, accessibles[0]), new Segment(this, accessibles[1])};
    }

    public AccessPoint findAccessPoint(Coordinate coord) {
        for (AccessPoint accessPoint : accessPoints) {
            if (accessPoint == null)
                continue;
            if (accessPoint.getCoord().equals(coord)) {
                return accessPoint;
            }
        }
        return null;
    }


//    public boolean sameAccessibles(Point other) {
//        if (this.accessNum != other.accessNum)
//            return false;
//    }

//    public int[] getAccessCost() {
//        return accessCost;
//    }
//
//    public Coordinate[] getAccessibles() {
//        return accessibles;
//    }

    public int getTongree() {
        return tongree;
    }

//    public Segment[] getConnectors() {
//        return connectors;
//    }
//
//    public void setConnectors(Segment[] connectors) {
//        this.connectors = connectors;
//    }


    public Segment getLocated() {
        return located;
    }

    public int getAccessNum() {
        return accessNum;
    }

//    public Direction[] getAccessDir() {
//        return accessDir;
//    }


    public AccessPoint[] getAccessPoints() {
        return accessPoints;
    }


    @Override
    public String toString() {
        return super.toString();
//        return String.format("%-20s", "Point (" + x + ", " + y + ") ");
//        String temp = String.format("%-20s", "Point (" + x + ", " + y + ") ");
//
//        StringBuilder stringBuilder = new StringBuilder(temp + "access:");
//        for (int i = 0; i < tongree; i++) {
//            stringBuilder.append("(").append(accessibles[i].getX()).append(",").append(accessibles[i].getY()).append(")");
//        }
//        return stringBuilder.toString();
//        return temp + "seg: " + Arrays.toString(connectors);
    }

}
