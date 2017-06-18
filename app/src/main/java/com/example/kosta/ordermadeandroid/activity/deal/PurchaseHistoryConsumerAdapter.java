package com.example.kosta.ordermadeandroid.activity.deal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.PurchaseHistory;
import com.example.kosta.ordermadeandroid.dto.loader.PurchaseHistoryLoader;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

/**
 * Created by kosta on 2017-06-09.
 */

public class PurchaseHistoryConsumerAdapter extends BaseAdapter{
    private Context context;
    private List<PurchaseHistory> purchaseHistoryList;
    private LayoutInflater inflater;

    public PurchaseHistoryConsumerAdapter(Context context, List<PurchaseHistory> purchaseHistoryList){
        this.context = context;
        this.purchaseHistoryList = purchaseHistoryList;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return purchaseHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return purchaseHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PurchaseHistory purchaseHistory = purchaseHistoryList.get(position);
        final Holder holder;

        if ( convertView == null ) {
            convertView = inflater.inflate(R.layout.purchasehistory_consumer_list_item, null);

            holder = new Holder();
            holder.makerId = (TextView)convertView.findViewById(R.id.makerId);
            holder.requestTitle = (TextView)convertView.findViewById(R.id.requestTitle);
            holder.purchaseDate = (TextView)convertView.findViewById(R.id.purchaseDate);
            holder.progressStatus = (TextView)convertView.findViewById(R.id.progressStatus);
            holder.decision = (Button)convertView.findViewById(R.id.decision);

            final AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle("구매확정 확인")
                    .setMessage("구매확정 하시겠습니까?")
                    .setPositiveButton("구매확정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            OkHttpUtils.initClient(CustomApplication.getClient())
                                    .get()
                                    .url(Constants.mBaseUrl + "/deal/xml/account/makerMoney.do")
                                    .addParams("requestId", purchaseHistory.getRequest().getId())
                                    .build()
                                    .execute(new PurchaseHistoryLoader(purchaseHistoryList, PurchaseHistoryConsumerAdapter.this) {
                                        @Override
                                        public void onAfter(int id) {
                                            super.onAfter(id);
                                            holder.decision.setText("구매완료");
                                            holder.decision.setEnabled(false);
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();

            holder.decision.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.show();
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        holder.makerId.setText(purchaseHistory.getMaker().getId());
        holder.requestTitle.setText(purchaseHistory.getRequest().getTitle());
        holder.purchaseDate.setText(purchaseHistory.getOrderDate());

        if(purchaseHistory.getDeliveryStatus().equals(Constants.DELIVERY_PREPARE)) {
            holder.progressStatus.setText("배송준비중");
        } else if(purchaseHistory.getDeliveryStatus().equals(Constants.DELIVERY_COMPLETE)) {
            holder.progressStatus.setText("배송완료");
        }

        if(purchaseHistory.getPayment().equals(Constants.PAYMENT_N)){
            holder.decision.setText("구매확정");
        } else if(purchaseHistory.getPayment().equals(Constants.PAYMENT_Y)) {
            holder.decision.setText("구매완료");
            holder.decision.setEnabled(false);
        }

        return convertView;
    }

    private class Holder {
        public TextView makerId;
        public TextView requestTitle;
        public TextView purchaseDate;
        public TextView progressStatus;
        public Button decision;
    }
}
