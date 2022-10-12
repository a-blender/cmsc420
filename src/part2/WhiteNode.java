package cmsc420.meeshquest.part2;

import cmsc420.geom.Geometry2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

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
     * Inserts a list of geometries into the pm quadtree
     * WhiteNode - adds a list of geometries by returning a black node
     * @param geom
     * @param center
     * @param dim
     * @return
     */
    public PRQTNode insert(Geometry2D geom, Point2D.Float center, int dim) {

        ArrayList<Geometry2D> list = new ArrayList<>();
        list.add(geom);
        return new BlackNode(list);
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
