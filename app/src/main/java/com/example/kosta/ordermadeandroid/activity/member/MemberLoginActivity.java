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
				new LoginCheckTask().execute(Constants.SERVICE_URL + "/member/login.do?id="+idEdit.getText()+"&password="+pwEdit.getText());
			}
		});


	}


	private class LoginCheckTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			String sessionId = createSession(params[0]);
			//Log.d("a","----------------"+params[0]);
			return sessionId;
		}

		@Override
		protected void onPostExecute(String s) {
			Log.d("a","------------onPostExecute-----------------" +s);
//			if(!s.isEmpty()){
//
//
//					SharedPreferences.Editor editor = prefs.edit();
//					String sessionId = "";//---------cookie에서 sessionId를 받아오기.
//					editor.putString("loginId", idEdit.getText().toString());
//					//editor.putString("memberType", memberType);//쿠키를 통해 보내주게 하자.
//					editor.putString("sessionId", sessionId);
//					editor.apply();
//
//
//				Intent intent = new Intent(MemberLoginActivity.this, MainActivity.class);
//				startActivity(intent);
//				finish();//loginActivity끄기.
//			}else{
//				Toast.makeText(MemberLoginActivity.this, "회원이 존재 하지 않습니다. ", Toast.LENGTH_SHORT).show();
//			}
		}
	}



//---------------------------------------server controller

//
//
//	@RequestMapping(value = "/xml/login.do", method = RequestMethod.POST, produces="text/plain") // end
//	public @ResponseBody String loginMember(Member member,HttpServletRequest req, HttpServletResponse resp) {
//		// 로그인 --아이디와 비밀 번호 일치할 경우 main/main.do로 이동 --아이디와 비밀 번호 불일치할 경우
//		// login.jsp으로 이동
//
//		//resp.setHeader("Content-Type","text/html");
//		//resp.setHeader("set-cookie","JSESSIONID=A0931A1CF88F966A2603CB6C94866B82; Path=/ordermade; HttpOnly");
//		//resp.setHeader("Set-Cookie","lang=en-US; Path=/; Domain=local");
//		//resp.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
//		//resp.setHeader("Pragma","no-cache");
//		//resp.setDateHeader("Expires", 0);
//
////		 Cookie cookie = new Cookie("user", "akakk");
////         cookie.setMaxAge(30 * 60);// 设置为30min
////         cookie.setPath("/");
////         resp.addCookie(cookie);
////
////		Cookie foo = new Cookie("foo", "bar"); //bake cookie
////		foo.setMaxAge(1000); //set expire time to 1000 sec
////		resp.addCookie(foo); //put cookie in response
////
////        // Set-Cookie: SID=31d4d96e407aad42; Path=/; Secure; HttpOnly
////        Cookie sidCookie = new Cookie("SID","31d4d96e407aad42");
////        sidCookie.setPath("/");
////        sidCookie.setSecure(true);
////        sidCookie.setHttpOnly(true);
////        resp.addCookie(sidCookie);
////
////        // Set-Cookie: lang=en-US; Path=/; Domain=example.com
////        Cookie langCookie = new Cookie("lang","en-US");
////        langCookie.setPath("/");
////        langCookie.setDomain("example.com");
////        resp.addCookie(langCookie);
////
//		Member loginedUser = service.findMemberById(member.getId());
//		System.out.println(member.getId()+"-------"+member.getPassword());
//		HttpSession session = req.getSession();
//		resp.setHeader("Set-Cookie","JSESSIONID=" + session.getId()+"; Path=/ordermade; HttpOnly");
//		if (loginedUser != null && loginedUser.getPassword().equals(member.getPassword())) {
//
//			session.setAttribute("loginId", loginedUser.getId());
//			session.setAttribute("memberType", loginedUser.getMemberType());
//			session.setAttribute("sessionId", session.getId());
//
//			System.out.println(session.getId()+"--"+session.getCreationTime());
//			return "true";
//		} else {
//			return "false";
//		}
//	}




