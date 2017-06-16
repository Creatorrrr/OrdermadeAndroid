package com.example.kosta.ordermadeandroid.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by kosta on 2017-06-16.
 */

public class XmlUtil {

	//xml
	public static final String getTagValue(String tag, Element element){
		Node node = element.getElementsByTagName(tag).item(0);
		if(node == null) return null;
		return node.getChildNodes().item(0).getNodeValue();
		//return element.getElementsByTagName(tag).item(0).getChildNodes().item(0).getNodeValue();
	}


}
