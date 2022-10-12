package cmsc420.geometry;

import java.awt.geom.Line2D;


/**
 * Road class provides an analogue to real-life roads on a map. A Road connects
 * one {@link City} to another city. The distance between the two
 * cities is calculated when the road is constructed to save time in distance
 * calculations. Note: roads are not interchangeable. That is, Road (A,B) is not
 * the same as Road (B,A).
 */
public class Road extends Geometry {
	/** start city */
	protected City start;

	/** end city */
	protected City end;

	/** distance from start city to end city */
	protected double distance;
	
	/**
	 * Constructs a new road based on start city and end city. Calculates and
	 * stores the distance between them.
	 * 
	 * @param start
	 *            start city
	 * @param end
	 *            end city
	 */
	public Road(final City start, final City end) {
		if (end.getName().compareTo(start.getName()) < 0) {
			this.start = end;
			this.end = start;
		} else {
			this.start = start;
			this.end = end;
		}
		distance = start.toPoint2D().distance(end.toPoint2D());
	}

	/**
	 * Gets the start city.
	 * 
	 * @return start city
	 */
	public City getStart() {
		return start;
	}

	/**
	 * Gets the end city.
	 * 
	 * @return end city
	 */
	public City getEnd() {
		return end;
	}

	/**
	 * Gets the distance between the start and end cities.
	 * 
	 * @return distance between the two cities
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * Returns a string representing a road. For example, a road from city A to
	 * city B will print out as: (A,B).
	 * 
	 * @return road string
	 */
	public String getCityNameString() {
		return "(" + start.getName() + "," + end.getName() + ")";
	}

	/**
	 * If the name of the start city is passed in, returns the name of the end
	 * city. If the name of the end city is passed in, returns the name of the
	 * start city. Else throws an <code>IllegalArgumentException</code>.
	 * 
	 * @param cityName
	 *            name of city contained by the road
	 * @return name of the other city contained by the road
	 * @throws IllegalArgumentException
	 *             city name passed in was not contained by the road
	 */
	public City getOtherCity(final String cityName) {
		if (start.getName().equals(cityName)) {
			return end;
		} else if (end.getName().equals(cityName)) {
			return start;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Returns a line segment representation of the road which extends
	 * Line2D.Double.
	 * 
	 * @return line segment representation of road
	 */
	public Line2D toLine2D() {
		return new Line2D.Float(start.toPoint2D(), end.toPoint2D());
	}

	/**
	 * Determines if one road is equal to another.
	 * 
	 * @param other
	 *            the other road
	 * @return <code>true</code> if the roads are equal, false otherwise
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj != null && (obj.getClass().equals(this.getClass()))) {
			Road r = (Road) obj;
			return (start.equals(r.start) && end.equals(r.end) && distance == r.distance);
		}
		return false;
	}

	/**
	 * Returns the hash code value of a road.
	 */
	public int hashCode() {
		final long dBits = Double.doubleToLongBits(distance);
		int hash = 35;
		hash = 37 * hash + start.hashCode();
		hash = 37 * hash + end.hashCode();
		hash = 37 * hash + (int) (dBits ^ (dBits >>> 32));
		return hash;
	}

	public boolean contains(City city) {
		return (city.equals(start) || city.equals(end));
	}
	
	public String toString() {
		return getCityNameString();
	}

	@Override
	public int getType() {
		return SEGMENT;
	}
}
