package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Mediator class to parse commands from input xml file
 */
public class Mediator {

    private TreeMap<String, City> map1;          // Dictionary 1
    private TreeMap<Point2D.Float, City> map2;   // Dictionary 2
    private PRQuadTree prqt;                     // PR QuadTree
    private Set mapped;                          // Mapped hash set
    private Element root;                        // Output xml root node
    private XMLBuilder xml_file;                 // Output xml builder

    /**
     * Constructs an instance of the mediator class
     * @param results
     */
    public Mediator(Document results, int width, int height) {

        this.map1 = new TreeMap<String, City>(new CityNameComparator());
        this.map2 = new TreeMap<Point2D.Float, City>(new CityCoordinateComparator());
        this.prqt = new PRQuadTree();
        this.mapped = new HashSet();

        this.xml_file = new XMLBuilder(results);
        root = results.createElement("results");
        results.appendChild(root);
    }

    /**
     * Wrapper class to parse input commands
     */
    public void parseCommand(Element commandNode) {

        Element node = null;

        switch (commandNode.getNodeName()) {
            case ("createCity"):
                node = createCity(commandNode);
                break;
            case ("deleteCity"):
                node = deleteCity(commandNode);
                break;
            case ("clearAll"):
                node = clearAll(commandNode);
                break;
            case ("listCities"):
                node = listCities(commandNode);
                break;
            case ("mapCity"):
                node = mapCity(commandNode);
                break;
            case ("unmapCity"):
                node = unmapCity(commandNode);
                break;
            case ("printPRQuadTree"):
                node = printPRQuadTree(commandNode);
                break;
            case ("saveMap"):
                node = saveMap(commandNode);
                break;
            case ("rangeCities"):
                node = rangeCities(commandNode);
                break;
            case ("nearestCity"):
                node = nearestCity(commandNode);
                break;
        }
        xml_file.appendToDocument(node);
    }


    /**
     * Creates a city object and adds it to both dictionaries
     * @param commandNode
     * @return
     */
    public Element createCity(Element commandNode) {

        String name = commandNode.getAttribute("name");
        int x = Integer.parseInt(commandNode.getAttribute("x"));
        int y = Integer.parseInt(commandNode.getAttribute("y"));
        int radius = Integer.parseInt(commandNode.getAttribute("radius"));
        String color = commandNode.getAttribute("color");

        Point2D.Float coordinates = new Point2D.Float(x, y);
        boolean no_error = true;

        Element status = null;

        if (map2.containsKey(coordinates)) {

            status = xml_file.generateErrorTag("duplicateCityCoordinates");
            no_error = false;
        }
        else if (map1.containsKey(name)) {

            status = xml_file.generateErrorTag("duplicateCityName");
            no_error = false;
        }
        else {
            City city = new City(name, coordinates, radius, color);
            map1.put(name, city);
            map2.put(coordinates, city);
            status = xml_file.generateSuccessTag();
        }

        // status node will be the top of our block
        Element command = xml_file.generateCommandTag(commandNode.getTagName());
        status.appendChild(command);

        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);

        Element name_node = xml_file.generateParameterTag("name", name);
        params.appendChild(name_node);
        Element x_node = xml_file.generateParameterTag("x", Integer.toString(x));
        params.appendChild(x_node);
        Element y_node = xml_file.generateParameterTag("y", Integer.toString(y));
        params.appendChild(y_node);
        Element radius_node = xml_file.generateParameterTag("radius", Integer.toString(radius));
        params.appendChild(radius_node);
        Element color_node = xml_file.generateParameterTag("color", color);
        params.appendChild(color_node);

        if (no_error) {
            Element output = xml_file.generateOutputTag();
            status.appendChild(output);
        }
        return status;
    }


    /**
     * Removes city from both dictionaries and the PR QuadTree
     * @param commandNode
     * @return
     */
    public Element deleteCity(Element commandNode) {

        Element status = null;
        String city = commandNode.getAttribute("name");
        boolean no_error = true;

        if (!map1.containsKey(city)) {
            status = xml_file.generateErrorTag("cityDoesNotExist");
            no_error = false;
        }
        else {
            if (mapped.contains(city)) {

                // unmapCity(commandNode);
                mapped.remove(city);
            }
            map1.remove(city);
            map2.remove(city);
            status = xml_file.generateSuccessTag();
        }

        Element command = xml_file.generateCommandTag(commandNode.getTagName());
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);
        Element name_node = xml_file.generateParameterTag("name", name);
        params.appendChild(name_node);

        if (no_error) {
            Element output = xml_file.generateOutputTag();
            status.appendChild(output);
        }
        return status;
    }


    /**
     * Clears all mappings from both dictionaries and the PR QuadTree
     */
    public Element clearAll(Element commandNode) {

        map1.clear();
        map2.clear();
        prqt = new PRQuadTree();

        Element success = xml_file.generateSuccessTag();
        Element command = xml_file.generateCommandTag(commandNode.getTagName());
        success.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        success.appendChild(params);
        Element output = xml_file.generateOutputTag();
        success.appendChild(output);

        return success;
    }




    // TO DO: add listCities() back in here



    /**
     * Adds city object to the PR QuadTree
     * @param results
     * @param city
     * @return
     */
    public static Element mapCity(Document results, City city, int dim) {

        Element node = null;

        if (!map1.containsKey(city)) {
            node = results.createElement("error");
            node.setAttribute("type", "nameNotInDictionary");
        }
        else if (mapped.contains(city)) {
            node = results.createElement("error");
            node.setAttribute("type", "cityAlreadyMapped");
        }
        else if (city.x < 0 || city.x >= 256 || city.y < 0 || city.y >= 256) {
            node = results.createElement("error");
            node.setAttribute("type", "cityOutOfBounds");
        }
        else {
            float radius = dim/2;
            prqt.insert(city, new Point2D.Float(radius, radius), dim);
            mapped.add(city);
        }

        return node;
    }




}
