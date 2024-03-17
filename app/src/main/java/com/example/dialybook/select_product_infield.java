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

public class select_product_infield extends AppCompatActivity {

    ListView li;
    static  String fieldname;



    ArrayList<String> productname = new ArrayList<>();
    ArrayList<String> productcost = new ArrayList<>();
    ArrayList<String> productstock = new ArrayList<>();
    ArrayList<String> productunits = new ArrayList<>();

    FirebaseFirestore firebaseFirestore;

    ProgressDialog progressDialog;
    String[] product_name ;
    String[] product_stock;
    String[] product_cost;
    String[] product_units;
    Button but_addproduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product_infield);
        li=findViewById(R.id.product_list);
        TextView selfield=this.findViewById(R.id.selected_field);
        Intent in=getIntent();
        fieldname=in.getStringExtra("field name");
        selfield.setText(fieldname);

        progressDialog=new ProgressDialog(this);


        progressDialog.show();

        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.user,0);
        String user= String.valueOf(sharedPreferences.getString("user",null));

        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);

        firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference coll=firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("stock").document(fieldname).collection("fieldstock");


        coll.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    QuerySnapshot collection = task.getResult();
                    if (!collection.isEmpty()) {
                            for(DocumentSnapshot c:collection){
                                productname.add(c.getString("itemname"));
                                productcost.add(c.getString("itemcost"));
                                productstock.add(c.getString("itemstock"));
                                productunits.add(c.getString("itemunits"));
                            }


                        int n = productname.size();

                        Log.d(TAG, "onCreate:                  "+n);
                        product_cost= new String[n];
                        product_name = new String[n];
                        product_stock=new String[n];
                        product_units=new String[n];
                        for(int i=0; i<n; i++) {
                            product_name[i]=productname.get(i);
                            product_stock[i]=productstock.get(i);
                            product_cost[i]=productcost.get(i);
                            product_units[i]=productunits.get(i);
                        }

                        select_product_infield.CustomBaseAdapter cust=new select_product_infield.CustomBaseAdapter(getApplicationContext(),product_name,product_stock,product_cost,product_units);
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
                Toast.makeText(select_product_infield.this, "UNABLE TO RETRIVE THE DATA", Toast.LENGTH_SHORT).show();
            }
        });










        but_addproduct=findViewById(R.id.but_add_product);
        but_addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(select_product_infield.this,product_list_add_field.class));
            }
        });



    }
    class CustomBaseAdapter extends BaseAdapter {
        Context context;
        String[] product_name ;
        String[] product_stock;
        String[] product_cost;
        String[] product_units;
        LayoutInflater inflater;

        public CustomBaseAdapter(Context applicationContext, String[] product_name, String[] product_stock, String[] product_cost, String[] product_units) {
            this.context=applicationContext;
            this.product_name=product_name;
            this.product_stock=product_stock;
            this.product_cost=product_cost;
            this.product_units=product_units;
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
            view=inflater.inflate(R.layout.activity_product_list,null);
            TextView name=view.findViewById(R.id.product_name);
            TextView stock=view.findViewById(R.id.product_stock);
            TextView cost=view.findViewById(R.id.product_cost);
            name.setText(product_name[i]);
            stock.setText(product_stock[i]);
            cost.setText(product_cost[i]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent=new Intent(select_product_infield.this,product_list_add_field.class);
                    intent.putExtra("product name",product_name[i]);

                    startActivity(intent);
                    /***product_list_add_field.setdefaultVales(product_name[i],product_cost[i],product_stock[i]);
                    System.out.println(product_name[i]);
                    System.out.println(product_cost[i]);
                    System.out.println(product_stock[i]);***/
                }
            });
            return view;
        }


        /***  public View getView(int i, View v1, ViewGroup viewGroup){
         v1=inflater.inflate(R.layout.activity_listview_field,viewGroup,false);

         TextView field=(TextView) findViewById(R.id.field_name);
         field.setText(data[i]);
         return v1;
         }***/
    }
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
