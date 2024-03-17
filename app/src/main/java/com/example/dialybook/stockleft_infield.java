package com.example.dialybook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;

public class stockleft_infield extends AppCompatActivity {

    ListView li;

    ArrayList<String> productname = new ArrayList<>();
    ArrayList<String> productcost = new ArrayList<>();
    ArrayList<String> productstockleft = new ArrayList<>();
    ArrayList<String> stockleftcost = new ArrayList<>();

    FirebaseFirestore firebaseFirestore;

    ProgressDialog progressDialog;
    String[] product_name ;
    String[] product_stock;
    String[] product_cost;
    String[] product_total_cost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockleft_infield);
        Intent in=getIntent();
        String name=in.getStringExtra("field name");
        TextView field=findViewById(R.id.selected_field);
        field.setText(name);

        li=findViewById(R.id.list_history);





        progressDialog=new ProgressDialog(this);


        progressDialog.show();

        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.user,0);
        String user= String.valueOf(sharedPreferences.getString("user",null));

        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);

        firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference coll=firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("stock").document(name).collection("fieldstock");


        coll.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot collection = task.getResult();
                    if (!collection.isEmpty()) {
                        for(DocumentSnapshot c:collection){
                            productname.add(c.getString("itemname"));
                            productcost.add(c.getString("itemcost"));
                            productstockleft.add(c.getString("stockleft"));

                            int totalcost1=Integer.parseInt((c.getString("itemcost")))*Integer.parseInt(c.getString("stockleft"));

                            String totalcost=String.valueOf(totalcost1);

                            stockleftcost.add(totalcost);
                        }


                        int n = productname.size();

                        Log.d(TAG, "onCreate:                  "+n);
                        product_cost= new String[n];
                        product_name = new String[n];
                        product_stock=new String[n];
                        product_total_cost=new String[n];
                        for(int i=0; i<n; i++) {
                            product_name[i]=productname.get(i);
                            product_stock[i]=productstockleft.get(i);
                            product_cost[i]=productcost.get(i);
                            product_total_cost[i]=stockleftcost.get(i);
                        }

                        stockleft_infield.CustomBaseAdapter cust=new stockleft_infield.CustomBaseAdapter(getApplicationContext(),product_name,product_stock,product_cost,product_total_cost);
                        li.setAdapter(cust);



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
                Toast.makeText(stockleft_infield.this, "UNABLE TO RETRIVE THE DATA", Toast.LENGTH_SHORT).show();
            }
        });










    }
    class CustomBaseAdapter extends BaseAdapter {
        Context context;
        String[] product_name ;
        String[] quantity1;
        String[] cost1,quantity_cost;

        LayoutInflater inflater;

        public CustomBaseAdapter(Context applicationContext, String[] product_name, String[] total_quantity, String[] product_cost, String[] quantity_cost) {
            this.context=applicationContext;
            this.product_name=product_name;
            this.quantity1=total_quantity;
            this.cost1=product_cost;
            this.quantity_cost=quantity_cost;

            inflater=LayoutInflater.from(applicationContext);
        }

        @Override
        public int getCount() {
            return product_cost.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=inflater.inflate(R.layout.activity_infeild_stock_bills,null);
            TextView name=view.findViewById(R.id.product_name);
            TextView quantity=view.findViewById(R.id.quantity_left);
            TextView cost=view.findViewById(R.id.product_cost);
            TextView quantitycost=view.findViewById(R.id.stock_cost);

            name.setText(product_name[i]);
            quantity.setText(quantity1[i]);
            cost.setText(cost1[i]);
            quantitycost.setText(quantity_cost[i]);


            return view;
        }


    }
}