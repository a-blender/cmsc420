/*
 * @(#)RoadAdjacencyList.java        2.0 2007
 *
 * Copyright Ben Zoller (University of Maryland, College Park), 2007
 * Copyright David Renie (University of Maryland, College Park), 2005
 * All rights reserved. Permission is granted for use and modification in CMSC420 
 * at the University of Maryland.
 */

package cmsc420.geometry;

import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Road adjacency list contains a map of cities to connected roads. While the
 * road objects themselves are directed, insertion and deletion is undirected.
 * That is, insertion of a road adds it to the start city's list of connected
 * roads and the end city's list of connected roads.
 * 
 * @author David Renie
 * @author Ben Zoller
 * @version 2.0, 2007
 */
public class RoadAdjacencyList {

	/** map of city names to connected roads */
	final protected TreeMap<City, TreeSet<Road>> adjacencyList = new TreeMap<City, TreeSet<Road>>(
			new CityNameComparator());

	/**
	 * Adds an undirected road to the adjacency list
	 * 
	 * @param one
	 *            a city contained by the road
	 * @param two
	 *            the other city contained by the road
	 * @return road added to the adjacency list
	 */
	public Road addRoad(final City one, final City two) {
		return addRoad(new Road(one, two));
	}

	/**
	 * Adds an undirected road to the adjacency list.
	 * 
	 * @param road
	 *            road to be added to adjacency list
	 * @return road added to adjacency list
	 */
	public Road addRoad(Road road) {
		final City start, end;

		start = road.getStart();
		end = road.getEnd();

		/* check if we need to flip start and end */
		if (start.getName().compareTo(end.getName()) > 0) {
			road = new Road(end, start);
		}

		addRoadForCity(road, start);
		addRoadForCity(road, end);

		return road;
	}

	/**
	 * Returns if the adjacency list contains the road.
	 * 
	 * @param road
	 *            road to be checked
	 * @return <code>true</code> if the road is in the list,
	 *         <code>false</code> otherwise
	 */
	public boolean containsRoad(final Road road) {
		TreeSet<Road> roadsForCity = adjacencyList.get(road.getStart());
		if (roadsForCity == null) {
			return false;
		} else {
			return roadsForCity.contains(road);
		}
	}

	/**
	 * Adds a road to the city's road list.
	 * 
	 * @param road
	 *            road to be added
	 * @param city
	 *            city whose road list the road will be added to
	 */
	protected void addRoadForCity(final Road road, final City city) {
		TreeSet<Road> roadsForCity = adjacencyList.get(city);

		if (roadsForCity == null) {
			roadsForCity = new TreeSet<Road>(new RoadNameComparator());
			adjacencyList.put(city, roadsForCity);
		}

		roadsForCity.add(road);
	}

	/**
	 * Removes an undirected road from the adjacency list
	 * 
	 * @param one
	 *            a city contained by the road
	 * @param two
	 *            the other city contained by the road
	 * @return the road removed from the adjacency list
	 */
	public Road removeRoad(final City one, final City two) {
		return removeRoad(new Road(one, two));
	}

	/**
	 * Removes an undirected road from the adjacency list.
	 * 
	 * @param road
	 *            road to be removed form the adjacency list
	 * @return road removed from the adjacency list
	 */
	public Road removeRoad(Road road) {
		if (!containsRoad(road)) {
			return null;
		} else {
			final City start, end;

			start = road.getStart();
			end = road.getEnd();

			// check if we need to flip start and end
			if (start.getName().compareTo(end.getName()) > 0) {
				road = new Road(end, start);
			}

			removeRoadForCity(road, start);
			removeRoadForCity(road, end);

			return road;
		}
	}

	/**
	 * Removes a road from the city's road list.
	 * 
	 * @param road
	 *            road to be removed from the adjacency list
	 * @param city
	 *            city whose road list the road will be removed from
	 */
	protected void removeRoadForCity(final Road road, final City city) {
		TreeSet<Road> roadsForCity = adjacencyList.get(city);

		if (roadsForCity != null) {
			roadsForCity.remove(road);
			if (roadsForCity.size() == 0) {
				adjacencyList.remove(city);
			}
		}
	}
	
	/**
	 * Removes all roads connected to the city
	 * 
	 * @param city
	 */
	public void removeRoadsForCity(final City city) {
		TreeSet<Road> roadsForCity = adjacencyList.get(city);
		
		if (roadsForCity != null) {
			adjacencyList.remove(city);
			for (Road r : roadsForCity) {
				if (r.getStart().equals(city)) {
					removeRoadForCity(r, r.getEnd());
				} else if (r.getEnd().equals(city)) {
					removeRoadForCity(r, r.getStart());
				}
			}
		}
	}

	/**
	 * Gets a list of the roads connected to a given city.
	 * 
	 * @param city
	 *            city
	 * @return set of connected roads to the city
	 */
	public TreeSet<Road> getRoadSet(final City city) {
		final TreeSet<Road> roadsForCity = adjacencyList.get(city);

		if (roadsForCity == null) {
			return new TreeSet<Road>(new RoadNameComparator());
		} else {
			return roadsForCity;
		}
	}

	/**
	 * Deletes a city and all of its connected roads from the adjacency list.
	 * 
	 * @param city
	 *            city to be deleted
	 * @return list of roads connected to city
	 */
	public TreeSet<Road> deleteCity(final City city) {
		final String cityName = city.getName();
		final TreeSet<Road> roadsForCity = adjacencyList.remove(city);

		for (Road road : roadsForCity) {
			// remove the road from the TreeSet of the City other than city
			final City otherCity = road.getOtherCity(cityName);
			//adjacencyList.get(otherCity).remove(road);
			removeRoadForCity(road, otherCity);
		}

		return roadsForCity;
		
	}

	/**
	 * Gets the number of cities in the road adjacency list.
	 * 
	 * @return number of cities in the list
	 */
	public int getNumberOfCities() {
		return adjacencyList.size();
	}

	/**
	 * Gets a set of the names of all cities in the adjacency list.
	 * 
	 * @return list of all city names in the adjacency list
	 */
	public Set<City> getCitySet() {
		return adjacencyList.keySet();
	}

	/**
	 * Clears the road adjacency list.
	 */
	public void clear() {
		adjacencyList.clear();
	}

	/**
	 * Gets all the connected roads for each city. Useful for debugging
	 * purposes.
	 * 
	 * @return string representing the road adjacency list
	 */
	public String toString() {
		final StringBuilder s = new StringBuilder();
		for (final City city : adjacencyList.keySet()) {
			final String cityName = city.getName();
			s.append(cityName);
			s.append(":\n");
			for (Road r : adjacencyList.get(city)) {
				s.append(r.getCityNameString());
				s.append("\n");
			}
			s.append("\n");
		}
		return s.toString();
	}
}
