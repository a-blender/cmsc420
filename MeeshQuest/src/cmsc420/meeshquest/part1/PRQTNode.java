package cmsc420.meeshquest.part1;
import java.awt.geom.Point2D;

/**
 * Abstract node class for the PR QuadTree data structure
 * think of it as: default interface functions
 */
public abstract class PRQTNode {


    /**
     * Returns true or false based on whether a city is in the quadtree
     * @return
     */
    public boolean contains(City city) {

        return false;
    }


    /**
     * Recursively inserts a city into the quadtree
     * @param city
     * @param center
     * @param dim
     * @return
     */
    public PRQTNode insert(City city, Point2D.Float center, int dim) {

        return new WhiteNode();
    }


    /**
     * Recursively deletes a city from the quadtree
     * @param city
     * @return
     */
    public PRQTNode delete(City city) {

        return new WhiteNode();
    }

}
