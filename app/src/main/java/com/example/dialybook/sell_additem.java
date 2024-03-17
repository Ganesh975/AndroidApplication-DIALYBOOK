package com.example.dialybook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class sell_additem extends AppCompatActivity {

    String[] units={"pack","unit","liters","kgs"};
    String[] field;
    String[] items;

    ArrayList<String> field3=new ArrayList<>();
    ArrayList<String> items3=new ArrayList<>();


    static ArrayList<Integer> cost2 = new ArrayList();
    static ArrayList<Integer> quantity = new ArrayList();
    static ArrayList<String> name = new ArrayList();

    static  ArrayList<String> field2=new ArrayList<>();
    static String cust_name="";
    int[] cost={20,10,40,80};

    AutoCompleteTextView autoCompleteTextView_field,autoCompleteTextView_item;
    ArrayAdapter<String> adapteritem_field,adapteritem_item;
    String field1,item1;
    Button additem,cancelitem,bill;
    ProgressDialog progressDialog;
    int k=0;
    int cost1;
    static EditText itemname,itemcost,itemstock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_sell_additem);
          TextView itemunits=findViewById(R.id.item_units);
          EditText itemquantity=findViewById(R.id.item_quantity);
          EditText itemcost=this.findViewById(R.id.itemcost);
          cancelitem=this.findViewById(R.id.but_cancelitem);
          bill=findViewById(R.id.but_bill);

          progressDialog=new ProgressDialog(this);

        autoCompleteTextView_field=findViewById(R.id.drop_downmenu_field);
        autoCompleteTextView_item=findViewById(R.id.drop_downmenu_item);




        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
        String user=sharedPreferences.getString("user",null);

        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);

        Log.d(TAG, "onCreate                : "+user+prtbranch);

        progressDialog.show();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference coll=firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("stock");
        coll.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot collection = task.getResult();

                    for (DocumentSnapshot c : collection) {
                        field3.add(c.getId());

                    }


                    int n = field3.size();

                    Log.d(TAG, "onCreate:                  " + n);
                    field = new String[n];
                    for (int i = 0; i < n; i++) {
                        field[i] = field3.get(i);
                    }
                    adapteritem_field = new ArrayAdapter<String>(sell_additem.this, R.layout.dropdown_field, field);


                    Log.d(TAG, "onCreate: " + field);


                    autoCompleteTextView_field.setAdapter(adapteritem_field);
                    autoCompleteTextView_field.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            field1 = adapterView.getItemAtPosition(i).toString();

                            progressDialog.show();


                            Log.d(TAG, "field  : "+field1);


                            coll.document(field1).collection("fieldstock").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot collection = task.getResult();
                                        if (!collection.isEmpty()) {
                                            for (DocumentSnapshot c : collection) {
                                                items3.add(c.getId());

                                            }


                                            int n = items3.size();

                                            Log.d(TAG, "onCreate:                  " + n);
                                            items = new String[n];

                                            for (int i = 0; i < n; i++) {
                                                items[i] = items3.get(i);
                                            }
                                            adapteritem_item = new ArrayAdapter<String>(sell_additem.this, R.layout.dropdown_item, items);
                                            autoCompleteTextView_item.setAdapter(adapteritem_item);
                                            autoCompleteTextView_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                    item1 = adapterView.getItemAtPosition(i).toString();
                                                    DocumentReference it=coll.document(field1).collection("fieldstock").document(item1);
                                                    it.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if(task.isSuccessful()){
                                                                DocumentSnapshot t=task.getResult();
                                                                itemunits.setText(t.getString("itemunits"));
                                                                itemquantity.setText("1");
                                                                itemcost.setText(String.valueOf(t.getString("itemcost")));
                                                                cost1=Integer.parseInt(t.getString("itemcost"));
                                                                Toast.makeText(sell_additem.this, "item selected " + item1, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }
                                            });
                                            autoCompleteTextView_item.setText(item1);


                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    progressDialog.cancel();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.cancel();
                                    Toast.makeText(sell_additem.this, "UNABLE TO RETRIVE THE DATA", Toast.LENGTH_SHORT).show();
                                }
                            });





                            Toast.makeText(sell_additem.this, "field selected " + field1, Toast.LENGTH_SHORT).show();
                        }
                    });








                }
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressDialog.cancel();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(sell_additem.this, "UNABLE TO RETRIVE THE DATA", Toast.LENGTH_SHORT).show();
            }
        });






            autoCompleteTextView_field.setText(field1);

















        /**  Button backButton = (Button)this.findViewById(R.id.back);
          backButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  finish();
              }
          });**/

        EditText customer_name=this.findViewById(R.id.customer_name);
        if (!cust_name.equals("")){
            customer_name.setText(cust_name);
        }else{
            cust_name=customer_name.getText().toString();

        }


          cancelitem.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  finish();
                  startActivity(getIntent());
              }
          });
          additem=findViewById(R.id.but_additem);



          additem.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  String f=autoCompleteTextView_field.getText().toString();
                  String I=autoCompleteTextView_item.getText().toString();
                  String q=itemquantity.getText().toString();
                  String c=itemcost.getText().toString();
                  cust_name=customer_name.getText().toString();
                  if(!cust_name.equals(null) && !f.equals("") && !I.equals("") && !q.equals("") && !c.equals("")){
                      name.add(I);
                      int c2=Integer.parseInt(c);
                      cost2.add(c2);
                      int q2=Integer.parseInt(q);
                      quantity.add(q2);
                      field2.add(f);

                      Toast.makeText(sell_additem.this, "ITEM ADDED SUCCESSFULLY ", Toast.LENGTH_SHORT).show();
                      finish();
                      startActivity(getIntent());
                  }else{
                      Toast.makeText(sell_additem.this, "PLEASE FILL ALL THE DETAILS", Toast.LENGTH_SHORT).show();
                  }


              }
          });

          bill.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  String f=autoCompleteTextView_field.getText().toString();
                  String I=autoCompleteTextView_item.getText().toString();
                  String q=itemquantity.getText().toString();
                  String c=itemcost.getText().toString();
                  cust_name=customer_name.getText().toString();
                  if(!f.equals("") && !I.equals("") && !q.equals("") && !c.equals("")){
                      name.add(I);
                      int c2=Integer.parseInt(c);
                      cost2.add(c2);
                      int q2=Integer.parseInt(q);
                      quantity.add(q2);
                      field2.add(f);

                      Toast.makeText(sell_additem.this, "ITEM ADDED SUCCESSFULLY ", Toast.LENGTH_SHORT).show();


                      finish();
                      startActivity(getIntent());
                  }else{
                      Toast.makeText(sell_additem.this, "PLEASE FILL ALL THE DETAILS", Toast.LENGTH_SHORT).show();
                  }
                  if(!cust_name.equals("")) {

                      Bundle bsend = new Bundle();
                      bsend.putSerializable("items list", name);
                      bsend.putSerializable("items cost", cost2);
                      bsend.putSerializable("items quantity", quantity);
                      bsend.putSerializable("item field",field2);
                      Intent in = new Intent(sell_additem.this, sell_billitems.class);
                      in.putExtra("customer name", cust_name);

                      in.putExtras(bsend);
                      startActivity(in);
                  }else{
                      Toast.makeText(sell_additem.this, "FILL THE DETAILS", Toast.LENGTH_SHORT).show();
                  }
              }
          });




        itemquantity.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                String c=itemquantity.getText().toString();
                if (!c.equals("")) {
                    int n = Integer.parseInt(c);
                    int cs = n * cost1;
                    itemcost.setText(String.valueOf(cs));
                }else{
                    itemcost.setText("0");
                }

            }
        });





    }
    public void onBackPressed(){
        cost2.clear();
        name.clear();
        quantity.clear();
        finish();
    }
    public static  void delete_item(int i){
        name.remove(i);
        quantity.remove(i);
        cost2.remove(i);
    }
    public static  void items_clear(){
        name.clear();
        quantity.clear();
        cost2.clear();
    }


}