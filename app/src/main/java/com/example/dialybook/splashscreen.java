package com.example.dialybook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();
        Handler hn=new Handler();
        hn.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
                boolean haslogedin=sharedPreferences.getBoolean("hasUserLogIn",false);
                boolean empLogIn=sharedPreferences.getBoolean("hasEmpLogIn",false);


                if(haslogedin || empLogIn){

                    Intent in=new Intent(splashscreen.this,MainActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //clear backstack
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(in);
                    finish();
                }else {
                    Intent in = new Intent(splashscreen.this, CHOOSE_LOGIN.class);
                    startActivity(in);
                    finish();
                }

            }
        },2000);
    }
}