package cmsc420.meeshquest.part1;

/**
 * Class for a black node in the PR Quadtree
 * represents a city point
 */
public class BlackNode extends PRQTNode {


    private City city;


    /**
     * Constructs a BlackNode object (city point)
     * @param city object
     */
    public BlackNode(City city) {
        this.city = city;
    }


    /***
     * Returns the city object associated with the node
     */
    public static City getValue() {
        return this.city;
    }


    @Override
    public String toString() {
        return "BlackNode{" +
                "city=" + city +
                '}';
    }
}
