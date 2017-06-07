package com.example.kosta.ordermadeandroid.activity.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.kosta.ordermadeandroid.R;

import portfolio.Register;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

        public void click(View v){
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
        }

}
