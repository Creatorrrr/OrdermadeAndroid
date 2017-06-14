package com.example.kosta.ordermadeandroid.activity.request;

import android.content.Intent;
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

public class RequestMyListFragment extends Fragment {

    private List<Request> requestMyListData;
    private RequestMyListAdapter requestMyListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_my_list, container, false);
        ListView listView = (ListView)view.findViewById(R.id.request_myList);

        final AsyncTask<String, Void, Void> task = new RequestMyListLoadingTask();
        // RequestController - 368
        task.execute("http://10.0.2.2:8080/ordermade/request/xml/searchMyRequests.do");
        Log.d("b", "RequestMyList Task Done");

        requestMyListData = new ArrayList<>();
        requestMyListAdapter = new RequestMyListAdapter(getActivity(), requestMyListData);

        listView.setAdapter(requestMyListAdapter);

        view.findViewById(R.id.request_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequestRegisterActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private class RequestMyListLoadingTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL((String)params[0]);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(url.openStream()));
                NodeList nodeList = doc.getElementsByTagName("request");
                Log.d("c", "--####--- RequestLoadingTask START --###---");
                for ( int i = 0 ; i < nodeList.getLength(); i++) {
                    Request request = new Request();
                    Node node = nodeList.item(i);

                    Element element = (Element)node;

                    request.setId(getTagFindValue("id", "request", element));
                    Log.d("c", "request Id : "+getTagFindValue("id", "request", element));
                    request.setTitle(getTagValue("title", element));
                    Log.d("c", "request Title : "+getTagValue("title", element));
                    request.setCategory(getTagValue("category", element));
                    Log.d("c", "request category : "+getTagValue("category", element));
                    request.setContent(getTagValue("content", element));
                    request.setHopePrice(Integer.parseInt(getTagValue("hopePrice", element)));
                    request.setPrice(Integer.parseInt(getTagValue("price", element)));
                    request.setBound(getTagValue("bound", element));
                    //request.setPage(element.getElementsByTagName("page")
                    //        .item(0).getChildNodes().item(0).getNodeValue());

                    Log.d("c", "--####-- consumer Start --####-- ");
                    Member consumer = new Member();
                    consumer.setId(getTagFindValue("id", "consumer", element));
                    Log.d("c", "----------"+(getTagFindValue("id", "consumer", element)));
                    consumer.setEmail(getTagFindValue("email", "consumer", element));
                    Log.d("c", "----------"+(getTagFindValue("email", "consumer", element)));
                    consumer.setAddress(getTagFindValue("address", "consumer", element));
                    consumer.setName(getTagFindValue("name", "consumer", element));
                    consumer.setIntroduce(getTagFindValue("introduce", "consumer", element));
                    consumer.setImage(getTagFindValue("image", "consumer", element));
                    request.setConsumer(consumer);

                    Log.d("c", "--####-- maker Start --####-- ");
                    Member maker = new Member();
                    maker.setId(getTagFindValue("id", "maker", element));
                    maker.setEmail(getTagFindValue("email", "maker", element));
                    Log.d("c", "----------"+(getTagFindValue("email", "maker", element)));
                    maker.setAddress(getTagFindValue("address", "maker", element));
                    Log.d("c", "----------"+(getTagFindValue("address", "maker", element)));
                    maker.setName(getTagFindValue("name", "maker", element));
                    maker.setIntroduce(getTagFindValue("introduce", "maker", element));
                    maker.setImage(getTagFindValue("image", "maker", element));
                    request.setMaker(maker);

                    requestMyListData.add(request);
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
            requestMyListAdapter.notifyDataSetChanged();
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
