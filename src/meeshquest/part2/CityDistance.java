package cmsc420.meeshquest.part2;

import java.awt.geom.Point2D;

public class CityDistance {

    private City city;
    private double distance;

    public CityDistance(City city, Point2D.Float point) {

        this.city = city;

        double x1 = (double) point.x;
        double y1 = (double) point.y;
        double x2 = (double) city.get_x();
        double y2 = (double) city.get_y();

        this.distance = Point2D.distance(x1, y1, x2, y2);
    }

    public City getCity() {
        return city;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "CityDistance{" +
                "city=" + city +
                ", distance=" + distance +
                '}';
    }
}
