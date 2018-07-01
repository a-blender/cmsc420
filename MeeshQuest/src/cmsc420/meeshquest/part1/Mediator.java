package cmsc420.meeshquest.part1;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import cmsc420.drawing.CanvasPlus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.HashMap;
import java.io.File;
import java.util.PriorityQueue;

/**
 * Mediator class to parse commands from input xml file
 */
public class Mediator {

    private int width;                          // spatial width
    private int height;                         // spatial height

    private TreeMap<String, City> map1;         // Dictionary 1
    private TreeMap<Point2D.Float, City> map2;  // Dictionary 2
    private PRQuadTree prqt;                    // PR QuadTree
    private Set<String> mapped;                 // Mapped hash set
    private Element root;                       // Output xml root node
    private XMLBuilder xml_file;                // Output xml builder
    private PriorityQueue<Distance> pq;         // Priority queue for quadtree

    /**
     * Constructs an instance of the mediator class
     * @param results
     */
    public Mediator(Document results, int width, int height) {

        this.width = width;
        this.height = height;

        this.map1 = new TreeMap<String, City>(new CityNameComparator());
        this.map2 = new TreeMap<Point2D.Float, City>(new CityCoordinateComparator());
        this.prqt = new PRQuadTree(width, height);
        this.mapped = new HashSet<String>();

        this.xml_file = new XMLBuilder(results);
        root = results.createElement("results");
        results.appendChild(root);
    }


    /***
     * Returns the spatial width from the xml file
     */
    public int getSpatialWidth() {

        return this.width;
    }


    /***
     * Returns the spatial height from the xml file
     */
    public int getSpatialHeight() {

        return this.height;
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
            case ("printPRQuadtree"):
                node = printPRQuadtree(commandNode);
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

        Element status;

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

        Element status;
        Element output = xml_file.generateOutputTag();
        String city_name = commandNode.getAttribute("name");
        boolean no_error = true;

        if (!map1.containsKey(city_name)) {
            status = xml_file.generateErrorTag("cityDoesNotExist");
            no_error = false;
        }
        else {

            // remove city from both dictionaries

            City city = map1.get(city_name);
            Point2D.Float coordinates = city.getCoordinates();
            map1.remove(city_name);
            map2.remove(coordinates);

            // if mapped, remove city from pr quadtree and mapped list

            if (mapped.contains(city_name)) {

                prqt.delete(city);
                mapped.remove(city_name);

                HashMap< String,String> attributes = new HashMap< String,String>();
                attributes.put("name", city.getName());
                attributes.put("x", Integer.toString((int) city.get_x()));
                attributes.put("y", Integer.toString((int) city.get_y()));
                attributes.put("color", city.getColor());
                attributes.put("radius", Integer.toString(city.getRadius()));

                Element unmapped = xml_file.generateTag("cityUnmapped", attributes);
                output.appendChild(unmapped);
            }

            status = xml_file.generateSuccessTag();
        }

        Element command = xml_file.generateCommandTag(commandNode.getTagName());
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);
        Element name_node = xml_file.generateParameterTag("name", city_name);
        params.appendChild(name_node);

        if (no_error) {
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
        prqt.clear();

        Element success = xml_file.generateSuccessTag();
        Element command = xml_file.generateCommandTag(commandNode.getTagName());
        success.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        success.appendChild(params);
        Element output = xml_file.generateOutputTag();
        success.appendChild(output);

        return success;
    }


    /**
     * Prints all cities from one dictionary
     */
    public Element listCities(Element commandNode) {

        Element status;
        String sort = commandNode.getAttribute("sortBy");
        boolean command_succeeded = false;

        if (map1.isEmpty()) {
            status = xml_file.generateErrorTag("noCitiesToList");
        }
        else {
            status = xml_file.generateSuccessTag();
            command_succeeded = true;
        }

        // status node will be the top
        Element command = xml_file.generateCommandTag(commandNode.getTagName());
        status.appendChild(command);

        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);
        Element sort_node = xml_file.generateParameterTag("sortBy", sort);
        params.appendChild(sort_node);

        Element output = xml_file.generateOutputTag();
        status.appendChild(output);


