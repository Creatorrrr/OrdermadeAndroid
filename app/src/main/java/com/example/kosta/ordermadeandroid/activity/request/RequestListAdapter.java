package com.example.kosta.ordermadeandroid.activity.request;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Request;

import java.util.List;

/**
 * Created by kosta on 2017-06-13.
 */

public class RequestListAdapter extends BaseAdapter {

    private List<Request> requestList;
    private LayoutInflater inflater;

    public RequestListAdapter(Context context, List<Request> requestList){
        this.requestList = requestList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return requestList.size();
    }

    @Override
    public Object getItem(int position) {
        return requestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ( convertView == null ) {
            convertView = inflater.inflate(R.layout.request_list_item, null);
        }

        TextView requestId = (TextView)convertView
                .findViewById(R.id.requestListId);
        TextView requestTitle = (TextView)convertView
                .findViewById(R.id.requestListTitle);
        TextView hopePrice = (TextView)convertView
                .findViewById(R.id.requestListHopePrice);
        TextView category = (TextView)convertView
                .findViewById(R.id.requestListCategory);

        Log.d("b", "--------RequestMyListAdapter Success-------");

        requestId.setText(requestList.get(position).getId());
        requestTitle.setText(requestList.get(position).getTitle());
        hopePrice.setText(requestList.get(position).getHopePrice()+"");
        category.setText(requestList.get(position).getCategory());

        return convertView;
    }
}
