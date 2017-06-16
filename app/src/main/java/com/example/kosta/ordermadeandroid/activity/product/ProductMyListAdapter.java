package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Product;

import java.util.List;

/**
 * Created by kosta on 2017-06-16.
 */

public class ProductMyListAdapter extends BaseAdapter {


    private Context context;
    private List<Product> products;
    private LayoutInflater inflater;

    public ProductMyListAdapter(Context context, List<Product> products){
        this.context = context;
        this.products = products;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ( convertView == null ) {
            convertView = inflater.inflate(R.layout.product_my_list_item, null);
        }

        TextView requestTitle = (TextView)convertView
                .findViewById(R.id.product_myList_title);
        TextView hopePrice = (TextView)convertView
                .findViewById(R.id.product_myList_price);
        TextView category = (TextView)convertView
                .findViewById(R.id.product_myList_category);

        Log.d("b", "-------productMyListAdapter Success-------");


        requestTitle.setText(products.get(position).getTitle());
        hopePrice.setText(products.get(position).getPrice()+"");
        category.setText(products.get(position).getCategory());

        return convertView;
    }
}
