package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.kosta.ordermadeandroid.databinding.ProductMyListDataBing;
import com.example.kosta.ordermadeandroid.dto.Product;

/**
 * Created by kosta on 2017-06-15.
 */

public class ProductMyListHolder extends RecyclerView.ViewHolder {
    private ProductMyListDataBing productMyListDataBing;
    public ProductMyListHolder(ProductMyListDataBing dataBing) {
        super(dataBing.getRoot());
        this.productMyListDataBing = dataBing;
    }

    public void bind(Product product) {

        this.productMyListDataBing.setProduct(product);
//        this.productMyListDataBing.setHandler(new UserClickHandler(product));
    }
    public ProductMyListDataBing getProductMyListDataBing() {
        return productMyListDataBing;
    }


}
