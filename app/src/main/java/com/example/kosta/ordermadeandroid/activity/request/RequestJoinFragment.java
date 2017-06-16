package com.example.kosta.ordermadeandroid.activity.request;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.kosta.ordermadeandroid.R;
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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by kosta on 2017-06-09.
 */

public class RequestJoinFragment extends Fragment {

    private List<InviteRequest> requestJoinData;
    private RequestJoinListAdapter requestJoinListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_join, container, false);
        ListView joinListView = (ListView)view.findViewById(R.id.request_join_listView);

        final AsyncTask<String, Void, Void> task = new RequestJoinLoadingTask();
        task.execute("http://10.0.2.2:8080/ordermade/request/xml/searchMyInviteRequestsForConsumer.do?form=R");
        Log.d("inviteRequest", "-----task.execute Start-----");


        requestJoinData = new ArrayList<>();
        requestJoinListAdapter = new RequestJoinListAdapter(getActivity(), requestJoinData);

        joinListView.setAdapter(requestJoinListAdapter);

        return view;
    }

    private class RequestJoinLoadingTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL((String)params[0]);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(url.openStream()));
                NodeList nodeList = doc.getElementsByTagName("inviterequest");
                Log.d("inviteRequest", "-----RJLoadingTask Start-----");

                for (int i = 0 ; i<nodeList.getLength() ; i++){
                    InviteRequest inviteRequest = new InviteRequest();
                    Node node = nodeList.item(i);

                    Element element = (Element)node;

                    inviteRequest.setId(getTagFindValue("id", "inviterequest", element));
                    inviteRequest.setForm(getTagValue("form", element));
                    inviteRequest.setMessage(getTagValue("message", element));

                    Member maker = new Member();
                    maker.setId(getTagFindValue("id", "maker", element));
                    Log.d("inviteRequest", "---maker ID---"+getTagFindValue("id", "maker", element));
                    maker.setEmail(getTagFindValue("email", "maker", element));
                    maker.setAddress(getTagFindValue("address", "maker", element));
                    maker.setName(getTagFindValue("name", "maker", element));
                    maker.setIntroduce(getTagFindValue("introduce", "maker", element));
                    maker.setImage(getTagFindValue("image", "maker", element));
                    inviteRequest.setMaker(maker);

                    Request request = new Request();
                    request.setId(getTagFindValue("id", "request", element));
                    request.setBound(getTagFindValue("bound", "request", element));
                    request.setCategory(getTagFindValue("category", "request", element));
                    request.setContent(getTagFindValue("content", "request", element));
                    request.setHopePrice(Integer.parseInt(getTagFindValue("hopePrice", "request", element)));
                    request.setPrice(Integer.parseInt(getTagFindValue("price", "request", element)));
                    request.setPayment(getTagFindValue("payment", "request", element));
                    request.setTitle(getTagFindValue("title", "request", element));

                    Member consumer = new Member();
                    consumer.setId(getTagFindValue("id", "consumer", element));
                    consumer.setEmail(getTagFindValue("email", "consumer", element));
                    consumer.setAddress(getTagFindValue("address", "consumer", element));
                    consumer.setName(getTagFindValue("name", "consumer", element));
                    consumer.setIntroduce(getTagFindValue("introduce", "consumer", element));
                    consumer.setImage(getTagFindValue("image", "consumer", element));
                    request.setConsumer(consumer);

                    inviteRequest.setRequest(request);

                    requestJoinData.add(inviteRequest);
                }
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
            requestJoinListAdapter.notifyDataSetChanged();
        }
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node value = (Node) nodeList.item(0);
        return value.getNodeValue();
    }

    private static String getTagFindValue(String tag, String className, Element element) {
        NodeList elementList = element.getElementsByTagName(tag);
        for ( int i = 0 ; i < elementList.getLength() ; i++){
            if ( className.equals(elementList.item(i).getParentNode().getNodeName())){
                return elementList.item(i).getChildNodes().item(0).getNodeValue();
            }
        }
        return null;
    }
}
