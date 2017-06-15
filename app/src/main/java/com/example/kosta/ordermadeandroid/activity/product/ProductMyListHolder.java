package com.example.kosta.ordermadeandroid.activity.product;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Product;

import java.util.ArrayList;

/**
 * Created by kosta on 2017-06-15.
 */

public class ProductMyListHolder extends RecyclerView.ViewHolder {
    public TextView tv_username, tv_postlikecount, tv_posttext;
    public ImageView iv_post;
    public CheckBox chk_like;

    public ProductMyListHolder(View itemView) {
        super(itemView);
        iv_post = (ImageView) itemView.findViewById(R.id.iv_post_img);
        tv_username = (TextView) itemView.findViewById(R.id.tv_user_nickname);
        tv_postlikecount = (TextView) itemView.findViewById(R.id.tv_like_count);
        tv_posttext = (TextView) itemView.findViewById(R.id.tv_post_text);
    }
}
