package com.example.kosta.ordermadeandroid.activity.product;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.databinding.ProductRegisterDataBinding;
import com.example.kosta.ordermadeandroid.dto.Product;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ProductRegisterActivity extends AppCompatActivity {
    public static final String TAG = ProductRegisterActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private Product product;
    ProductRegisterDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_register);
        product = new Product();
        binding.setProduct(product);
        binding.setActivity(this);


        mToolbar = (Toolbar) findViewById(R.id.actionbar_productRegister);
        mToolbar.setTitle("상품 등록");
        //mToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_folder_open_white);

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

    public void onButtonClick(String title, String content) {
        Log.d(TAG, "Title :" + binding.getProduct().getTitle());
        Log.d(TAG, "Content :" + binding.getProduct().getContent());
        new productRegisterTask().execute("http://10.0.2.2:8080/")

    }


    private class productRegisterTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection http = null;
            InputStream is = null;
            try {
                URL url = new URL(params[0]);
                http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
