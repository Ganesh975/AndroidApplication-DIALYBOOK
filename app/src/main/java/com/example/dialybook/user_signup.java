package com.example.dialybook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dialybook.databinding.ActivityUserSignupBinding;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

public class user_signup extends AppCompatActivity {

    Button signup;
    EditText fullname,username,gender,address,mobile,emailaddress,password;
    ActivityUserSignupBinding usersignupbind;
    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersignupbind=ActivityUserSignupBinding.inflate(getLayoutInflater());
        setContentView(usersignupbind.getRoot());

        getSupportActionBar().hide();
        fullname=this.findViewById(R.id.user_full_name);
        username=this.findViewById(R.id.enter_username);
        gender=this.findViewById(R.id.user_gender);
        address=this.findViewById(R.id.user_address);
        mobile=this.findViewById(R.id.user_mobile);
        emailaddress=findViewById(R.id.user_email_address);
        password=this.findViewById(R.id.user_password);


        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        signup=this.findViewById(R.id.but_signup);
        firebaseFirestore=FirebaseFirestore.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname=usersignupbind.userFullName.getText().toString();
                String username=usersignupbind.enterUsername.getText().toString();
                String gender=usersignupbind.userGender.getText().toString();
                String address=usersignupbind.userAddress.getText().toString();
                String mobilenumber=usersignupbind.userMobileNumber.getText().toString();
                String email=usersignupbind.userEmailAddress.getText().toString();
                String password=usersignupbind.userPassword.getText().toString();
                String presentbranch="none";
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                firebaseFirestore.collection("users/").document(email).set(new Usermodel(fullname,
                                        username,
                                        gender,
                                        address,
                                        mobilenumber,
                                        email,
                                        password,presentbranch));
                                progressDialog.cancel();

                                Toast.makeText(user_signup.this, "USER CREATED", Toast.LENGTH_SHORT).show();


                               /*** SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putBoolean("hasLogIn",true);
                                editor.putString("user",email);
                                editor.putString("branch",null);
                                editor.commit();***/
                                SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putBoolean("hasUserLogIn",true);
                                editor.putString("user",email);
                                editor.commit();


                                Intent intent=new Intent(user_signup.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(user_signup.this, "UNABLE TO CREATE USER "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }
}