        if (command_succeeded && sort.equals("name")) {

            Element cityList = xml_file.generateTag("cityList");
            output.appendChild(cityList);

            for (City city : map1.values()) {

                HashMap< String,String> attributes = new HashMap< String,String>();
                attributes.put("name", city.getName());
                attributes.put("x", Integer.toString((int) city.get_x()));
                attributes.put("y", Integer.toString((int) city.get_y()));
                attributes.put("color", city.getColor());
                attributes.put("radius", Integer.toString(city.getRadius()));

                Element city_node = xml_file.generateTag("city", attributes);
                cityList.appendChild(city_node);
            }
        }
        else if (command_succeeded && sort.equals("coordinate")) {

            Element cityList = xml_file.generateTag("cityList");
            output.appendChild(cityList);

            for (City city : map2.values()) {

                HashMap< String,String> attributes = new HashMap< String,String>();
                attributes.put("name", city.getName());
                attributes.put("x", Integer.toString((int) city.get_x()));
                attributes.put("y", Integer.toString((int) city.get_y()));
                attributes.put("color", city.getColor());
                attributes.put("radius", Integer.toString(city.getRadius()));

                Element city_node = xml_file.generateTag("city", attributes);
                cityList.appendChild(city_node);
            }
        }
        return status;
    }


    /**
     * Adds city object to the PR QuadTree
     * @param commandNode
     * @return
     */
    public Element mapCity(Element commandNode) {

        Element status;
        boolean no_error = true;
        String city_name = commandNode.getAttribute("name");
        float city_x, city_y;

        if (!map1.containsKey(city_name)) {

            status = xml_file.generateErrorTag("nameNotInDictionary");
            no_error = false;
        }
        else if (mapped.contains(city_name)) {

            status = xml_file.generateErrorTag("cityAlreadyMapped");
            no_error = false;
        }
        else {

            city_x = map1.get(city_name).x;
            city_y = map1.get(city_name).y;

            if (city_x < 0 || city_x >= 256 || city_y < 0 || city_y >= 256) {

                status = xml_file.generateErrorTag("cityOutOfBounds");
                no_error = false;
            }
            else {
                // insert the city into the PR QuadTree
                int dim = prqt.getWidth();
                int radius = prqt.getWidth() / 2;
                Point2D.Float center = new Point2D.Float(radius, radius);
                City city = map1.get(city_name);
                prqt.insert(city, center, dim);
                mapped.add(city.getName());
                status = xml_file.generateSuccessTag();
            }
        }

        Element command = xml_file.generateCommandTag(commandNode.getTagName());
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);
        Element name_node = xml_file.generateParameterTag("name", city_name);
        params.appendChild(name_node);

        if (no_error) {
            Element output = xml_file.generateOutputTag();
            status.appendChild(output);
        }
        return status;
    }


    /**
     * Removes a city object from the PR QuadTree
     * @param commandNode
     * @return
     */
    public Element unmapCity(Element commandNode) {

        Element status;
        String city_name = commandNode.getAttribute("name");
        City city;
        boolean no_error = true;

        if (!map1.containsKey(city_name)) {
            status = xml_file.generateErrorTag("nameNotInDictionary");
            no_error = false;
        }
        else if (!mapped.contains(city_name)) {
            status = xml_file.generateErrorTag("cityNotMapped");
            no_error = false;
        }
        else {
            // delete city from the pr quadtree
            city = map1.get(city_name);
            prqt.delete(city);
            status = xml_file.generateSuccessTag();
        }

        Element command = xml_file.generateCommandTag(commandNode.getTagName());
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);
        Element name_node = xml_file.generateParameterTag("name", city_name);
        params.appendChild(name_node);

        if (no_error) {
            Element output = xml_file.generateOutputTag();
            status.appendChild(output);
        }
        return status;
    }


    /**
     * Prints the PR QuadTree as an xml representation
     * @param commandNode
     * @return
     */
    public Element printPRQuadtree(Element commandNode) {

        Element status;
        boolean no_error = true;

        if (prqt.getRoot() instanceof WhiteNode) {
            status = xml_file.generateErrorTag("mapIsEmpty");
            no_error = false;
        }
        else {
            status = xml_file.generateSuccessTag();
        }

        Element command = xml_file.generateCommandTag(commandNode.getTagName());
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);

        if (no_error) {
            Element output = xml_file.generateOutputTag();
            Element quadtree = xml_file.generateTag("quadtree");
            Element results = printHelper(prqt.getRoot(), quadtree);
            output.appendChild(results);
            status.appendChild(output);
        }
        return status;
    }


    public Element printHelper(PRQTNode root, Element output) {

        Element new_node = null;

        if (root instanceof WhiteNode) {
            new_node = xml_file.generateTag("white");
            output.appendChild(new_node);

        }
        else if (root instanceof BlackNode) {

            City city = ((BlackNode) root).getCity();
            HashMap< String,String> attributes = new HashMap< String,String>();
            attributes.put("name", city.getName());
            attributes.put("x", Integer.toString((int) city.get_x()));
            attributes.put("y", Integer.toString((int) city.get_y()));

            new_node = xml_file.generateTag("black", attributes);
            output.appendChild(new_node);
        }
        else if (root instanceof GrayNode) {

            float gray_x = ((GrayNode) root).getCenter().x;
            float gray_y = ((GrayNode) root).getCenter().y;
            HashMap< String,String> attributes = new HashMap< String,String>();
            attributes.put("x", Integer.toString((int) gray_x));
            attributes.put("y", Integer.toString((int) gray_y));

            new_node = xml_file.generateTag("gray", attributes);
            output.appendChild(new_node);

            // recursively print child nodes

            for (PRQTNode child : ((GrayNode) root).getChildren()) {
                printHelper(child, new_node);
            }
        }
        return output;
    }


    /**
     * Saves the current spatial map to a file
     * @param commandNode
     * @return
     */
    public Element saveMap(Element commandNode) {

        Element status = null;
        CanvasPlus canvas = null;
        String filename = commandNode.getAttribute("name");

        try {
            canvas = buildMap();
            canvas.save(filename);
            canvas.dispose();
            status = xml_file.generateSuccessTag();
        }
        catch(IOException e) {
            System.out.println("saveMap error");
;        }

        Element command = xml_file.generateCommandTag(commandNode.getTagName());
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);
        Element file = xml_file.generateParameterTag("name", filename);
        params.appendChild(file);
        Element output = xml_file.generateOutputTag();
        status.appendChild(output);
        return status;
    }


    private CanvasPlus buildMap() {

        // create a new canvasplus map

        final CanvasPlus canvas = new CanvasPlus("MeeshQuest");
        canvas.setFrameSize(width, height);
        canvas.addRectangle(0, 0, width, height, Color.WHITE, true);
        canvas.addRectangle(0, 0, width, height, Color.BLACK, false);

        // call helper to add elements from the pr quadtree

        buildMapHelper(prqt.getRoot(), canvas);

        // return the map

        return canvas;
    }


    private void buildMapHelper(PRQTNode node, CanvasPlus canvas) {

        /*
        black node - add black city point with radius
        gray node - add a cross using the center/dimension from that node
         */

        float x, y, radius;
        String color;

        if (node instanceof BlackNode) {

            City city = ((BlackNode) node).getCity();
            String name = city.getName();
            x = city.get_x();
            y = city.get_y();
            radius = city.getRadius();
            color = city.getColor();

            canvas.addPoint(name, x, y, Color.getColor(color));
            canvas.addCircle(x, y, radius, Color.getColor(color), false);
        }
        else if (node instanceof GrayNode) {

            x = ((GrayNode) node).getCenter().x;
            y = ((GrayNode) node).getCenter().y;
            radius = ((GrayNode) node).getDimension()/2;

            canvas.addCross(x, y, radius, Color.GRAY);

            for (int i = 0; i <= 3; i++) {
                buildMapHelper(((GrayNode) node).getChildren()[i], canvas);
            }
        }
    }


    /**
     * Lists the cities present within a radius of (x,y)
     * Parameters: x, y, radius, saveMap (optional)
     * @param commandNode
     * @return
     */
    public Element rangeCities(Element commandNode) {

        System.out.println("HERE");
        Element status, output = null;
        boolean no_error = true;

        String x_param = commandNode.getAttribute("x");
        String y_param = commandNode.getAttribute("y");
        String radius_param = commandNode.getAttribute("radius");
        String file_param;
        File file;

        if (commandNode.hasAttribute("saveMap")) {
            file_param = commandNode.getAttribute("saveMap");
            file = new File(file_param);

            // TO DO: finish this
        }

        // error if radius = 0, no city exists, or rangeCities() returns none
        int x = Integer.parseInt(x_param);
        int y = Integer.parseInt(y_param);
        int radius = Integer.parseInt(radius_param);
        Point2D.Float coords = new Point2D.Float(x, y);

        if (radius == 0 || !map2.containsKey(coords)) {
            status = xml_file.generateErrorTag("noCitiesExistInRange");
            no_error = false;
            System.out.println("1");
        }
        else {


            status = xml_file.generateSuccessTag();
            output = xml_file.generateOutputTag();
            Element cityList = xml_file.generateTag("cityList");
            output.appendChild(cityList);

            // create a new priority queue and add distances for all mapped cities

            pq = new PriorityQueue<Distance>(new PQComparator());
            for (String name : mapped) {
                pq.add(new Distance(map1.get(name), coords));
            }

            System.out.println("2");


            // find cities up to a certain radius

            boolean within_radius = true;
            while (within_radius) {

                System.out.println("3");
                City city = nearestCityHelper(x_param, y_param);
                Distance d = new Distance(city, coords);
                System.out.println("4");

                if (d.getDistance() > radius) {
                    within_radius = false;
                }
                else {

                    HashMap< String,String> attributes = new HashMap< String,String>();
                    attributes.put("name", city.getName());
                    attributes.put("x", Integer.toString((int) city.get_x()));
                    attributes.put("y", Integer.toString((int) city.get_y()));
                    attributes.put("color", city.getColor());
                    attributes.put("radius", Integer.toString(city.getRadius()));

                    Element city_node = xml_file.generateTag("city", attributes);
                    cityList.appendChild(city_node);
                }
            }
        }

        Element command = xml_file.generateCommandTag(commandNode.getTagName());
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);
        Element x_node = xml_file.generateParameterTag("x", x_param);
        Element y_node = xml_file.generateParameterTag("y", y_param);
        Element radius_node = xml_file.generateParameterTag("radius", radius_param);
        params.appendChild(x_node);
        params.appendChild(y_node);
        params.appendChild(radius_node);

        if (no_error) {
            status.appendChild(output);
        }
        return status;
    }


    /**
     * Finds the name, location of the nearest city
     * @param commandNode
     * @return
     */
    public Element nearestCity(Element commandNode) {

        Element status, output = null;
        String x_param = commandNode.getAttribute("x");
        String y_param = commandNode.getAttribute("y");
        boolean no_error = true;

        if (prqt.getRoot() instanceof WhiteNode) {
            status = xml_file.generateErrorTag("mapIsEmpty");
            no_error = false;
        }
        else {

            status = xml_file.generateSuccessTag();

            // get the nearest city and append that to the output


            City city = nearestCityHelper(x_param, y_param);
            output = xml_file.generateOutputTag();

            HashMap< String,String> attributes = new HashMap< String,String>();
            attributes.put("name", city.getName());
            attributes.put("x", Integer.toString((int) city.get_x()));
            attributes.put("y", Integer.toString((int) city.get_y()));
            attributes.put("color", city.getColor());
            attributes.put("radius", Integer.toString(city.getRadius()));

            Element result = xml_file.generateTag("city", attributes);
            output.appendChild(result);
        }

        Element command = xml_file.generateCommandTag(commandNode.getTagName());
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);
        Element x_node = xml_file.generateParameterTag("x", x_param);
        Element y_node = xml_file.generateParameterTag("y", y_param);
        params.appendChild(x_node);
        params.appendChild(y_node);

        if (no_error) {
            status.appendChild(output);
        }
        return status;
    }


    /**
     * Helper function that calculates the nearest city to (x,y)
     * @param x_str = x parameter
     * @param y_str = y parameter
     * @return
     */
    public City nearestCityHelper(String x_str, String y_str) {

        int x = Integer.parseInt(x_str);
        int y = Integer.parseInt(y_str);
        Point2D.Float point = new Point2D.Float(x, y);

        // create a priority queue
        // create distance objects from all mapped cities and add them

        PriorityQueue<Distance> pq = new PriorityQueue<Distance>(new PQComparator());

        for (String name : mapped) {
            Distance distance = new Distance(map1.get(name), point);
            pq.add(distance);
        }
        Distance result = pq.poll();
        pq.remove(result);
        return result.getCity();
    }

}
