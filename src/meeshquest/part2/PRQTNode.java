package cmsc420.meeshquest.part2;

import cmsc420.geom.Geometry2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Abstract node class for the PM QuadTree data structure
 * think of it as: default interface functions
 */
public abstract class PRQTNode {

  /**
   * Checks whether a city or road is in the pm quadtree
   * @return
   */
  public boolean contains(Geometry2D city) {
    return false;
  }

  /**
   * Recursively inserts a list of geometries into the pm quadtree
   * @param geom
   * @param center
   * @param dim
   * @return
   */
  public PRQTNode insert(Geometry2D geom, Point2D.Float center, int dim) {
    return new WhiteNode();
  }

}
