package cmsc420.geometry;


import java.util.Comparator;

/**
 * Compares two cities based on their names.
 * 
 * @author Ben Zoller
 * @version 1.0, 23 Jan 2007
 */
public class CityNameComparator implements Comparator<City> {
	public int compare(final City c1, final City c2) {
		return c2.getName().compareTo(c1.getName());
	}
}