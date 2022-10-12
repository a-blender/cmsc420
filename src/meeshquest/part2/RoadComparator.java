package cmsc420.meeshquest.part2;

import java.util.Comparator;
import java.lang.*;

/**
 * Comparator class for comparing road objects by endpoints
 */
public class RoadComparator implements Comparator<Road> {

    @Override
    /**
     * Overridden compare method
     * @param r1 = road object 1
     * @param r2 = road object 2
     * @return
     */
    public int compare(Road r1, Road r2) {

        String r1_start = r1.getStartCity().getName();
        String r2_start = r2.getStartCity().getName();

        if (r1_start.equals(r2_start)) {

            String r1_end = r1.getEndCity().getName();
            String r2_end = r2.getEndCity().getName();
            return r2_end.compareTo(r1_end);
        }
        return r2_start.compareTo(r1_start);
    }
}


