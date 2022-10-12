package cmsc420.meeshquest.part2;

import java.util.Comparator;

/**
 * Reverse comparator for CityDistance objects in rangeCities
 */
public class RangeCitiesComparator implements Comparator<CityDistance> {

    /**
     * Overridden compare method
     * @param d1 = distance object 1
     * @param d2 = distance object 2
     * @return
     */
    public int compare(CityDistance d1, CityDistance d2) {
        String city_name1 = d1.getCity().getName();
        String city_name2 = d2.getCity().getName();
        return (city_name2.compareTo(city_name1));
    }
}