package cmsc420.meeshquest.part2;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Geometry2D;
import cmsc420.sortedmap.ReverseComparator;
import cmsc420.sortedmap.Treap;
import cmsc420.sortedmap.TreapNode;
import cmsc420.sortedmap.TreapTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import cmsc420.geom.Inclusive2DIntersectionVerifier;

/**
 * Mediator class to parse commands from input xml file
 */
public class Mediator {

    private int width;                                  // spatial width
    private int height;                                 // spatial height
    private int order;                                  // PM Quadtree order

    private TreeMap<String, City> map1;                 // Dictionary 1
    private TreeMap<Point2D.Float, City> map2;          // Dictionary 2

    private Element root;                               // Output xml root node
    private XMLBuilder xml_file;                        // Output xml builder
    private PriorityQueue<CityDistance> pq;             // Priority queue for quadtree
    private Treap treap;                                // Treap (bst + max heap)

    private PMQuadtree pmqt;                            // PM Quadtree
    private Set<String> mappedCities;                   // Set of mapped city objects
    private TreeMap<String, TreeSet<String>> adjList;   // Adjacency list of roads

    /**
     * Constructs an instance of the mediator class
     * @param results
     */
    public Mediator(Document results, int width, int height, int order) {

        this.width = width;
        this.height = height;
        this.order = order;

        this.map1 = new TreeMap<>(new CityNameComparator());
        this.map2 = new TreeMap<>(new CityCoordinateComparator());
        this.pmqt = new PMQuadtree(width, height);
        this.mappedCities = new HashSet<>();
        this.adjList = new TreeMap<>();

        this.xml_file = new XMLBuilder(results);
        root = results.createElement("results");
        results.appendChild(root);

        treap = new Treap(new ReverseComparator());
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
            case ("clearAll"):
                node = clearAll(commandNode);
                break;
            case ("listCities"):
                node = listCities(commandNode);
                break;
            case ("printTreap"):
                node = printTreap(commandNode);
                break;
            case ("mapRoad"):
                node = mapRoad(commandNode);
                break;
            case ("mapCity"):
                node = mapCity(commandNode);
                break;
            case ("printPMQuadtree"):
                node = printPMQuadtree(commandNode);
                break;
            case ("saveMap"):
                node = saveMap(commandNode);
                break;
            case ("rangeCities"):
                node = rangeCities(commandNode);
                break;
            case ("rangeRoads"):
                node = rangeRoads(commandNode);
                break;
            case ("nearestCity"):
                node = nearestCity(commandNode);
                break;
            case ("nearestIsolatedCity"):
                node = nearestIsolatedCity(commandNode);
                break;
            case ("nearestRoad"):
                node = nearestRoad(commandNode);
                break;
            /* case:
                node = nearestCityToRoad(commandNode);
                break;
            case ("shortestPath"):
                node = shortestPath(commandNode);
                break; */
        }
        xml_file.appendToDocument(node);
    }

    /**
     * Creates city and adds it to both dictionaries and the treap
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

            map1.put(name, city);                       // add (name, city) to the first dictionary
            map2.put(coordinates, city);                // add (coords, city) to the second dictionary
            treap.put(name, city.getCoordinates());     // add (name, coords) to the treap
            status = xml_file.generateSuccessTag();
        }

        // status node will be the top of our block
        Element command;
        if (commandNode.hasAttribute("id")) {
            String id = commandNode.getAttribute("id");
            command = xml_file.generateCommandTagWithID(commandNode.getTagName(), id);
        }
        else {
            command = xml_file.generateCommandTag(commandNode.getTagName());
        }

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
     * Clears all mappings from both dictionaries, the treap, and pm quadtree
     */
    public Element clearAll(Element commandNode) {

        map1.clear();
        map2.clear();
        treap.clear();
        pmqt.clear();

        Element success = xml_file.generateSuccessTag();
        Element command;
        if (commandNode.hasAttribute("id")) {
            String id = commandNode.getAttribute("id");
            command = xml_file.generateCommandTagWithID(commandNode.getTagName(), id);
        }
        else {
            command = xml_file.generateCommandTag(commandNode.getTagName());
        }
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

        Element status, output = null;
        String sort = commandNode.getAttribute("sortBy");
        boolean no_error = true;

        if (map1.isEmpty()) {
            status = xml_file.generateErrorTag("noCitiesToList");
            no_error = false;
        }
        else {
            status = xml_file.generateSuccessTag();
            output = xml_file.generateOutputTag();
        }

        // status node will be the top
        Element command;
        if (commandNode.hasAttribute("id")) {
            String id = commandNode.getAttribute("id");
            command = xml_file.generateCommandTagWithID(commandNode.getTagName(), id);
        }
        else {
            command = xml_file.generateCommandTag(commandNode.getTagName());
        }
        status.appendChild(command);

        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);
        Element sort_node = xml_file.generateParameterTag("sortBy", sort);
        params.appendChild(sort_node);

        if (no_error) {

            Element cityList = xml_file.generateTag("cityList");

            if (sort.equals("name")) {
                for (City city : map1.values()) {

                    HashMap< String,String> attributes = new HashMap();
                    attributes.put("name", city.getName());
                    attributes.put("x", Integer.toString((int) city.get_x()));
                    attributes.put("y", Integer.toString((int) city.get_y()));
                    attributes.put("color", city.getColor());
                    attributes.put("radius", Integer.toString(city.getRadius()));

                    Element city_node = xml_file.generateTag("city", attributes);
                    cityList.appendChild(city_node);
                }
            }
            else if (sort.equals("coordinate")) {
                for (City city : map2.values()) {

                    HashMap< String,String> attributes = new HashMap();
                    attributes.put("name", city.getName());
                    attributes.put("x", Integer.toString((int) city.get_x()));
                    attributes.put("y", Integer.toString((int) city.get_y()));
                    attributes.put("color", city.getColor());
                    attributes.put("radius", Integer.toString(city.getRadius()));

                    Element city_node = xml_file.generateTag("city", attributes);
                    cityList.appendChild(city_node);
                }
            }
            output.appendChild(cityList);
            status.appendChild(output);
        }
        return status;
    }

    /**
     * Prints all entries in the Treap (key + value + priority)
     * @param commandNode
     * @return
     */
    public Element printTreap(Element commandNode) {

        // RUN THE FUCKING TESTS
        // TreapTest test = new TreapTest();

        Element status;
        boolean no_error = true;

        if (treap.isEmpty()) {
            status = xml_file.generateErrorTag("emptyTree");
            no_error = false;
        }
        else {
            status = xml_file.generateSuccessTag();
        }

        Element command;
        if (commandNode.hasAttribute("id")) {
            String id = commandNode.getAttribute("id");
            command = xml_file.generateCommandTagWithID(commandNode.getTagName(), id);
        }
        else {
            command = xml_file.generateCommandTag(commandNode.getTagName());
        }
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);

        if (no_error) {

            Element output = xml_file.generateOutputTag();
            HashMap<String, String> attributes = new HashMap<String, String>();
            attributes.put("cardinality", Integer.toString(treap.size()));
            Element treap_node = xml_file.generateTag("treap", attributes);
            output.appendChild(treap_node);

            printTreapHelper(treap_node, treap.getRoot());
            status.appendChild(output);
        }
        return status;
    }

    public Element printTreapHelper(Element results, TreapNode node) {

        // recurse left subtree
        if (node == null) {
            Element empty = xml_file.generateTag("emptyChild");
            results.appendChild(empty);
        }
        else {

            // print root node (pre-order)
            HashMap<String, String> attributes = getNodeAttributes(node);
            Element print_node = xml_file.generateTag("node", attributes);
            results.appendChild(print_node);

            printTreapHelper(print_node, node.left);

            printTreapHelper(print_node, node.right);
        }
        return results;
    }

    public HashMap<String, String> getNodeAttributes(TreapNode node) {

        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put("key", node.key.toString());
        attributes.put("value", node.value.toString());

        int x_val = (int) map2.get(node.value).get_x();
        int y_val = (int) map2.get(node.value).get_y();
        attributes.put("value", "(" + x_val + "," + y_val + ")");

        attributes.put("priority", Integer.toString(node.priority));
        return attributes;
    }

    /**
     * Adds a road object to the pm quadtree
     * @param commandNode
     * @return
     */
    public Element mapRoad(Element commandNode) {

        Element status;
        boolean no_error = true;
        String start_city = commandNode.getAttribute("start");
        String end_city = commandNode.getAttribute("end");

        if (!map1.containsKey(start_city)) {

            status = xml_file.generateErrorTag("startPointDoesNotExist");
            no_error = false;
        }
        else if (!map1.containsKey(end_city)) {

            status = xml_file.generateErrorTag("endPointDoesNotExist");
            no_error = false;
        }
        else if (start_city.equals(end_city)) {

            status = xml_file.generateErrorTag("startEqualsEnd");
            no_error = false;
        }
        // At this point, we can assume both cities exist
        else {
            City city1 = map1.get(start_city);
            City city2 = map1.get(end_city);

             if (city1.isIsolated() || city2.isIsolated()) {

                status = xml_file.generateErrorTag("startOrEndIsIsolated");
                no_error = false;
            }
            else if (adjList.containsKey(start_city) &&
                     adjList.get(start_city).contains(end_city)) {

                 status = xml_file.generateErrorTag("roadAlreadyMapped");
                 no_error = false;
            }
            else if (!validateRoad(city1, city2)) {

                 status = xml_file.generateErrorTag("roadOutOfBounds");
                 no_error = false;
            }
            // if we've reached this point
             // we can assume, a road can be added to the pmqt
            else {
                 int dim = pmqt.getWidth();
                 int radius = pmqt.getWidth() / 2;
                 Point2D.Float center = new Point2D.Float(radius, radius);

                 if (!mappedCities.contains(start_city)) {
                     pmqt.insert(city1, center, dim);
                     mappedCities.add(start_city);
                 }
                 if (!mappedCities.contains((end_city))) {
                     pmqt.insert(city2, center, dim);
                     mappedCities.add(end_city);
                 }
                 Road road = new Road(city1, city2);
                 pmqt.insert(road, center, dim);
                 addMappedRoad(start_city, end_city);
                 status = xml_file.generateSuccessTag();
             }
        }
        Element command;
        if (commandNode.hasAttribute("id")) {
            String id = commandNode.getAttribute("id");
            command = xml_file.generateCommandTagWithID(commandNode.getTagName(), id);
        }
        else {
            command = xml_file.generateCommandTag(commandNode.getTagName());
        }
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);
        Element start_node = xml_file.generateParameterTag("start", start_city);
        params.appendChild(start_node);
        Element end_node = xml_file.generateParameterTag("end", end_city);
        params.appendChild(end_node);

        if (no_error) {

            Element output = xml_file.generateOutputTag();
            status.appendChild(output);

            HashMap< String,String> attributes = new HashMap< String,String>();
            attributes.put("start", start_city);
            attributes.put("end", end_city);
            Element road_created = xml_file.generateTag("roadCreated", attributes);
            output.appendChild(road_created);
        }
        return status;
    }

    // THIS HAS BEEN CHANGED RECENTLY
    public void addMappedRoad(String start, String end) {

        // if we're here, we assume the road has not already been mapped

        // option 1: start is mapped, add end to list of roads
        // option 2: start is not mapped, add (start,end)

        if (adjList.containsKey(start)) {
            adjList.get(start).add(end);
        }
        else {
            TreeSet<String> set = new TreeSet<>();
            set.add(end);
            adjList.put(start, set);
        }
    }

    public boolean validateRoad(City start, City end) {

        Line2D.Float line = new Line2D.Float(start.getCoordinates(), end.getCoordinates());
        Rectangle2D.Float plane = new Rectangle.Float(0, 0, width, height);
        boolean roadIntersects = Inclusive2DIntersectionVerifier.intersects(line, plane);

        float start_x = start.get_x();
        float start_y = start.get_y();
        float end_x = end.get_x();
        float end_y = end.get_y();

        return (roadIntersects && start_x >= 0 && start_y >= 0 &&
                end_x >= 0 && end_y >= 0);
    }

    /**
     * Adds a city object to the pm quadtree
     * @param commandNode
     * @return
     */
    public Element mapCity(Element commandNode) {

        Element status;
        boolean no_error = true;
        String city_name = commandNode.getAttribute("name");

        if (!map1.containsKey(city_name)) {

            status = xml_file.generateErrorTag("nameNotInDictionary");
            no_error = false;
        }
        else if (mappedCities.contains(city_name)) {

            status = xml_file.generateErrorTag("cityAlreadyMapped");
            no_error = false;
        }
        else {
            City city = map1.get(city_name);
            if (!validateCity(city)) {

                status = xml_file.generateErrorTag("cityOutOfBounds");
                no_error = false;
            }
            else {
                // insert the city into the pm quadtree
                int dim = pmqt.getWidth();
                int radius = pmqt.getWidth() / 2;
                Point2D.Float center = new Point2D.Float(radius, radius);

                pmqt.insert(city, center, dim);
                mappedCities.add(city_name);
                city.setIsolated(true);
                status = xml_file.generateSuccessTag();
            }
        }
        Element command;
        if (commandNode.hasAttribute("id")) {
            String id = commandNode.getAttribute("id");
            command = xml_file.generateCommandTagWithID(commandNode.getTagName(), id);
        }
        else {
            command = xml_file.generateCommandTag(commandNode.getTagName());
        }
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

    public boolean validateCity(City city) {

        Point2D.Float point = city.getCoordinates();
        Rectangle2D.Float plane = new Rectangle.Float(0, 0, width, height);
        boolean cityIntersects = Inclusive2DIntersectionVerifier.intersects(point, plane);

        float city_x = city.get_x();
        float city_y = city.get_y();
        return (cityIntersects && city_x >= 0 && city_y >= 0);
    }

    /**
     * Prints the PM Quadtree as an xml representation
     * @param commandNode
     * @return
     */
    public Element printPMQuadtree(Element commandNode) {

        Element status;
        boolean no_error = true;

        if (pmqt.getRoot() instanceof WhiteNode) {
            status = xml_file.generateErrorTag("mapIsEmpty");
            no_error = false;
        }
        else {
            status = xml_file.generateSuccessTag();
        }

        Element command;
        if (commandNode.hasAttribute("id")) {
            String id = commandNode.getAttribute("id");
            command = xml_file.generateCommandTagWithID(commandNode.getTagName(), id);
        }
        else {
            command = xml_file.generateCommandTag(commandNode.getTagName());
        }
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);

        if (no_error) {
            Element output = xml_file.generateOutputTag();
            HashMap<String,String> order_attribute = new HashMap<>();
            order_attribute.put("order", Integer.toString(order));
            Element quadtree = xml_file.generateTag("quadtree", order_attribute);

            Element results = printHelper(pmqt.getRoot(), quadtree);
            output.appendChild(results);
            status.appendChild(output);
        }
        return status;
    }

    public Element printHelper(PRQTNode root, Element output) {

        Element new_node;

        if (root instanceof WhiteNode) {
            new_node = xml_file.generateTag("white");
            output.appendChild(new_node);

        }
        else if (root instanceof BlackNode) {

            HashMap< String,String> card_attr = new HashMap<>();
            String card = Integer.toString(((BlackNode) root).getCardinality());
            card_attr.put("cardinality", card);
            new_node = xml_file.generateTag("black", card_attr);
            output.appendChild(new_node);

            // add city to the xml output (if one exists)

            City city = (City) ((BlackNode) root).getCity();

            if (city != null) {
                HashMap< String,String> city_attr = new HashMap<>();
                city_attr.put("name", city.getName());
                city_attr.put("x", Integer.toString((int) city.get_x()));
                city_attr.put("y", Integer.toString((int) city.get_y()));
                city_attr.put("color", city.getColor());
                city_attr.put("radius", Integer.toString(city.getRadius()));
                Element city_node;
                if (city.isIsolated()) {
                    city_node = xml_file.generateTag("isolatedCity", city_attr);
                }
                else {
                    city_node = xml_file.generateTag("city", city_attr);
                }
                new_node.appendChild(city_node);
            }

            for (Geometry2D item : ((BlackNode) root).getRoads()) {

                Road road = (Road) item;
                HashMap<String,String> road_attr = new HashMap<>();

                // "end" must have the higher name (z > a)
                String name1 = road.getStartCity().getName();
                String name2 = road.getEndCity().getName();

                if (name1.compareTo(name2) >= 0) {
                    road_attr.put("start", name2);
                    road_attr.put("end", name1);
                }
                else {
                    road_attr.put("start", name1);
                    road_attr.put("end", name2);
                }
                Element city_node = xml_file.generateTag("road", road_attr);
                new_node.appendChild(city_node);
            }
        }
        else if (root instanceof GrayNode) {

            // modify
            float gray_x = ((GrayNode) root).getCenter().x;
            float gray_y = ((GrayNode) root).getCenter().y;
            HashMap< String,String> attributes = new HashMap<>();
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

        Element status;
        String filename = commandNode.getAttribute("name");
        // TO DO: Create map if you have to test the quadtree

        status = xml_file.generateSuccessTag();
        Element command;
        if (commandNode.hasAttribute("id")) {
            String id = commandNode.getAttribute("id");
            command = xml_file.generateCommandTagWithID(commandNode.getTagName(), id);
        }
        else {
            command = xml_file.generateCommandTag(commandNode.getTagName());
        }
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

        buildMapHelper(pmqt.getRoot(), canvas);
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

            City city = (City) ((BlackNode) node).getCity();
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

        Element status, output = null;
        boolean no_error = true;

        String x_param = commandNode.getAttribute("x");
        String y_param = commandNode.getAttribute("y");
        String radius_param = commandNode.getAttribute("radius");

        // error if radius = 0, no city exists, or rangeCities() returns none
        int x = Integer.parseInt(x_param);
        int y = Integer.parseInt(y_param);
        int radius = Integer.parseInt(radius_param);
        Point2D.Float coords = new Point2D.Float(x, y);

        // create a new priority queue and add distances for all mapped cities

        PriorityQueue<CityDistance> pq = new PriorityQueue(new PQCityComparator());
        for (String name : mappedCities) {
            pq.add(new CityDistance(map1.get(name), coords));
        }

        CityDistance test = pq.peek();
        if (!validatePoint(coords) || test == null || test.getDistance() > radius) {
            status = xml_file.generateErrorTag("noCitiesExistInRange");
            no_error = false;
        }
        else {

            status = xml_file.generateSuccessTag();
            output = xml_file.generateOutputTag();
            Element cityList = xml_file.generateTag("cityList");
            output.appendChild(cityList);

            // append cities to the cityList up to a certain radius away
            // remove from the priority queue as you go

            ArrayList<CityDistance> nearestCities = new ArrayList<>();
            boolean within_radius = true;
            while (within_radius && !pq.isEmpty()) {

                CityDistance distance = pq.poll();
                pq.remove(distance);

                if (distance.getDistance() > radius) {
                    within_radius = false;
                }
                else {
                    nearestCities.add(distance);
                }
            }
            Collections.sort(nearestCities, new RangeCitiesComparator());
            for (CityDistance distance : nearestCities) {
                City city = distance.getCity();
                HashMap<String,String> attributes = new HashMap();
                attributes.put("name", city.getName());
                attributes.put("x", Integer.toString((int) city.get_x()));
                attributes.put("y", Integer.toString((int) city.get_y()));
                attributes.put("color", city.getColor());
                attributes.put("radius", Integer.toString(city.getRadius()));

                Element city_node = xml_file.generateTag("city", attributes);
                cityList.appendChild(city_node);
            }
        }

        Element command;
        if (commandNode.hasAttribute("id")) {
            String id = commandNode.getAttribute("id");
            command = xml_file.generateCommandTagWithID(commandNode.getTagName(), id);
        }
        else {
            command = xml_file.generateCommandTag(commandNode.getTagName());
        }
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);
        Element x_node = xml_file.generateParameterTag("x", x_param);
        Element y_node = xml_file.generateParameterTag("y", y_param);
        Element radius_node = xml_file.generateParameterTag("radius", radius_param);
        params.appendChild(x_node);
        params.appendChild(y_node);
        params.appendChild(radius_node);

        if (commandNode.hasAttribute("saveMap")) {
            String file = commandNode.getAttribute("saveMap");
            Element saveMap_node = xml_file.generateParameterTag("saveMap", file);
            params.appendChild(saveMap_node);

            // TO DO: possibly create the save map
        }
        if (no_error) {
            status.appendChild(output);
        }
        return status;
    }

    public boolean validatePoint(Point2D.Float point) {

        Rectangle2D.Float plane = new Rectangle.Float(0, 0, width, height);
        boolean cityIntersects = Inclusive2DIntersectionVerifier.intersects(point, plane);

        float point_x = point.x;
        float point_y = point.y;

        return (cityIntersects && point_x >= 0 && point_y >= 0);
    }

    /**
     * Finds a list of Roads within a specific radius
     * @param commandNode
     * @return
     */
    public Element rangeRoads(Element commandNode) {

        Element status, output = null;
        boolean no_error = true;

        String x_param = commandNode.getAttribute("x");
        String y_param = commandNode.getAttribute("y");
        String radius_param = commandNode.getAttribute("radius");

        int x = Integer.parseInt(x_param);
        int y = Integer.parseInt(y_param);
        int radius = Integer.parseInt(radius_param);
        Point2D.Float center = new Point2D.Float(x, y);

        // make an arraylist to add nearest roads to

        ArrayList<Road> nearestRoads = new ArrayList<>();

        // add roads that exist / are within range to hashmap
        // this is pretty nifty

        for (Map.Entry<String, TreeSet<String>> entry : adjList.entrySet()) {

            String start_name = entry.getKey();
            City start_city = map1.get(start_name);
            Point2D.Float point1 = new Point2D.Float(start_city.get_x(), start_city.get_y());

            for (String end_name : entry.getValue()) {

                City end_city = map1.get(end_name);
                Point2D.Float point2 = new Point2D.Float(end_city.get_x(), end_city.get_y());
                Line2D.Float line = new Line2D.Float(point1, point2);
                double distance = line.ptSegDist(center);

                // if City at (x,y) is within range, add to hashmap

                if (distance <= radius) {
                    nearestRoads.add(new Road(start_city, end_city));
                }
            }
        }

        if (!validatePoint(center) || nearestRoads.isEmpty()) {
            status = xml_file.generateErrorTag("noRoadsExistInRange");
            no_error = false;
        }
        else {
            status = xml_file.generateSuccessTag();
            output = xml_file.generateOutputTag();
            Element cityList = xml_file.generateTag("roadList");
            output.appendChild(cityList);

            // sort nearest roads in reverse order

            Collections.sort(nearestRoads, new RoadComparator());

            for (Road road : nearestRoads) {

                HashMap< String,String> attributes = new HashMap< String,String>();
                attributes.put("start", road.getStartCity().getName());
                attributes.put("end", road.getEndCity().getName());

                Element road_node = xml_file.generateTag("road", attributes);
                cityList.appendChild(road_node);
            }
        }

        Element command;
        if (commandNode.hasAttribute("id")) {
            String id = commandNode.getAttribute("id");
            command = xml_file.generateCommandTagWithID(commandNode.getTagName(), id);
        }
        else {
            command = xml_file.generateCommandTag(commandNode.getTagName());
        }
        status.appendChild(command);
        Element params = xml_file.generateTag("parameters");
        status.appendChild(params);
        Element x_node = xml_file.generateParameterTag("x", x_param);
        Element y_node = xml_file.generateParameterTag("y", y_param);
        Element radius_node = xml_file.generateParameterTag("radius", radius_param);
        params.appendChild(x_node);
        params.appendChild(y_node);
        params.appendChild(radius_node);

        if (commandNode.hasAttribute("saveMap")) {
            String file = commandNode.getAttribute("saveMap");
            Element saveMap_node = xml_file.generateParameterTag("saveMap", file);
            params.appendChild(saveMap_node);

            // TO DO: possibly create the save map
        }
        if (no_error) {
            status.appendChild(output);
        }
        return status;
    }

    /**
     * Finds the name, location of the nearest non-isolated city
     * @param commandNode
     * @return
     */
    public Element nearestCity(Element commandNode) {

        return nearestCityHelper(commandNode, false);
    }

    /**
     * Finds the name, location of the nearest isolated city
     * @param commandNode
     * @return
     */
    public Element nearestIsolatedCity(Element commandNode) {

        return nearestCityHelper(commandNode, true);
    }

    public Element nearestCityHelper(Element commandNode, boolean isolated) {

        Element status, output = null;
        boolean no_error = true;

        String x_param = commandNode.getAttribute("x");
        String y_param = commandNode.getAttribute("y");
        int x = Integer.parseInt(x_param);
        int y = Integer.parseInt(y_param);
        Point2D.Float point = new Point2D.Float(x, y);

        // create a priority queue
        // create distance objects from all mapped cities and add them

        PriorityQueue<CityDistance> pq = new PriorityQueue<>(new PQCityComparator());

        for (String name : mappedCities) {

            City city = map1.get(name);
            if (city.isIsolated() == isolated) {
                CityDistance distance = new CityDistance(city, point);
                pq.add(distance);
            }
        }

        // check priority queue for the nearest city

        if (pq.isEmpty()) {
            status = xml_file.generateErrorTag("cityNotFound");
            no_error = false;
        }
        else {
            status = xml_file.generateSuccessTag();
            output = xml_file.generateOutputTag();

            // get cities from pq (accounting for distance ties)

            CityDistance distance = pq.peek();
            Double bestDistance = distance.getDistance();
            String highestName = distance.getCity().getName();
            boolean done = false;
            while (!done && !pq.isEmpty()) {

                CityDistance d = pq.poll();
                String name = d.getCity().getName();

                // this line - what the fucking fuck??
                if (d.getDistance() == bestDistance && name.compareTo(highestName) >= 0) {
                    distance = d;
                }
                else if (d.getDistance() > bestDistance) {
                    done = true;
                }
            }
            City city = distance.getCity();
            HashMap< String,String> attributes = new HashMap<>();
            attributes.put("name", city.getName());
            attributes.put("x", Integer.toString((int) city.get_x()));
            attributes.put("y", Integer.toString((int) city.get_y()));
            attributes.put("color", city.getColor());
            attributes.put("radius", Integer.toString(city.getRadius()));

            Element result;
            if (isolated) {
                result = xml_file.generateTag("isolatedCity", attributes);
            }
            else {
                result = xml_file.generateTag("city", attributes);
            }
            output.appendChild(result);
        }
        Element command;
        if (commandNode.hasAttribute("id")) {
            String id = commandNode.getAttribute("id");
            command = xml_file.generateCommandTagWithID(commandNode.getTagName(), id);
        }
        else {
            command = xml_file.generateCommandTag(commandNode.getTagName());
        }
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
     * Finds the nearest road to any given spatial point
     * @param commandNode
     * @return
     */
    public Element nearestRoad(Element commandNode) {

        Element status, output = null;
        boolean no_error = true;

        String x_param = commandNode.getAttribute("x");
        String y_param = commandNode.getAttribute("y");
        int x = Integer.parseInt(x_param);
        int y = Integer.parseInt(y_param);
        Point2D.Float center = new Point2D.Float(x, y);

        // loop through roads and add to pq

        PriorityQueue<RoadDistance> pq = new PriorityQueue(new PQRoadComparator());
        for (Map.Entry<String, TreeSet<String>> entry : adjList.entrySet()) {

            String start_name = entry.getKey();
            City start_city = map1.get(start_name);
            Point2D.Float point1 = new Point2D.Float(start_city.get_x(), start_city.get_y());
            City city1 = map2.get(point1);

            for (String end_name : entry.getValue()) {

                City end_city = map1.get(end_name);
                Point2D.Float point2 = new Point2D.Float(end_city.get_x(), end_city.get_y());
                City city2 = map2.get(point2);

                RoadDistance distance = new RoadDistance(new Road(city1, city2), center);
                pq.add(distance);
            }
        }

        if (!validatePoint(center) || pq.isEmpty()) {
            status = xml_file.generateErrorTag("roadNotFound");
            no_error = false;
        }
        else {
            status = xml_file.generateSuccessTag();
            output = xml_file.generateOutputTag();

            // poll the pq until the distance increases
            // add road distances to an arraylist, sort, and pop off the top

            double lowest = pq.peek().getDistance();
            ArrayList<Road> nearestRoads = new ArrayList<>();

            while (!pq.isEmpty()) {
                RoadDistance distance = pq.poll();
                if (distance.getDistance() == lowest) {
                    nearestRoads.add(distance.getRoad());
                }
            }
            Collections.sort(nearestRoads, new RoadComparator());
            Road road = nearestRoads.get(0);

            // sort nearest roads in reverse order

            HashMap<String,String> attributes = new HashMap<>();
            attributes.put("start", road.getStartCity().getName());
            attributes.put("end", road.getEndCity().getName());
            Element road_node = xml_file.generateTag("road", attributes);
            output.appendChild(road_node);
        }

        Element command;
        if (commandNode.hasAttribute("id")) {
            String id = commandNode.getAttribute("id");
            command = xml_file.generateCommandTagWithID(commandNode.getTagName(), id);
        }
        else {
            command = xml_file.generateCommandTag(commandNode.getTagName());
        }
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

    // TO DO: do nearestCityToRoad
    public Element nearestCityToRoad(Element commandNode) {
        return null;
    }

    // TO DO: do shortestPath
    public Element shortestPath(Element commandNode) {
        return null;
    }

}
