package com.example.kosta.ordermadeandroid.activity.request;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.deal.DealConsumerFragment;
import com.example.kosta.ordermadeandroid.activity.main.MainActivity;

public class RequestDetailActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_request_detail, relativeLayout);

        setTitle("의뢰서 상세정보");

        Log.d("requestDetail", "---- detail Activity ---");
        RequestDetailFragment requestDetailFragment = new RequestDetailFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativeLayout_for_frame, requestDetailFragment).commit();
    }
}
