package cmsc420.meeshquest.part1;
import java.awt.geom.Point2D;

public class Distance {

    private City city;
    private double distance;

    public Distance(City city, Point2D.Float point) {

        this.city = city;

        double x1 = (double) point.x;
        double y1 = (double) point.y;
        double x2 = (double) city.get_x();
        double y2 = (double) city.get_y();

        distance = Point2D.distance(x1, y1, x2, y2);
    }

    public City getCity() {
        return this.city;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "city=" + city +
                ", distance=" + distance +
                '}';
    }
}
