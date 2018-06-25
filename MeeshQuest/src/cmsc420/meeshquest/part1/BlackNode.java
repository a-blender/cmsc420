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
    public City getCity() {

        return this.city;
    }


    /**
     * Inserts a city into the quadtree
     * BlackNode - create a gray node and re-insert both cities into child nodes
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

        PRQTNode[] child_nodes = new PRQTNode[4];
        for (int x = 0; x <= 3; x++) {
            child_nodes[x] = new WhiteNode();
        }

        // Create the new gray node

        PRQTNode gray_node = new GrayNode(center, dim, child_nodes);

        // Re-insert the the black node that you replaced with the gray node
        // Insert the new black node

        gray_node.insert(this.city, center, dim);
        gray_node.insert(city, center, dim);
        return gray_node;
    }


    /**
     * Deletes a city from the quadtree
     * BlackNode - returns white node to remove city, or returns black node
     * @param city
     * @return
     */
    public PRQTNode delete(City city) {

        if (this.getCity().equals(city)) {
            return new WhiteNode();
        }
        else return this;
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
