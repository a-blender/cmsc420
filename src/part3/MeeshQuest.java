package cmsc420.meeshquest.part3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmsc420.command.Command;
import cmsc420.xml.XmlUtility;

/**
 * The Main MeeshQuest for input and output
 * 
 * Canonical Solution
 * 
 * @editor Ruofei Du
 */
public class MeeshQuest {
	/**
	 *  whether to open local XML test file or test on submit server. -Ruofei 
	 */
	private static final boolean LOCAL_TEST = false; 
	private static final boolean GENERATE_JUNIT_SRC = false;
	private static String testName = "";
	
	/**
	 * The input stream file
	 */
	private final InputStream systemInput = System.in;
	/**
	 * The XML input file
	 */
	private File xmlInput;
	/**
	 * The XML output file
	 */
	private File xmlOutput;	

	/**
	 * output DOM Document tree 
	 */
	private Document results;

	/**
	 * processes each command
	 */
	private Command command;

    public static void main(String[] args) {
        final MeeshQuest m = new MeeshQuest();
        
		if (LOCAL_TEST) {
			File[] files = new File("testfiles/").listFiles();
			
			PrintWriter JUnitWriter = null;

			if(GENERATE_JUNIT_SRC) {
				try {
					JUnitWriter = new PrintWriter("junit.txt");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}	
			}
			
			Pattern p = Pattern.compile(".*input.xml$");
			for (File file : files) {
				testName = file.getName();
				Matcher matcher = p.matcher(testName);
				if (file.isFile() && matcher.matches()) {
					testName = testName.substring(0, testName.length() - 10);
					m.processInput();
					
					if(GENERATE_JUNIT_SRC) {
						String testSubName = testName.substring(6, testName.length());
						JUnitWriter.write("\tpublic void test_" + testSubName + "() throws Exception {\n\t\trunXmlTest(\""
								+ testName + "\");\n\t}\n");
					}
				}
			}
			
			if(GENERATE_JUNIT_SRC) {
				JUnitWriter.close();
			}
		} else {
			m.processInput();
		}
    }

    public void processInput() {
        try {
			
			if (LOCAL_TEST) {
				System.out.println("Open " + "part2." + testName);
				xmlInput = new File("testfiles/" + testName + ".input.xml");
			}
			Document doc = LOCAL_TEST ? XmlUtility.validateNoNamespace(xmlInput) : XmlUtility.validateNoNamespace(systemInput); 

            /* create output */
            results = XmlUtility.getDocumentBuilder().newDocument();
            command = new Command();
            command.setResults(results);

            /* process commands element */
            Element commandNode = doc.getDocumentElement();
            processCommand(commandNode);

            /* process each command */
            final NodeList nl = commandNode.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Document.ELEMENT_NODE) {
                    /* need to check if Element (ignore comments) */
                    commandNode = (Element) nl.item(i);
                    processCommand(commandNode);
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
            addFatalError();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            addFatalError();
        } catch (IOException e) {
            e.printStackTrace();
            addFatalError();
        } catch (TransformerException e) {
			e.printStackTrace();
		} finally {
			if (LOCAL_TEST) {
				try {
					xmlOutput = new File("testfiles/" + testName + ".output.xml");
					XmlUtility.write(results, xmlOutput);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					XmlUtility.print(results);
				} catch (TransformerException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
        }
    }

    private void addFatalError() {
        try {
            results = XmlUtility.getDocumentBuilder().newDocument();
            final Element fatalError = results.createElement("fatalError");
            results.appendChild(fatalError);
        } catch (ParserConfigurationException e) {
            System.exit(-1);
        }
    }

    private void processCommand(final Element commandNode) throws IOException, ParserConfigurationException,
            TransformerException {
        final String name = commandNode.getNodeName();
        
        if (name.equals("commands")) {
            command.processCommands(commandNode);
        } else if (name.equals("createCity")) {
            command.processCreateCity(commandNode);
        } else if (name.equals("clearAll")) {
            command.processClearAll(commandNode);
        } else if (name.equals("listCities")) {
            command.processListCities(commandNode);
        } else if (name.equals("printTreap")) {
            command.processPrintTreap(commandNode);
        } else if (name.equals("mapRoad")) {
            command.processMapRoad(commandNode);
        } else if (name.equals("mapCity")) {
            command.processMapCity(commandNode);
        } else if (name.equals("printPMQuadtree")) {
            command.processPrintPMQuadtree(commandNode);
        } else if (name.equals("saveMap")) {
            command.processSaveMap(commandNode);
        } else if (name.equals("rangeCities")) {
            command.processRangeCities(commandNode);
        } else if (name.equals("rangeRoads")) {
            command.processRangeRoads(commandNode);
        } else if (name.equals("nearestCity")) {
            command.processNearestCity(commandNode);
        } else if (name.equals("nearestIsolatedCity")) {
            command.processNearestIsolatedCity(commandNode);
        } else if (name.equals("nearestRoad")) {
            command.processNearestRoad(commandNode);
        } else if (name.equals("nearestCityToRoad")) {
            command.processNearestCityToRoad(commandNode);
        } else if (name.equals("shortestPath")) {
            command.processShortestPath(commandNode);
        } else {
            System.exit(-1);
        }
    }
}
