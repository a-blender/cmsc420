package cmsc420.meeshquest.part1;
import java.awt.geom.Point2D;


/**
 * Class for a black node in the PR Quadtree
 * Represents a node with a city
 */
public class BlackNode extends PRQTNode {


    private City city;


    /**
     * Constructs an instance of the BlackNode class
     * @param city
     */
    public BlackNode(City city) {

        this.city = city;
    }


    /***
     * Returns the city object associated with the node
     */
    public City getValue() {

        return this.city;
    }


    /**
     * Inserts a node at a BlackNode in the PR QuadTree
     * Basically: create a GrayNode and re-insert both BlackNodes into the correct partitions
     * @param city
     * @param center
     * @param dim
     * @return
     */
    public PRQTNode insert(City city, Point2D.Float center, int dim) {

        /*
        You need three things to create a gray node:
        1. center point of the four quadrants
        2. dimension of the gray node
        3. array of child nodes
         */

        // Create an array of white nodes as children for the gray node

        PRQTNode[] child_nodes = null;
        for (int x = 0; x < child_nodes.length; x++) {
            child_nodes[x] = new WhiteNode();
        }

        // Create the new gray node

        PRQTNode gray_node = new GrayNode(center, dim, child_nodes);

        // Re-insert the the black node that you replaced with the gray node
        // Insert the new black node

        gray_node.insert(this.city, center, dim);
        return gray_node.insert(city, center, dim);
    }


    /**
     * Returns a string representation of the BlackNode class
     * @return
     */
    @Override
    public String toString() {
        return "BlackNode{" +
                "city=" + city +
                '}';
    }
}
