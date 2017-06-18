package com.example.kosta.ordermadeandroid.activity.request;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Request;

import java.util.List;

/**
 * Created by HoneyM on 2017-06-17.
 */

public class RequestReceivedAdapter extends BaseAdapter {

    private Context context;
    private List<Request> requestReceivedData;
    private LayoutInflater inflater;

    public RequestReceivedAdapter(Context context, List<Request> requestReceivedData) {
        this.context = context;
        this.requestReceivedData = requestReceivedData;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return requestReceivedData.size();
    }

    @Override
    public Object getItem(int position) {
        return requestReceivedData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ( convertView == null ) {
            convertView = inflater.inflate(R.layout.request_received_list_item, null);
        }

        if ( requestReceivedData.get(position).getMaker().getId() != null ){
            ((TextView)convertView.findViewById(R.id.request_received_makerId))
                    .setText(requestReceivedData.get(position).getMaker().getId());
        }else{
            ((TextView)convertView.findViewById(R.id.request_received_makerId))
                    .setText("없음");
        }
        ((TextView)convertView.findViewById(R.id.request_received_title))
                .setText(requestReceivedData.get(position).getTitle());
        ((TextView)convertView.findViewById(R.id.request_received_hopePrice))
                .setText(requestReceivedData.get(position).getHopePrice()+"");
        ((TextView)convertView.findViewById(R.id.request_received_category))
                .setText(requestReceivedData.get(position).getCategory());
        ((TextView)convertView.findViewById(R.id.request_received_content))
                .setText(requestReceivedData.get(position).getContent());

        return convertView;
    }
}
