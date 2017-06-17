package com.example.kosta.ordermadeandroid.activity.request;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Request;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.zhy.http.okhttp.OkHttpUtils;
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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestReceivedFragment extends Fragment {

    private List<Request> requestReceivedData;
    private RequestReceivedAdapter requestReceivedAdapter;

    private OkHttpClient okHttpClient;
    private GridView listView;

    public RequestReceivedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_received, container, false);
        listView = (GridView)view.findViewById(R.id.request_received_listView);
        requestReceivedData = new ArrayList<>();

        RequestReceivedLoadingTask(Constants.mBaseUrl+"/request/xml/searchMyRequestsByMakerIdExceptPayment.do");

        registerForContextMenu(view.findViewById(R.id.request_received_listView));

        // 의뢰서 상세 정보
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request request = requestReceivedData.get(position);
                Intent intent = new Intent(getActivity(), RequestDetailActivity.class);
                intent.putExtra("makerId", request.getMaker().getId());
                intent.putExtra("category", request.getCategory());
                intent.putExtra("title", request.getTitle());
                intent.putExtra("price", request.getPrice());
                intent.putExtra("requestId", request.getId());
                intent.putExtra("detailContent", request.getContent());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.request_received_context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.edit_item:
                Toast.makeText(getActivity(), "edit selected", Toast.LENGTH_SHORT).show();

            case R.id.delete_item:
                Toast.makeText(getActivity(), "delete selected", Toast.LENGTH_SHORT).show();
        }

        return super.onContextItemSelected(item);
    }

    private void RequestReceivedLoadingTask(String...params) {

        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(params[0])
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("requestReceived", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            //xml-------
                            StringReader sr = new StringReader(response);
                            InputSource is = new InputSource(sr);
                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            Document doc = builder.parse(is);

                            NodeList nodeList = doc.getElementsByTagName("request");
                            Log.d("requestReceived", "--####--- RequestReceivedLoadingTask START --###---");
                            for (int i = 0; i < nodeList.getLength(); i++) {
                                Request request = new Request();
                                Node node = nodeList.item(i);

                                Element element = (Element) node;

                                request.setId(getTagFindValue("id", "request", element));
                                Log.d("requestList", "request Id : " + getTagFindValue("id", "request", element));
                                request.setTitle(getTagValue("title", element));
                                Log.d("requestList", "request Title : " + getTagValue("title", element));
                                if ( node.getNodeName().equals("category")){
                                    request.setCategory(getTagValue("category", element));
                                    Log.d("requestList", "request category : "+getTagValue("category", element));
                                }
                                request.setContent(getTagFindValue("content", "request", element));
                                request.setHopePrice(Integer.parseInt(getTagValue("hopePrice", element)));
                                request.setPrice(Integer.parseInt(getTagValue("price", element)));
                                request.setBound(getTagValue("bound", element));
                                //request.setPage(element.getElementsByTagName("page")
                                //        .item(0).getChildNodes().item(0).getNodeValue());

                                Log.d("requestList", "--####-- consumer Start --####-- ");
                                Member consumer = new Member();
                                consumer.setId(getTagFindValue("id", "consumer", element));
                                Log.d("requestList", "----------" + (getTagFindValue("id", "consumer", element)));
                                consumer.setEmail(getTagFindValue("email", "consumer", element));
                                Log.d("requestList", "----------" + (getTagFindValue("email", "consumer", element)));
                                consumer.setAddress(getTagFindValue("address", "consumer", element));
                                consumer.setName(getTagFindValue("name", "consumer", element));
                                consumer.setIntroduce(getTagFindValue("introduce", "consumer", element));
                                consumer.setImage(getTagFindValue("image", "consumer", element));
                                request.setConsumer(consumer);

                                Log.d("requestList", "--####-- maker Start --####-- ");
                                Member maker = new Member();
                                maker.setId(getTagFindValue("id", "maker", element));
                                maker.setEmail(getTagFindValue("email", "maker", element));
                                Log.d("requestList", "----------" + (getTagFindValue("email", "maker", element)));
                                maker.setAddress(getTagFindValue("address", "maker", element));
                                Log.d("requestList", "----------" + (getTagFindValue("address", "maker", element)));
                                maker.setName(getTagFindValue("name", "maker", element));
                                maker.setIntroduce(getTagFindValue("introduce", "maker", element));
                                maker.setImage(getTagFindValue("image", "maker", element));
                                request.setMaker(maker);

                                requestReceivedData.add(request);

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

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("requestReceived", "RequestReceived Task Done");
                                requestReceivedAdapter = new RequestReceivedAdapter(getActivity(), requestReceivedData);
                                listView.setAdapter(requestReceivedAdapter);
                            }
                        });
                    }
                });
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
