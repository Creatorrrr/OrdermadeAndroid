package com.example.kosta.ordermadeandroid.activity.request;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.member.MemberRegisterActivity;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.dto.Tag;
import com.example.kosta.ordermadeandroid.util.CustomApplication;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class RequestRegisterActivity extends AppCompatActivity {

    private static final String SELECT_ACCESSORY = "ACCESSORY";
    private static final String SELECT_CLOTHING = "CLOTHING";
    private static final String SELECT_DIGITAL = "DIGITAL";
    private static final String SELECT_FUNITURE = "FUNITURE";
    private static final String SELECT_KITCHEN = "KITCHEN";
    private static final String SELECT_SPORT = "SPORT";

    private Toolbar mToolbar;

    private SharedPreferences prefs;
    private OkHttpClient okHttpClient;

    private EditText title;
    private EditText content;
    private EditText hopePrice;
    private Spinner categorySpinner;
    private String category;
    private String bound;
    private String loginId;
    private RadioButton publicRadio;
    private RadioButton privateRadio;

    //private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_register);

        mToolbar = (Toolbar)findViewById(R.id.actionbar_requestRegister);
        mToolbar.setTitle("의뢰서 등록");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = getSharedPreferences("login_info", MODE_PRIVATE);
        loginId = prefs.getString("loginId","");

        categorySpinner = (Spinner)findViewById(R.id.request_register_category);
        title = (EditText)findViewById(R.id.request_register_title);
        content = (EditText)findViewById(R.id.request_register_content);
        hopePrice = (EditText)findViewById(R.id.request_register_hopeprice);
        publicRadio = (RadioButton)findViewById(R.id.publicRadio);
        privateRadio = (RadioButton)findViewById(R.id.privateRadio);
        bound = "PRIVATE";

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ( position == 0 ){
                    //Toast.makeText(RequestRegisterActivity.this, "accessory selected", Toast.LENGTH_SHORT).show();
                    category = SELECT_ACCESSORY;
                }else if ( position == 1) {
                    category = SELECT_CLOTHING;
                }else if ( position == 2) {
                    category = SELECT_DIGITAL;
                }else if ( position == 3) {
                    category = SELECT_FUNITURE;
                }else if ( position == 4) {
                    category = SELECT_KITCHEN;
                }else if ( position == 5) {
                    category = SELECT_SPORT;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 의뢰서 공개, 비공개 라디오버튼
        RadioButton.OnClickListener boundRadioClickListener = new RadioButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( publicRadio.isChecked()) {
                    bound = "PUBLIC";
                }else{
                    bound = "PRIVATE";
                }
            }
        };

        publicRadio.setOnClickListener(boundRadioClickListener);
        privateRadio.setOnClickListener(boundRadioClickListener);



        // 취소버튼
        findViewById(R.id.request_register_cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText("");
                content.setText("");
                hopePrice.setText("");
            }
        });

        // 의뢰서 등록 버튼
        findViewById(R.id.request_register_registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkHttpUtils.initClient(CustomApplication.getClient())
                        .post().tag(this)
                        .url(Constants.mBaseUrl + "/request/xml/register.do")
                        .addParams("title", title.getText().toString())
                        .addParams("content", content.getText().toString())
                        .addParams("hopePrice", hopePrice.getText().toString())
                        .addParams("bound", bound.toString())
                        .addParams("maker.id", "")
                        .addParams("consumer.id", loginId.toString())
                        .addParams("category", category.toString())
                        .addParams("price", "0")
                        .addParams("payment", "N")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.d("d", e.getMessage());
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                // RequestController return value 수정
                                if (response != null) {
                                    Toast.makeText(RequestRegisterActivity.this, "의뢰서 등록 성공", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplication(), RequestMyListActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(RequestRegisterActivity.this, "의뢰서 등록 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
}
