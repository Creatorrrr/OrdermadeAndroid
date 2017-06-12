package com.example.kosta.ordermadeandroid.activity.deal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.PurchaseHistory;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by kosta on 2017-06-09.
 */

public class PurchaseHistoryAdapter extends BaseAdapter{

    private Context context;
    private List<PurchaseHistory> purchaseData;
    private LayoutInflater inflater;

    public PurchaseHistoryAdapter(Context context, List<PurchaseHistory> purchaseData){
        this.context = context;
        this.purchaseData = purchaseData;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return purchaseData.size();
    }

    @Override
    public Object getItem(int position) {
        return purchaseData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ( convertView == null ) {
            convertView = inflater.inflate(R.layout.purchasehistory_list_item, null);
        }

        TextView makerId = (TextView)convertView.findViewById(R.id.makerId);
        TextView productName = (TextView)convertView.findViewById(R.id.productName);
        TextView consumerId = (TextView)convertView.findViewById(R.id.consumerId);
        TextView productPrice = (TextView)convertView.findViewById(R.id.productPrice);
        TextView purchaseDate = (TextView)convertView.findViewById(R.id.purchaseDate);
        TextView progressStatus = (TextView)convertView.findViewById(R.id.progressStatus);

        Log.d("a", "---------Adapter success------");

        makerId.setText(purchaseData.get(position).getMaker().getId());
        Log.d("a", "-------------"+purchaseData.get(position).getMaker().getId());
        productName.setText(purchaseData.get(position).getRequest().getTitle());
        Log.d("a", "-------------"+purchaseData.get(position).getRequest().getTitle());
        consumerId.setText(purchaseData.get(position).getRequest().getConsumer().getId());
        Log.d("a", "-------------"+purchaseData.get(position).getRequest().getConsumer().getId());
//        productPrice.setText(purchaseData.get(position).getRequest().getPrice());
//        Log.d("a", "-------------"+purchaseData.get(position).getRequest().getPrice());

        // ?? Date Type -> String
        /*String gotDate = purchaseData.get(position).getOrderDate().toString();
        try {
            Date date = new SimpleDateFormat("yy/MM/dd").parse(gotDate);
            purchaseDate.setText(new SimpleDateFormat("yy MM dd").format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        //purchaseDate.setText((String)purchaseData.get(position).getOrderDate());

        progressStatus.setText(purchaseData.get(position).getDeliveryStatus());

        return convertView;
    }
}
