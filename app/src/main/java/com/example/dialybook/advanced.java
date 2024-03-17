package com.example.dialybook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class advanced extends AppCompatActivity {

    Button myprofile,branchsettings,addbranch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);
        myprofile=findViewById(R.id.but_myprofile);
        branchsettings=findViewById(R.id.but_branchsetting);
        TextView prbranch=this.findViewById(R.id.present_branch);
        prbranch.setText(MainActivity.user_branch);
        addbranch=findViewById(R.id.but_addbranch);
        addbranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(advanced.this,add_branch.class);
                startActivity(in);
            }
        });
        myprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(advanced.this,my_profile.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);


        branchsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prtbranch!=null){
                Intent intent=new Intent(advanced.this,branch_settings.class);
                startActivity(intent);}

        else{
                Toast.makeText(advanced.this, "PLEASE SELECT THE BRANCH", Toast.LENGTH_SHORT).show();
            }

        }});
        }

}