package com.example.kosta.ordermadeandroid.activity.member;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.main.MainActivity;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class MemberRegisterActivity extends AppCompatActivity {


	private SharedPreferences prefs;
	private OkHttpClient okHttpClient;

	private EditText id;
	private EditText password;
	private EditText password2;
	private EditText name;
	private EditText email;
	private EditText address;
	private EditText introduce;
	private RadioGroup memberType;
	private RadioButton consumerRadio;
	private RadioButton makerRadio;
	private EditText licenseNumber;
	private ImageView image;

	private String memberTypeString;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_register);


		id = (EditText) findViewById(R.id.id);
		password = (EditText) findViewById(R.id.password);
		password2 = (EditText) findViewById(R.id.password2);
		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.email);
		address = (EditText) findViewById(R.id.address);
		introduce = (EditText) findViewById(R.id.introduce);
		memberType = (RadioGroup) findViewById(R.id.memberType);
		consumerRadio = (RadioButton) findViewById(R.id.consumerRadio);
		makerRadio = (RadioButton) findViewById(R.id.makerRadio);
		licenseNumber = (EditText) findViewById(R.id.licenseNumber);
		image = (ImageView) findViewById(R.id.image);

		memberTypeString = "C";


		RadioButton.OnClickListener radioOnClickListener = new RadioButton.OnClickListener() {

			public void onClick(View v) {
				if(consumerRadio.isChecked()) {
					memberTypeString = "C";
					licenseNumber.setVisibility(View.INVISIBLE);
				}else{
					memberTypeString="M";
					licenseNumber.setVisibility(View.VISIBLE);
				}
				Toast.makeText(getApplication(), memberTypeString, Toast.LENGTH_SHORT).show();
//				Toast.makeText(getApplication(), "consumerRadio  : " + consumerRadio.isChecked() + "\n"
//						+ "makerRadio : " + makerRadio.isChecked() + "\n", Toast.LENGTH_SHORT).show();

			}
		};

		consumerRadio.setOnClickListener(radioOnClickListener);
		makerRadio.setOnClickListener(radioOnClickListener);


		//취소 버튼
		findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				id.setText("");
				password.setText("");
			}
		});

		//회원가입 버튼
		findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MemberRegisterActivity.this));

				okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
				OkHttpUtils.initClient(okHttpClient)
						.post().tag(this)//.params("image",new File(file))
						.url(Constants.mBaseUrl + "/member/join.do")
						.addParams("id", id.getText().toString())
						.addParams("password", password.getText().toString())
						.addParams("password2", password2.getText().toString())
						.addParams("name", name.getText().toString())
						.addParams("email", email.getText().toString())
						.addParams("address", address.getText().toString())
						.addParams("introduce", introduce.getText().toString())
						.addParams("memberType", memberTypeString)
						.addParams("licenseNumber", licenseNumber.getText().toString())
						.addParams("image","")
						//.addFile("image","aa.jpg", file)
						.build()
						.execute(new StringCallback() {
							@Override
							public void onError(Call call, Exception e, int id) {
								Log.d("a", e.getMessage());
							}

							@Override
							public void onResponse(final String response, int id) {
								if(response.equals("true")){//회원가입 성공시
									Toast.makeText(getApplication(),"회원가입 되었습니다.", Toast.LENGTH_SHORT).show();
									startActivity(new Intent(getApplication(), MemberLoginActivity.class));
								}else{
									Toast.makeText(getApplication(),"회원가입에 실패 했습니다.", Toast.LENGTH_SHORT).show();
								}
							}
						});

//
//				String url = Constants.mBaseUrl + "/member/join.do";
//				Map<String, String> params = new HashMap<String, String>();
//				params.put("id", id.getText().toString());
//				params.put("password", password.getText().toString());
//				//params.put("password2", password2.getText().toString());
//				params.put("name", name.getText().toString());
//				params.put("email", email.getText().toString());
//				params.put("address", address.getText().toString());
//				params.put("introduce", introduce.getText().toString());
//				params.put("memberType", memberTypeString);
//				params.put("licenseNumber", licenseNumber.getText().toString());
//				params.put("image","1.jpg");
////				Map<String, String> headers = new HashMap<>();
////				headers.put("image", "1.jpg");
//
//
//
//
////				String url = Constants.mBaseUrl + "/request/xml/register.do";
////
////				Map<String, String> params = new HashMap<String, String>();
////				params.put("title","의뢰");
////				params.put("maker.id","maker1");
////				params.put("consumer.id","user1");
////				params.put("category","aaa");
////				params.put("content","cccc");
////				params.put("hopePrice","10000");
////				params.put("price","12000");
////				params.put("bound","PUBLIC");
//
//
//				OkHttpUtils.initClient(okHttpClient)
//						.post()//
//						.url(url)//
//						.params(params)//
//						//.headers(headers)//
//						.build()//
//						.execute(new StringCallback() {
//							@Override
//							public void onError(Call call, Exception e, int id) {
//								Log.d("a", e.getMessage());
//							}
//
//							@Override
//							public void onResponse(final String response, int id) {
//								if(response.equals("true")){//회원가입 성공시
//									Toast.makeText(getApplication(),"회원가입 되었습니다.", Toast.LENGTH_SHORT).show();
//									startActivity(new Intent(getApplication(), MemberLoginActivity.class));
//								}else{
//									Toast.makeText(getApplication(),"회원가입에 실패 했습니다.", Toast.LENGTH_SHORT).show();
//								}
//							}
//						});
//
			}
		});


	}




}
