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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class public_stockleftselectfield extends AppCompatActivity {

    ListView li;

    String[] data ;


    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_stockleftselectfield);
        TextView prbranch=this.findViewById(R.id.present_branch);
        prbranch.setText(MainActivity.user_branch);


        li=this.findViewById(R.id.field_list);




        ArrayList<String> list = new ArrayList<>();
        progressDialog=new ProgressDialog(this);

        progressDialog.show();
        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.user,0);
        String user= String.valueOf(sharedPreferences.getString("user",null));

        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);



        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference coll=firestore.collection("users").document(user).collection("branches").document(prtbranch);


        coll.collection("stock").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {
                    QuerySnapshot document = task.getResult();
                    if (!document.isEmpty()) {


                        for (DocumentSnapshot co:document){

                            list.add(co.getId());
                        }




                        int n = list.size();


                        data = new String[list.size()];

                        for(int i=0; i<n; i++) {
                            data[i] = list.get(i);
                        }




                        public_stockleftselectfield.CustomBaseAdapter cust=new public_stockleftselectfield.CustomBaseAdapter(getApplicationContext(),data);
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
                    Intent intent=new Intent(public_stockleftselectfield.this,public_stockleftinfield.class);
                    intent.putExtra("field name",data[i]);
                    startActivity(intent);
                }
            });
            return view;
        }


    }
}