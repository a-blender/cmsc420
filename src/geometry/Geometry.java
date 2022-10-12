package cmsc420.geometry;

import cmsc420.geom.Geometry2D;

public abstract class Geometry implements Geometry2D, Comparable<Geometry> {
	@Override
	public int compareTo(Geometry o) {
		if (this.isCity()) {
			if (o.isCity()) {
				// both are cities
				return ((City) o).getName().compareTo(((City) this).getName());
			} else {
				// this is a city, o is a road
				return -1;
			}
		} else {
			// this is a road
			if (o.isCity()) {
				// o is a city
				return 1;
			} else {
				// o is a road
				if (((Road) this).getStart().getName()
						.compareTo(((Road) o).getStart().getName()) == 0) {
					// start names are the same so compare end names
					return ((Road) o).getEnd().getName()
							.compareTo(((Road) this).getEnd().getName());
				} else {
					/* start names are different; compare start names */
					return ((Road) o).getStart().getName()
							.compareTo(((Road) this).getStart().getName());
				}
			}
		}
	}
	
	public boolean isRoad() {
		return getType() == Geometry2D.SEGMENT;
	}

	public boolean isCity() {
		return getType() == Geometry2D.POINT;
	}

}
