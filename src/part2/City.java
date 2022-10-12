package cmsc420.meeshquest.part2;

import cmsc420.geom.Geometry2D;
import java.awt.geom.Point2D;

/**
 * City defines a cartesian point with city name, coordinates, radius, and color
 * This class extends Point2D.Float
 */
public class City extends Point2D.Float implements Geometry2D {

    private String name;
    private Float coordinates;
    private int radius;
    private String color;
    private boolean isIsolated;

    public City(String name, Float coordinates, int radius, String color) {

        this.name = name;
        this.coordinates = coordinates;
        this.radius = radius;
        this.color = color;
        this.isIsolated = false;
    }

    public String getName() {
        return name;
    }

    public Float getCoordinates() {
        return coordinates;
    }

    public float get_x() {
        return coordinates.x;
    }

    public float get_y() {
        return coordinates.y;
    }

    public int getRadius() {
        return radius;
    }

    public String getColor() {
        return color;
    }

    public boolean isIsolated() {
        return isIsolated;
    }

    public void setIsolated(boolean isIsolated) {
        this.isIsolated = isIsolated;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String toString() {
        return "cmsc420.meeshquest.part2.City{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", radius=" + radius +
                ", color='" + color + '\'' +
                '}';
    }
}