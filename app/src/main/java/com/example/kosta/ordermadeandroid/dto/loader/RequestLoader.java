package com.example.kosta.ordermadeandroid.dto.loader;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.InviteRequest;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Request;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;

/**
 * Created by kosta on 2017-06-16.
 */

public class RequestLoader extends DTOLoader {
    private List<Request> requestList;
    private BaseAdapter adapter;

    public RequestLoader(List<Request> requestList) {
        this.requestList = requestList;
    }

    public RequestLoader(List<Request> requestList, BaseAdapter adapter) {
        this.requestList = requestList;
        this.adapter = adapter;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        Log.d("rs", e.getMessage());
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response)));

            Log.d("rs", "-------RequestLoader start");

            if (doc.getElementsByTagName("requests").item(0) != null) {
                NodeList nodeList = doc.getElementsByTagName("request");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    requestList.add(getRequestFromElement((Element) node));
                }
            } else {
                NodeList nodeList = doc.getElementsByTagName("inviterequest");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = ((Element) (nodeList.item(i))).getElementsByTagName("request").item(0);
                    requestList.add(getRequestFromElement((Element) node));
                }
            }

            Log.d("rs", "-------RequestLoader finished");
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAfter(int id) {
        if(adapter != null) adapter.notifyDataSetChanged();
    }

    private Request getRequestFromElement(Element element) {
        Request request = new Request();

        request.setId(getTagValue("id", "request", element));
        request.setTitle(getTagValue("title", "request", element));

        if(element.getElementsByTagName("maker").item(0) != null) {
            Member maker = new Member();
            maker.setId(getTagValue("id", "maker", "request", element));
            maker.setEmail(getTagValue("email", "maker", "request", element));
            maker.setAddress(getTagValue("address", "maker", "request", element));
            maker.setName(getTagValue("name", "maker", "request", element));
            maker.setImage(getTagValue("image", "maker", "request", element));

            request.setMaker(maker);
        }

        Member consumer = new Member();
        consumer.setId(getTagValue("id", "consumer", "request", element));
        consumer.setEmail(getTagValue("email", "consumer", "request", element));
        consumer.setAddress(getTagValue("address", "consumer", "request", element));
        consumer.setName(getTagValue("name", "consumer", "request", element));
        consumer.setImage(getTagValue("image", "consumer", "request", element));

        request.setConsumer(consumer);
        request.setCategory(getTagValue("category", "request", element));
        request.setContent(getTagValue("content", "request", element));
        request.setHopePrice(Integer.parseInt(getTagValue("hopePrice", "request", element)));
        request.setPrice(Integer.parseInt(getTagValue("price", "request", element)));
        request.setBound(getTagValue("bound", "request", element));
        request.setPayment(getTagValue("payment", "request", element));

        return request;
    }
}
