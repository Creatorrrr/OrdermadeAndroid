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
import com.example.kosta.ordermadeandroid.dto.loader.RequestLoader;
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
        requestReceivedData = new ArrayList<>();
        requestReceivedAdapter = new RequestReceivedAdapter(getActivity(), requestReceivedData);

        listView = (GridView)view.findViewById(R.id.request_received_listView);
        listView.setAdapter(requestReceivedAdapter);

        RequestReceivedLoadingTask(Constants.mBaseUrl+"/request/xml/searchMyRequestsByMakerIdExceptPayment.do");

        registerForContextMenu(view.findViewById(R.id.request_received_listView));

        // 의뢰서 상세 정보
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("request", requestReceivedData.get(position));
                getActivity().setIntent(intent);
                getActivity().setTitle("의뢰서 상세");
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.relativeLayout_for_frame, RequestDetailFragment.getInstance()).commit();
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
                .execute(new RequestLoader(requestReceivedData, requestReceivedAdapter));
    }

}
