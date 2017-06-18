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
import com.example.kosta.ordermadeandroid.activity.product.ProductMyListAdapter;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.InviteRequest;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Request;
import com.example.kosta.ordermadeandroid.dto.loader.InviteRequestLoader;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;

/**
 * Created by kosta on 2017-06-09.
 */

public class RequestJoinFragment extends Fragment {

    private List<InviteRequest> requestJoinData;
    private RequestJoinListAdapter requestJoinListAdapter;

    private ListView joinListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_join, container, false);
        joinListView = (ListView)view.findViewById(R.id.request_join_listView);

        requestJoinData = new ArrayList<>();
        requestJoinListAdapter = new RequestJoinListAdapter(getActivity(), requestJoinData);

        Log.d("inviteRequest", "-----task.execute Start-----");
        RequestJoinLoadingTask(Constants.mBaseUrl+"/request/xml/searchMyInviteRequestsForConsumer.do?form=R");

        joinListView.setAdapter(requestJoinListAdapter);

        return view;
    }

    private void RequestJoinLoadingTask (String...params) {

        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(params[0])
                .build()
                .execute(new InviteRequestLoader(requestJoinData, requestJoinListAdapter));
    }
}
