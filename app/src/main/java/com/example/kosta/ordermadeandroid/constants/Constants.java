package com.example.kosta.ordermadeandroid.constants;

import com.example.kosta.ordermadeandroid.activity.main.MainActivity;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

/**
 * Created by kosta on 2017-06-12.
 */

public class Constants {

    public static final String mBaseUrl = "http://10.0.2.2:8080/ordermade";
    public static final SharedPrefsCookiePersistor sharedPrefsCookiePersistor = new SharedPrefsCookiePersistor(new MainActivity());


}
