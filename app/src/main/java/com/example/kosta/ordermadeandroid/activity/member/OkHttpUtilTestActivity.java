package com.example.kosta.ordermadeandroid.activity.member;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class OkHttpUtilTestActivity extends AppCompatActivity {
	//참고 https://github.com/hongyangAndroid/okhttputils

	private OkHttpClient okHttpClient;
	private TextView mTvResult;
	private ImageView mIvResult;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ok_http_test);

//		okHttpClient = new OkHttpClient.Builder().cookieJar(new CookiesManager()).build();
		ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
		okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();

		mTvResult = (TextView) findViewById(R.id.id_tv_result);
		mIvResult = (ImageView) findViewById(R.id.id_iv_result);
	}


	//--------------- Auto Cookies Manager
//	private class CookiesManager implements CookieJar {
//		private final PersistentCookieStore cookieStore = new PersistentCookieStore(getApplicationContext());
//
//		@Override
//		public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//			if (cookies != null && cookies.size() > 0) {
//				cookieStore.add(url,cookies);
//			}
//		}
//
//		@Override
//		public List<Cookie> loadForRequest(HttpUrl url) {
//			List<Cookie> cookies = cookieStore.get(url);
//			return cookies;
//		}
//	}

	//------------------

	//GET
	public void doGet(View view) throws IOException{
		OkHttpUtils.initClient(okHttpClient)
				.get()
				.url(Constants.mBaseUrl + "/product/xml/main/category/hit.do")
				.addParams("category", "FUNITURE").addParams("page", "10")
				.url(Constants.mBaseUrl + "/member/xml/myPage.do")
				/*.addParams("id", "user1").addParams("password", "1111")*/
				.build()
				.execute(callback);
	}


		private StringCallback callback = new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				Log.d("a","----------onFailure-----" +e.getMessage());
				e.printStackTrace();
			}

			@Override
			public void onResponse(final String response, int id) {
				Log.d("a","----------onResponse-----"+id);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mTvResult.setText(response.toString());
					}
				});
			}
		};



	//POST
	public void doPost(View view) throws IOException{
		OkHttpUtils.initClient(okHttpClient)
				.post()
				.url(Constants.mBaseUrl + "/member/login.do")
				.addParams("id", "c1")
				.addParams("password", "123")
				.build()
				.execute(callback);
	}


	//POST String
	public void doPostString(View view){
		OkHttpUtils.initClient(okHttpClient)
				.postString()
				.url(Constants.mBaseUrl + "/test/postString.do")
				.content("string..kadjfadf{ds:sdf}")
				.mediaType(MediaType.parse("text/plain; charset=utf-8"))
				//.content(new Gson().toJson(new User("zhy", "123")))
				//.mediaType(MediaType.parse("application/json; charset=utf-8"))
				.build()
				.execute(callback);

	}

//
//		//StringCallBack,FileCallBack,BitmapCallback and custom...
//		public abstract class UserCallback extends Callback<User>
//		{
//			@Override
//			public User parseNetworkResponse(Response response) throws IOException
//			{
//				String string = response.body().string();
//				User user = new Gson().fromJson(string, User.class);
//				return user;
//			}
//		}
//
//	//POST File
//	public void doUserCallback(View view) {
//		OkHttpUtils
//				.get()//
//				.url(url)//
//				.addParams("id", "user1")//
//				.addParams("password", "123")//
//				.build()//
//				.execute(new UserCallback() {
//					@Override
//					public void onError(Request request, Exception e) {
//						mTv.setText("onError:" + e.getMessage());
//					}
//
//					@Override
//					public void onResponse(User response) {
//						mTv.setText("onResponse:" + response.username);
//					}
//				});
//	}
//
	//POST File-----아래부터 다 안됨. (아직 수정하지 못했음.)
	public void doPostFile(View view){
		File file = new File("/storage/emulated/0/DCIM/Camera/IMG_20170420_042659.jpg");
		//File file = new File(Environment.getExternalStorageDirectory(), "all_downloads/7/2017-06-10-06-44-56-2079066860.png");
		if(!file.exists()){
			Log.d("a",file.getAbsolutePath()+" not exist!");
			return ;
		}

		OkHttpUtils.initClient(okHttpClient)
				.postFile()
				.url(Constants.mBaseUrl + "/test/postFile.do")
				.file(file)
				.build()
				.execute(callback);

	}

