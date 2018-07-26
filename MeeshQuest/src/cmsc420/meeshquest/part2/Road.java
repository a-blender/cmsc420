package cmsc420.meeshquest.part2;
import cmsc420.geom.Geometry2D;

import java.awt.geom.Line2D;

/**
 * Road defines a line in the PM3 Quadtree with start and end cities
 * This class extends Line2D.Float
 */
public class Road extends Line2D.Float implements Geometry2D {

    private City startCity;
    private City endCity;

    public Road(City start, City end) {

        startCity = start;
        endCity = end;
    }

    public City getStartCity() {
        return startCity;
    }

    public City getEndCity() {
        return endCity;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String toString() {
        return "Road{" +
                "startCity=" + startCity +
                ", endCity=" + endCity +
                '}';
    }
}
