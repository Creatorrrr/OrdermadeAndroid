package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.example.kosta.ordermadeandroid.dto.Product;

/**
 * Created by kosta on 2017-06-16.
 */

public class UserClickHandler {
    private final Product product;

    public UserClickHandler(Product product) {
        this.product = product;
    }

    public void onClick(View view) {
        Snackbar.make(view, product.getTitle() + "is selected", Snackbar.LENGTH_LONG).show();
    }
}
