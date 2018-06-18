package cmsc420.meeshquest.part1;
import java.awt.geom.Point2D;

/**
 * Abstract node class for the PR QuadTree data structure
 * think of it as: default functions
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
     * Inserts a PRQTNode (white, black, or gray) into the quadtree
     * @param city
     * @param center
     * @param dim
     * @return
     */
    public PRQTNode insert(City city, Point2D.Float center, int dim) {

        return new WhiteNode();
    }


    /**
     * Deletes a PRQTNode (white, gray, or black) from the quadtree
     * @param city
     * @return
     */
    public static WhiteNode delete(City city, Point2D.Float origin, int dim) {

        return new WhiteNode();
    }

}