//
//	public  void doPostFile(View view){
//
//		File file = new File("/storage/emulated/0/DCIM/Camera/IMG_20170420_042659.jpg");
//		if(!file.exists()){
//			Log.d("a",file.getAbsolutePath()+" not exist!");
//			return ;
//		}
//		OkHttpUtils.post()//
//				.addFile("mFile", "messenger_01.png", file)//
//				//.addFile("mFile", "test1.txt", file2)//
//				.url(url)
//				.params(params)//
//				.headers(headers)//
//				.build()//
//				.execute(new MyStringCallback());
//	}



	//POST Upload
	public void doUpload(View view){

	}



	//GET download
	public void doDownload(){

	}


	//GET Download Image
	public void doDownloadImg(){
		mTvResult.setText("");
		String url = "http://images.csdn.net/20150817/1.jpg";
		Log.d("a","------------");
//
//		OkHttpUtils.initClient(okHttpClient)
//				.get()//
//				.url(url)//
//				.tag(this)//
//				.build()//
//				.connTimeOut(20000)
//				.readTimeOut(20000)
//				.writeTimeOut(20000)
//				.execute(new BitmapCallback()
//				{
//					@Override
//					public void onError(Call call, Exception e, int id)
//					{
//						//mTvResult.setText("onError:" + e.getMessage());
//					}
//
//					@Override
//					public void onResponse(Bitmap bitmap, int id)
//					{
//						Log.e("a", "onResponse：complete");
//						//mIvResult.setImageBitmap(bitmap);
//					}
//				});

	}



	//-------------test server file code

//
//	package ordermade.controller;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//	@Controller
//	public class OkHttpTestController {
//
//		//------------------test
//		//POST String
//		@RequestMapping(value="test/postString.do", method=RequestMethod.POST, produces="text/plain")
//		public @ResponseBody String postString(HttpServletRequest request) throws IOException{
//			System.out.println("sessionId="+request.getSession().getId());
//			ServletInputStream is = request.getInputStream();
//			StringBuilder sb = new StringBuilder();
//			int len = 0;
//			byte[] buf = new byte[1024];
//			while((len = is.read(buf)) != -1){
//				sb.append(new String(buf, 0, len));
//			}
//			System.out.println(sb.toString());
//			return sb.toString();
//		}
//
//
//		//POST File
//		@RequestMapping(value="test/postFile.do",method=RequestMethod.POST,produces="text/plain")
//		public @ResponseBody String postFile(HttpServletRequest request) throws IOException{
//			ServletInputStream is = request.getInputStream();
//			System.out.println("sessionId="+request.getSession().getId());
//
//			String dir = request.getRealPath("");
//			System.out.println(dir);
//			File file = new File(dir, "man1.jpg");
//			FileOutputStream fos = new FileOutputStream(file);
//			int len = 0;
//			byte[] buf = new byte[1024];
//			while((len = is.read(buf)) != -1){
//				fos.write(buf, 0, len);
//			}
//			fos.flush();
//			fos.close();
//			return "true";
//		}
//
//
//		//POST UploadFile
//		@RequestMapping(value="test/uploadInfo.do",method=RequestMethod.POST)
//		public String uploadInfo(String id, String password, File mPhoto, HttpServletRequest request) throws IOException{
//			System.out.println("sessionId="+request.getSession().getId());
//			//File mPhoto = null;
//			String mPhotoFileName = null;
//			if(mPhoto == null){
//				System.out.println(mPhotoFileName +" is null.");
//			}
//			String dir = request.getRealPath("files");
//			File file = new File(dir, mPhotoFileName);
//			FileCopyUtils.copy(mPhoto, file);
//			return null;
//		}
//
//
//		//-----------------------
//
//
//
//
//	}

}
