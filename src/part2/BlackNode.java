package cmsc420.meeshquest.part2;

import cmsc420.geom.Geometry2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

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

    public ArrayList<Road> getRoads() {

        // get arraylist of roads
        ArrayList<Road> newList = new ArrayList<>();
        for (Geometry2D item : geom) {
            if (item instanceof Road) {
                newList.add((Road) item);
            }
        }

        // sort roads in descending order by start, end
        Collections.sort(newList, new RoadComparator());

        return newList;
    }

    public int getCardinality() {
        return geom.size();
    }

    /**
     * Inserts a city (with roads, optional) into the pm quadtree
     * BlackNode - add city/roads and return gray node that partitions correctly
     * @param geom
     * @param center
     * @param dim
     * @return
     */
    public PRQTNode insert(Geometry2D geom, Point2D.Float center, int dim) {

        // Option 1: adding a road

        if (geom instanceof Road) {
            this.geom.add(geom);
            return this;
        }

        // Option 2: adding a city

        else {

            // Option 2A: adding a city to a black node with no city
            if (this.getCity() == null) {
                this.geom.add(geom);
                return this;
            }

            // Option 2B: adding a city to a black node that has a city
            else {
                PRQTNode[] child_nodes = new PRQTNode[4];
                for (int x = 0; x <= 3; x++) {
                    child_nodes[x] = new WhiteNode();
                }

                PRQTNode gray_node = new GrayNode(center, dim, child_nodes);

                // - reinsert all of the current black node's geometries
                for (Geometry2D item_to_reinsert : this.geom) {
                    gray_node.insert(item_to_reinsert, center, dim);
                }
                // - insert the new city
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
