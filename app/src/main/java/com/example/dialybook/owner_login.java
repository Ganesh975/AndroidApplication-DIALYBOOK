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
import android.widget.TextView;
import android.widget.Toast;

import com.example.dialybook.databinding.ActivityOwnerLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class owner_login extends AppCompatActivity {

    Button signup,login;
    TextView passreset;
    ActivityOwnerLoginBinding ownerbinding;
    ProgressDialog progressDialog;
    FirebaseAuth fauth;


    EditText user_email,user_password;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ownerbinding=ActivityOwnerLoginBinding.inflate(getLayoutInflater());
        setContentView(ownerbinding.getRoot());
        getSupportActionBar().hide();
        fauth=FirebaseAuth.getInstance();
        user_email=findViewById(R.id.user_email);
        user_password=findViewById(R.id.user_password);
        signup=findViewById(R.id.user_signup);
        login=findViewById(R.id.user_login);
        passreset=this.findViewById(R.id.pass_reset);
        progressDialog=new ProgressDialog(this);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(owner_login.this,user_signup.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=ownerbinding.userEmail.getText().toString();
                String password=ownerbinding.userPassword.getText().toString();
                progressDialog.show();
                fauth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.cancel();
                        Toast.makeText(owner_login.this, "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(owner_login.this,MainActivity.class);



                        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putBoolean("hasUserLogIn",true);
                        editor.putString("user",email);
                        editor.commit();


                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //clear backstack
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();


                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(owner_login.this, "LOGIN UNSUCESSFULL "+e.getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        passreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ownerbinding.userEmail.getText().toString();
                if (!email.equals("")) {

                    progressDialog.show();

                    fauth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.cancel();
                            Toast.makeText(owner_login.this, "EMAIL SENT", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            Toast.makeText(owner_login.this, " EMAIL NOT FOUND ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(owner_login.this, "PLEASE ENTER EMAIL TO SENT CONFIRM ", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}