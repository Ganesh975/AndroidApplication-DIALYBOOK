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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class publichistory_bills extends AppCompatActivity {

    ListView li;
    int total_sales=0;

    FirebaseFirestore firebaseFirestore;



    String[] customer_name;
    String[] total_bill;
    String[] dateandtime;
    String[] pay_method,seller_name;

    String[] customer_id;

    ArrayList<String> customer_name2=new ArrayList<>();
    ArrayList<String> pay_method2=new ArrayList<>();
    ArrayList<String> total_bill2=new ArrayList<>();
    ArrayList<String> dateandtime2=new ArrayList<>();
    ArrayList<String> customer_id2=new ArrayList<>();
    ArrayList<String> seller_name2=new ArrayList<>();


    ArrayList<String> filtered=new ArrayList<>();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publichistory_bills);
        TextView totsales=findViewById(R.id.total_sales);
        TextView prbranch=this.findViewById(R.id.present_branch);
        prbranch.setText(MainActivity.user_branch);
        progressDialog=new ProgressDialog(this);

        progressDialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.U_LOGIN, 0);
        String user = sharedPreferences.getString("user", null);


        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);


        firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference coll = firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("bills");


        coll.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot document = task.getResult();
                    if (!document.isEmpty()) {
                        for (DocumentSnapshot co : document) {

                            String datetime = co.getString("datetime");

                            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");

                            Date currentDate = new Date();



                            // Format the date and time as a string
                            String formattedDate = df.format(currentDate);





                            try {


                                Date date2 = df.parse(datetime);

                                Date date1 = df.parse(formattedDate);

                                Log.d(TAG, "onComplete:         "+date1+"    "+date2);


                                // Compare the two Date objects for equality
                                boolean datesAreEqual = date1.equals(date2);

                                if (datesAreEqual){
                                filtered.add(co.getId());}
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }



                        progressDialog.setTitle("Getting Purchases");
                        progressDialog.show();

                        final AtomicInteger taskCounter = new AtomicInteger(filtered.size());
                        Log.d(TAG, "fildered dates "+filtered);

                        if (filtered.size()!=0) {

                            for (String s : filtered) {
                                coll.document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            dateandtime2.add(document.getString("datetime"));
                                            pay_method2.add(document.getString("payement_method"));
                                            customer_name2.add(document.getString("customername"));
                                            total_bill2.add(String.valueOf(document.get("total_purchase")));
                                            seller_name2.add(document.getString("seller_name"));

                                            Log.d(TAG, "date time and customer name : " + customer_name2 + dateandtime2);
                                            String cost = String.valueOf(document.get("total_purchase"));
                                            Log.d(TAG, "cost : " + cost);
                                            int cost1 = Integer.parseInt(cost);
                                            Log.d(TAG, "int cost : " + cost1);
                                            total_sales += cost1;

                                            Log.d(TAG, ": ");

                                            customer_id2.add(document.getId());
                                        }

                                        // Decrement the task counter
                                        int tasksLeft = taskCounter.decrementAndGet();

                                        // If all tasks are completed, dismiss the ProgressDialog
                                        if (tasksLeft == 0) {
                                            progressDialog.dismiss();

                                            // Update your UI or perform any necessary post-processing here
                                            Log.d(TAG, "filtered dates are   : " + filtered);

                                            int n = dateandtime2.size();


                                            dateandtime = new String[dateandtime2.size()];
                                            pay_method=new String[n];
                                            customer_name = new String[customer_name2.size()];
                                            total_bill = new String[total_bill2.size()];
                                            customer_id = new String[customer_name2.size()];
                                            seller_name=new String[n];

                                            for (int i = 0; i < n; i++) {
                                                seller_name[i]=seller_name2.get(i);
                                                dateandtime[i] = dateandtime2.get(i);
                                                pay_method[i]=pay_method2.get(i);
                                                customer_name[i] = customer_name2.get(i);
                                                total_bill[i] = total_bill2.get(i);
                                                customer_id[i] = customer_id2.get(i);
                                            }
                                            Log.d(TAG, "sellers: "+seller_name2);
                                            totsales.setText(String.valueOf(total_sales));

                                            li = findViewById(R.id.history_list);
                                            publichistory_bills.CustomBaseAdapter cust = new publichistory_bills.CustomBaseAdapter(getApplicationContext(), customer_name, total_bill, dateandtime,pay_method,seller_name);
                                            li.setAdapter(cust);
                                            progressDialog.cancel();
                                        }
                                    }
                                });
                            }

                        }else{
                            Toast.makeText(publichistory_bills.this, "bills not found", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }





                        /***progressDialog.setMessage("Loading...");
                         progressDialog.show();
                         for (String s : filtered) {
                         coll.document(s).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        progressDialog.setMessage("retriving the data");
                        progressDialog.show();
                        if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        dateandtime2.add(document.getString("datetime"));
                        customer_name2.add(document.getString("customername"));
                        total_bill2.add(String.valueOf(document.get("total_purchase")));
                        total_sales += Integer.valueOf(String.valueOf(document.get("total_purchase")));
                        customer_id2.add(document.getId());
                        }

                        }
                        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                        }
                        }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                        });


                         }***/






                    } else {
                        Log.d(TAG, "No such document");
                        progressDialog.cancel();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    progressDialog.cancel();
                }
            }
        });



    }
    class CustomBaseAdapter extends BaseAdapter {
        Context context;
        String[] customer_name ;

        String[] total_bill,dateandtime,pay_method,seller_name;

        LayoutInflater inflater;

        public CustomBaseAdapter(Context applicationContext, String[] customer_name, String[] total_bill, String[] dateandtime,String[] pay_method,String[] seller_name) {
            this.context=applicationContext;
            this.customer_name=customer_name;
            this.total_bill=total_bill;
            this.dateandtime=dateandtime;
            this.pay_method=pay_method;
            this.seller_name=seller_name;
            inflater=LayoutInflater.from(applicationContext);
        }

        @Override
        public int getCount() {
            return customer_name.length;
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
            view=inflater.inflate(R.layout.history_bills_list,null);
            TextView name=view.findViewById(R.id.customer_name);
            TextView totbills=view.findViewById(R.id.total_bill);
            TextView datetime=view.findViewById(R.id.date_time);
            TextView pay=view.findViewById(R.id.payement_method);
            TextView sell=view.findViewById(R.id.seller_name);
            sell.setText(seller_name[i]);
            pay.setText(pay_method[i]);
            name.setText(customer_name[i]);
            totbills.setText(total_bill[i]);
            datetime.setText(dateandtime[i]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in=new Intent(publichistory_bills.this,history_billspurchase.class);
                    in.putExtra("customer name",customer_name[i]);
                    in.putExtra("total bill",total_bill[i]);
                    in.putExtra("date time",dateandtime[i]);
                    in.putExtra("customer id",customer_id[i]);
                    in.putExtra("payement method",pay_method[i]);
                    in.putExtra("seller_name",seller_name[i]);
                    startActivity(in);
                }

            });

            return view;
        }


    }

}