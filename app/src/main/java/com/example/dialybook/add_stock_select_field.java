package com.example.dialybook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class add_stock_select_field extends AppCompatActivity {
    ListView li;
    Button add_field;

    ProgressDialog progressDialog;
    String[] data ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock_select_field);
        progressDialog=new ProgressDialog(this);



        TextView prbranch=this.findViewById(R.id.present_branch);
        prbranch.setText(MainActivity.user_branch);

        ArrayList<String> list = new ArrayList<>();

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



                        li=findViewById(R.id.field_list);
                        CustomBaseAdapter cust=new CustomBaseAdapter(getApplicationContext(),data);
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






            add_field=findViewById(R.id.but_add_field);
            add_field.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {




                    Intent intent=new Intent(add_stock_select_field.this,add_stock_add_field.class);
                    startActivity(intent);

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
                     Intent intent=new Intent(add_stock_select_field.this,select_product_infield.class);
                     intent.putExtra("field name",data[i]);
                     startActivity(intent);
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



 /***  public View onCreateView ( LayoutInflater inflater , ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);

        View view=inflater.inflate(R.layout.yourlayout,container,false);
        TextView textView=view.findViewById(R.id.textView);
        textView.setText(content);
        return view;
    }***/
