package com.example.kosta.ordermadeandroid.activity.member;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.main.MainActivity;

public class MemberMyPageActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_member_mypage, relativeLayout);

        setTitle("나의 프로필");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativeLayout_for_frame, new MemberMyPageFragment())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.getMenu().getItem(0).setChecked(true);
    }
}
