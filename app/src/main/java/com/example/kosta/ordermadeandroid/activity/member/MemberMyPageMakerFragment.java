package com.example.kosta.ordermadeandroid.activity.member;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.main.MainActivity;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Member;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.example.kosta.ordermadeandroid.util.ImageLoadingTask;
import com.example.kosta.ordermadeandroid.util.XmlUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import okhttp3.Call;

/**
 * Created by kosta on 2017-06-08.
 */

public class MemberMyPageMakerFragment extends Fragment {

	private View view;

	private TextView mId;
	private TextView mName;
	private TextView mEmail;
	private TextView mAddress;
	//private TextView mIntroduce;
	//private TextView mLicenseNumber;
	private ImageView mImage;
	private TextView mAccountMoney;

	private String memberType;



	@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_member_maker_mypage, container, false);


		mId = (TextView) view.findViewById(R.id.id);
		mName = (TextView) view.findViewById(R.id.name);
		mEmail = (TextView) view.findViewById(R.id.email);
		mAddress = (TextView) view.findViewById(R.id.address);
		//mIntroduce = (TextView) view.findViewById(R.id.introduce);
		//mLicenseNumber = (TextView) view.findViewById(R.id.licenseNumber);
		mImage = (ImageView) view.findViewById(R.id.image);
		//mAccountMoney = (TextView) view.findViewById(R.id.accountMoney);

		//memberType = (TextView) view.findViewById(R.id.memberType);

		doGetMemberInfo();//회원 정보


        //로그아웃 버튼
        view.findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

				doLogout();//로그아웃

            }
        });

        //수정 버튼
        view.findViewById(R.id.modifyBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "회원 수정", Toast.LENGTH_SHORT).show();


            }
        });


        return view;
    }



	//로그아웃
	private void doLogout() {

		//---서버에서도 로그아웃됨.
		OkHttpUtils.initClient(CustomApplication.getClient())
				.get()
				.url(Constants.mBaseUrl + "/member/xml/logout.do")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e, int id) {
						Log.d("a", e.getMessage());
					}

					@Override
					public void onResponse(final String response, final int id) {
						Log.d("a","=========="+response);
						if (response.equals("true")){
							Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
						}
					}
				});

		//---
		Toast.makeText(getActivity(), "로그아웃", Toast.LENGTH_SHORT).show();
		SharedPreferences prefs = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
		prefs.edit().clear().apply();
		Log.d("a","------"+prefs.getString("memberType",""));
		startActivity(new Intent(getActivity(), MainActivity.class));

	}




    //로그인 성공시 멤버 정보 불러옴
    private void doGetMemberInfo() {

        OkHttpUtils.initClient(CustomApplication.getClient())
                .get()
                .url(Constants.mBaseUrl + "/member/xml/myPage.do")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("a", "--------"+e.getMessage());
						doLogout(); //로그아웃
                    }

                    @Override
                    public void onResponse(final String response, final int id) {
                        Log.d("a","=========="+response);

                        final Member member = new Member();

                        //xml-------

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
                            member.setName(XmlUtil.getTagValue("name",element));
                            member.setMemberType(XmlUtil.getTagValue("memberType",element));
                            member.setImage(XmlUtil.getTagValue("image",element));
                            member.setAddress(XmlUtil.getTagValue("address",element));
                            member.setEmail(XmlUtil.getTagValue("email",element));
                            member.setIntroduce(XmlUtil.getTagValue("introduce",element));
                            //member.setLicenseNumber(XmlUtil.getTagValue("licenseNumber",element));

                            Log.d("a","----------"+member.getId()+"-------------"+member.getMemberType());


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mId.setText(member.getId());
                                    mName.setText(member.getName());
                                    mEmail.setText(member.getEmail());
                                    mAddress.setText(member.getAddress());
                                    //mIntroduce.setText(member.getIntroduce());
                                    //mLicenseNumber.setText(member.getLicenseNumber());
									if(member.getImage() != null){
										new ImageLoadingTask(mImage).execute(Constants.mBaseUrl+"/main/file/download.do?fileName="+member.getImage());
										//mImage.setImageURI(Uri.parse(Constants.mBaseUrl+"/main/file/download.do?fileName="+member.getImage()));
									}else{
										mImage.setImageResource(R.drawable.image_default);
									}
                                }
                            });

                            //}



                        } catch (ParserConfigurationException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }




                    }
                });




    }





}
