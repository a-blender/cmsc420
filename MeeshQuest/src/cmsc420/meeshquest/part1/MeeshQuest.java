package cmsc420.meeshquest.part2;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import cmsc420.xml.XmlUtility;
import java.io.File;

/**
 * MeeshQuest main class for the google maps project
 */
public class MeeshQuest {

    /**
     * Main function reads in an xml file in tree format
     * @param args
     */
    public static void main(String[] args) {

    	Document results = null;

        try {
            // Document doc = XmlUtility.validateNoNamespace(System.in);
            Document doc = XmlUtility.validateNoNamespace(new File("part2in.xml"));
            results = XmlUtility.getDocumentBuilder().newDocument();
        
        	Element commandNode = doc.getDocumentElement();
            final NodeList nl = commandNode.getChildNodes();

            int width = Integer.parseInt(commandNode.getAttribute("spatialWidth"));
            int height = Integer.parseInt(commandNode.getAttribute("spatialHeight"));
            Mediator mediator = new Mediator(results, width, height);

            for (int i = 0; i < nl.getLength(); i++) {
        		if (nl.item(i).getNodeType() == Document.ELEMENT_NODE) {
        			commandNode = (Element) nl.item(i);
                
        			/* TODO: Process your commandNode here */
                    mediator.parseCommand(commandNode);
        		}
        	}

        } catch (SAXException | IOException | ParserConfigurationException e) {
        	
        	/* TODO: Process fatal error here */
            try {
                results = XmlUtility.getDocumentBuilder().newDocument();
            }
            catch (ParserConfigurationException e1) {
                System.out.println("parser error");
            }
            Element error = results.createElement("fatalError");
            results.appendChild(error);
        	
		} finally {
            try {
				XmlUtility.print(results);
			} catch (TransformerException e) {

				e.printStackTrace();
			}
        }
    }
}