package com.example.ulhas.weather.app.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by ULHAS on 1/21/2015.
 */
public class NoInternetConnection extends Activity implements View.OnClickListener {
    Button Reload;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_connection);
        intent= getIntent();
        String exit= intent.getStringExtra("exit");
        if(exit!=null){
            if(exit.equalsIgnoreCase("exit")){
                System.exit(0);
                finish();
            }
        }
        Reload = (Button) findViewById(R.id.reload);
        Reload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
        finish();
    }
}
