/*
 * @(#)Dijkstranator.java        2.0 2007/01/23
 *
 * Copyright David Renie (University of Maryland, College Park), 2005
 * Copyright Ben Zoller (University of Maryland, College Park), 2006
 * All rights reserved. Permission is granted for use and modification in CMSC420 
 * at the University of Maryland.
 */

package cmsc420.dijkstra;

import java.util.PriorityQueue;
import java.util.TreeMap;

import cmsc420.geometry.City;
import cmsc420.geometry.CityNameComparator;
import cmsc420.geometry.Road;
import cmsc420.geometry.RoadAdjacencyList;

/**
 * Implementation of Dijkstra's algorithm for MeeshQuest. Finds the shortest
 * path between two cities.
 * 
 * @author David Renie
 * @author Ben Zoller
 * @version 2.0, 23 Jan 2007
 * 
 */
public class Dijkstranator {

	/** road adjacency list stores all the roads connected to each city */
	protected RoadAdjacencyList roads;

	/**
	 * Constructs a new object to implement Dijkstra's algorithm.
	 * 
	 * @param roads
	 *            road adjacency list
	 */
	public Dijkstranator(final RoadAdjacencyList roads) {
		this.roads = roads;
	}

	/**
	 * Gets the shortest path between 2 cities using Dijkstra's algorithm.
	 * Returns <code>null</code> if no path is found.
	 * 
	 * @param startCity
	 *            start city
	 * @param endCity
	 *            end city
	 * @return shortest path from start city to end city
	 */
	public Path getShortestPath(final City startCity, final City endCity) {
		final String endCityName = endCity.getName();

		/*
		 * the set of settled vertices, the vertices whose shortest distances
		 * from the source have been found
		 */
		final TreeMap<String, Double> settledCities = new TreeMap<String, Double>();

		/*
		 * the set of unsettled vertices, the vertices whose shortest distances
		 * from the source have not been found
		 */
		final PriorityQueue<DijkstraCity> unsettledCities;

		if (roads.getNumberOfCities() > 0) {
			/*
			 * initialize a priority queue with an initial capacity of the
			 * number of cities for efficiency purposes
			 */
			unsettledCities = new PriorityQueue<DijkstraCity>(roads
					.getNumberOfCities());
		} else {
			/*
			 * PriorityQueue reports an error if initialCapacity is set to 0.
			 * Use default initial capacity.
			 */
			unsettledCities = new PriorityQueue<DijkstraCity>();
		}

		/* the predecessor of each vertex on the shortest path from the source */
		final TreeMap<City, City> previousCity = new TreeMap<City, City>(
				new CityNameComparator());

		/*
		 * the best estimate of the shortest distance from the source to each
		 * vertex
		 */
		final TreeMap<City, Double> shortestDistanceFound = new TreeMap<City, Double>(
				new CityNameComparator());

		/* if a path has been found from the start city to the end city */
		boolean pathFound = false;

		/* initialize the shortest distances for each city to be infinity */
		for (City c : roads.getCitySet()) {
			shortestDistanceFound.put(c, Double.POSITIVE_INFINITY);
		}

		/*
		 * add the start city name to priority queue of unsettled cities (has
		 * distance of 0 from itself)
		 */
		unsettledCities.add(new DijkstraCity(startCity, 0.0d));
		shortestDistanceFound.put(startCity, 0.0d);
		previousCity.put(startCity, null);

		while (!unsettledCities.isEmpty()) {
			/*
			 * remove the city with the shortest distance from the start city
			 * from priority queue of unsettled cities
			 */
			final DijkstraCity cityToSettle = unsettledCities.poll();

			/* name of city with shortest distance from the start city */
			final String cityToSettleName = cityToSettle.getName();

			/* check to see if path to end has been found */
			if (cityToSettleName.equals(endCityName)) {
				/* path has been found, break out of loop */
				pathFound = true;
				break;
			}

			if (!settledCities.containsKey(cityToSettleName)) {
				/* add city to map of settled cities */
				settledCities.put(cityToSettleName, cityToSettle.getDistance());

				/*
				 * relax the neighbors of the city to be settled by looking at
				 * the roads connected to it
				 */
				for (Road road : roads.getRoadSet(cityToSettle.getCity())) {
					/* get the adjacent city */
					final City adjacentCity = road
							.getOtherCity(cityToSettleName);
					final String adjacentCityName = adjacentCity.getName();

					if (!settledCities.containsKey(adjacentCityName)) {
						/*
						 * if the adjacent city has not been settled, get its
						 * distance to the start city
						 */
						final double adjacentCityDistance = shortestDistanceFound
								.get(adjacentCity);
						final double distanceViaCityToSettle = shortestDistanceFound
								.get(cityToSettle.getCity())
								+ road.getDistance();

						if (adjacentCityDistance > distanceViaCityToSettle) {
							/*
							 * if this new distance is smaller, update the
							 * shortest distance found for the adjacent city
							 */
							shortestDistanceFound.put(adjacentCity,
									distanceViaCityToSettle);

							/* update the other data structures */
							previousCity.put(adjacentCity, cityToSettle
									.getCity());
							unsettledCities.offer(new DijkstraCity(
									adjacentCity, distanceViaCityToSettle));
						}
					}
				}
			}
		}

		if (pathFound) {
			/* build the path from the start city to the end city */
			final Path path = new Path(shortestDistanceFound.get(endCity));

			City curr = endCity;
			while (curr != null) {
				path.addEdge(curr);
				curr = previousCity.get(curr);
			}

			return path;
		} else {
			/* no path found from start city to end city */
			return null;
		}
	}

	/**
	 * A city used in Dijkstra's algorithm. Stores the name of a city and its
	 * distance from the start city.
	 * 
	 * @author David Renie
	 * @author Ben Zoller (merged comparator into class)
	 * @version 2.0
	 * 
	 */
	protected class DijkstraCity implements Comparable<DijkstraCity> {
		/** name of the city */
		protected City city;

		/** distance from this city to the start city */
		protected double distance;

		/**
		 * Constructs a DijkstraCity.
		 * 
		 * @param name
		 *            name of the city
		 * @param distance
		 *            distance from this city to start city
		 */
		public DijkstraCity(final City city, final double distance) {
			this.city = city;
			this.distance = distance;
		}

		public City getCity() {
			return city;
		}

		/**
		 * Gets the name of the city
		 * 
		 * @return name of the city
		 */
		public String getName() {
			return city.getName();
		}

		/**
		 * Gets the distance from this city to the start city
		 * 
		 * @return distance from this city to the start city
		 */
		public double getDistance() {
			return distance;
		}

		/**
		 * A city closer to the start city than another is less than the other.
		 * If distances are equal, the city names are compared.
		 * 
		 * @param other
		 *            the other city
		 * @return comparison of this city with the other city
		 */
		public int compareTo(final DijkstraCity other) {
			if (getDistance() < other.getDistance()) {
				return -1;
			} else if (getDistance() > other.getDistance()) {
				return 1;
			} else {
				/* getDistance == other.getDistance */
				return city.getName().compareTo(other.city.getName());
			}
		}
	}
}