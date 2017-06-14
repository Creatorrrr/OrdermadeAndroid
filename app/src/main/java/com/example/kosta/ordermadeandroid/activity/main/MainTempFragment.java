package com.example.kosta.ordermadeandroid.activity.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.portfolio.PortfolioDetailActivity;
import com.example.kosta.ordermadeandroid.activity.portfolio.PortfolioRegister2Activity;
import com.example.kosta.ordermadeandroid.activity.product.ProductDetailActivity;
import com.example.kosta.ordermadeandroid.activity.product.ProductRegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainTempFragment extends Fragment {
    public static final String TAG = MainTempFragment.class.getSimpleName();

    public MainTempFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_temp, container, false);


        view.findViewById(R.id.productRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick");
                Intent intent = new Intent(getActivity(), ProductRegisterActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.portfolioRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PortfolioRegister2Activity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.productDetail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.portfolioDetail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PortfolioDetailActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
