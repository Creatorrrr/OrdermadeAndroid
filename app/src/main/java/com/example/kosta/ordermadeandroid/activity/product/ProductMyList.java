package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Product;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kosta on 2017-06-15.
 */

public class ProductMyList extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static Context mContext;
    private static final String TAG = "ProductMyList";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_mylist);
        Log.d(TAG, "---------------------1---------------------");
        AsyncTask<String, Void, String> task = new productList();
        task.execute("http://10.0.2.2:8080/ordermade/product/ajax/products/json/makerid.do?page=1&makerId=m10");
        Log.d(TAG, "----------------------2--------------------");
        recyclerView = (RecyclerView) findViewById(R.id.myProductListEom);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new ProductMyListAdapterEom(this, getData()));


    }

    private ArrayList<Product> getData() {
        ArrayList<Product> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Product p = new Product();
            p.title = String.valueOf(i);
            p.setImage("http://1.bp.blogspot.com/-lzIhy7uMVYs/UFXkn68gAgI/AAAAAAAABnE/TaFLub7tDo8/s400/parrot.jpg");
            p.price = i;

            data.add(p);
        }
        return data;
    }

    class productList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, "************************************** ");

            String url = params[0];

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.d(TAG, s);
//        }
    }


}
