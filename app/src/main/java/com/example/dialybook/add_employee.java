package com.example.dialybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dialybook.databinding.ActivityAddEmployeeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class add_employee extends AppCompatActivity {

    EditText empname,empid,empaddress,empgender,empmobile,emppassword;
    Button addemployee;


    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;


    ActivityAddEmployeeBinding addEmployeeBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addEmployeeBinding=ActivityAddEmployeeBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_add_employee);
        TextView prbranch=this.findViewById(R.id.present_branch);
        prbranch.setText(MainActivity.user_branch);


        empname=this.findViewById(R.id.emp_name);
        empid=this.findViewById(R.id.emp_id);
        empaddress=this.findViewById(R.id.emp_address);
        empgender=this.findViewById(R.id.emp_gender);
        emppassword=this.findViewById(R.id.emp_password);
        empmobile=this.findViewById(R.id.user_mobile_number);


        addemployee=this.findViewById(R.id.but_createemp);


        progressDialog=new ProgressDialog(this);
        firebaseFirestore=FirebaseFirestore.getInstance();



        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.user,0);
        String user= String.valueOf(sharedPreferences.getString("user",null));
        String branch=String.valueOf(sharedPreferences.getString("branch",null));



        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference coll=firebaseFirestore.collection("users").document(user).collection("branches").document(branch).collection("employee");


        addemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                progressDialog.show();


                String empname1=empname.getText().toString();
                String empid1=empid.getText().toString();
                String empgender1=empgender.getText().toString();
                String empaddress1=empaddress.getText().toString();
                String empmobile1=empmobile.getText().toString();
                String emppassword1=emppassword.getText().toString();
                String empbranch=branch;


                coll.document(empid1).set(new empmodel(empname1,empid1,empgender1,empaddress1,empmobile1,emppassword1,empbranch)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.cancel();
                        Toast.makeText(add_employee.this, "EMPLOYEE CREATED", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(add_employee.this, "UNSUCCESSFULL", Toast.LENGTH_SHORT).show();
                    }
                }) ;
            }
        });
    }
}