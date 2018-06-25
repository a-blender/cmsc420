package cmsc420.meeshquest.part1;
import java.awt.geom.Point2D;


/**
 * Data structure class for the PR QuadTree
 * Serves as a wrapper class for the spatial data structure
 */
public class PRQuadTree {


    private PRQTNode root;
    private int width;
    private int height;


    /**
     * Constructs the PR QuadTree spatial data structure
     */
    public PRQuadTree(int width, int height) {

        root = new WhiteNode();
        this.width = width;
        this.height = height;
    }


    /**
     * Returns the root of the PR QuadTree
     * @return
     */
    public PRQTNode getRoot() {

        return this.root;
    }

    /**
     * Returns the spatial width of the quadtree
     * @return
     */
    public int getWidth() {
        return this.width;
    }


    /**
     * Returns the spatial height of the quadtree
     * @return
     */
    public int getHeight() {
        return this.height;
    }


    /**
     * Clears all nodes in the entire quadtree
     */
    public void clear() {
        this.root = null;
    }


    /**
     * Inserts a city into the quadtree (wrapper function)
     * @param city
     * @param center
     * @param dim
     * @return
     */
    public PRQTNode insert(City city, Point2D.Float center, int dim) {

        this.root = root.insert(city, center, dim);
        return root;
    }


    /**
     * Deletes a city from the quadtree (wrapper function)
     * @param city
     * @return
     */
    public PRQTNode delete(City city) {

        this.root = root.delete(city);
        return root;
    }

}
