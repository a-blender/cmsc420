package cmsc420.meeshquest.part2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.Map;

/**
 * Helper class to build XML documents
 * Primarily to avoid writing duplicate code
 */
public class XMLBuilder {


    private Document doc;


    public XMLBuilder(Document doc) {
        this.doc = doc;
    }


    public Document getDocument() {
        return this.doc;
    }


    public void appendToDocument(Element node) {
        doc.getDocumentElement().appendChild(node);
    }


    public Element generateSuccessTag() {
        return doc.createElement("success");
    }


    public Element generateErrorTag(String error) {
        Element node = doc.createElement("error");
        node.setAttribute("type", error);
        return node;
    }


    public Element generateCommandTag(String commandName) {
        Element tag = doc.createElement("command");
        tag.setAttribute("name", commandName);
        return tag;
    }

    public Element generateCommandTagWithID(String commandName, String id) {
        Element tag = doc.createElement("command");
        tag.setAttribute("name", commandName);
        tag.setAttribute("id", id);
        return tag;
    }


    public Element generateParameterTag(String parameterName, String value) {
        Element tag = doc.createElement(parameterName);
        tag.setAttribute("value", value);
        return tag;
    }


    public Element generateOutputTag() {
        return doc.createElement("output");
    }


    public Element generateTag(String tagName) {
        return doc.createElement(tagName);
    }


    public Element generateTag(String tagName, Map<String,String> attributes) {
        Element main = doc.createElement(tagName);
        for (Map.Entry<String,String> entry : attributes.entrySet()) {
            main.setAttribute(entry.getKey(), entry.getValue());
        }
        return main;
    }
}