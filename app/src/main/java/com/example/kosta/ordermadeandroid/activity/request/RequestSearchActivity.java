package com.example.kosta.ordermadeandroid.activity.request;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.InviteRequest;
import com.example.kosta.ordermadeandroid.dto.Request;
import com.example.kosta.ordermadeandroid.dto.loader.RequestLoadingTask;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class RequestSearchActivity extends AppCompatActivity {
    private static final int ALL_REQUEST = 0;
    private static final int SEND_REQUEST = 1;

    private static final int SEARCH_TITLE = 0;
    private static final int SEARCH_CONTENT = 1;

    private OkHttpClient okHttpClient;

    private List<Request> requestList;
    private RequestListAdapter adapter;

    private int listChange;
    private int searchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_search);

        requestList = new ArrayList<>();
        adapter = new RequestListAdapter(this, requestList);

        ListView listView = (ListView)findViewById(R.id.request_bound_list);
        listView.setAdapter(adapter);

        final Spinner searchTypeSpinner = (Spinner)findViewById(R.id.requestSearchType);
        searchTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    searchType = SEARCH_TITLE;
                } else if(position == 1) {
                    searchType = SEARCH_CONTENT;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final EditText keyword = (EditText)findViewById(R.id.requestSearchKeyword);
        findViewById(R.id.requestSearchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestList.clear();
                if (searchType == SEARCH_TITLE) {
                    new RequestLoadingTask(requestList, adapter)
                            .execute("http://10.0.2.2:8080/ordermade/request/xml/searchBoundAndTitle.do?title=" + keyword.getText().toString());
                } else {
                    new RequestLoadingTask(requestList, adapter)
                            .execute("http://10.0.2.2:8080/ordermade/request/xml/searchBoundAndContent.do?content=" + keyword.getText().toString());
                }
                listChange = ALL_REQUEST;
            }
        });

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
                listChange = ALL_REQUEST;
            }
        });

        final List<InviteRequest> inviteRequestList = new ArrayList<InviteRequest>();

        findViewById(R.id.sendRequestBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestList.clear();
                new RequestLoadingTask(requestList, adapter)
                            .execute("http://10.0.2.2:8080/ordermade/request/xml/searchMyInviteRequestsForMaker.do?form=R");
                listChange = SEND_REQUEST;
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
        if (listChange == ALL_REQUEST) {
            // custom dialog
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.request_join_dialog);
            dialog.setTitle("참가요청");

            LayoutParams params = dialog.getWindow().getAttributes();
            params.width = LayoutParams.MATCH_PARENT;
            params.height = LayoutParams.WRAP_CONTENT;

            final Request request = requestList.get(((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position);
            final EditText content = (EditText) dialog.findViewById(R.id.requestJoinContent);
            Button okButton = (Button)dialog.findViewById(R.id.requestJoinDialogOK);
            Button cancelButton = (Button)dialog.findViewById(R.id.requestJoinDialogCancel);

            // if okButton is clicked, send join request
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(RequestSearchActivity.this));
                    okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
                    OkHttpUtils.initClient(okHttpClient)
                            .post()
                            .url(Constants.mBaseUrl + "/request/xml/registerInviteRequest.do")
                            .addParams("message", content.getText().toString())
                            .addParams("request.id", request.getId() + "")
                            .addParams("form", "R")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.d("rs", e.getMessage());
                                }

                                @Override
                                public void onResponse(final String response, int id) {
                                    Log.d("rs",response);
                                    if(response.equals("true")){
                                        Toast.makeText(getApplication(),"참가 요청 성공", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplication(),"참가 요청 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            // if cancelButton is clicked, close the dialog
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        return true;
    }
}
