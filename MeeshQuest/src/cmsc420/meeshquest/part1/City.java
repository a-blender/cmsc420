package cmsc420.meeshquest.part1;
import java.awt.geom.Point2D;


/**
 * City defines a cartesian point with city name, coordinates, radius, and color
 * This class extends Point2D.Float
 */
public class City extends Point2D.Float {


    private String name;
    private Float coordinates;
    private int radius;
    private String color;


    /**
     * Constructor for the city class
     * @param name=city name, color=point color
     */
    public City(String name, Float coordinates, int radius, String color) {

        this.name = name;
        this.coordinates = coordinates;
        this.radius = radius;
        this.color = color;
    }


    /**
     * Returns the city name
     */
    public String getName() {
        return this.name;
    }


    /**
     * Returns a Point2D.Float object with the city coordinates
     */
    public Float getCoordinates() {
        return this.coordinates;
    }


    /**
     * Returns the city's x coordinate
     */
    public float get_x() {
        return this.coordinates.x;
    }


    /**
     * Returns the city's y coordinate
     */
    public float get_y() {
        return this.coordinates.y;
    }


    /**
     * Returns the city radius
     */
    public int getRadius() {
        return this.radius;
    }


    /**
     * Returns the city color
     */
    public String getColor() {
        return this.color;
    }


    /**
     * Returns a string representation of the current city object
     * @return name, coordinates, radius, color
     */
    @Override
    public String toString() {
        return "cmsc420.meeshquest.part1.City{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", radius=" + radius +
                ", color='" + color + '\'' +
                '}';
    }

}