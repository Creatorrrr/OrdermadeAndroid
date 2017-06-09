package com.example.kosta.ordermadeandroid.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.kosta.ordermadeandroid.activity.main.MainActivity;
import com.example.kosta.ordermadeandroid.constants.Constants;

import java.net.URL;
import java.util.Date;

/**
 * Created by kosta on 2017-06-09.
 */

public class LoginCheck {


	//  [ General ]
	//	Request URL:http://localhost:8080/ordermade/product/xml/main/category/hit.do?category=FUNITURE&page=4
	//	Request Method:GET
	//	Status Code:200
	//	Remote Address:[::1]:8080
	//	Referrer Policy:no-referrer-when-downgrade

	//	[ Response Headers ]
	//	Content-Type:application/xml
	//	Date:Fri, 09 Jun 2017 02:11:42 GMT
	//	Transfer-Encoding:chunked

	//	[ Request Headers ]
	//	Accept:application/xml, text/xml, * / *; q=0.01
	//	Accept-Encoding:gzip, deflate, sdch, br
	//	Accept-Language:ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4
	//	Cache-Control:no-cache
	//	Connection:keep-alive
	//	Cookie:JSESSIONID=68CF6A86F7ED76E6B969600932F9023F
	//	Host:localhost:8080
	//	Pragma:no-cache
	//	Referer:http://localhost:8080/ordermade/index.jsp
	//	User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36
	//	X-Requested-With:XMLHttpRequest

	//	[ Query String Parameters ]
	//	view URL encoded
	//	category:FUNITURE
	//	page:4

//	public boolean loginCheck(){
//		//모든 client에서 보내는 URL요청페이지에서 다 유저 체크를 한다.
//		//클라이언트에서 보내는 sessionId와 일치한지 보고 그에 따른 처리를 해준다.
//		//SharedPreferences에 저장해둔 sessionId값을 cookie에 담아 보낸다.
//
////		URL url = new URL(requrl);
////		HttpURLConnectioncon= (HttpURLConnection) url.openConnection();
////		if(sessionid != null) {
////			con.setRequestProperty("cookie", sessionid);
////		}
//		return false;
//	}

//
//
//	public static String getSessionId(){
//
//		URL _url;
//		String sessionid=null;
//		try {
//			_url = new URL(Constants.SERVICE_URL + "/");
//
//			HttpURLConnection con= (HttpURLConnection) _url.openConnection();
//
//			// 取得sessionid.
//			String cookieval = con.getHeaderField("Set-Cookie");
//
//			if(cookieval != null) {
//				sessionid = cookieval.substring(0, cookieval.indexOf(";"));
//			}
//
//
//			//App.sessionId=sessionid;
//
//			con.disconnect();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return sessionid;
//	}



//
//	//서버측에서 현재 쓰고있던 sessionID가 종료 됬는지 판단. 종료되면 새로운 sessionID를 보내줌.
//	public synchronized static void createSession() {
//
//
//		if(null==App.sessionId || "".equals(App.sessionId)){
//			App.sessionId=getSessionId();
//			System.out.println("session创建！");
//			sessionCreateTime=new Date().getTime();
//		}
//
//		//20分钟后更换session
//		long nowTime=new Date().getTime();
//		int min=(int)((nowTime-sessionCreateTime)/(1000*60));
//		if(min>=15){
//			App.sessionId=getSessionId();
//			System.out.println("session重新创建！");
//			sessionCreateTime=new Date().getTime();
//		}else{
//			sessionCreateTime=new Date().getTime();
//		}
//
//	}



}