//----------------------------------------





	//로그인페이지:
	//한번도 로그인 한적이 없으면 새로운 sessionId를 만든다.
	//로그인 한적이 있으면 직접 SharedPreferences에서 멤버 정보를 불러온다.
	//

	//사용페이지에서는 항상 먼저 로그인 했는지 판다하고 로그인 됬으면 그 기능을 쓸수 있도록 한다.
	public boolean isLogined(){//sessionId가 있을때 (전에 로그인한 기록이 있으면)
		boolean check = false;

		//SharedPreferences에서 멤버 정보가 있는지 본다.
		prefs = getSharedPreferences("login_info", Context.MODE_PRIVATE);

		String sessionId = prefs.getString("sessionId","");
		String loginId = prefs.getString("loginId","");
		String memberType = prefs.getString("memberType","");
		String loginTime = prefs.getString("loginTime","");//session이 만들어진 시간으로 session이 무효화 됬는지 체크할떄 쓴다.

		if(!sessionId.isEmpty() && !memberType.isEmpty()){//sessionId가 있다는 것은 전에 로그인한 기록이 있다는 뜻.
			//시간으로
			check = true;
		}


		return check;
	}

	public String createSession(String url){//로그인 성공시 받아오는 sessionId
		HttpURLConnection http = null;
		InputStream is = null;
		String response = null;
		String sid = null;
		url=Constants.SERVICE_URL + "/member/xml/login.do";
		try {
			http = (HttpURLConnection) new URL(url).openConnection();
			String cookieString = CookieManager.getInstance().getCookie("http://10.0.2.2:8080");
			Log.d("a","=--------------"+cookieString);
//			if (cookieString != null) {
//				http.setRequestProperty("Cookie", cookieString);
//			}
			String cookieval = http.getHeaderField("set-cookie");//cookie 받아옴
			//String cookieval = http.getHeaderField("Cookie");
			Log.d("a","========cdd========" +cookieval);

//			if(cookieval != null) {
//				sid = cookieval.substring(0, cookieval.indexOf(";"));
//				Log.d("a","=======sid=========" +sid);
//			}
			http.setRequestMethod("POST");
			http.connect();

			DataOutputStream out = new DataOutputStream(http.getOutputStream());
			String content = "id=user1&password=123";
			out.writeBytes(content);
			out.flush();
			out.close();


			is = http.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				//response = br.readLine();
				response = response + readLine;
				Log.d("a","================" +response);
			}

//			is = http.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//			checkStr = reader.readLine();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		String sid = null;
//		HttpURLConnection uRLConnection;
//		String response = "";
//		url=Constants.SERVICE_URL + "/member/xml/login.do";
//		Log.d("a","================" +url);
//		try {
//			uRLConnection = (HttpURLConnection) new URL(url).openConnection();
//			uRLConnection.setDoInput(true);
//			uRLConnection.setDoOutput(true);
//			uRLConnection.setRequestMethod("POST");
//			uRLConnection.setUseCaches(false);		//캐시 사용하지 않음
//			uRLConnection.setInstanceFollowRedirects(false);
//			uRLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
////			uRLConnection.setRequestProperty("Content-length", "" + requestStringBytes.length);
////			uRLConnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
////			uRLConnection.setRequestProperty("Charset", "UTF-8");
//
//			String cookieval = uRLConnection.getHeaderField("sid");//cookie 받아옴
//			Log.d("a","================" +cookieval);
////			if(cookieval != null) {
////				sid = cookieval.substring(0, cookieval.indexOf(";"));
////				Log.d("a","=======sid=========" +sid);
////			}
//			uRLConnection.setRequestMethod("POST");
//			uRLConnection.connect();
//
//			DataOutputStream out = new DataOutputStream(uRLConnection.getOutputStream());
//			String content = "id=user1&password=123";
//			out.writeBytes(content);
//			out.flush();
//			out.close();
//
//			InputStream is = uRLConnection.getInputStream();
//			BufferedReader br = new BufferedReader(new InputStreamReader(is));
//
//			String readLine = null;
//			while ((readLine = br.readLine()) != null) {
//				//response = br.readLine();
//				response = response + readLine;
//				Log.d("a","================" +response);
//			}
//
//			is.close();
//			br.close();
//			uRLConnection.disconnect();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//			return null;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}


		Log.d("a","================" +response);//결과; 성공 혹은 실패
		if(response == "true"){//로그인 성공시
			Log.d("a","=======sid=========" +sid);
			return sid;
		}else{//로그인 실패시
			//Log.d("a","====================" +checkStr);
		}

		//return response;
		return response;
	}

	//long sessionCreateTime =  new Date().getTime();


}
