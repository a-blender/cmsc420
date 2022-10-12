package cmsc420.meeshquest.part2;

import cmsc420.geom.Geometry2D;
import java.awt.geom.Point2D;

/**
 * Data structure class for the PR QuadTree
 * Serves as a wrapper class for the spatial data structure
 */
public class PMQuadtree {

  private PRQTNode root;
  private int width;
  private int height;

  public PMQuadtree(int width, int height) {

    root = new WhiteNode();
    this.width = width;
    this.height = height;
  }

  public PRQTNode getRoot() {

    return this.root;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public void clear() {
    this.root = new WhiteNode();
  }


  public PRQTNode insert(Geometry2D geom, Point2D.Float center, int dim) {

    this.root = root.insert(geom, center, dim);
    return root;
  }
}
