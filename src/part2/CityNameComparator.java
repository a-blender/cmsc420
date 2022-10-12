package cmsc420.meeshquest.part2;
import java.util.Comparator;

/**
 * Comparator class for comparing city objects by name
 */
public class CityNameComparator implements Comparator<String>{

    /**
     * Overridden compare method
     * @param v1 = city name 1
     * @param v2 = city name 2
     * @return
     */
    public int compare(String v1, String v2) {
        return v2.compareTo(v1);
    }

}
