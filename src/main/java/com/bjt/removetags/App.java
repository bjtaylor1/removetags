package com.bjt.removetags;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Usage: removetags <xpath> <file>");
            System.exit(1);
            return;
        }

        String fileName = args[args.length - 1];
        File file = new File(fileName);
        if (!file.exists()) {
            System.err.println("File " + fileName + " does not exist");
            System.exit(1);
            return;
        }

        for (int i = 0; i < args.length - 1; i++) {
            String tag = args[i];
            try {
                int removed = removeTags(file, tag);
                removeBlankLines(file);
                System.out.println(String.format("Removed %d matching %s from %s", removed, tag, file.getName()));
            } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException | TransformerException e) {
                System.err.println(String.format("With file %s and xpath %s:\n%s", file.getName(), tag, e.getMessage()));
            }
        }
    }

    private static void removeBlankLines(File file) {

    }

    private static int removeTags(File file, String tag) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, TransformerException {
        final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        final XPathExpression xPathExpression = XPathFactory.newInstance().newXPath().compile(tag);
        final NodeList nodes = (NodeList) xPathExpression.evaluate(document, XPathConstants.NODESET);
        int removed = 0;
        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            node.getParentNode().removeChild(node);
            removed++;
        }

        if(removed > 0) {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            final DOMSource domSource = new DOMSource(document);
            final StreamResult streamResult = new StreamResult(file);
            transformer.transform(domSource, streamResult);
        }
        return removed;
    }
}
