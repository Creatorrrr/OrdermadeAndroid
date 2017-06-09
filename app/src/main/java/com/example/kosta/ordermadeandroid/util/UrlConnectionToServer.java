package com.example.kosta.ordermadeandroid.util;

import com.example.kosta.ordermadeandroid.constants.Constants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kosta on 2017-06-08.
 * //참고 http://blog.csdn.net/zhongguozhichuang/article/details/53259870
 */

public class UrlConnectionToServer {

	String urlAddress = Constants.SERVICE_URL + "/member/login.do";
	URL url;
	HttpURLConnection uRLConnection;

	public UrlConnectionToServer() {

	}

	//向服务器发送get请求
	public String doGet(String username, String password) {
		String getUrl = urlAddress + "?id=" + username + "&password=" + password;
		try {
			url = new URL(getUrl);
			uRLConnection = (HttpURLConnection) url.openConnection();
			InputStream is = uRLConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String response = "";
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				//response = br.readLine();
				response = response + readLine;
			}
			is.close();
			br.close();
			uRLConnection.disconnect();
			return response;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	//向服务器发送post请求
	public String doPost(String username, String password) {
		try {
			url = new URL(urlAddress);
			uRLConnection = (HttpURLConnection) url.openConnection();
			uRLConnection.setDoInput(true);
			uRLConnection.setDoOutput(true);
			uRLConnection.setRequestMethod("POST");
			uRLConnection.setUseCaches(false);		//캐시 사용하지 않음
			uRLConnection.setInstanceFollowRedirects(false);
			uRLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//			uRLConnection.setRequestProperty("Content-length", "" + requestStringBytes.length);
//			uRLConnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
//			uRLConnection.setRequestProperty("Charset", "UTF-8");
			uRLConnection.connect();

			DataOutputStream out = new DataOutputStream(uRLConnection.getOutputStream());
			String content = "id=" + username + "&password=" + password;
			out.writeBytes(content);
			out.flush();
			out.close();

			InputStream is = uRLConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String response = "";
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				//response = br.readLine();
				response = response + readLine;
			}
			is.close();
			br.close();
			uRLConnection.disconnect();
			return response;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
