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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class product_list_add_field extends AppCompatActivity {

    String[] units={"pack","unit","liters","kgs"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapteritem;
    static  EditText itemname,itemcost,itemstock;

    ProgressDialog progressDialog;
    String itemname2;
    String itemcost2;
    String itemstock2;
    String itemunit2;






    String item;
    String name1;

    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_add_field);
        Intent in=getIntent();
        name1=in.getStringExtra("product name");

        Log.d(TAG, "onCreate:      pro   "+name1+"  "+select_product_infield.fieldname);



        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);

        String user=sharedPreferences.getString("user",null);


        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);



        adapteritem= new ArrayAdapter<String>(this,R.layout.quantity_units,units);
        autoCompleteTextView=findViewById(R.id.drop_downmenu_units);
        autoCompleteTextView.setAdapter(adapteritem);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(product_list_add_field.this, "item selected "+item, Toast.LENGTH_SHORT).show();
            }
        });




        Button addstock=this.findViewById(R.id.but_createstock);

        progressDialog=new ProgressDialog(this);


        firebaseFirestore= FirebaseFirestore.getInstance();
        CollectionReference coll=firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("stock").document(select_product_infield.fieldname).collection("fieldstock");

        if(name1!=null) {

            coll.document(name1).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot collection = task.getResult();

                        itemname2 = collection.getString("itemname");
                        itemcost2 = collection.getString("itemcost");
                        itemstock2 = collection.getString("itemstock");
                        itemunit2 = collection.getString("itemunits");


                        itemname.setText(itemname2);
                        itemcost.setText(itemcost2);
                        itemstock.setText(itemstock2);
                        autoCompleteTextView.setText(itemunit2);


                        Log.d(TAG, "onComplete: " + name1 + itemname2 + "  " + itemcost2);


                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }

        itemname= this.<EditText>findViewById(R.id.item_name);
        TextView selbranch=this.findViewById(R.id.selected_field);
        selbranch.setText(select_product_infield.fieldname);
        itemcost= this.<EditText>findViewById(R.id.item_cost_per_unit);
        itemstock= this.<EditText>findViewById(R.id.total_item_stock);












        addstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                firebaseFirestore= FirebaseFirestore.getInstance();
                CollectionReference coll=firebaseFirestore.collection("users").document(user).collection("branches");






                String name=itemname.getText().toString();
                String cost=itemcost.getText().toString();
                String stock=itemstock.getText().toString();
                String stockleft=itemstock.getText().toString();


                if(name1!=null){
                    name=name1;
                }





                item=autoCompleteTextView.getText().toString();
                Log.d(TAG, "onClick:         "+prtbranch+"       "+select_product_infield.fieldname+"        "+item);
                coll.document(prtbranch).collection("stock").document(select_product_infield.fieldname).collection("fieldstock").document(name).set(new createstock(select_product_infield.fieldname,name,cost,stock,item,stockleft)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(product_list_add_field.this, "ITEM ADDED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(product_list_add_field.this, "ITEM NOT ADDED", Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });
        progressDialog.cancel();








        /***itemname.setText(name);
        itemcost.setText(cost);
        itemstock.setText(stock);
        autoCompleteTextView.setText(unit);***/

    }
    public static  void setdefaultVales(String prod_name,String pro_cost,String prod_stock){
        itemname.setText(prod_name);
        itemcost.setText(pro_cost);
        itemstock.setText(prod_stock);
    }

    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}