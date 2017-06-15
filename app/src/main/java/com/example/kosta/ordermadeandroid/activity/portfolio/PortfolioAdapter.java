package com.example.kosta.ordermadeandroid.activity.portfolio;

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
import com.example.kosta.ordermadeandroid.dto.Portfolio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by kosta on 2017-06-14.
 */

public class PortfolioAdapter extends BaseAdapter {

    private Context context;
    private List<Portfolio> data;
    private LayoutInflater inflater;

    public PortfolioAdapter(Context context, List<Portfolio> data) {
        this.context = context;
        this.data = data;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_portfolio_listitem, null);
        }


        TextView title = (TextView) convertView.findViewById(R.id.titleText);
        TextView category = (TextView) convertView.findViewById(R.id.categoryText);

        ImageView img = (ImageView) convertView.findViewById(R.id.image);

        //      id.setText(data.get(position).getNo() + ""); //문자열로 전환

        title.setText(data.get(position).getTitle());
        category.setText(data.get(position).getCategory());

        //image세팅 해야함
        new ImageLoadingTask(img).execute(data.get(position).getImage());

        return convertView;
    }


    private class ImageLoadingTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewWeakReference;

        public ImageLoadingTask(ImageView img) {
            this.imageViewWeakReference = new WeakReference<ImageView>(img);
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

            if(isCancelled()){
                bitmap = null;
            }

            if(imageViewWeakReference != null){
                ImageView imageView = imageViewWeakReference.get();
                if(imageView != null){
                    imageView.setImageBitmap(bitmap);
                }
            }

        }
    }

    private Bitmap getRemoteImage(final URL url) {
        Bitmap bitmap = null;
        URLConnection connection = null;

        try {
            connection = url.openConnection();
            connection.connect();

            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
