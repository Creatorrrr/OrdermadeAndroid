package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Product;

import java.util.ArrayList;

/**
 * Created by kosta on 2017-06-15.
 */

public class ProductMyListAdapter extends RecyclerView.Adapter<ProductMyListHolder> {

    private Context mContext;
    private ArrayList<Product> products;

    public ProductMyListAdapter(Context mContext, ArrayList<Product> listItem) {
        this.mContext = mContext;
        this.products = listItem;
    }

    @Override
    public ProductMyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View baseView = View.inflate(mContext, R.layout.fragment_mylist, null);
        ProductMyListHolder productMyListHolder = new ProductMyListHolder(baseView);

        return productMyListHolder;
    }

    @Override
    public void onBindViewHolder(ProductMyListHolder holder, int position) {
        Product item = products.get(position);

        holder.tv_posttext.setText(item.getTitle());
        holder.tv_username.setText(item.getMaker().getId());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
