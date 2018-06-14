package cmsc420.meeshquest.part1;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmsc420.xml.XmlUtility;
import sun.reflect.generics.tree.Tree;

import java.awt.geom.Point2D;
import java.util.TreeMap;
import java.io.File;

/**
 * MeeshQuest main class for the google maps project
 */
public class MeeshQuest {


    /**
     * Dictionary 1
     * maps city names -> city objects
     */
    private static TreeMap<String, City> map1 =
            new TreeMap<String, City>(new CityNameComparator());


    /**
     * Dictionary 2
     * maps city coordinates -> city objects
     */
    private static TreeMap<Point2D.Float, City> map2 =
            new TreeMap<Point2D.Float, City>(new CityCoordinateComparator());


    /**
     * Creates a city object and adds it to both dictionaries
     * @param name=city name
     * @param x=x-coordinate of city
     * @param y=y-coordinate of city
     * @param radius=radius of city point
     * @param color=color of city point
     * @return true or false
     */
    public static Element createCity(Document results, String name, int x, int y,
                                     int radius, String color) {

        Element node = null;
        Point2D.Float coordinates = new Point2D.Float(x, y);
        boolean no_error = true;

        if (map2.containsKey(coordinates)) {

            node = results.createElement("error");
            node.setAttribute("type", "duplicateCityCoordinates");
            no_error = false;
        }
        else if (map1.containsKey(name)) {

            node = results.createElement("error");
            node.setAttribute("type", "duplicateCityName");
            no_error = false;
        }
        else {
            City city = new City(name, coordinates, radius, color);
            map1.put(name, city);
            map2.put(coordinates, city);

            node = results.createElement("success");
        }

        Element command = results.createElement("command");
        command.setAttribute("name", "createCity");
        node.appendChild(command);

        Element params = results.createElement("parameters");
        node.appendChild(params);

        Element name_node = results.createElement("name");
        name_node.setAttribute("value", name);
        params.appendChild(name_node);

        Element x_node = results.createElement("x");
        x_node.setAttribute("value", Integer.toString(x));
        params.appendChild(x_node);

        Element y_node = results.createElement("y");
        y_node.setAttribute("value", Integer.toString(y));
        params.appendChild(y_node);

        Element radius_node = results.createElement("radius");
        radius_node.setAttribute("value", Integer.toString(radius));
        params.appendChild(radius_node);

        Element color_node = results.createElement("color");
        color_node.setAttribute("value", color);
        params.appendChild(color_node);

        if (no_error) {
            Element output = results.createElement("output");
            node.appendChild(output);
        }
        return node;
    }




    /**
     * Prints all cities from one dictionary
     */
    public static Element listCities(Document results, String sort) {

        Element node = null;
        boolean command_succeeded = false;

        if (map1.isEmpty()) {

            node = results.createElement("error");
            node.setAttribute("type", "noCitiesToList");
        }
        else {

            node = results.createElement("success");
            command_succeeded = true;
        }

        Element command = results.createElement("command");
        command.setAttribute("name", "listCities");
        node.appendChild(command);

        Element params = results.createElement("parameters");
        node.appendChild(params);

        Element sortBy = results.createElement("sortBy");
        sortBy.setAttribute("value", sort);
        params.appendChild(sortBy);

        Element output = results.createElement("output");
        node.appendChild(output);

        if (command_succeeded && sort.equals("name")) {

            Element list = results.createElement("cityList");
            output.appendChild(list);

            for (City city : map1.values()) {

                Element city_node = results.createElement("city");
                city_node.setAttribute("name", city.getName());
                city_node.setAttribute("x", Integer.toString((int) city.get_x()));
                city_node.setAttribute("y", Integer.toString((int) city.get_y()));
                city_node.setAttribute("color", city.getColor());
                city_node.setAttribute("radius", Integer.toString(city.getRadius()));
                list.appendChild(city_node);
            }
        }
        else if (command_succeeded && sort.equals("coordinate")) {

            Element list = results.createElement("cityList");
            output.appendChild(list);

            for (City city : map2.values()) {

                Element city_node = results.createElement("city");
                city_node.setAttribute("name", city.getName());
                city_node.setAttribute("x", Integer.toString((int) city.get_x()));
                city_node.setAttribute("y", Integer.toString((int) city.get_y()));
                city_node.setAttribute("color", city.getColor());
                city_node.setAttribute("radius", Integer.toString(city.getRadius()));
                list.appendChild(city_node);
            }
        }
        return node;
    }


