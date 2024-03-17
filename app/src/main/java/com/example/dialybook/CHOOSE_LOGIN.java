package com.example.dialybook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CHOOSE_LOGIN extends AppCompatActivity {

    Button button_owner,button_emp;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login);
        getSupportActionBar().hide();

        button_owner=findViewById(R.id.owner_login);
        button_emp=findViewById(R.id.employee_login);

        button_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CHOOSE_LOGIN.this,owner_login.class);
                startActivity(intent);
            }
        });
        button_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CHOOSE_LOGIN.this,WORKER_LOGIN.class);
                startActivity(intent);
            }
        });
    }
    public void onBackPressed(){
        Intent a=new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}