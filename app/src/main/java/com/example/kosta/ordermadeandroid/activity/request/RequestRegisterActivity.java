package com.example.kosta.ordermadeandroid.activity.request;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.member.MemberRegisterActivity;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class RequestRegisterActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private SharedPreferences prefs;
    private OkHttpClient okHttpClient;

    private EditText title;
    private EditText content;
    private EditText hopePrice;
    private String bound;
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

        //TextView category = (TextView)findViewById(R.id.request_register_category);
        title = (EditText)findViewById(R.id.request_register_title);
        content = (EditText)findViewById(R.id.request_register_content);
        hopePrice = (EditText)findViewById(R.id.request_register_hopeprice);
        publicRadio = (RadioButton)findViewById(R.id.publicRadio);
        privateRadio = (RadioButton)findViewById(R.id.privateRadio);
        bound = "PRIVATE";

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
                ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache()
                        , new SharedPrefsCookiePersistor(getApplication()));

                okHttpClient = new OkHttpClient.Builder().cookieJar(cookieJar).build();
                OkHttpUtils.initClient(okHttpClient)
                        .post().tag(this)
                        .url(Constants.mBaseUrl + "/request/xml/register.do")
                        .addParams("title", title.getText().toString())
                        .addParams("content", content.getText().toString())
                        .addParams("hopePrice", hopePrice.getText().toString())
                        .addParams("bound", bound.toString())
                        .addParams("maker.id", "")
                        .addParams("consumer.id", "")
                        .addParams("category", "")
                        .addParams("price", "10000")
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
                                if (response.equals("true")) {
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
}
