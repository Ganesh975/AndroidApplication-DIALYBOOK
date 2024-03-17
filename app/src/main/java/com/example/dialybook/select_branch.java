package com.example.dialybook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.example.dialybook.databinding.ActivityAddBranchBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class select_branch extends AppCompatActivity {


    ActivityAddBranchBinding addBranchBinding;
    DatabaseReference reference;
    FirebaseFirestore firestore;





    ListView li;

    List<String> branch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBranchBinding=ActivityAddBranchBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_select_branch);

        ArrayList<String> list = new ArrayList<>();
        TextView t=this.findViewById(R.id.select_branch);

        li=this.findViewById(R.id.branch_list);


        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.user,0);
        String user= String.valueOf(sharedPreferences.getString("user",null));
        t.setText(user);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference coll=firestore.collection("users").document(user).collection("branches");

        coll.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG,"ONsuccess:getting data");
                Toast.makeText(select_branch.this, "successful", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"before " +list.toString());
                for(QueryDocumentSnapshot query:queryDocumentSnapshots){
                    list.add(query.getId());
                }
                Log.d(TAG,"after "+ list.toString());

                select_branch.CustomBaseAdapter cust=new select_branch.CustomBaseAdapter(getApplicationContext(),list);
                li.setAdapter(cust);
            }
        });
        /***firestore.collection("users").document(user).collection("branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(select_branch.this, "successful", Toast.LENGTH_SHORT).show();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                    }

                } else {
                    Toast.makeText(select_branch.this, "unsuccessful", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });***/






    }
    class CustomBaseAdapter extends BaseAdapter {


        Context context;
        ArrayList<String> list;

        LayoutInflater inflater;

        public CustomBaseAdapter(Context applicationContext, ArrayList<String> list) {
            this.context=applicationContext;
            this.list=new ArrayList<>(list);
            inflater=LayoutInflater.from(applicationContext);
        }

        @Override
        public int getCount() {
            Log.d(TAG,"after1 "+ list.toString());
            Log.d(TAG,"size "+ list.size());
            return list.size();

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
            Log.d(TAG,"after2 "+ list.toString());
            view=inflater.inflate(R.layout.activity_listview_field,null);
            TextView field=view.findViewById(R.id.field_name);
            field.setText(list.get(i));
            field.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.user,0);
                    String user= String.valueOf(sharedPreferences.getString("user",null));



                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    CollectionReference coll=firestore.collection("users");
                    coll.document(user).update("branch",list.get(i));




                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("branch",list.get(i));
                    editor.commit();


                    Intent intent=new Intent(select_branch.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("branch name",list.get(i));
                    startActivity(intent);
                }
            });
            return view;
        }


    }
}