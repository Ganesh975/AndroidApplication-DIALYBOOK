package com.example.dialybook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String user;
    public static  String U_LOGIN;

    public static  String pbranch;
    TextView presentbranch;
    Button advanced,sell,history,homelogout,stockleft;
    static String user_branch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        presentbranch=findViewById(R.id.user_present_branch);
        advanced=findViewById(R.id.but_advanced);
        sell=findViewById(R.id.but_sell);
        history=findViewById(R.id.but_history);
        homelogout=findViewById(R.id.home_logout);
        stockleft=this.findViewById(R.id.but_stock_left);


        SharedPreferences sharedPreferences2=getSharedPreferences(MainActivity.U_LOGIN,0);
        Boolean emplogin=sharedPreferences2.getBoolean("hasEmpLogIn",true);
        Boolean ownlogin=sharedPreferences2.getBoolean("hasUserLogIn",true);

        if(emplogin==Boolean.TRUE){
            String br=sharedPreferences2.getString("branch",null);
            presentbranch.setText(br);
            user_branch=br;

            advanced.setVisibility(View.GONE);//makes it disappear

            Toast.makeText(this, "EMPLOYEE HAS LOGGED IN ", Toast.LENGTH_SHORT).show();

        }else if(ownlogin==Boolean.TRUE) {
            Toast.makeText(this, "OWNER HAS LOGGED IN ", Toast.LENGTH_SHORT).show();
            Intent in = getIntent();

            user_branch = in.getStringExtra("branch name");
            if (user_branch == null) {
                user_branch = "SELECT BRANCH";
            }


            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.pbranch, 0);
            String prtbranch = sharedPreferences.getString("branch", null);


            if (prtbranch == null) {
                user_branch = "SELECT BRANCH";
                presentbranch.setText(user_branch);
            } else {
                presentbranch.setText(prtbranch);
                user_branch = prtbranch;
            }


            presentbranch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(MainActivity.this, select_branch.class);
                    startActivity(in);
                }
            });
        }

        stockleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_branch!="SELECT BRANCH") {
                    Intent in = new Intent(MainActivity.this, public_stockleftselectfield.class);
                    startActivity(in);
                }else{
                    Toast.makeText(MainActivity.this, "PLEASE SELECT BRANCH", Toast.LENGTH_SHORT).show();
                }
            }
        });
        homelogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean("hasUserLogIn",false);
                editor.putString("user",null);
                editor.putString("branch",null);
                editor.putBoolean("hasEmpLogIn",false);
                editor.commit();


                user_branch="SELECT BRANCH";
                    Intent in=new Intent(MainActivity.this,CHOOSE_LOGIN.class);
                    startActivity(in);




            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_branch!="SELECT BRANCH") {
                        Intent in=new Intent(MainActivity.this,publichistory_bills.class);
                    startActivity(in);
                }else{
                    Toast.makeText(MainActivity.this, "PLEASE SELECT BRANCH", Toast.LENGTH_SHORT).show();
                }

            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_branch!="SELECT BRANCH") {
                    Intent intent=new Intent(MainActivity.this,sell_additem.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "PLEASE SELECT BRANCH", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if(ownlogin) {
            advanced.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainActivity.this, advanced.class);
                    startActivity(intent);

                }
            });
        }
    }
}