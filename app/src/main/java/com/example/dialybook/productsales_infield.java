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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class productsales_infield extends AppCompatActivity {


    ListView li;
    String[] product_name ;
    String[] total_quantity;
    String[] product_cost;
    String[] quantity_cost;

    ArrayList<String > product_name2=new ArrayList<>();
    ArrayList<String > total_quantity2=new ArrayList<>();
    ArrayList<String > product_cost2=new ArrayList<>();
    ArrayList<String > quantity_cost2=new ArrayList<>();

    ProgressDialog progressDialog;

    FirebaseFirestore firebaseFirestore;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productsales_infield);
        Intent in=getIntent();
        name=in.getStringExtra("field name");
        TextView field=this.<TextView>findViewById(R.id.selected_field);
        field.setText(name);
        li=this.<ListView>findViewById(R.id.list_history);

        progressDialog=new ProgressDialog(this);
        progressDialog.show();

        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
        String user=sharedPreferences.getString("user",null);


        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);

        Log.d(TAG, "  : "+user+"           "+prtbranch);

        firebaseFirestore= FirebaseFirestore.getInstance();
        CollectionReference collection1=firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("stock").document(name).collection("fieldstock");

        collection1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    progressDialog.show();
                    QuerySnapshot document = task.getResult();
                    if (!document.isEmpty()) {
                        progressDialog.show();
                        final AtomicInteger taskCounter1 = new AtomicInteger(document.size());

                        int tasksLeft1 = taskCounter1.get();

                        Log.d(TAG, "onComplete: "+tasksLeft1);
                        for (DocumentSnapshot co : document) {
                            progressDialog.show();
                            product_name2.add(co.getId());
                            product_cost2.add(co.getString("itemcost"));
                            total_quantity2.add(co.getString("stockleft"));
                            int k=Integer.parseInt(co.getString("stockleft"));
                            int c=Integer.parseInt(co.getString("itemcost"));
                            int f=k*c;
                            quantity_cost2.add(String.valueOf(f));
                        }
                        int n=product_cost2.size();
                        product_name=new String[n];
                        product_cost=new String[n];
                        total_quantity=new String[n];
                        quantity_cost=new String[n];
                        for(int i=0;i<n;i++){
                            product_name[i]=product_name2.get(i);
                            product_cost[i]=product_cost2.get(i);
                            total_quantity[i]=total_quantity2.get(i);
                            quantity_cost[i]=quantity_cost2.get(i);
                        }

                        progressDialog.cancel();

                        productsales_infield.CustomBaseAdapter cust=new productsales_infield.CustomBaseAdapter(getApplicationContext(),product_name,quantity_cost,total_quantity,product_cost);
                        li.setAdapter(cust);


                    }
                }
            }
        });







    }
    class CustomBaseAdapter extends BaseAdapter {
        Context context;
        String[] product_name ;
        String[] quantity1;
        String[] cost1,quantity_cost;

        LayoutInflater inflater;

        public CustomBaseAdapter(Context applicationContext, String[] product_name, String[] quantity_cost, String[] total_quantity, String[] product_cost) {
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
            TextView n=view.findViewById(R.id.product_name);
            TextView quantity=view.findViewById(R.id.quantity_left);
            TextView cost=view.findViewById(R.id.product_cost);
            TextView quantitycost=view.findViewById(R.id.stock_cost);

            n.setText(product_name[i]);
            quantity.setText(quantity1[i]);
            cost.setText(cost1[i]);
            quantitycost.setText(quantity_cost[i]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(productsales_infield.this,productsales_productdetails.class);
                    intent.putExtra("product field",name);
                    intent.putExtra("product name",product_name[i]);
                    intent.putExtra("product cost",cost1[i]);
                    intent.putExtra("total quantity",quantity1[i]);
                    intent.putExtra("quantity cost",quantity_cost[i]);
                    startActivity(intent);
                }
            });
            return view;
        }


    }
}