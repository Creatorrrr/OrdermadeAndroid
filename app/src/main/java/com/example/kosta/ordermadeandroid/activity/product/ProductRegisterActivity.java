package com.example.kosta.ordermadeandroid.activity.product;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.kosta.ordermadeandroid.R;
import com.example.kosta.ordermadeandroid.activity.member.OkHttpTestActivity;
import com.example.kosta.ordermadeandroid.constants.Constants;
import com.example.kosta.ordermadeandroid.databinding.ActivityProductRegisterBinding;
import com.example.kosta.ordermadeandroid.dto.Product;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.io.IOException;
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
    public static final String TAG = ProductRegisterActivity.class.getSimpleName();
    private OkHttpClient okHttpClient;
    private Toolbar mToolbar;
    private Product product;
    ActivityProductRegisterBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_register);

        okHttpClient = new OkHttpClient.Builder().cookieJar(new ProductRegisterActivity.CookiesManager()).build();

        product = new Product();
        dataBinding.setProduct(product);
        dataBinding.setActivity(this);


        mToolbar = (Toolbar) findViewById(R.id.actionbar_productRegister);
        mToolbar.setTitle("상품 등록");
        //mToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_folder_open_white);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //POST
    public void onPost(Product product) {
        OkHttpUtils.initClient(okHttpClient)
                .post()
                .url(Constants.mBaseUrl + "/product/xml/register.do")
                .addParams("title", product.getTitle())

                .build()
                .execute(callback);
    }

    public void off() {

    }

    private StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            Log.d("a", "----------onFailure-----" + e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onResponse(final String response, int id) {
            Log.d("a", "----------onResponse-----" + id);
        }
    };

    private class CookiesManager implements CookieJar {
        private final PersistentCookieStore cookieStore = new PersistentCookieStore(getApplicationContext());

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                cookieStore.add(url, cookies);
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url);
            return cookies;
        }
    }

}

