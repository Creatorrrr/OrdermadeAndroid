package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Product;
import com.example.kosta.ordermadeandroid.util.ImageLoadingTask;

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

        TextView title = (TextView)convertView.findViewById(R.id.product_my_list_title);
        TextView price = (TextView)convertView.findViewById(R.id.product_my_list_price);
        ImageView img = (ImageView)convertView.findViewById(R.id.product_my_list_image);

        title.setText(products.get(position).getTitle());
        price.setText(products.get(position).getPrice()+"원~");

        new ImageLoadingTask(img)
                .execute(Constants.mBaseUrl+"/main/file/download.do?fileName="
                        +products.get(position).getImage());

        return convertView;
    }
}
