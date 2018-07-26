package cmsc420.meeshquest.part2;

import cmsc420.geom.Geometry2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Class for a black node in the PR Quadtree
 * Represents a node with a city
 */
public class BlackNode extends PRQTNode {

    private ArrayList<Geometry2D> geom;

    public BlackNode(ArrayList<Geometry2D> geom) {
        this.geom = geom;
    }

    public ArrayList<Geometry2D> getGeometry() {
        return geom;
    }

    public Geometry2D getCity() {
        for (Geometry2D item : geom) {
            if (item instanceof City) {
                return item;
            }
        }
        return null;
    }

    public ArrayList<Geometry2D> getRoads() {
        ArrayList<Geometry2D> newList = new ArrayList<Geometry2D>();
        for (Geometry2D item : geom) {
            if (item instanceof Road) {
                newList.add(item);
            }
        }
        return newList;
    }

    /**
     * Inserts a city (with roads, optional) into the pm quadtree
     * BlackNode - add city/roads and return gray node that partitions correctly
     * @param geom
     * @param center
     * @param dim
     * @return
     */
    public PRQTNode insert(ArrayList<Geometry2D> geom, Point2D.Float center, int dim) {

        boolean addingCity = false;
        for (Geometry2D item : geom) {
            if (item instanceof City) addingCity = true;
        }

        // Option 1: only adding road/roads

        if (!addingCity) {
            this.geom.addAll(geom);
            return this;
        }

        // Option 2: adding a city (with possibly more road/roads)

        else {

            // Option 2A: adding a city to a black node with no city
            if (this.getCity() == null) {
                this.geom.addAll(geom);
                return this;
            }

            // Option 2B: adding a city to a black node that has a city
            else {
                PRQTNode[] child_nodes = new PRQTNode[4];
                for (int x = 0; x <= 3; x++) {
                    child_nodes[x] = new WhiteNode();
                }

                PRQTNode gray_node = new GrayNode(center, dim, child_nodes);
                gray_node.insert(this.geom, center, dim);
                gray_node.insert(geom, center, dim);
                return gray_node;
            }
        }
    }

    @Override
    public String toString() {
        return "BlackNode{" +
                "list=" + geom +
                '}';
    }
}
