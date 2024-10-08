package com.example.kosta.ordermadeandroid.activity.request;

import android.app.Activity;
import android.os.Bundle;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.main.MainActivity;

public class RequestJoinActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_request_join, relativeLayout);

        setTitle("참가요청 내역");

        RequestJoinFragment joinFragment = new RequestJoinFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativeLayout_for_frame, joinFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
        view.getMenu().getItem(3).setChecked(true);
    }
}
