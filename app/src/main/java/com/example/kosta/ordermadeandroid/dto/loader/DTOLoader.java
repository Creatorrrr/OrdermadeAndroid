package com.example.kosta.ordermadeandroid.dto.loader;

import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by Creator on 2017-06-17.
 */

public abstract class DTOLoader extends StringCallback {

    protected String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node value = nodeList.item(0);
        return value.getNodeValue();
    }

    protected String getTagValue(String tag, String className, Element element) {
        NodeList elementList = element.getElementsByTagName(tag);
        for ( int i = 0 ; i < elementList.getLength() ; i++){
            if ( className.equals(elementList.item(i).getParentNode().getNodeName())){
                return elementList.item(i).getChildNodes().item(0).getNodeValue();
            }
        }
        return null;
    }

    protected String getTagValue(String tag, String className1, String className2, Element element) {
        NodeList elementList = element.getElementsByTagName(tag);
        for ( int i = 0 ; i < elementList.getLength() ; i++){
            Node item = elementList.item(i);
            if ( className1.equals(item.getParentNode().getNodeName())){
                if ( className2.equals(item.getParentNode().getParentNode().getNodeName())) {
                    return item.getChildNodes().item(0).getNodeValue();
                }
            }
        }
        return null;
    }
}
