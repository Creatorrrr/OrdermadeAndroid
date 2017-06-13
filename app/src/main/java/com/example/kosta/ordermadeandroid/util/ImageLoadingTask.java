package com.example.kosta.ordermadeandroid.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class ImageLoadingTask extends AsyncTask<String, Void, Bitmap> {

	private final WeakReference<ImageView> imageViewReference;

	public ImageLoadingTask(ImageView image){
		imageViewReference = new WeakReference<ImageView>(image);
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
		ImageView imageView = imageViewReference.get();
		if(imageView != null){
			imageView.setImageBitmap(bitmap);
		}
	}




	private Bitmap getRemoteImage(final URL url){
		Bitmap bitmap =null;
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
		return bitmap;
	}



}

