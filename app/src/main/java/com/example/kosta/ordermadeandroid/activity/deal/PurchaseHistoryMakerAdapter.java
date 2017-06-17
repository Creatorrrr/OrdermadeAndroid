package com.example.kosta.ordermadeandroid.activity.deal;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.PurchaseHistory;
import com.example.kosta.ordermadeandroid.dto.Request;
import com.example.kosta.ordermadeandroid.dto.loader.PurchaseHistoryLoader;
import com.example.kosta.ordermadeandroid.dto.loader.RequestLoader;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

/**
 * Created by kosta on 2017-06-09.
 */

public class PurchaseHistoryMakerAdapter extends BaseAdapter{
    private Context context;
    private List<PurchaseHistory> purchaseHistoryList;
    private LayoutInflater inflater;

    public PurchaseHistoryMakerAdapter(Context context, List<PurchaseHistory> purchaseHistoryList){
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
            convertView = inflater.inflate(R.layout.purchasehistory_maker_list_item, null);

            holder = new Holder();
            holder.consumerId = (TextView)convertView.findViewById(R.id.consumerId);
            holder.requestTitle = (TextView)convertView.findViewById(R.id.requestTitle);
            holder.purchaseDate = (TextView)convertView.findViewById(R.id.purchaseDate);
            holder.progressStatus = (TextView)convertView.findViewById(R.id.progressStatus);
            holder.delivery = (Button)convertView.findViewById(R.id.delivery);

            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.purchasehistory_maker_delivery_dialog);
            dialog.setTitle("배송정보입력");

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;

            final EditText invoiceNumber = (EditText)dialog.findViewById(R.id.invoiceNumber);
            final EditText charge = (EditText)dialog.findViewById(R.id.charge);

            dialog.findViewById(R.id.dialogOK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkHttpUtils.initClient(CustomApplication.getClient())
                            .post()
                            .url(Constants.mBaseUrl + "/deal/xml/purchaseHistory/delivery.do")
                            .addParams("id", purchaseHistory.getId())
                            .addParams("invoiceNumber", invoiceNumber.getText().toString())
                            .addParams("charge", charge.getText().toString())
                            .build()
                            .execute(new PurchaseHistoryLoader(purchaseHistoryList, PurchaseHistoryMakerAdapter.this) {
                                @Override
                                public void onAfter(int id) {
                                    super.onAfter(id);
                                    holder.delivery.setText("배송완료");
                                    holder.delivery.setEnabled(false);
                                    dialog.dismiss();
                                }
                            });
                }
            });

            dialog.findViewById(R.id.dialogCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            holder.delivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (Holder)convertView.getTag();
        }

        holder.consumerId.setText(purchaseHistory.getConsumer().getId());
        holder.requestTitle.setText(purchaseHistory.getRequest().getTitle());
        holder.purchaseDate.setText(purchaseHistory.getOrderDate());

        if(purchaseHistory.getDeliveryStatus().equals(Constants.DELIVERY_PREPARE)){
            holder.progressStatus.setText("배송준비중");
            holder.delivery.setText("상품배송");
        } else if(purchaseHistory.getDeliveryStatus().equals(Constants.DELIVERY_COMPLETE)) {
            holder.progressStatus.setText("배송완료");
            holder.delivery.setText("배송완료");
            holder.delivery.setEnabled(false);
        }

        return convertView;
    }

    private class Holder {
        public TextView consumerId;
        public TextView requestTitle;
        public TextView purchaseDate;
        public TextView progressStatus;
        public Button delivery;
    }
}
