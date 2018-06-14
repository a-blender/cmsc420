package cmsc420.meeshquest.part1;


/**
 * Singleton class for a white node in the PR Quadtree
 * represents an empty point
 */
public class WhiteNode extends PRQTNode {


    /**
     * Constructs the only instance of the WhiteNode class
     */
    public WhiteNode() {}


    /**
     * gets the only instance of the WhiteNode class
     * @return WhiteNode object
     */
    public static WhiteNode getInstance() {
        return instance;
    }


    /**
     * instantiates the only instance of the WhiteNode class
     */
    private static final WhiteNode instance =
            new WhiteNode();

}
