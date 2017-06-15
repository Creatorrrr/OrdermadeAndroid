package com.example.kosta.ordermadeandroid.activity.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Product;

import java.util.ArrayList;

/**
 * Created by kosta on 2017-06-15.
 */

public class ProductMyList extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_mylist);

        ArrayList<Product> listItem = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.myProductList);

        for (int i = 0; i < 30; i++) {
            Product p = new Product();
            p.setId("http://1.bp.blogspot.com/-lzIhy7uMVYs/UFXkn68gAgI/AAAAAAAABnE/TaFLub7tDo8/s400/parrot.jpg");
            p.setTitle(String.valueOf(i));
            p.setPrice(i);

            listItem.add(i, p);
        }
        ProductMyListAdapter adapter = new ProductMyListAdapter(this, listItem);
        recyclerView.setAdapter(adapter);
    }
}
