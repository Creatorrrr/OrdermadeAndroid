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

public class RequestMyListAdapter extends BaseAdapter {

    private Context context;
    private List<Request> requestMyListData;
    private LayoutInflater inflater;

    public RequestMyListAdapter(Context context, List<Request> requestMyListData){
        this.context = context;
        this.requestMyListData = requestMyListData;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return requestMyListData.size();
    }

    @Override
    public Object getItem(int position) {
        return requestMyListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ( convertView == null ) {
            convertView = inflater.inflate(R.layout.request_my_list_item, null);
        }

        TextView requestId = (TextView)convertView
                .findViewById(R.id.request_myList_requestId);
        TextView makerId = (TextView)convertView
                .findViewById(R.id.request_myList_makerId);
        TextView requestTitle = (TextView)convertView
                .findViewById(R.id.request_myList_title);
        TextView hopePrice = (TextView)convertView
                .findViewById(R.id.request_myList_hopePrice);
        TextView category = (TextView)convertView
                .findViewById(R.id.request_myList_category);
        TextView requestContent = (TextView)convertView
                .findViewById(R.id.request_myList_content);

        Log.d("b", "--------RequestMyListAdapter Success-------");

        // 나의 의뢰서 리스트 출력
        requestId.setText(requestMyListData.get(position).getId());
        if ( requestMyListData.get(position).getMaker().getId() != null){
            makerId.setText(requestMyListData.get(position).getMaker().getId());
        }else{
            makerId.setText("없음");
        }
        requestTitle.setText(requestMyListData.get(position).getTitle());
        hopePrice.setText(requestMyListData.get(position).getHopePrice()+"");
        category.setText(requestMyListData.get(position).getCategory());
        requestContent.setText(requestMyListData.get(position).getContent());

        return convertView;
    }
}
