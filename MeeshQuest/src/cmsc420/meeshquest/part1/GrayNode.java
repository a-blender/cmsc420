package cmsc420.meeshquest.part1;
import java.awt.geom.Point2D;
import java.util.Arrays;


/**
 * Class for a gray node in the PR Quadtree
 * Represents an internal node with four child nodes
 */
public class GrayNode extends PRQTNode {


    private Point2D.Float center;
    private int dim;
    private PRQTNode[] children;


    /**
     * Constructs an instance of the GrayNode class
     * @param center = center coordinate
     * @param dim = dimension of the node
     * @param children = array of references to the four children nodes
     */
    public GrayNode(Point2D.Float center, int dim, PRQTNode[] children) {

        this.center = center;
        this.dim = dim;
        this.children = children;
    }


    /**
     * Gets the center coordinate of the gray node
     * @return
     */
    public Point2D.Float getCenter() {

        return this.center;
    }


    /**
     * Returns the dimension of the current gray node
     * @return
     */
    public int getDimension () {

        return this.dim;
    }


    /**
     * Returns the array with child node references
     * @return
     */
    public PRQTNode[] getChildren() {

        return this.children;
    }


    /**
     * Inserts a node at a GrayNode into the PR Quadtree
     * Basically: insert the node into the correct partition of the gray node
     * @param city
     * @param center
     * @param dim
     * @return
     */
    public PRQTNode insert(City city, Point2D.Float center, int dim) {

        /*
        1. Find out what quadrant the city to be added intersects
        2. Assign to that child the return value of inserting the city
        3. Ex: children[1] = children[1].insert(city)
         */

        /*
        Where city = (x,y) and gray node origin = (a, b)
        - quadrant 1: x < a , y >= b
        - quadrant 2: x >= a , y >= b
        - quadrant 3: x < a , y < b
        - quadrant 4: x >= a , y < b
         */

        // Find out what quadrant the city to be added intersects

        PRQTNode gray_node = null;      // gray node child to return
        float new_x, new_y;             // (x, y) values of the node to return
        Point2D.Float new_center;       // center point of the node to return
        int new_dim = dim/2;            // dimension of the node to return

        if (city.x < center.x && city.y >= center.y) {

            // quadrant 1
            new_x = center.x - center.x/2;
            new_y = center.y + center.y/2;
            new_center = new Point2D.Float(new_x, new_y);
            this.children[0] = children[0].insert(city, new_center, new_dim);
            gray_node = this.children[0];
        }
        else if (city.x >= center.x && city.y >= center.y) {

            // quadrant 2
            new_x = center.x + center.x/2;
            new_y = center.y + center.y/2;
            new_center = new Point2D.Float(new_x, new_y);
            this.children[1] = children[1].insert(city, new_center, new_dim);
            gray_node = this.children[1];
        }
        else if (city.x < center.x && city.y < center.y) {

            // quadrant 3
            new_x = center.x - center.x/2;
            new_y = center.y - center.y/2;
            new_center = new Point2D.Float(new_x, new_y);
            this.children[2] = children[2].insert(city, new_center, new_dim);
            gray_node = this.children[2];
        }
        else if (city.x >= center.x && city.y < center.y) {

            // quadrant 4
            new_x = center.x + center.x/2;
            new_y = center.y - center.y/2;
            new_center = new Point2D.Float(new_x, new_y);
            this.children[3] = children[3].insert(city, new_center, new_dim);
            gray_node = this.children[3];
        }

        // return the child node that contains the new city

        return gray_node;
    }

    @Override
    public String toString() {
        return "GrayNode{" +
                "center=" + center +
                ", dim=" + dim +
                ", children=" + Arrays.toString(children) +
                '}';
    }
}
