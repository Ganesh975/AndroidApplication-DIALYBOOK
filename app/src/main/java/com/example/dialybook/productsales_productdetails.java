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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class productsales_productdetails extends AppCompatActivity {

    TextView pro_name,pro_cost,pro_left_sales,pro_left_cost,pro_quantity,sales_quantity,sales_cost;
    Button productbills;

    ProgressDialog progressDialog;
    String productname,productcost;
    String name;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productsales_productdetails);


        pro_name=findViewById(R.id.item_name);
        pro_cost=findViewById(R.id.item_cost);
        pro_quantity=findViewById(R.id.item_quantity);
        sales_quantity=findViewById(R.id.sales_quantity);
        sales_cost=findViewById(R.id.sales_cost);
        pro_left_cost=findViewById(R.id.quantity_leftcost);
        pro_left_sales=findViewById(R.id.quantity_left);

        Intent in=getIntent();
        name=in.getStringExtra("product field");
        String p=in.getStringExtra("product name");
        String cost=in.getStringExtra("product cost");
        String quantity=in.getStringExtra("total quantity");
        String quantity_cost=in.getStringExtra("quantity cost");



        progressDialog=new ProgressDialog(this);
        progressDialog.show();

        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
        String user=sharedPreferences.getString("user",null);


        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);

        Log.d(TAG, "  : "+user+"           "+prtbranch);

        firebaseFirestore= FirebaseFirestore.getInstance();
        DocumentReference collection1=firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("stock").document(name).collection("fieldstock").document(p);


        collection1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

                pro_name.setText(document.getString("itemname"));
                productname=document.getString("itemname");
                pro_cost.setText(document.getString("itemcost"));
                productcost=document.getString("itemcost");
                pro_quantity.setText(document.getString("itemstock"));

                pro_left_sales.setText(document.getString("stockleft"));
                int k=Integer.parseInt(document.getString("stockleft"));
                int c=Integer.parseInt(document.getString("itemcost"));
                int f=k*c;
                pro_left_cost.setText(String.valueOf(f));


                int m=Integer.parseInt(document.getString("itemstock"));
                int f1=m-k;
                sales_quantity.setText(String.valueOf(f1));

                int f2=f1*c;
                sales_cost.setText(String.valueOf(f2));

                progressDialog.cancel();




            }
        });





        productbills=findViewById(R.id.but_productbills);
        productbills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(productsales_productdetails.this,productsales_productbills.class);
                intent.putExtra("field name",name);
                intent.putExtra("product name",productname);
                intent.putExtra("product cost",productcost);
                startActivity(intent);
            }
        });
    }
}