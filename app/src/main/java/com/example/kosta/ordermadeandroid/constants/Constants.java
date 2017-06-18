package com.example.kosta.ordermadeandroid.constants;

import com.example.kosta.ordermadeandroid.activity.main.MainActivity;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

/**
 * Created by kosta on 2017-06-12.
 */

public class Constants {

//    public static final String mBaseUrl = "http://10.0.2.2:8080/ordermade";
    public static final String mBaseUrl = "http://52.78.240.104:8080/ordermade";

    public static final String DELIVERY_PREPARE = "P";
    public static final String DELIVERY_COMPLETE = "C";
    public static final String PAYMENT_Y = "Y";
    public static final String PAYMENT_N = "N";

    public static final SharedPrefsCookiePersistor sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(new MainActivity());

    public static final ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(new MainActivity()));

}
