package com.example.kosta.ordermadeandroid.activity.member;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.util.PersistentCookieStore;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class OkHttpUtilTestActivity extends AppCompatActivity {
	//참고 https://github.com/hongyangAndroid/okhttputils

	private OkHttpClient okHttpClient;
	private TextView mTvResult;
	private ImageView mIvResult;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ok_http_test);

		okHttpClient = new OkHttpClient.Builder().cookieJar(new CookiesManager()).build();

		mTvResult = (TextView) findViewById(R.id.id_tv_result);
		mIvResult = (ImageView) findViewById(R.id.id_iv_result);
	}


	//--------------- Auto Cookies Manager
	private class CookiesManager implements CookieJar {
		private final PersistentCookieStore cookieStore = new PersistentCookieStore(getApplicationContext());

		@Override
		public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
			if (cookies != null && cookies.size() > 0) {
				for (Cookie item : cookies) {
					cookieStore.add(url, item);
				}
			}
		}

		@Override
		public List<Cookie> loadForRequest(HttpUrl url) {
			List<Cookie> cookies = cookieStore.get(url);
			return cookies;
		}
	}

	//------------------

	//GET
	public void doGet(View view) throws IOException{
		OkHttpUtils
				.get()
				.url(Constants.mBaseUrl + "/member/login.do")
				.addParams("id", "user1").addParams("password", "123").build()
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
		OkHttpUtils
				.post()
				.url(Constants.mBaseUrl + "/member/login.do")
				.addParams("id", "user1")
				.addParams("password", "123")
				.build()
				.execute(callback);
	}


	//POST String
	public void doPostString(View view){
		OkHttpUtils
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

		OkHttpUtils
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
		File file = new File("man1.jpg");
		if(!file.exists()){
			Log.d("a",file.getAbsolutePath()+" not exist!");
			return ;
		}
		MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
		RequestBody requestBody = multipartBuilder.setType(MultipartBody.FORM)
				.addFormDataPart("id","aaa")
				.addFormDataPart("password","bbb")
				.addFormDataPart("mPhoto","newname.jpg",RequestBody.create(MediaType.parse("application/octet-stream"), file))
				.build();

//		Request.Builder builder = new Request.Builder();
//		Request request = builder.url(Constants.mBaseUrl + "/test/uploadInfo.do").post(requestBody).build();
//
		CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {//
			@Override
			public void onRequestProgress(long byteWrited, long contentLength) {
				Log.d("a",byteWrited + " / "+ contentLength);//
			}
		});
		Request.Builder builder = new Request.Builder();
		Request request = builder.url(Constants.mBaseUrl + "/test/uploadInfo.do").post(countingRequestBody).build();//
	}



	//GET download
	public void doDownload(){
		Request.Builder builder = new Request.Builder();
		final Request request = builder.get().url(Constants.mBaseUrl + "/views/images/panda4.jpg").build();
		Call call = okHttpClient.newCall(request);

		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.d("a","----------onFailure-----" +e.getMessage());
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Log.d("a","----------onResponse-----");
				InputStream is = response.body().byteStream();
				int len =0 ;

//				final long total = response.body().contentLength();//
//				long sum = 0L;//

				//File file = new File(Environment.getExternalStorageDirectory(), "man1.jpg");//sd카드에서 불러옴
				File file = new File(Constants.mBaseUrl + "/views/images/panda4.jpg");
				byte[] buf = new byte[128];
				FileOutputStream fos = new FileOutputStream(file);
				while ( (len = is.read(buf)) != -1){
					fos.write(buf, 0 , len);

//					sum += len;//
//					Log.d("a",sum+" / "+total);//
//					final long finalSum = sum;//
//					runOnUiThread(new Runnable() {//
//						@Override
//						public void run() {
//							mTvResult.setText(finalSum+" / "+total);//다운로드 진도
//						}
//					});
				}
				fos.flush();
				fos.close();
				Log.d("a","--------download succes--------");

			}
		});
	}


	//GET Download Image
	public void doDownloadImg(){

		OkHttpUtils
				.get()//
				.url(Constants.mBaseUrl + "/views/images/panda4.jpg")//
				.build()//
				.execute(new BitmapCallback()
				{
					@Override
					public void onError(Call call, Exception e, int id) {
						mTvResult.setText("onError:" + e.getMessage());
					}

					@Override
					public void onResponse(Bitmap response, int id) {
						mIvResult.setImageBitmap(response);
					}

				});


//		Request.Builder builder = new Request.Builder();
//		final Request request = builder.get().url(Constants.mBaseUrl + "/views/images/panda4.jpg").build();
//		Call call = okHttpClient.newCall(request);
//
//		call.enqueue(new Callback() {
//			@Override
//			public void onFailure(Call call, IOException e) {
//				Log.d("a","----------onFailure-----" +e.getMessage());
//				e.printStackTrace();
//			}
//
//			@Override
//			public void onResponse(Call call, Response response) throws IOException {
//				Log.d("a","----------onResponse-----");
//				InputStream is = response.body().byteStream();
//				//BitmapFactory.Options
//				final Bitmap bitmap = BitmapFactory.decodeStream(is);
//				//is.mark();
//				//is.reset();
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						mIvResult.setImageBitmap(bitmap);
//					}
//				});
//
//			}
//		});
	}


	//------------------

	//Counting Message
	public static class CountingRequestBody extends RequestBody{

		protected  RequestBody delegate;
		private Listener listener;
		private CountingSink countingSink;
		public CountingRequestBody(RequestBody delegate, Listener  listener){
			this.delegate = delegate;
			this.listener = listener;
		}

		@Override
		public MediaType contentType() {
			return delegate.contentType();
		}

		@Override
		public long contentLength() {
			try {
				return delegate.contentLength();
			} catch (IOException e) {
				return -1;
			}
		}

		@Override
		public void writeTo(BufferedSink sink) throws IOException {
			countingSink = new CountingSink(sink);
			BufferedSink bufferedSink = Okio.buffer(countingSink);
			delegate.writeTo(bufferedSink);
			bufferedSink.flush();
		}

		protected  final class CountingSink extends ForwardingSink{
			private long bytesWritten;

			public CountingSink(Sink delegate){
				super(delegate);
			}

			@Override
			public void write(Buffer source, long byteCount) throws IOException {
				super.write(source, byteCount);
				bytesWritten+=byteCount;
				listener.onRequestProgress(byteCount,contentLength());
			}
		}


		public static interface Listener{
			void onRequestProgress(long byteWrited, long contentLength);
		}
	}


	//-------------

	//excute function
	private void excuteRequest(Request request) {

		Call call = okHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				Log.d("a","----------onFailure-----" +e.getMessage());
				e.printStackTrace();
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				Log.d("a","----------onResponse-----");
				final String res = response.body().string();
				Log.d("a",res);

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mTvResult.setText(res);
					}
				});
			}
		});

	}


}
