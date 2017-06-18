package com.example.kosta.ordermadeandroid.activity.request;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Comment;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailFragment extends Fragment {

    private SharedPreferences prefs;
    private String memberType;

    private EditText registerPrice;
    private ListView commentListView;
    private View view;

    private List<Comment> requestCommentData;
    private RequestCommentListAdapter requestCommentListAdapter;

    private static RequestDetailFragment instance;

    synchronized public static RequestDetailFragment getInstance() {
        if (instance == null) instance = new RequestDetailFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_request_detail, container, false);
        commentListView = (ListView)view.findViewById(R.id.request_detail_comment_listView);
        commentListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        prefs = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        memberType = prefs.getString("memberType","");

        final Request request = (Request)getActivity().getIntent().getExtras().get("request");
        //getActivity().getIntent().removeExtra("request");

        // 의뢰서 상세 정보 출력
        if (request.getMaker() != null) {
            ((TextView)view.findViewById(R.id.request_detail_makerId)).setText(request.getMaker().getId());
        } else {
            ((TextView)view.findViewById(R.id.request_detail_makerId)).setText("없음");
        }
        ((TextView)view.findViewById(R.id.request_detail_category))
                .setText(request.getCategory());
        ((TextView)view.findViewById(R.id.request_detail_title))
                .setText(request.getTitle());
        ((TextView)view.findViewById(R.id.request_detail_content))
                .setText(request.getContent());
        ((TextView)view.findViewById(R.id.request_detail_price))
                .setText(request.getPrice() + "");
        ((TextView)view.findViewById(R.id.request_detail_price_decided))
                .setText(request.getPrice() + "");
        registerPrice = ((EditText)view.findViewById(R.id.request_detail_price_register));


        final AsyncTask<String, Void, Void> task = new RequestCommentListLoadingTask();
        task.execute(Constants.mBaseUrl+"/comment/xml/searchRequestId.do?requestId="+request.getId()+"&page=1");
        Log.d("requestComment", "---- asyncTask start ----");
        requestCommentData = new ArrayList<>();
        requestCommentListAdapter = new RequestCommentListAdapter(getActivity(), requestCommentData);

        commentListView.setAdapter(requestCommentListAdapter);

        if ( memberType.equals("C")) {
            ((LinearLayout)view.findViewById(R.id.request_detail_priceRegister_layout))
                    .setVisibility(View.GONE);
            if( request.getPrice() == 0 ) {}
            else if( request.getPayment().equals("N") ) {
                ((LinearLayout)view.findViewById(R.id.request_detail_pricePurchase_layout))
                        .setVisibility(View.VISIBLE);
            }

            if ( request.getPayment().equals("Y") ) {
                ((LinearLayout)view.findViewById(R.id.request_detail_pricePurchase_layout))
                        .setVisibility(View.VISIBLE);
                ((Button)view.findViewById(R.id.request_detail_purchaseBtn)).setVisibility(View.GONE);
            }

            view.findViewById(R.id.request_detail_purchaseBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestDetailPurchaseTask(request.getId());
                }
            });

        }else if ( memberType.equals("M")) {
            if (request.getPrice() != 0) {
                view.findViewById(R.id.request_detail_price_register).setVisibility(View.GONE);
                view.findViewById(R.id.request_detail_price_decided).setVisibility(View.VISIBLE);
                view.findViewById(R.id.request_detail_priceRegisterBtn).setVisibility(View.INVISIBLE);
            }
            view.findViewById(R.id.request_detail_priceRegisterBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestDetailRegisterPriceTask(request.getId(), registerPrice.getText().toString());
                }
            });
        }

        return view;
    }

    private class RequestCommentListLoadingTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL((String)params[0]);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(url.openStream()));
                NodeList nodeList = doc.getElementsByTagName("comment");
                for (int i = 0; i < nodeList.getLength(); i++){
                    Comment comment = new Comment();
                    Node node = nodeList.item(i);

                    Element element = (Element)node;

                    comment.setId(getTagFindValue("id", "comment",element));
                    Log.d("requestComment", "---- comment Id ----"+getTagFindValue("id", "comment",element));
                    comment.setContent(getTagValue("content", element));
                    comment.setContentType(getTagValue("contentType", element));
                    Member member = new Member();
                    member.setId(getTagFindValue("id", "member", element));
                    member.setEmail(getTagFindValue("email", "member", element));
                    member.setAddress(getTagFindValue("address", "member", element));
                    member.setName(getTagFindValue("name", "member", element));
                    member.setIntroduce(getTagFindValue("introduce", "member", element));
                    member.setImage(getTagFindValue("image", "member", element));
                    comment.setMember(member);

                    requestCommentData.add(comment);
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
            requestCommentListAdapter.notifyDataSetChanged();
        }
    }

    private void RequestDetailRegisterPriceTask(String requestId, final String registerPrice) {

        Log.d("a", "------register price :"+requestId.toString());
        Log.d("a", "------register price :"+registerPrice);
        OkHttpUtils.initClient(CustomApplication.getClient())
                .post()
                .url(Constants.mBaseUrl +"/request/xml/modifyPaymentValue.do")
                .addParams("id",requestId)
                .addParams("price",registerPrice)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("a", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if ( response.equals("true")){
                            Toast.makeText(getActivity(), "등록 성공!", Toast.LENGTH_SHORT).show();
                            view.findViewById(R.id.request_detail_price_register).setVisibility(View.GONE);
                            view.findViewById(R.id.request_detail_price_decided).setVisibility(View.VISIBLE);
                            ((TextView)view.findViewById(R.id.request_detail_price_decided)).setText(registerPrice);
                            view.findViewById(R.id.request_detail_priceRegisterBtn).setVisibility(View.INVISIBLE);
                        }else{
                            Toast.makeText(getActivity(), "등록 실패", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void RequestDetailPurchaseTask(String requestId) {

        Log.d("a", "--product purchase--"+requestId);

        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(Constants.mBaseUrl +"/deal/xml/account/consumerMoney.do")
                .addParams("requestId",requestId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("a", e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if ( response.equals("true") ){
                            Toast.makeText(getActivity(), "결제 성공", Toast.LENGTH_SHORT).show();
                            ((Button)view.findViewById(R.id.request_detail_purchaseBtn)).setText("결제완료");
                            ((Button)view.findViewById(R.id.request_detail_purchaseBtn)).setEnabled(false);
                        }
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
