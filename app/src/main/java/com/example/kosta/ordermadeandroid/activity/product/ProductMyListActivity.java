package com.example.kosta.ordermadeandroid.activity.product;

import android.os.Bundle;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.main.MainActivity;

public class ProductMyListActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_product_my_list, relativeLayout);

        setTitle("나의 상품페이지");

        ProductMyListFragment products = new ProductMyListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativeLayout_for_frame, products).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // to check current activity in the navigation drawer
        view.getMenu().getItem(2).setChecked(true);
    }

}
