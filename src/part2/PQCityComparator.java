package cmsc420.meeshquest.part2;
import java.util.Comparator;

/**
 * Comparator class for sorting PriorityQueue based on point
 */
public class PQCityComparator implements Comparator<CityDistance> {

    @Override
    /**
     * Compare method for comparing CityDistance objects
     * @param v1 = distance 1
     * @param v2 = distance 2
     * @return
     */
    public int compare(CityDistance v1, CityDistance v2) {

        if (v1.getDistance() > v2.getDistance()) {
            return 1;
        }
        else if (v1.getDistance() < v2.getDistance()) {
            return -1;
        }
        else return 0;
    }
}
