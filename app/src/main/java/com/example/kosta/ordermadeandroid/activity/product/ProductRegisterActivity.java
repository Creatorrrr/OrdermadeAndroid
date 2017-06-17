package com.example.kosta.ordermadeandroid.activity.product;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.member.MemberLoginActivity;
import com.example.kosta.ordermadeandroid.activity.member.MemberRegisterActivity;
import com.example.kosta.ordermadeandroid.activity.member.OkHttpTestActivity;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Product;
import com.example.kosta.ordermadeandroid.dto.Tag;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.example.kosta.ordermadeandroid.util.URI2RealPath;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductRegisterActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 1;
    private SharedPreferences prefs;
    private OkHttpClient okHttpClient;

    private Toolbar mToolbar;

    private EditText title;
    private ImageView image;
    private EditText content;
    private EditText price;
    private EditText period;
    private String loginId;
    private String imageSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_register);

        mToolbar = (Toolbar)findViewById(R.id.actionbar_productRegister);
        mToolbar.setTitle("상품 등록");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = getSharedPreferences("login_info", MODE_PRIVATE);

        //TextView category = (TextView)findViewById(R.id.request_register_category);
        title = (EditText)findViewById(R.id.product_register_title);
        content = (EditText)findViewById(R.id.product_register_content);
        price = (EditText)findViewById(R.id.product_register_price);
        period = (EditText)findViewById(R.id.product_register_period);
        image = (ImageView)findViewById(R.id.product_register_image);
        loginId = prefs.getString("loginId","");

        //이미지 클릭할때
        findViewById(R.id.product_register_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//sd카드에서 불러오기
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });


        // 취소버튼
        findViewById(R.id.product_register_cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText("");
                content.setText("");
                price.setText("");
                period.setText("");
            }
        });

        // 상품 등록 버튼
        findViewById(R.id.product_register_registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    imageUpload(imageSrc);
            }
        });
    }

    //이미지 업로드 및 경로 받기
    public void imageUpload(String imageSrc){
        //Log.d("a","------"+imageSrc);
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ProductRegisterActivity.this));
        okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
        OkHttpUtils.initClient(okHttpClient)
                .post()
                .url(Constants.mBaseUrl + "/main/file/upload.do")
                .addFile("upload","", new File(imageSrc))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("productRegister", e.getMessage());
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        Log.d("productRegister", response.toString());
                        //이미지 받은후 Form데이터에 넣고 보내기.
                        if(response == "fail"){
                            Toast.makeText(getApplication(), "이미지 업로드 실패 하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                        sendFormData(response);

                    }
                });

    }

    //상품등록 데이터에 업로된 이미지 파일명을 넣어 보낸다.
    public void sendFormData(String uploadFileName) {
        if (uploadFileName == "fail") uploadFileName = "";

        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ProductRegisterActivity.this));
        okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
        OkHttpUtils.initClient(okHttpClient)
                .post()
                .url(Constants.mBaseUrl + "/product/xml/register.do")
                .addParams("title", title.getText().toString())
                .addParams("content", content.getText().toString())
                .addParams("image", uploadFileName)
                .addParams("price", price.getText().toString())
                .addParams("period", period.getText().toString())
                .addParams("maker.id", loginId.toString())
                .addParams("category", "")
                .addParams("hit", "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("a", e.getMessage());
                    }

                    @Override
                    public void onResponse(final String response, int id) {
                        if (response.equals("true")) {//상품등록 성공시
                            Toast.makeText(getApplication(), "상품 등록 성공", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplication(), ProductMyListActivity.class));
                        } else {
                            Toast.makeText(getApplication(), "상품 등록 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //상품 분류
    public ArrayList<Tag> getTags(){
        ArrayList<Tag> tags = new ArrayList<>();
        OkHttpUtils.initClient(CustomApplication.getClient())
                .post()
                .url(Constants.mBaseUrl + "/xml/categoryList.do")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("a", e.getMessage());
                    }

                    @Override
                    public void onResponse(final String response, final int id) {
                        Log.d("a","=========="+response);

                    }
                });

        return tags;
    }

    //다른 VIEW에서 보내주는 데이터를 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK) return;
        switch (requestCode){
            case PICK_FROM_ALBUM: //로컬에 있는 이미지 경로
                Log.d("a","-----------"+data.getData());//content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F57/ACTUAL/1625851485
                ((ImageView) findViewById(R.id.image)).setImageURI(data.getData());
                imageSrc =new URI2RealPath().getRealPathFromURI(getApplication(),data.getData());
                break;
        }
    }

}

