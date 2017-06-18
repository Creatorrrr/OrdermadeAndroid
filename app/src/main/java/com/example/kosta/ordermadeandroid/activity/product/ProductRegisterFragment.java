package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Tag;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.example.kosta.ordermadeandroid.util.URI2RealPath;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class ProductRegisterFragment extends Fragment {
    private static final int PICK_FROM_ALBUM = 1;

    private EditText title;
    private ImageView image;
    private EditText content;
    private EditText price;
    private EditText period;
    private Spinner categorySpinner;
    private String imageSrc;

    private static ProductRegisterFragment instance;

    synchronized public static ProductRegisterFragment getInstance() {
        if(instance == null) instance = new ProductRegisterFragment();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_register, container, false);

        title = (EditText)view.findViewById(R.id.product_register_title);
        content = (EditText)view.findViewById(R.id.product_register_content);
        price = (EditText)view.findViewById(R.id.product_register_price);
        period = (EditText)view.findViewById(R.id.product_register_period);
        image = (ImageView)view.findViewById(R.id.product_register_image);
        categorySpinner = (Spinner)view.findViewById(R.id.product_register_category);

        //이미지 클릭할때
        view.findViewById(R.id.product_register_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//sd카드에서 불러오기
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });


        // 취소버튼
        view.findViewById(R.id.product_register_cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, ProductMyListFragment.getInstance()).commit();
            }
        });

        // 상품 등록 버튼
        view.findViewById(R.id.product_register_registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUpload(imageSrc);
            }
        });

        return view;
    }

    //이미지 업로드 및 경로 받기
    public void imageUpload(String imageSrc){
        File image = new File(imageSrc);

        OkHttpUtils.initClient(CustomApplication.getClient())
                .post()
                .url(Constants.mBaseUrl + "/main/file/upload.do")
                .addFile("upload", "product" + image.getName().substring(image.getName().lastIndexOf(".")), image)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("productRegister", e.getMessage());
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        Log.d("productRegister", response);
                        //이미지 받은후 Form데이터에 넣고 보내기.
                        if(response == "fail"){
                            Toast.makeText(getActivity().getApplication(), "이미지 업로드 실패 하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                        sendFormData(response);
                    }
                });

    }

    //상품등록 데이터에 업로된 이미지 파일명을 넣어 보낸다.
    public void sendFormData(String uploadFileName) {
        if (uploadFileName == "fail") uploadFileName = "";

        OkHttpUtils.initClient(CustomApplication.getClient())
                .post()
                .url(Constants.mBaseUrl + "/product/xml/register.do")
                .addParams("title", title.getText().toString())
                .addParams("content", content.getText().toString())
                .addParams("image", uploadFileName)
                .addParams("price", price.getText().toString())
                .addParams("period", period.getText().toString())
                .addParams("category", categorySpinner.getSelectedItem().toString())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("a", e.getMessage());
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        if (response.equals("true")) {//상품등록 성공시
                            Toast.makeText(getActivity().getApplication(), "상품 등록 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplication(), "상품 등록 실패", Toast.LENGTH_SHORT).show();
                        }
                        getFragmentManager().beginTransaction().replace(R.id.relativeLayout_for_frame, ProductMyListFragment.getInstance()).commit();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_FROM_ALBUM: //로컬에 있는 이미지 경로
                Log.d("a","-----------"+data.getData());//content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F57/ACTUAL/1625851485
                image.setImageURI(data.getData());
                imageSrc =new URI2RealPath().getRealPathFromURI(getActivity().getApplication(),data.getData());
                Log.d("a","-----------"+imageSrc);
                break;
        }
    }

}

