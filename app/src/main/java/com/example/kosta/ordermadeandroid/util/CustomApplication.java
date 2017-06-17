package com.example.kosta.ordermadeandroid.util;

import android.app.Application;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;

/**
 * Created by kosta on 2017-06-17.
 */

public class CustomApplication extends Application {

	private static OkHttpClient sOkHttpClient;

	@Override
	public void onCreate() {
		super.onCreate();

		initializeClient();
	}

	private void initializeClient() {
		ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
		sOkHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
	}

	public static OkHttpClient getClient() {
		return sOkHttpClient;
	}

}