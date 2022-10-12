/*
 * @(#)Path.java        1.0 2005
 *
 * Copyright David Renie (University of Maryland, College Park), 2005
 * All rights reserved. Permission is granted for use and modification in CMSC420 
 * at the University of Maryland.
 */

package cmsc420.dijkstra;

import java.util.LinkedList;

import cmsc420.geometry.City;

/**
 * 
 * @author David Renie
 * @author Ben Zoller (Javadoc)
 * @version 1.0, 2005
 */
public class Path {
	/* list of city names along path from start city to end city */
	final protected LinkedList<City> pathList;

	/* total distance of path from start city to end city */
	protected double distance;

	/**
	 * Initializes the path list and sets the path distance.
	 * 
	 * @param distance
	 *            path distance from start city to end city
	 */
	public Path(final double distance) {
		pathList = new LinkedList<City>();
		this.distance = distance;
	}

	/**
	 * Add edge to path.
	 * 
	 * @param cityName
	 *            name of city (edge) to be added to path
	 */
	void addEdge(final City city) {
		pathList.addFirst(city);
	}

	/**
	 * Number of roads in path from start city to end city
	 * 
	 * @return number of roads in path
	 */
	public int getHops() {
		return pathList.size() - 1;
	}

	/**
	 * Gets the total distance of the path from start city to end city
	 * 
	 * @return path distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Gets an array of city names on the path from start city to end city.
	 * 
	 * @return array of city names in the path
	 */
	public LinkedList<City> getCityList() {
		return pathList;
	}

	/**
	 * Sets the total distance of the path from start city to end city
	 * 
	 * @param distance
	 *            distance of path
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}
}