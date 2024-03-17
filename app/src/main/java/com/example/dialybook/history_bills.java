package com.example.dialybook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class history_bills extends AppCompatActivity {

    TextView startdate,enddate;
    ImageView start,end;
    private int sdate,smonth,syear,edate,emonth,eyear;

    FirebaseFirestore firebaseFirestore;

    String endDate,startDate;

    ProgressDialog progressDialog;

    ListView li;
    int total_sales=0;
    String[] customer_name;
    String[] total_bill;
    String[] pay_method;
    String[] dateandtime;

    String[] customer_id , seller_name;

    ArrayList<String> customer_name2=new ArrayList<>();
    ArrayList<String> total_bill2=new ArrayList<>();
    ArrayList<String> pay_method2=new ArrayList<>();
    ArrayList<String> dateandtime2=new ArrayList<>();
    ArrayList<String> customer_id2=new ArrayList<>();
    ArrayList<String> seller_name2=new ArrayList<>();


    ArrayList<String> filtered=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_bills);
        startdate=findViewById(R.id.startdate);
        enddate=findViewById(R.id.enddate);
        start=findViewById(R.id.image_start);
        TextView prbranch=this.findViewById(R.id.present_branch);
        prbranch.setText(MainActivity.user_branch);
        end=findViewById(R.id.image_end);
        TextView totsales=findViewById(R.id.total_sales);

        progressDialog=new ProgressDialog(this);


        Button Butfilter=this.findViewById(R.id.but_filter);


        ///for(int i = 0; i < customer_name.length; i++)
        ///    total_sales += Integer.valueOf(total_bill[i]);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                sdate = cal.get(Calendar.DATE);
                smonth = cal.get(Calendar.MONTH);
                syear = cal.get(Calendar.YEAR);

                DatePickerDialog datestart = new DatePickerDialog(history_bills.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        startdate.setText(i + "-" + (i1 + 1) + "-" + i2);

                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(i, i1, i2);

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        startDate = format.format(selectedCalendar.getTime());
                    }
                }, syear, smonth, sdate);

                // Set the maximum date to prevent selecting future dates
                datestart.getDatePicker().setMaxDate(System.currentTimeMillis());

                // Show the dialog
                datestart.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                edate = cal.get(Calendar.DATE);
                emonth = cal.get(Calendar.MONTH);
                eyear = cal.get(Calendar.YEAR);

                DatePickerDialog dateend = new DatePickerDialog(history_bills.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        enddate.setText(i + "-" + (i1 + 1) + "-" + i2);

                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(i, i1, i2);

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        endDate = format.format(selectedCalendar.getTime());
                    }
                }, eyear, emonth, edate);

                // Set the maximum date to prevent selecting future dates
                dateend.getDatePicker().setMaxDate(System.currentTimeMillis());

                // Show the dialog
                dateend.show();
            }
        });






            Butfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onCreate:                            startdate"+startdate+"         "+endDate);

                    if (startDate != null && endDate != null) {
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
                                            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                                            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");


                                            Log.d(TAG, "onComplete:   start  " + startDate);
                                            Log.d(TAG, "onComplete: end   " + endDate);


                                            try {

                                                Date startDate1 = formatter.parse(startDate);
                                                Date endDate1 = formatter.parse(endDate);
                                                Date date = df.parse(datetime);


                                                if (isDateInBetweenIncludingEndPoints(startDate1, endDate1, date)) {
                                                    filtered.add(co.getId());
                                                }
                                            } catch (ParseException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }



                                        progressDialog.setTitle("Getting Purchases");
                                        progressDialog.show();

                                        final AtomicInteger taskCounter = new AtomicInteger(filtered.size());

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
                                                        total_sales += Integer.valueOf(String.valueOf(document.get("total_purchase")));
                                                        customer_id2.add(document.getId());

                                                        String cost = String.valueOf(document.get("total_purchase"));
                                                        Log.d(TAG, "cost : " + cost);
                                                        int cost1 = Integer.parseInt(cost);
                                                        Log.d(TAG, "int cost : " + cost1);
                                                        total_sales += cost1;
                                                        totsales.setText(String.valueOf(total_sales));

                                                        Log.d(TAG, ": ");
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
                                                        seller_name=new String[n];
                                                        pay_method=new String[n];
                                                        customer_name = new String[customer_name2.size()];
                                                        total_bill = new String[total_bill2.size()];
                                                        customer_id = new String[customer_name2.size()];

                                                        Log.d(TAG, "sellers: "+seller_name2);

                                                        for (int i = 0; i < n; i++) {
                                                            seller_name[i]=seller_name2.get(i);
                                                            dateandtime[i] = dateandtime2.get(i);
                                                            pay_method[i]=pay_method2.get(i);
                                                            customer_name[i] = customer_name2.get(i);
                                                            total_bill[i] = total_bill2.get(i);
                                                            customer_id[i] = customer_id2.get(i);
                                                        }

                                                        li = findViewById(R.id.history_list);
                                                        history_bills.CustomBaseAdapter cust = new history_bills.CustomBaseAdapter(getApplicationContext(), customer_name, total_bill, dateandtime, customer_id,pay_method,seller_name);
                                                        li.setAdapter(cust);
                                                    }
                                                }
                                            });}





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
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                    progressDialog.cancel();
                                }
                            }
                        });



                    }
                    else {
                        Toast.makeText(view.getContext(), "PLEASE SELECT THE DATE", Toast.LENGTH_SHORT).show();
                    }
                }
           });






        ///DateFormat df = new SimpleDateFormat("d MMM yyyy");
       /// String date = df.format(getTime());







    }
    class CustomBaseAdapter extends BaseAdapter {
        Context context;
        String[] customer_name ;

        String[] total_bill,dateandtime,pay_method,seller_name;
        String[] customer_id;

        LayoutInflater inflater;

        public CustomBaseAdapter(Context applicationContext, String[] customer_name, String[] total_bill, String[] dateandtime,String[] customer_id,String[] pay_method,String[] seller_name) {
            this.context=applicationContext;
            this.customer_name=customer_name;
            this.pay_method=pay_method;
            this.total_bill=total_bill;
            this.dateandtime=dateandtime;
            this.customer_id=customer_id;
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
            pay.setText(pay_method[i]);
            name.setText(customer_name[i]);
            totbills.setText(total_bill[i]);
            datetime.setText(dateandtime[i]);
            sell.setText(seller_name[i]);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in=new Intent(history_bills.this,history_billspurchase.class);
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
    public static boolean isDateInBetweenIncludingEndPoints(final Date min, final Date max, final Date date){
        return !(date.before(min) || date.after(max));
    }

}
