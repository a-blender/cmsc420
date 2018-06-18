package cmsc420.meeshquest.part1;
import java.awt.geom.Point2D;


/**
 * Data structure class for the PR QuadTree
 * Serves as a wrapper class for the spatial data structure
 */
public class PRQuadTree {


    private PRQTNode root = null;


    /**
     * Constructs the PR QuadTree spatial data structure
     */
    public PRQuadTree() {

        root = new WhiteNode();
    }


    /**
     * Returns the root of the PR QuadTree
     * @return
     */
    public PRQTNode getRoot() {

        return this.root;
    }


    /**
     * Inserts a city point into the quadtree
     * @param city = City object
     * @return
     */
    public PRQTNode insert(City city, Point2D.Float center, int dim, int level) {

        /*
        What happens here?
        - white node: inserts the city into the white node -> black node
        - black node: creates a gray node and inserts the city into the correct child node
        - gray node: inserts the city into the correct child node
         */
        return this.root.insert(city, center, dim);
    }

}
