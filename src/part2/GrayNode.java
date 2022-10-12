package cmsc420.meeshquest.part2;

import cmsc420.geom.Geometry2D;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import cmsc420.geom.Inclusive2DIntersectionVerifier;

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

    public PRQTNode insert(Geometry2D geom, Point2D.Float center, int dim) {

        float new_x, new_y;             // (x, y) values for the new center point
        Point2D.Float new_center;       // center point of the gray node to return
        int new_dim = dim / 2;            // dimension of the gray node to return

        float center_x = center.x;      // x-value of the current gray node center
        float center_y = center.y;      // y-value of the current gray node center

        // calculate rectangles for each quadrant

        Rectangle2D.Float quad1 =
                new Rectangle2D.Float(center_x-new_dim, center_y, new_dim, new_dim);
        Rectangle2D.Float quad2 =
                new Rectangle2D.Float(center_x, center_y, new_dim, new_dim);
        Rectangle2D.Float quad3 =
                new Rectangle2D.Float(center_x-new_dim, center_y-new_dim, new_dim, new_dim);
        Rectangle2D.Float quad4 =
                new Rectangle2D.Float(center_x, center_y-new_dim, new_dim, new_dim);

        // insert City into the pm quadtree**************************************

        if (geom instanceof City) {

            City city = (City) geom;
            Point2D.Float point = city.getCoordinates();

            if (Inclusive2DIntersectionVerifier.intersects(point, quad1)) {

                // quadrant 1
                new_x = center.x - new_dim / 2;
                new_y = center.y + new_dim / 2;
                new_center = new Point2D.Float(new_x, new_y);
                this.children[0] = children[0].insert(city, new_center, new_dim);
            }
            if (Inclusive2DIntersectionVerifier.intersects(point, quad2)) {

                // quadrant 2
                new_x = center.x + new_dim / 2;
                new_y = center.y + new_dim / 2;
                new_center = new Point2D.Float(new_x, new_y);
                this.children[1] = children[1].insert(city, new_center, new_dim);
            }
            if (Inclusive2DIntersectionVerifier.intersects(point, quad3)) {

                // quadrant 3
                new_x = center.x - new_dim / 2;
                new_y = center.y - new_dim / 2;
                new_center = new Point2D.Float(new_x, new_y);
                this.children[2] = children[2].insert(city, new_center, new_dim);
            }
            if (Inclusive2DIntersectionVerifier.intersects(point, quad4)) {

                // quadrant 4
                new_x = center.x + new_dim / 2;
                new_y = center.y - new_dim / 2;
                new_center = new Point2D.Float(new_x, new_y);
                this.children[3] = children[3].insert(city, new_center, new_dim);
            }
        }

        // insert Road into the pm quadtree*****************************************

        else if (geom instanceof Road) {

            Road road = (Road) geom;
            Line2D.Float line = new Line2D.Float(road.getStartCity().getCoordinates(),
                    road.getEndCity().getCoordinates());

            // insert road into all quadrants that it intersects

            if (Inclusive2DIntersectionVerifier.intersects(line, quad1)) {

                // quadrant 1
                new_x = center.x - new_dim / 2;
                new_y = center.y + new_dim / 2;
                new_center = new Point2D.Float(new_x, new_y);
                this.children[0] = children[0].insert(road, new_center, new_dim);
            }
            if (Inclusive2DIntersectionVerifier.intersects(line, quad2)) {

                // quadrant 2
                new_x = center.x + new_dim / 2;
                new_y = center.y + new_dim / 2;
                new_center = new Point2D.Float(new_x, new_y);
                this.children[1] = children[1].insert(road, new_center, new_dim);
            }
            if (Inclusive2DIntersectionVerifier.intersects(line, quad3)) {

                // quadrant 3
                new_x = center.x - new_dim / 2;
                new_y = center.y - new_dim / 2;
                new_center = new Point2D.Float(new_x, new_y);
                this.children[2] = children[2].insert(road, new_center, new_dim);
            }
            if (Inclusive2DIntersectionVerifier.intersects(line, quad4)) {

                // quadrant 4
                new_x = center.x + new_dim / 2;
                new_y = center.y - new_dim / 2;
                new_center = new Point2D.Float(new_x, new_y);
                this.children[3] = children[3].insert(road, new_center, new_dim);
            }
        }
        return this;
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
