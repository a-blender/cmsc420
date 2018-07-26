package cmsc420.meeshquest.part2;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Class for adjacency list of cities in the PM3 Quadtree
 * Initialized with pmqt creation
 */
public class AdjacencyList {

    private TreeMap<City, ArrayList<City>> roadmap;

    public AdjacencyList() {
        roadmap = new TreeMap<City, ArrayList<City>>();
    }

    public void addAdjacentCity(City source, City adjacent) {

        if (roadmap.containsKey(source)) {
            roadmap.get(source).add(adjacent);
        }
        else {
            ArrayList<City> list = new ArrayList();
            list.add(adjacent);
            roadmap.put(source, list);
        }
    }

    public TreeMap<City, ArrayList<City>> getRoadmap() {
        return roadmap;
    }

    @Override
    public String toString() {
        return "AdjacencyList{" +
                "roadmap=" + roadmap +
                '}';
    }
}
