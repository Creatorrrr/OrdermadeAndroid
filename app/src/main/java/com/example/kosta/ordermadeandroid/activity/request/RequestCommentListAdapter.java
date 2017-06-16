package com.example.kosta.ordermadeandroid.activity.request;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Comment;

import java.util.List;

/**
 * Created by kosta on 2017-06-16.
 */

public class RequestCommentListAdapter extends BaseAdapter {

    private Context context;
    private List<Comment> requestCommentData;
    private LayoutInflater inflater;

    public RequestCommentListAdapter(Context context, List<Comment> requestCommentData){
        this.context = context;
        this.requestCommentData = requestCommentData;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return requestCommentData.size();
    }

    @Override
    public Object getItem(int position) {
        return requestCommentData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ( convertView == null) {
            convertView = inflater.inflate(R.layout.request_detail_comment_list_item, null);
        }

        ((TextView)convertView.findViewById(R.id.request_detail_comment_memberId))
                .setText(requestCommentData.get(position).getId());
        ((TextView)convertView.findViewById(R.id.request_detail_comment_content))
                .setText(requestCommentData.get(position).getContent());

        return convertView;
    }
}
