package com.example.dialybook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dialybook.databinding.ActivityAddBranchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class add_branch extends AppCompatActivity {

    EditText brchname,brchtype,brchaddress,brchpartners,brchmobile;
    Button createbranch;

    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    ActivityAddBranchBinding addBranchBinding;
    DocumentReference user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBranchBinding=ActivityAddBranchBinding.inflate(getLayoutInflater());
        setContentView(addBranchBinding.getRoot());


        brchname=this.findViewById(R.id.branch_name);
        brchtype=this.findViewById(R.id.branch_type);
        brchaddress=this.findViewById(R.id.branch_address);
        brchpartners=this.findViewById(R.id.branch_partners);
        brchmobile=this.findViewById(R.id.branch_mobile);
        createbranch=this.findViewById(R.id.but_creatbranch);


        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);

        String udocument=sharedPreferences.getString("user",null);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        firebaseFirestore=FirebaseFirestore.getInstance();
        user=firebaseFirestore.collection("users").document(udocument);



        createbranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String branchname=addBranchBinding.branchName.getText().toString();
                String branchtype=addBranchBinding.branchType.getText().toString();
                String branchaddress=addBranchBinding.branchAddress.getText().toString();
                String branchpartners=addBranchBinding.branchPartners.getText().toString();
                String branchmobile=addBranchBinding.branchMobile.getText().toString();

                progressDialog.show();

                user.collection("branches").document(branchname).set(new branchcreate(branchname,branchtype,
                        branchaddress,branchpartners,branchmobile));

                progressDialog.cancel();
                Toast.makeText(add_branch.this, "BRANCH CREATED", Toast.LENGTH_SHORT).show();



            }
        });

    }
}