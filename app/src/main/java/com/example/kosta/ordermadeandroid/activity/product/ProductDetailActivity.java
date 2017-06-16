package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.databinding.ActivityProductDetailBinding;
import com.example.kosta.ordermadeandroid.dto.Product;

public class ProductDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ActivityProductDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Intent intent = getIntent();
        final Product product = (Product) intent.getExtras().get("product");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail);

        binding.setProduct(product);


        mToolbar = (Toolbar) findViewById(R.id.actionbar_productDetail);
        mToolbar.setTitle("상품 상세페이지");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
