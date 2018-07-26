package cmsc420.meeshquest.part2;

import cmsc420.geom.Geometry2D;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Class for a gray node in the PM Quadtree
 * Represents an internal node with four children nodes
 */
public class GrayNode extends PRQTNode {

    private Point2D.Float center;
    private int dim;
    private PRQTNode[] children;

    public GrayNode(Point2D.Float center, int dim, PRQTNode[] children) {

        this.center = center;           // center coordinate
        this.dim = dim;                 // dimension of the node
        this.children = children;       // array of references to four children nodes
    }

    public Point2D.Float getCenter() {
        return this.center;
    }

    public int getDimension () {
        return this.dim;
    }

    public PRQTNode[] getChildren() {
        return this.children;
    }

    // TO DO: finish modifying this function
    /**
     * Inserts a node at a gray node into the pm quadtree
     * Step 1: insert the node into the correct partition of the gray node
     * Step 2: insert road/roads into all the nodes they pass through
     * @return
     */

    /*
    public PRQTNode insert(ArrayList<Geometry2D> geom, Point2D.Float center, int dim) {

        float new_x, new_y;             // (x, y) values of the node to return
        Point2D.Float new_center;       // center point of the node to return
        int new_dim = dim/2;            // dimension of the node to return

        float city_x = city.get_x();
        float city_y = city.get_y();
        float center_x = center.x;
        float center_y = center.y;

        if (city_x < center_x && city_y >= center_y) {

            // quadrant 1
            new_x = center.x - new_dim/2;
            new_y = center.y + new_dim/2;
            new_center = new Point2D.Float(new_x, new_y);
            this.children[0] = children[0].insert(city, new_center, new_dim);
        }
        else if (city_x >= center_x && city_y >= center_y) {

            // quadrant 2
            new_x = center.x + new_dim/2;
            new_y = center.y + new_dim/2;
            new_center = new Point2D.Float(new_x, new_y);
            this.children[1] = children[1].insert(city, new_center, new_dim);
        }
        else if (city_x < center_x && city_y < center_y) {

            // quadrant 3
            new_x = center.x - new_dim/2;
            new_y = center.y - new_dim/2;
            new_center = new Point2D.Float(new_x, new_y);
            this.children[2] = children[2].insert(city, new_center, new_dim);
        }
        else if (city_x >= center_x && city_y < center_y) {

            // quadrant 4
            new_x = center.x + new_dim/2;
            new_y = center.y - new_dim/2;
            new_center = new Point2D.Float(new_x, new_y);
            this.children[3] = children[3].insert(city, new_center, new_dim);
        }

        // return the gray node with the new city added
        return this;
    } */

    @Override
    public String toString() {
        return "GrayNode{" +
                "center=" + center +
                ", dim=" + dim +
                ", children=" + Arrays.toString(children) +
                '}';
    }
}
