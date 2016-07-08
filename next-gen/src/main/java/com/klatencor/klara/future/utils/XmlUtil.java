package com.klatencor.klara.future.utils;
/**
 * Provide utility for developers to generate xml documents.
 * @author zhiyang
 */
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class XmlUtil {
	
	private final static Logger logger = Logger.getLogger(XmlUtil.class);
	
	private Document document;
	
	public XmlUtil() {
		newDocument();
	}
	
	public Element newDocument(String rootName) {
		newDocument();
		Element root = document.createElement(rootName);
		document.appendChild(root);
		return root;
	}
	
	
	/**
	 * Create a document instance.
	 */
	public void newDocument() {		
		DocumentBuilderFactory dbFactory ;
		DocumentBuilder dBuilder ;		
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			document = dBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage(), e);
		}	
	}
	
	/**
	 * Create a element with node value of {@code nodeVal} and attributes.
	 * If {@code nodeVal} = null, node value is not set.
	 */
	public Element createElement(String name, String nodeVal, String... attributes) {
		Element element = document.createElement(name);
		for (int i = 0; i < attributes.length; i+=2) {
			element.setAttribute(attributes[i], attributes[i+1]);
		}
		if (nodeVal != null) {
			element.setTextContent(nodeVal);
		}
		return element;
	}
	
	/**
	 * Create an element with attributes
	 * @param name is the name of the element.
	 * @param attributes contain the name and the value of attributes.
	 * @return the element
	 * @throws ParserConfigurationException
	 */
	public Element createElement(String name, HashMap<String, String> attributes) throws ParserConfigurationException{	
		Element element = document.createElement(name);
		for(String key: attributes.keySet()){
			element.setAttribute(key, attributes.get(key));
		}
		return element;
	}
	
	public void append(Element father, Element child){
		father.appendChild(child);
	}
	
	public void appendDocument(Element ele) {
		document.appendChild(ele);
	}
	
	public String getString() throws ParserConfigurationException, TransformerException{
		TransformerFactory transfac = TransformerFactory.newInstance();
		Transformer trans = transfac.newTransformer();
		trans.setOutputProperty(OutputKeys.METHOD, "xml");
		trans.setOutputProperty(OutputKeys.INDENT, "no"); //Omit indent
		trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); //omit XML declaration.
		trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(2));
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(document.getDocumentElement());
		trans.transform(source, result);
		String xmlString = sw.toString();		
		return xmlString;
	}
	
	public static void main(String[] args) throws ParserConfigurationException, TransformerException{
		XmlUtil creator = new XmlUtil();
		HashMap<String, String> rootAttr = new HashMap<String, String>();
		rootAttr.put("attr0", "value0");
		rootAttr.put("attr1", "value1");
		Element root = creator.createElement("root", rootAttr);
		creator.appendDocument(root);
		HashMap<String, String> childAttr0 = new HashMap<String, String>();
		childAttr0.put("attr0", "value0");
		childAttr0.put("attr1", "value1");
		Element child0 = creator.createElement("child0", childAttr0);
		
		creator.append(root, child0);
		System.out.println(creator.getString());
		
		root = creator.newDocument("Jni");
		root.appendChild(creator.createElement("ID", "1001", "attr", "val"));
		root.appendChild(creator.createElement("version", "1"));
		System.out.println(creator.getString());
	}
	
}
