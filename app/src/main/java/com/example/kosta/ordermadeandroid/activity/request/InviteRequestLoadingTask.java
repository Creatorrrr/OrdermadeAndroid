package com.example.kosta.ordermadeandroid.activity.request;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

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

public class InviteRequestLoadingTask extends AsyncTask<String, Void, Void> {
    private List<InviteRequest> inviteRequestList;
    private BaseAdapter adapter;

    public InviteRequestLoadingTask(List<InviteRequest> inviteRequestList, BaseAdapter adapter) {
        this.inviteRequestList = inviteRequestList;
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
            NodeList nodeList = doc.getElementsByTagName("inviterequest");
            Log.d("c", "-------InviteRequestLoadingTask start");

            for (int i = 0; i < nodeList.getLength(); i++){
                InviteRequest inviteRequest = new InviteRequest();
                Node node = nodeList.item(i);

                Element element = (Element)node;
                inviteRequest.setId(getTagValue("id", element));
                inviteRequest.setMessage(getTagValue("message", element));

                Element memberElement = (Element)element.getElementsByTagName("maker").item(0);

                Member maker = new Member();
                maker.setId(getTagValue("id", memberElement));
                maker.setEmail(getTagValue("email", memberElement));
                maker.setAddress(getTagValue("address", memberElement));
                maker.setName(getTagValue("name", memberElement));
                maker.setImage("http://10.0.2.2:8080/ordermade/main/file/download.do?fileName=" + getTagValue("image", memberElement));

                inviteRequest.setMaker(maker);

                inviteRequest.setRequestTime(getTagValue("requestTime", element));

                Element requestElement = (Element)element.getElementsByTagName("request").item(0);

                Request request = new Request();
                request.setId(getTagValue("id", requestElement));
                request.setTitle(getTagValue("title", requestElement));
                request.setMaker(new Member());
                request.getMaker().setId(getTagFindValue("id", "maker", requestElement));
                request.setConsumer(new Member());
                request.getMaker().setId(getTagFindValue("id", "consumer", requestElement));
                request.setCategory(getTagValue("category", requestElement));
                request.setContent(getTagValue("content", requestElement));
                request.setHopePrice(Integer.parseInt(getTagValue("hopePrice", requestElement)));
                request.setPrice(Integer.parseInt(getTagValue("price", requestElement)));
                request.setBound(getTagValue("bound", requestElement));

                inviteRequest.setRequest(request);

                inviteRequest.setForm(getTagValue("form", element));

                inviteRequestList.add(inviteRequest);
            }

            Log.d("c", "-------InviteRequestLoadingTask finished");

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

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node value = (Node) nodeList.item(0);
        if(value == null) {
            return "";
        } else {
            return value.getNodeValue();
        }
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
