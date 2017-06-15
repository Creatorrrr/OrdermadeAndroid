package com.example.kosta.ordermadeandroid.activity.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.dto.Product;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by kosta on 2017-06-13.
 */

public class MainProductAdapter extends BaseAdapter {

    private Context context;
    private List<Product> productData;
    private LayoutInflater inflater;

    public  MainProductAdapter(Context context, List<Product> productData){
        this.context = context;
        this.productData = productData;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return productData.size();
    }

    @Override
    public Object getItem(int position) {
        return productData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if ( convertView == null ) {
            convertView = inflater.inflate(R.layout.main_list_item, null);
        }
        TextView title = (TextView)convertView.findViewById(R.id.hitProduct_title);
        TextView price = (TextView)convertView.findViewById(R.id.hitProduct_price);
        ImageView img = (ImageView)convertView.findViewById(R.id.hitProduct_for_main);

        title.setText(productData.get(position).getTitle());
        price.setText(productData.get(position).getPrice()+"원~");

        new ImageLoadingTask(img)
                .execute("http://10.0.2.2:8080/ordermade/main/file/download.do?fileName="
                        +productData.get(position).getImage());

        return convertView;
    }

    private class ImageLoadingTask extends AsyncTask<String, Void, Bitmap>{

        private final WeakReference<ImageView> imageViewWeakReference;

        public ImageLoadingTask(ImageView img) {
            imageViewWeakReference = new WeakReference<ImageView>(img);
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return getRemoteImage(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = imageViewWeakReference.get();
            if ( imageView != null ) {
                imageView.setImageBitmap(bitmap);
            }
            super.onPostExecute(bitmap);
        }
    }

    private Bitmap getRemoteImage(final URL url) {
        Bitmap bitmap = null;
        URLConnection conn;

        try {
            conn = url.openConnection();
            conn.connect();

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap; //return getRemoteImage(Url)
    }
}
