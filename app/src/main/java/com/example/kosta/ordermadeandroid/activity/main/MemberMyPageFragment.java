package com.example.kosta.ordermadeandroid.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kosta.ordermadeandroid.R;

/**
 * Created by kosta on 2017-06-08.
 */

public class MemberMyPageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_mypage, container, false);
        return view;
    }
}
