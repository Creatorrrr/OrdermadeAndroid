package com.example.kosta.ordermadeandroid.activity.request;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.InviteRequest;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * Created by kosta on 2017-06-15.
 */

public class RequestInviteListAdapter extends BaseAdapter {

    private Context context;
    private List<InviteRequest> inviteRequestList;
    private LayoutInflater inflater;

    public RequestInviteListAdapter(Context context, List<InviteRequest> inviteRequestList){
        this.context = context;
        this.inviteRequestList = inviteRequestList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return inviteRequestList.size();
    }

    @Override
    public Object getItem(int position) {
        return inviteRequestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if ( convertView == null ) {
            convertView = inflater.inflate(R.layout.request_join_list_item, null);
        }

        TextView title = (TextView)convertView.findViewById(R.id.request_join_title);
        TextView makerId = (TextView)convertView.findViewById(R.id.request_join_makerId);
        TextView message = (TextView)convertView.findViewById(R.id.request_join_message);
        Button acceptBtn = (Button)convertView.findViewById(R.id.request_join_acceptBtn);
        Button rejectBtn = (Button)convertView.findViewById(R.id.request_join_rejectBtn);

        title.setText(inviteRequestList.get(position).getRequest().getTitle());
        makerId.setText(inviteRequestList.get(position).getMaker().getId());
        message.setText(inviteRequestList.get(position).getMessage());

        // 수락 버튼 구현
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 의뢰서에 제작자 Id 업데이트
                OkHttpUtils.initClient(CustomApplication.getClient())
                        .get()
                        .url(Constants.mBaseUrl + "/request/xml/modifyMakerId.do")
                        .addParams("requestId", inviteRequestList.get(position).getRequest().getId().toString())
                        .addParams("makerId", inviteRequestList.get(position).getMaker().getId().toString())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("rs", e.getMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                // RequestController return value 수정
                                if (response.equals("true")) {
                                    Toast.makeText(context, "수락 성공", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "수락 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                // 1:1 매칭된 이후 참가요청 목록에서 제거
                OkHttpUtils.initClient(CustomApplication.getClient())
                        .get()
                        .url(Constants.mBaseUrl + "/request/xml/removeInviteByRequestId.do")
                        .addParams("requestId", inviteRequestList.get(position).getRequest().getId().toString())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("d", e.getMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                // RequestController return value 수정
                                if (response.equals("true")) {
                                    Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show();
                                    inviteRequestList.remove(position);
                                    notifyDataSetChanged();
                                }else{
                                    Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // 거절 버튼 구현
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 참가요청 목록에서 제거
                OkHttpUtils.initClient(CustomApplication.getClient())
                        .get()
                        .url(Constants.mBaseUrl + "/request/xml/removeInviteById.do")
                        .addParams("id", inviteRequestList.get(position).getId())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("rs", e.getMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                // RequestController return value 수정
                                if (response.equals("true")) {
                                    Toast.makeText(context, "거절 성공", Toast.LENGTH_SHORT).show();
                                    inviteRequestList.remove(position);
                                    notifyDataSetChanged();
                                }else{
                                    Toast.makeText(context, "거절 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return convertView;
    }

}
