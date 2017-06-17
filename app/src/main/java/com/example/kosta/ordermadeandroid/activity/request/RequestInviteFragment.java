package com.example.kosta.ordermadeandroid.activity.request;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.InviteRequest;
import com.example.kosta.ordermadeandroid.dto.loader.InviteRequestLoader;
import com.example.kosta.ordermadeandroid.dto.loader.RequestLoader;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestInviteFragment extends Fragment {
    private List<InviteRequest> inviteRequestList;
    private RequestInviteListAdapter adapter;

    private static RequestInviteFragment instance;

    synchronized public static RequestInviteFragment newInstance() {
        if(instance == null) instance = new RequestInviteFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_request_invite, container, false);
        ListView inviteListView = (ListView)view.findViewById(R.id.requestInviteList);

        inviteRequestList = new ArrayList<>();
        adapter = new RequestInviteListAdapter(getActivity(), inviteRequestList);
        inviteListView.setAdapter(adapter);

        // load all inviterequests on create fragment
        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(Constants.mBaseUrl + "/request/xml/searchMyInviteRequestsForMaker.do")
                .addParams("form", "R")
                .build()
                .execute(new InviteRequestLoader(inviteRequestList, adapter));

        return view;
    }

}
