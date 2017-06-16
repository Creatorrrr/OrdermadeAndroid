package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.databinding.ProductMyListDataBing;
import com.example.kosta.ordermadeandroid.dto.Product;

import java.util.ArrayList;

/**
 * Created by kosta on 2017-06-15.
 */

public class ProductMyListAdapter extends RecyclerView.Adapter<ProductMyListHolder> {

    private Context mContext;
    private ArrayList<Product> products;
    private LayoutInflater inflater;

    public ProductMyListAdapter(Context mContext, ArrayList<Product> listItem) {
        this.mContext = mContext;
        this.products = listItem;
    }

    //제네릭 형식의 변수로 ViewHoler를 생성
    @Override
    public ProductMyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        ProductMyListDataBing dataBing = ProductMyListDataBing.inflate(inflater, parent, false);

        return new ProductMyListHolder(dataBing);
    }

    //만들어진 ViewHolder에 데이터를 넣는 작업
    @Override
    public void onBindViewHolder(ProductMyListHolder holder, int position) {
        final Product item = products.get(position);
        holder.bind(item);

        final ProductMyListDataBing dataBing = holder.getProductMyListDataBing();
        dataBing.setHandler(new ItemClickHandler() {
            @Override
            public void onImgClick() {
                Toast.makeText(mContext, "Good good", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("product",item);
                mContext.startActivity(intent);
            }
        });

    }

    //데이터의 갯수
    @Override
    public int getItemCount() {
        return products.size();
    }
}
