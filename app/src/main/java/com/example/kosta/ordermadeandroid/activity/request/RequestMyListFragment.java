package com.example.kosta.ordermadeandroid.activity.request;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.member.MemberRegisterActivity;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Request;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.example.kosta.ordermadeandroid.util.XmlUtil;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by kosta on 2017-06-09.
 */

public class RequestMyListFragment extends Fragment {

    private List<Request> requestMyListData;
    private RequestMyListAdapter requestMyListAdapter;

    private OkHttpClient okHttpClient;
    private GridView listView;
    private String requestId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_my_list, container, false);
        listView = (GridView) view.findViewById(R.id.request_myList);
        requestMyListData = new ArrayList<>();

        // 나의 의뢰서 리스트 출력, RequestController - 368
        requestMyListLoadingTask(Constants.mBaseUrl+"/request/xml/searchMyRequests.do");



        // 모든 의뢰서 버튼
        view.findViewById(R.id.request_myList_allBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMyListData.removeAll(requestMyListData);
                requestMyListLoadingTask(Constants.mBaseUrl+"/request/xml/searchMyRequests.do");
            }
        });

        // 진행중 버튼
        view.findViewById(R.id.request_myList_ingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMyListData.removeAll(requestMyListData);
                requestMyListLoadingTask(Constants.mBaseUrl+"/request/xml/searchMyRequestsWithMaker.do");
            }
        });

        // 완료된 의뢰서 버튼
        view.findViewById(R.id.request_myList_doneBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMyListData.removeAll(requestMyListData);
                requestMyListLoadingTask(Constants.mBaseUrl+"/request/xml/searchMyRequestsWithPayment.do");
            }
        });

        // 의뢰서 추가 버튼
        view.findViewById(R.id.request_myList_registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequestRegisterActivity.class);
                startActivity(intent);
            }
        });

        // Context Menu 구현
        registerForContextMenu(view.findViewById(R.id.request_myList));

        // 나의 의뢰서 상세정보 구현
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Request request = requestMyListData.get(position);
                if( request.getMaker().getId() != null){
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), RequestDetailActivity.class);
                    // Request DTO, required implements serializable
                    intent.putExtra("makerId", request.getMaker().getId());
                    intent.putExtra("category", request.getCategory());
                    intent.putExtra("title", request.getTitle());
                    intent.putExtra("price", request.getPrice());
                    intent.putExtra("requestId", request.getId());
                    intent.putExtra("detailContent", request.getContent());
                    startActivity(intent);
                }
            }
        });

        return view;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.request_my_list_context_menu, menu);

        /*menu.add(Menu.NONE, 1, Menu.NONE, "Edit");
        menu.add(Menu.NONE, 2, Menu.NONE, "Delete");*/
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        //info.id, info.position 은 rowId
        switch (item.getItemId()) {
            case R.id.edit_item:
                Toast.makeText(getActivity(), "Edit selected", Toast.LENGTH_SHORT).show();



                break;
            case R.id.delete_item:
                requestId = ((TextView)info.targetView.findViewById(R.id.request_myList_requestId)).getText().toString();
                RequestMyListDeleteTask(requestId);
                Toast.makeText(getActivity(), requestId, Toast.LENGTH_SHORT).show();
                /*Toast.makeText(getActivity()
                        , ((TextView)info.targetView.findViewById(R.id.request_myList_requestId))
                                .getText().toString(), Toast.LENGTH_SHORT).show();*/
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void RequestMyListDeleteTask(String requestId) {

        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(Constants.mBaseUrl +"/request/xml/remove.do")
                .addParams("id", requestId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("requestDelete", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if(response.equals("true")) {
                            Toast.makeText(getActivity(), "삭제 완료", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), RequestMyListActivity.class));
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), "삭제 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void requestMyListLoadingTask(String... params){


        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(params[0])
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("a", e.getMessage());
                    }

                    @Override
                    public void onResponse(final String response, final int id) {
                        Log.d("a", "==========" + response);

                        try {
                            //xml-------
                            StringReader sr = new StringReader(response);
                            InputSource is = new InputSource(sr);
                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            Document doc = builder.parse(is);
//                            url = new URL((String)params[0]);//----------del
//                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//---del
//                            DocumentBuilder builder = factory.newDocumentBuilder();//---del
//                            Document doc = builder.parse(new InputSource(url.openStream()));//---del

                            NodeList nodeList = doc.getElementsByTagName("request");
                            Log.d("requestList", "--####--- RequestLoadingTask START --###---");
                            for (int i = 0; i < nodeList.getLength(); i++) {
                                Request request = new Request();
                                Node node = nodeList.item(i);

                                Element element = (Element) node;

                                request.setId(getTagFindValue("id", "request", element));
                                Log.d("requestList", "request Id : " + getTagFindValue("id", "request", element));
                                request.setTitle(getTagValue("title", element));
                                Log.d("requestList", "request Title : " + getTagValue("title", element));
                                //request.setCategory(getTagValue("category", element));
                                //Log.d("requestList", "request category : "+getTagValue("category", element));
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
                                /*
                                    private String id;-
                                    private String title;-
                                    private Member maker;-
                                    private Member consumer;-
                                    private String category;
                                    private String content;
                                    private int hopePrice;-
                                    private int price;-
                                    private List<Comment> comments;
                                    private List<Attach> attachs;
                                    private String bound;-
                                    private String page;
                                */
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

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("requestList", "RequestMyList Task Done");
                                requestMyListAdapter = new RequestMyListAdapter(getActivity(), requestMyListData);
                                listView.setAdapter(requestMyListAdapter);
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
