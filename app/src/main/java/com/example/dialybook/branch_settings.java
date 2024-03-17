package com.example.dialybook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class branch_settings extends AppCompatActivity {
    Button addemployee,addstock,historybills,stockleft,productsales;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_settings);
        addemployee=findViewById(R.id.but_addseller);
        TextView prbranch=this.findViewById(R.id.present_branch);
        prbranch.setText(MainActivity.user_branch);
        historybills=findViewById(R.id.but_historybills);
        productsales=findViewById(R.id.but_productsales);
        productsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(branch_settings.this,productsales_selectfield.class);
                startActivity(intent);
            }
        });
        stockleft=findViewById(R.id.but_stockleft);
        stockleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(branch_settings.this,stockleft_selectfield.class));

            }
        });
        historybills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(branch_settings.this,history_bills.class));
            }
        });
        addemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(branch_settings.this,add_employee.class);
                startActivity(intent);
            }
        });
        addstock=findViewById(R.id.but_addstock);




        addstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(branch_settings.this,add_stock_select_field.class);
                startActivity(intent);
            }
        });
    }
}