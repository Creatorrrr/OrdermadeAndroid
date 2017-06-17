package com.example.kosta.ordermadeandroid.activity.request;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.example.kosta.ordermadeandroid.dto.loader.RequestLoader;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class RequestSearchFragment extends Fragment {
    private static final int ALL_REQUEST = 0;
    private static final int SEND_REQUEST = 1;

    private static final int SEARCH_TITLE = 0;
    private static final int SEARCH_CONTENT = 1;

    private List<Request> requestList;
    private RequestListAdapter adapter;

    private int listChange;
    private int searchType;

    private static RequestSearchFragment instance;

    synchronized public static RequestSearchFragment newInstance() {
        if(instance == null) instance =  new RequestSearchFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_request_search, container, false);

        requestList = new ArrayList<>();
        adapter = new RequestListAdapter(getActivity(), requestList);

        ListView listView = (ListView)view.findViewById(R.id.request_bound_list);
        listView.setAdapter(adapter);

        final Spinner searchTypeSpinner = (Spinner)view.findViewById(R.id.requestSearchType);
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

        final EditText keyword = (EditText)view.findViewById(R.id.requestSearchKeyword);
        view.findViewById(R.id.requestSearchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestList.clear();
                if (searchType == SEARCH_TITLE) {
                    OkHttpUtils.initClient(CustomApplication.getClient())
                            .get()
                            .url(Constants.mBaseUrl + "/request/xml/searchBoundAndTitle.do")
                            .addParams("title", keyword.getText().toString())
                            .build()
                            .execute(new RequestLoader(requestList, adapter));
                } else {
                    OkHttpUtils.initClient(CustomApplication.getClient())
                            .get()
                            .url(Constants.mBaseUrl + "/request/xml/searchBoundAndContent.do")
                            .addParams("content", keyword.getText().toString())
                            .build()
                            .execute(new RequestLoader(requestList, adapter));
                }
                listChange = ALL_REQUEST;
            }
        });

        // load all requests on create fragment
        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(Constants.mBaseUrl + "/request/xml/searchBound.do")
                .addParams("page", "1")
                .build()
                .execute(new RequestLoader(requestList, adapter));

        // set buttons listener
        view.findViewById(R.id.allRequestBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestList.clear();
                OkHttpUtils.initClient(CustomApplication.getClient())
                        .get()
                        .url(Constants.mBaseUrl + "/request/xml/searchBound.do")
                        .addParams("page", "1")
                        .build()
                        .execute(new RequestLoader(requestList, adapter));
                listChange = ALL_REQUEST;
            }
        });

        final List<InviteRequest> inviteRequestList = new ArrayList<InviteRequest>();

        view.findViewById(R.id.sendRequestBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestList.clear();
                OkHttpUtils.initClient(CustomApplication.getClient())
                        .get()
                        .url(Constants.mBaseUrl + "/request/xml/searchMyInviteRequestsForMaker.do")
                        .addParams("form", "R")
                        .build()
                        .execute(new RequestLoader(requestList, adapter));
                listChange = SEND_REQUEST;
            }
        });

        registerForContextMenu(listView);

        return view;
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
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.request_join_dialog);
            dialog.setTitle("참가요청");

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;

            final Request request = requestList.get(((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position);
            final EditText content = (EditText) dialog.findViewById(R.id.requestJoinContent);
            Button okButton = (Button)dialog.findViewById(R.id.requestJoinDialogOK);
            Button cancelButton = (Button)dialog.findViewById(R.id.requestJoinDialogCancel);

            // if okButton is clicked, send join request
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkHttpUtils.initClient(CustomApplication.getClient())
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
                                        Toast.makeText(getActivity().getApplication(),"참가 요청 성공", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getActivity().getApplication(),"참가 요청 실패", Toast.LENGTH_SHORT).show();
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
