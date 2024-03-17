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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class productsales_selectfield extends AppCompatActivity {

    ListView li;

    String[] field_name;
    FirebaseFirestore firebaseFirestore;

    ProgressDialog progressDialog;

    ArrayList<String > field_name2=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productsales_selectfield);
        TextView prbranch=findViewById(R.id.present_branch);
        prbranch.setText(MainActivity.user_branch);

        progressDialog=new ProgressDialog(this);
        progressDialog.show();

        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
        String user=sharedPreferences.getString("user",null);


        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);

        Log.d(TAG, "  : "+user+"           "+prtbranch);

        firebaseFirestore= FirebaseFirestore.getInstance();
        CollectionReference collection1=firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("stock");

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
                            field_name2.add(co.getId());
                        }
                        int n=field_name2.size();
                        field_name=new String[n];
                        for(int i=0;i<n;i++){
                            field_name[i]=field_name2.get(i);
                        }
                        Log.d(TAG, "    fields  : "+field_name.length);
                        progressDialog.cancel();
                        li=findViewById(R.id.field_list);
                        productsales_selectfield.CustomBaseAdapter cust=new productsales_selectfield.CustomBaseAdapter(getApplicationContext(),field_name);
                        li.setAdapter(cust);


                    }
                }

            }
        });





    }
    class CustomBaseAdapter extends BaseAdapter {
        Context context;
        String data[];

        LayoutInflater inflater;

        public CustomBaseAdapter(Context applicationContext, String[] data) {
            this.context=applicationContext;
            this.data=data;
            inflater=LayoutInflater.from(applicationContext);
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=inflater.inflate(R.layout.activity_listview_field,null);
            TextView field=view.findViewById(R.id.field_name);
            field.setText(data[i]);
            field.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(productsales_selectfield.this,productsales_infield.class);
                    intent.putExtra("field name",data[i]);
                    startActivity(intent);
                }
            });
            return view;
        }


    }
}