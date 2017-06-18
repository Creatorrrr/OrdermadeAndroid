package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Review;
import com.example.kosta.ordermadeandroid.util.ImageLoadingTask;

import java.util.List;

/**
 * Created by HoneyM on 2017-06-17.
 */

public class ProductReviewListAdapter extends BaseAdapter {

    private Context context;
    private List<Review> productReviewData;
    private LayoutInflater inflater;

    public ProductReviewListAdapter(Context context, List<Review> productReviewData) {
        this.context = context;
        this.productReviewData = productReviewData;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return productReviewData.size();
    }

    @Override
    public Object getItem(int position) {
        return productReviewData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ( convertView == null ) {
            convertView = inflater.inflate(R.layout.product_detail_review_list_item, null);
        }

        ((TextView)convertView.findViewById(R.id.product_detail_review_title))
                .setText(productReviewData.get(position).getTitle());
        ((TextView)convertView.findViewById(R.id.product_detail_review_content))
                .setText(productReviewData.get(position).getContent());
        ((TextView)convertView.findViewById(R.id.product_detail_review_memberId))
                .setText(productReviewData.get(position).getConsumer().getId());

        ImageView image = (ImageView)convertView.findViewById(R.id.product_detail_review_image);

        new ImageLoadingTask(image).execute(Constants.mBaseUrl
                +"/main/file/download.do?fileName="+productReviewData
                .get(position).getConsumer().getImage());

        return convertView;
    }
}
