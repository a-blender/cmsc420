/*
 * @(#)RoadNameComparator.java        1.0 2005
 *
 * Copyright David Renie (University of Maryland, College Park), 2005
 * All rights reserved. Permission is granted for use and modification in CMSC420 
 * at the University of Maryland.
 */
package cmsc420.geometry;

import java.util.Comparator;

/**
 * Compares two roads based on the names of their cities. By default, order does
 * not matter (roads are undirected). Whether order matters can be set in the
 * constructor.
 * <p>
 * If order does not matter, the least city names are extracted from each road
 * and compared. If necessary, the remaining city names are compared.
 * <p>
 * If order matters, compares two roads according to the names of their start
 * cities, and then according to the names of their end cities if necessary.
 * 
 * @author Dave Renie
 * @author Ben Zoller (Javadoc)
 * @version 1.0, 2005
 */
public class RoadNameComparator implements Comparator<Road> {

	/** whether the roads are directed or undirected */
	final boolean orderMatters;

	/**
	 * Constructs a new road comparator based on city names. By default, roads
	 * are undirected.
	 */
	public RoadNameComparator() {
		orderMatters = false;
	}

	/**
	 * Constructs a new road comparator based on city names.
	 * 
	 * @param orderMatters
	 *            <code>true</code> if roads are directed, <code>false</code>
	 *            otherwise
	 */
	public RoadNameComparator(final boolean orderMatters) {
		this.orderMatters = orderMatters;
	}

	/**
	 * Compares two roads based on the names of their cities.
	 * <p>
	 * If order does not matter, the least city names are extracted from each
	 * road and compared. If necessary, the remaining city names are compared.
	 * <p>
	 * If order matters, compares two roads according to the names of their
	 * start cities, and then according to the names of their end cities if
	 * necessary.
	 * 
	 * @param one
	 *            one road
	 * @param two
	 *            the other road
	 * @return result of road comparison based on city names
	 */
	public int compare(Road one, Road two) {
		if (!orderMatters) {
			/*
			 * reorder each road so the start city name is less than the end
			 * city name
			 */
			one = orderRoad(one);
			two = orderRoad(two);
		}

		final int startCityNameCompare = two.getStart().getName().compareTo(
				one.getStart().getName());

		if (startCityNameCompare != 0) {
			/* return result of comparison of start city names */
			return startCityNameCompare;
		} else {
			/* return result of comparison of end city names */
			return two.getEnd().getName().compareTo(one.getEnd().getName());
		}
	}

	/**
	 * Reorder the road if necessary so that the name of the start city is less
	 * than the name of the end city.
	 * 
	 * @param road
	 *            road to be reordered
	 * @return reordered road
	 */
	protected Road orderRoad(final Road road) {
		final String startName = road.getStart().getName();
		final String endName = road.getEnd().getName();

		if (startName.compareTo(endName) > 0) {
			return new Road(road.getEnd(), road.getStart());
		} else {
			return road;
		}
	}
}