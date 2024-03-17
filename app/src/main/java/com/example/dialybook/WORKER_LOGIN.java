package com.example.dialybook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WORKER_LOGIN extends AppCompatActivity {


    EditText owner,branch_id,emp_id,emp_pass;
    Button emp_login;
    ProgressDialog progressDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);
        getSupportActionBar().hide();



        emp_login=findViewById(R.id.emp_login);
        owner=this.findViewById(R.id.owner_email);
        branch_id=this.findViewById(R.id.branch_id);
        emp_id=this.findViewById(R.id.emp_id);
        emp_pass=this.findViewById(R.id.emp_password);

        progressDialog=new ProgressDialog(this);




        emp_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();


                String owneremail=owner.getText().toString();
                String branchid=branch_id.getText().toString();
                String empid=emp_id.getText().toString();
                String password=emp_pass.getText().toString();



                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                CollectionReference coll=firestore.collection("users");

                Log.d(TAG, "email: "+owneremail);
               coll.document(owneremail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if(task.getResult().exists()){

                           coll.document(owneremail).collection("branches").document(branchid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<DocumentSnapshot> task1) {
                                   if(task1.getResult().exists()){

                                       coll.document(owneremail).collection("branches").document(branchid).collection("employee").document(empid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                               if(task2.getResult().exists()){
                                                   DocumentSnapshot db=task2.getResult();
                                                   String pass=String.valueOf(db.get("emppassword"));

                                                   Log.d(TAG, "onComplete: entered pass  "+pass+"  act  "+password);

                                                   if(pass.equals(password)){
                                                       progressDialog.cancel();



                                                       Intent intent=new Intent(WORKER_LOGIN.this,MainActivity.class);


                                                       SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
                                                       SharedPreferences.Editor editor=sharedPreferences.edit();
                                                       editor.putBoolean("hasEmpLogIn",true);
                                                       editor.putString("user",owneremail);
                                                       editor.putString("employee",empid);
                                                       editor.putString("branch",branchid);
                                                       editor.commit();



                                                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); //clear backstack
                                                       intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


                                                       startActivity(intent);
                                                       finish();
                                                   }else{
                                                       progressDialog.cancel();
                                                       Toast.makeText(WORKER_LOGIN.this, "ENTERED PASSWORD IS WRONG", Toast.LENGTH_SHORT).show();
                                                   }

                                               }
                                               else{
                                                   progressDialog.cancel();
                                                   Toast.makeText(WORKER_LOGIN.this, "EMPLOYEE ID DOESNOT EXISTS", Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       });



                                   }else{
                                       progressDialog.cancel();
                                       Toast.makeText(WORKER_LOGIN.this, "BRANCH DOES NOT EXISTS", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
                       }else{
                           progressDialog.cancel();
                           Toast.makeText(WORKER_LOGIN.this, "INVALID USERNAME", Toast.LENGTH_SHORT).show();
                       }
                   }
               });


                /****c  if (coll.ex) {
                   
                } else {

                }

                oll.document(owneremail).get()
                        .then((docSnapshot) => {
                if (docSnapshot.exists) {
                    usersRef.onSnapshot((doc) => {
                            
                    });
                } else {
                    // create the document
                }***/






            }
        });
    }
}