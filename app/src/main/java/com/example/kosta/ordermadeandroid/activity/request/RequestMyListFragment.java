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
                Log.d("b", "--####--- RequestLoadingTask START --###---");
                for ( int i = 0 ; i < nodeList.getLength(); i++) {
                    Request request = new Request();
                    Node node = nodeList.item(i);

                    Element element = (Element)node;

                    request.setId(node.getChildNodes()
                            .item(5).getFirstChild().getNodeValue());
                    Log.d("b", "request Id : "+node.getChildNodes()
                            .item(5).getFirstChild().getNodeValue());
                    request.setTitle(node.getChildNodes()
                            .item(8).getFirstChild().getNodeValue());
                    Log.d("b", "request Title : "+node.getChildNodes()
                            .item(8).getFirstChild().getNodeValue());
                    request.setCategory(node.getChildNodes()
                            .item(1).getFirstChild().getNodeValue());
                    Log.d("b", "request category : "+node.getChildNodes()
                            .item(1).getFirstChild().getNodeValue());
                    request.setContent(node.getChildNodes()
                            .item(3).getFirstChild().getNodeValue());
                    request.setHopePrice(Integer.parseInt(node.getChildNodes()
                            .item(4).getFirstChild().getNodeValue()));
                    request.setPrice(Integer.parseInt(node.getChildNodes()
                            .item(7).getFirstChild().getNodeValue()));
                    request.setBound(node.getChildNodes()
                            .item(0).getFirstChild().getNodeValue());
                    //request.setPage(element.getElementsByTagName("page")
                    //        .item(0).getChildNodes().item(0).getNodeValue());

                    Log.d("b", "--####-- consumer Start --####-- ");
                    Member consumer = new Member();
                    consumer.setId(node.getChildNodes().item(2)
                            .getChildNodes().item(0).getFirstChild().getNodeValue());
                    Log.d("b", "----------"+(node.getChildNodes().item(2)
                            .getChildNodes().item(0).getFirstChild().getNodeValue()));
                    consumer.setEmail(node.getChildNodes().item(2)
                            .getChildNodes().item(1).getFirstChild().getNodeValue());
                    Log.d("b", "----------"+(node.getChildNodes().item(2)
                            .getChildNodes().item(1).getFirstChild().getNodeValue()));
                    consumer.setAddress(node.getChildNodes().item(2)
                            .getChildNodes().item(2).getFirstChild().getNodeValue());
                    consumer.setName(node.getChildNodes().item(2)
                            .getChildNodes().item(3).getFirstChild().getNodeValue());
                    consumer.setIntroduce(node.getChildNodes().item(2)
                            .getChildNodes().item(4).getFirstChild().getNodeValue());
                    consumer.setImage(node.getChildNodes().item(2)
                            .getChildNodes().item(5).getFirstChild().getNodeValue());
                    request.setConsumer(consumer);

                    Log.d("b", "--####-- maker Start --####-- ");
                    Member maker = new Member();
                    maker.setId(node.getChildNodes().item(6)
                            .getChildNodes().item(0).getFirstChild().getNodeValue());
                    maker.setEmail(node.getChildNodes().item(6)
                            .getChildNodes().item(1).getFirstChild().getNodeValue());
                    Log.d("b", "----------"+(node.getChildNodes().item(6)
                            .getChildNodes().item(1).getFirstChild().getNodeValue()));
                    maker.setAddress(node.getChildNodes().item(6)
                            .getChildNodes().item(2).getFirstChild().getNodeValue());
                    Log.d("b", "----------"+(node.getChildNodes().item(6)
                            .getChildNodes().item(2).getFirstChild().getNodeValue()));
                    maker.setName(node.getChildNodes().item(6)
                            .getChildNodes().item(3).getFirstChild().getNodeValue());
                    maker.setIntroduce(node.getChildNodes().item(6)
                            .getChildNodes().item(4).getFirstChild().getNodeValue());
                    maker.setImage(node.getChildNodes().item(6)
                            .getChildNodes().item(5).getFirstChild().getNodeValue());
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
}
