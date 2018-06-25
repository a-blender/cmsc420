package cmsc420.meeshquest.part1;
import java.awt.geom.Point2D;

/**
 * Singleton class for a white node in the PR Quadtree
 * Represents an empty node
 */
public class WhiteNode extends PRQTNode {


    /**
     * Constructs the only instance of the WhiteNode class
     */
    public WhiteNode() {}


    /**
     * Gets the only instance of the WhiteNode class
     * @return WhiteNode object
     */
    public WhiteNode getInstance() {

        return instance;
    }


    /**
     * Inserts a city into the quadtree
     * WhiteNode - adds a city to the white node by returning a black node
     * @param city
     * @param center
     * @param dim
     * @return
     */
    public PRQTNode insert(City city, Point2D.Float center, int dim) {

        return new BlackNode(city);
    }


    /**
     *
     * @param city
     * @param center
     * @param dim
     * @return
     */

    /**
     * Deletes a city from the quadtree
     * WhiteNode - nothing to delete, so just return a white node
     * @param city
     * @return
     */
    public PRQTNode delete(City city) {

        return new WhiteNode();
    }


    /**
     * Instantiates the only instance of the WhiteNode class
     */
    private static final WhiteNode instance =

            new WhiteNode();


    /**
     * Returns a string representation of the WhiteNode class
     * @return
     */
    @Override
    public String toString() {
        return "WhiteNode{}";
    }
}
