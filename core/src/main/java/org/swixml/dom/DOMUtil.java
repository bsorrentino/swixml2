package org.swixml.dom;

import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.swixml.LogAware;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMUtil implements LogAware {

	protected DOMUtil() {
	}


	public static org.w3c.dom.Element getChildByTagName( org.w3c.dom.Element parent, String tagName ) {
		return getChildByTagName(parent, tagName, null);
	}
	
	public static org.w3c.dom.Element getChildByTagName( org.w3c.dom.Element parent, String tagName, String namespaceURI ) {
		
		NodeList children = parent.getChildNodes();
		
		for( int i=0; i<children.getLength(); ++i ) {
			
			org.w3c.dom.Node n = children.item(i);
			
			if( n instanceof org.w3c.dom.Element ) {
				
				org.w3c.dom.Element e = (org.w3c.dom.Element) n;
				if( tagName.equals(e.getTagName()) && (namespaceURI==null || e.getNamespaceURI().equals(namespaceURI))) {
					return e;
				}
			}
			
		}
		
		return null;
	}

	  public static java.util.List<Node> getContent(Element source) {
		  
		  logger.warning( String.format("getContent of [%s] ", source.getLocalName()) );
		
		  return Collections.emptyList();
	  }
	  
	  
	  public  static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		    builderFactory.setNamespaceAware(true);
		    builderFactory.setValidating(false);
		    builderFactory.setIgnoringElementContentWhitespace(true);
	
		    DocumentBuilder builder = null;
		    builder = builderFactory.newDocumentBuilder();
	    
		    return builder;
	  }

	  public  static void print( NamedNodeMap map )  {
		  
		  for( int i=0; i<map.getLength(); ++i ) {
			  Node n = map.item(i);
			  
			  System.out.printf( "\tnode [%s] [%s] [%s] [%s]\n", n.getLocalName(), n.getTextContent(), n.getNamespaceURI(), n.getPrefix());
		  }
	  }
}
