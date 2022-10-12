package cmsc420.meeshquest.part2;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class RoadDistance {

    private Road road;
    private Point2D.Float point;
    private double distance;

    public RoadDistance(Road road, Point2D.Float point) {

        this.road = road;
        this.point = point;

        Line2D.Float line = new Line2D.Float(road.getStartCity().getCoordinates(),
                road.getEndCity().getCoordinates());
        this.distance = line.ptSegDist(point);
    }

    public Road getRoad() {
        return road;
    }

    public Point2D.Float getPoint() {
        return point;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "RoadDistance{" +
                "road=" + road +
                ", point=" + point +
                ", distance=" + distance +
                '}';
    }
}
