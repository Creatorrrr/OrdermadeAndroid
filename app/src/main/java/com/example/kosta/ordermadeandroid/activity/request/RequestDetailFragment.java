package com.example.kosta.ordermadeandroid.activity.request;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Comment;
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
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailFragment extends Fragment {

    private List<Comment> requestCommentData;
    private RequestCommentListAdapter requestCommentListAdapter;

    public RequestDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_detail, container, false);
        ListView commentListView = (ListView)view.findViewById(R.id.request_detail_comment_listView);

        Intent intent = getActivity().getIntent();
        String makerId = (String)intent.getExtras().get("makerId");
        String category = (String)intent.getExtras().get("category");
        String title = (String)intent.getExtras().get("title");
        String content = (String)intent.getExtras().get("detailContent");
        int price = (int)intent.getExtras().get("price");

        // 의뢰서 상세 정보 출력
        ((TextView)view.findViewById(R.id.request_detail_makerId))
                .setText(makerId);
        ((TextView)view.findViewById(R.id.request_detail_category))
                .setText(category);
        ((TextView)view.findViewById(R.id.request_detail_title))
                .setText(title);
        ((TextView)view.findViewById(R.id.request_detail_content))
                .setText(content);
        ((TextView)view.findViewById(R.id.request_detail_price))
                .setText(price+"");

        final AsyncTask<String, Void, Void> task = new RequestCommentListLoadingTask();
        task.execute("http://10.0.2.2:8080/ordermade/comment/xml/searchRequestId.do?requestId=1&page=1");
        Log.d("requestComment", "---- asyncTask start ----");
        requestCommentData = new ArrayList<>();
        requestCommentListAdapter = new RequestCommentListAdapter(getActivity(), requestCommentData);

        //commentListView.setAdapter(requestCommentListAdapter);

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
