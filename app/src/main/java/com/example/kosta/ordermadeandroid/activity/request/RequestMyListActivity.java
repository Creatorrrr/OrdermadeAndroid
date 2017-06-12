package com.example.kosta.ordermadeandroid.activity.request;

import android.app.Activity;
import android.os.Bundle;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.main.MainActivity;

public class RequestMyListActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_request_my_list, relativeLayout);

        setTitle("나의 의뢰서");

        RequestMyListFragment requestMyList = new RequestMyListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativeLayout_for_frame, requestMyList).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
        view.getMenu().getItem(2).setChecked(true);
    }
}
