package com.example.ulhas.weather.app.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class MainActivity extends Activity {
    ConnectionDetector cDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Inside MainAcitity");
        cDetector = new ConnectionDetector(this);
        int SPLASH_TIME = 2000;
        Handler HANDLER = new Handler();
        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
                if (cDetector.isConnectingToInternet()) {
                    Intent intent = new Intent(MainActivity.this, WeatherReport.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, NoInternetConnection.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        }, SPLASH_TIME);
    }
}