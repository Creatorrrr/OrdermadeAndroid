package com.example.kosta.ordermadeandroid.activity.member;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.main.MainActivity;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.util.XmlUtil;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.store.CookieStore;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class MemberLoginActivity extends AppCompatActivity {

	private SharedPreferences prefs;
	private String loginId;
	private String memberType;

	private OkHttpClient okHttpClient;

	private EditText idEdit;
	private EditText pwEdit;



	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_login);


		prefs = getSharedPreferences("login_info", Context.MODE_PRIVATE);
		memberType = prefs.getString("memberType","");
		if(!memberType.isEmpty()){//오토 로그인 됬을떄
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			//finish();
		}


		idEdit = (EditText) findViewById(R.id.idEdit);
		pwEdit = (EditText) findViewById(R.id.pwEdit);

		//취소 버튼
		findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				idEdit.setText("");
				pwEdit.setText("");
			}
		});

		//로그인 버튼
		findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doLogin();
			}
		});


	}


	//로그인
	private void doLogin(){


		ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplication()));
		//Log.d("a",cookieJar.loadForRequest(Constants.mBaseUrl + "/member/login.do").size());
		OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
		OkHttpUtils.initClient(okHttpClient)
				.post()
				.url(Constants.mBaseUrl + "/member/login.do")
				.addParams("id", idEdit.getText().toString())
				.addParams("password", pwEdit.getText().toString())
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						Log.d("a", e.getMessage());
					}

					@Override
					public void onResponse(final String response, int id) {
						if(response.equals("true")){//로그인 성공시
							//Log.d("a","---------doLogin------");
							doGetMemberInfo();
						}else{
							Toast.makeText(MemberLoginActivity.this,"로그인 실패 했습니다.", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	//로그인 성공시 멤버 정보 불러옴
	private void doGetMemberInfo(){


		ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplication()));
		//Log.d("a",cookieJar.loadForRequest(Constants.mBaseUrl + "/member/login.do").size());
		OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
		OkHttpUtils.initClient(okHttpClient)
				.get()
				.url(Constants.mBaseUrl + "/member/xml/myPage.do")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						Log.d("a", e.getMessage());
					}

					@Override
					public void onResponse(final String response, int id) {
						Log.d("a","=========="+response);

						Member member = new Member();

						//String2xml-------

						StringReader sr = new StringReader(response);
						InputSource is = new InputSource(sr);
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

						try {
							DocumentBuilder builder=factory.newDocumentBuilder();
							Document doc = builder.parse(is);

							NodeList nodeList = doc.getElementsByTagName("member");
							//for(int i=0 ; i<nodeList.getLength(); i++){
								Node node = nodeList.item(0);
								Element element = (Element) node;

								member.setId(XmlUtil.getTagValue("id",element));
								member.setMemberType(XmlUtil.getTagValue("memberType",element));

								Log.d("a","----------"+member.getId()+"-------------"+member.getMemberType());

							//}



						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}


						//---------
						prefs = getSharedPreferences("login_info", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = prefs.edit();
						editor.putString("loginId", member.getId());//SharedPreferences에 넣는다.
						editor.putString("memberType", member.getMemberType());
						editor.apply();


						startActivity(new Intent(MemberLoginActivity.this, MemberMyPageActivity.class));
					}
				});
	}



	//--------------- Auto Cookies Manager
//	private class CookiesManager implements CookieJar {
//		private final PersistentCookieStore cookieStore = new PersistentCookieStore(getApplication());
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



	//로그인페이지:
	//한번도 로그인 한적이 없으면 새로운 sessionId를 만든다.
	//로그인 한적이 있으면 직접 SharedPreferences에서 멤버 정보를 불러온다.
	//

	//사용페이지에서는 항상 먼저 로그인 했는지 판단하고 로그인 됐으면 그 기능을 쓸수 있도록 한다.
	public boolean isLogined(){//sessionId가 있을때 (전에 로그인한 기록이 있으면)
		boolean check = false;

		//SharedPreferences에서 멤버 정보가 있는지 본다.
		prefs = getSharedPreferences("login_info", Context.MODE_PRIVATE);

		String sessionId = prefs.getString("sessionId","");
		String loginId = prefs.getString("loginId","");
		String memberType = prefs.getString("memberType","");
		//String loginTime = prefs.getString("loginTime","");//session이 만들어진 시간으로 session이 무효화 됬는지 체크할떄 쓴다.

		if(!sessionId.isEmpty() && !memberType.isEmpty()){//sessionId가 있다는 것은 전에 로그인한 기록이 있다는 뜻.
			//시간으로
			check = true;
		}

		return check;
	}




}
