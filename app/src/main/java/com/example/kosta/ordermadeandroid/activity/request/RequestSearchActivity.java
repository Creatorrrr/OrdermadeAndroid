package com.example.kosta.ordermadeandroid.activity.request;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.InviteRequest;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.dto.Product;
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

public class RequestSearchActivity extends AppCompatActivity {
    private static final int ALL_REQUEST = 0;
    private static final int SEND_REQUEST = 1;

    private List<Request> requestList;
    private RequestMyListAdapter adapter;

    private int listChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_search);

        requestList = new ArrayList<>();
        adapter = new RequestMyListAdapter(this, requestList);

        ListView listView = (ListView)findViewById(R.id.request_bound_list);
        listView.setAdapter(adapter);

        // load all requests on create activity
        new RequestLoadingTask(requestList, adapter)
                .execute("http://10.0.2.2:8080/ordermade/request/xml/searchBound.do?page=1");

        // set buttons listener
        findViewById(R.id.allRequestBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestList.clear();
                new RequestLoadingTask(requestList, adapter)
                        .execute("http://10.0.2.2:8080/ordermade/request/xml/searchBound.do?page=1");
            }
        });

        final List<InviteRequest> inviteRequestList = new ArrayList<InviteRequest>();

        findViewById(R.id.sendRequestBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestList.clear();
                new RequestLoadingTask(requestList, adapter)
                        .execute("http://10.0.2.2:8080/ordermade/request/xml/searchMyInviteRequestsForMaker.do?form=R");
            }
        });

        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(listChange == ALL_REQUEST) {
            menu.add("참가");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
        Request request = requestList.get(pos);

        if (listChange == ALL_REQUEST) {
//            new MusicFavoriteTask(this)
//                    .execute("http://10.0.2.2:8080/ordermade/request/xml/registerInviteRequest.do" +
//                            "?loginId=" + loginId);
        }

        return true;
    }
}
