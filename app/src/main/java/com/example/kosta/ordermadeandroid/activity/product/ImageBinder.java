package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by kosta on 2017-06-15.
 */

public final class ImageBinder {
    private ImageBinder() {

    }

    @BindingAdapter({"imageUrl"})
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Picasso.with(context).load(url).into(imageView);
    }
}
