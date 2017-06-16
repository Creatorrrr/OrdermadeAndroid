package com.example.kosta.ordermadeandroid.dto.loader;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.InviteRequest;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Request;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by kosta on 2017-06-16.
 */

public class RequestLoadingTask extends AsyncTask<String, Void, Void> {
    private List<Request> requestList;
    private BaseAdapter adapter;

    public RequestLoadingTask(List<Request> requestList, BaseAdapter adapter) {
        this.requestList = requestList;
        this.adapter = adapter;
    }

    @Override
    protected Void doInBackground(String... params) {
        URL url = null;

        try {
            url = new URL((String)params[0]);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(url.openStream()));

            Log.d("rs", "-------RequestLoadingTask start");

            if(doc.getElementsByTagName("requests").item(0) != null) {
                NodeList nodeList = doc.getElementsByTagName("request");
                for (int i = 0; i < nodeList.getLength(); i++){
                    Node node = nodeList.item(i);
                    requestList.add(getRequestFromElement((Element)node));
                }
            } else {
                NodeList nodeList = doc.getElementsByTagName("inviterequest");
                for (int i = 0; i < nodeList.getLength(); i++){
                    Node node = ((Element)(nodeList.item(i))).getElementsByTagName("request").item(0);
                    requestList.add(getRequestFromElement((Element)node));
                }
            }

            Log.d("rs", "-------RequestLoadingTask finished");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        adapter.notifyDataSetChanged();
    }

    private Request getRequestFromElement(Element element) {
        Request request = new Request();

        request.setId(getTagFindValue("id", "request", element));
        request.setTitle(getTagValue("title", element));

        Element makerElement = (Element)element.getElementsByTagName("maker").item(0);

        if(makerElement != null) {
            Member maker = new Member();
            maker.setId(getTagValue("id", makerElement));
            maker.setEmail(getTagValue("email", makerElement));
            maker.setAddress(getTagValue("address", makerElement));
            maker.setName(getTagValue("name", makerElement));
            maker.setImage(Constants.mBaseUrl + "/main/file/download.do?fileName=" + getTagValue("image", makerElement));

            request.setMaker(maker);
        }

        Element consumerElement = (Element)element.getElementsByTagName("consumer").item(0);

        Member consumer = new Member();
        consumer.setId(getTagValue("id", consumerElement));
        consumer.setEmail(getTagValue("email", consumerElement));
        consumer.setAddress(getTagValue("address", consumerElement));
        consumer.setName(getTagValue("name", consumerElement));
        consumer.setImage(Constants.mBaseUrl + "/main/file/download.do?fileName=" + getTagValue("image", consumerElement));

        request.setConsumer(consumer);
        request.setCategory(getTagValue("category", element));
        request.setContent(getTagValue("content", element));
        request.setHopePrice(Integer.parseInt(getTagValue("hopePrice", element)));
        request.setPrice(Integer.parseInt(getTagValue("price", element)));
        request.setBound(getTagValue("bound", element));

        return request;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node value = nodeList.item(0);
        return value.getNodeValue();
    }

    private String getTagFindValue(String tag, String className, Element element) {
        NodeList elementList = element.getElementsByTagName(tag);
        for ( int i = 0 ; i < elementList.getLength() ; i++){
            if ( className.equals(elementList.item(i).getParentNode().getNodeName())){
                return elementList.item(i).getChildNodes().item(0).getNodeValue();
            }
        }
        return null;
    }
}
