package com.example.dialybook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class add_stock_add_field extends AppCompatActivity {

    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock_add_field);
        TextView prbranch=this.findViewById(R.id.present_branch);
        prbranch.setText(MainActivity.user_branch);


        Button addfield=this.findViewById(R.id.but_add_field);

        EditText fieldname=this.findViewById(R.id.field_name);
        progressDialog=new ProgressDialog(this);


        addfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
        String field_name=fieldname.getText().toString();

        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);

        String user=sharedPreferences.getString("user",null);


        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);



        firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference coll=firebaseFirestore.collection("users").document(user).collection("branches");


                Log.d(TAG, "onClick:         "+prtbranch+"       "+field_name);
        coll.document(prtbranch).collection("stock").document(field_name).set(new createstock(field_name)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                progressDialog.cancel();
                Toast.makeText(add_stock_add_field.this, "BRANCH ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(add_stock_add_field.this, "UNABLE TO ADD BRANCH", Toast.LENGTH_SHORT).show();
            }
        });





        fieldname.setText("");





            }
        });



    }
}