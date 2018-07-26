package cmsc420.meeshquest.part2;
import java.awt.geom.Point2D;
import java.util.*;
import java.lang.*;

/**
 * Comparator class for comparing city objects by coordinate
 */
public class CityCoordinateComparator implements Comparator<Point2D.Float> {

    /**
     * Overridden compare method
     * @param v1 = city coordinate 1
     * @param v2 = city coordinate 2
     * @return
     */
    public int compare(Point2D.Float v1, Point2D.Float v2) {
        if (v1.y == v2.y){
            return Float.compare(v1.x, v2.x);
        }
        else
            return Float.compare(v1.y, v2.y);
    }

}
