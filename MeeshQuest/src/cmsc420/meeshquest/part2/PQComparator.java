package cmsc420.meeshquest.part2;
import java.util.Comparator;

/**
 * Comparator class for sorting PriorityQueue based on point
 */
public class PQComparator implements Comparator<Distance>{

    @Override
    /**
     * Overridden compare method
     * @param v1 = city coordinate 1
     * @param v2 = city coordinate 2
     * @return
     */
    public int compare(Distance v1, Distance v2) {

        if (v1.getDistance() > v2.getDistance()) {
            return 1;
        }
        else if (v1.getDistance() < v2.getDistance()) {
            return -1;
        }
        else return 0;
    }

}
