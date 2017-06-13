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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;

public class MemberLoginActivity extends AppCompatActivity {

	private SharedPreferences prefs;

	private EditText idEdit;
	private EditText pwEdit;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_login);


		if(isLogined()){
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
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
				//new LoginCheckTask().execute(Constants.SERVICE_URL + "/member/login.do?id="+idEdit.getText()+"&password="+pwEdit.getText());
			}
		});


	}






	//로그인페이지:
	//한번도 로그인 한적이 없으면 새로운 sessionId를 만든다.
	//로그인 한적이 있으면 직접 SharedPreferences에서 멤버 정보를 불러온다.
	//

	//사용페이지에서는 항상 먼저 로그인 했는지 판다하고 로그인 됬으면 그 기능을 쓸수 있도록 한다.
	public boolean isLogined(){//sessionId가 있을때 (전에 로그인한 기록이 있으면)
		boolean check = false;

//		//SharedPreferences에서 멤버 정보가 있는지 본다.
//		prefs = getSharedPreferences("login_info", Context.MODE_PRIVATE);
//
//		String sessionId = prefs.getString("sessionId","");
//		String loginId = prefs.getString("loginId","");
//		String memberType = prefs.getString("memberType","");
//		String loginTime = prefs.getString("loginTime","");//session이 만들어진 시간으로 session이 무효화 됬는지 체크할떄 쓴다.
//
//		if(!sessionId.isEmpty() && !memberType.isEmpty()){//sessionId가 있다는 것은 전에 로그인한 기록이 있다는 뜻.
//			//시간으로
//			check = true;
//		}


		return check;
	}

	public String createSession(String url){//로그인 성공시 받아오는 session
		return null;
	}




}