    public static Element mapCity(Document results, City city) {

        /*
        TO DO: check if city is in the "mapped" arraylist
        if yes -> generate an error
        if no -> add city to the pr quadtree, add city to the "mapped" arraylist
        return xml result
         */

        return null;
    }


    public static Element unmapCity(Document results, City city) {

        /*
        TO DO: check if the city is in the "mapped" arraylist
        if yes -> remove city from pr quadtree, remove city from "mapped" arraylist
        if no -> generate an error
         */

        return null;
    }


    public static Element deleteCity(Document results, City city) {

        /*
        TO DO: call unmapCity() to remove city from the pr quadtree
        remove city from both dictionaries
        return xml result
         */

        return null;
    }


    /**
     * Clears all mappings from both dictionaries
     */
    public static Element clearAll(Document results) {
        map1.clear();
        map2.clear();

        Element node = results.createElement("success");

        Element command = results.createElement("command");
        command.setAttribute("name", "clearAll");
        node.appendChild(command);

        Element params = results.createElement("parameters");
        node.appendChild(params);

        Element output = results.createElement("output");
        node.appendChild(output);

        /*
        TO DO: remove all nodes from the pr quadtree
         */

        return node;
    }


    /**
     * Main function reads in an xml file in tree format
     * @param args
     */
    public static void main(String[] args) {
    	
    	Document results = null;

        try {
        	Document doc = XmlUtility.validateNoNamespace(System.in);
            //Document doc = XmlUtility.validateNoNamespace(new File("part1in.xml"));
            results = XmlUtility.getDocumentBuilder().newDocument();
        
        	Element commandNode = doc.getDocumentElement();
            final NodeList nl = commandNode.getChildNodes();

            Element root = results.createElement("results");
            results.appendChild(root);

        	for (int i = 0; i < nl.getLength(); i++) {
        		if (nl.item(i).getNodeType() == Document.ELEMENT_NODE) {
        			commandNode = (Element) nl.item(i);
                
        			/* TODO: Process your commandNode here */

                    /* student code for part 0 */
                    if(commandNode.getNodeName().equals("createCity")) {

                        String name = commandNode.getAttribute("name");
                        String x_str = commandNode.getAttribute("x");
                        String y_str = commandNode.getAttribute("y");
                        String radius_str = commandNode.getAttribute("radius");
                        String color = commandNode.getAttribute("color");

                        if (name != null && x_str != null && y_str != null && radius_str != null && color != null) {

                            int x = Integer.parseInt(x_str);
                            int y = Integer.parseInt(y_str);
                            int radius = Integer.parseInt(radius_str);
                            root.appendChild(createCity(results, name, x, y, radius, color));
                        }

                    }
                    else if(commandNode.getNodeName().equals("clearAll")) {
                        root.appendChild(clearAll(results));
                    }
                    else if (commandNode.getNodeName().equals("listCities")) {

                        String sort = commandNode.getAttribute("sortBy");


                        root.appendChild(listCities(results, sort));
                    }
        		}
        	}

        } catch (SAXException | IOException | ParserConfigurationException e) {
        	
        	/* TODO: Process fatal error here */
        	
		} finally {
            try {
				XmlUtility.print(results);
			} catch (TransformerException e) {
				e.printStackTrace();
			}
        }
    }
}