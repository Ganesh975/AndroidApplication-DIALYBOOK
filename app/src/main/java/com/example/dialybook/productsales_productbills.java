package com.example.dialybook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.dialybook.history_bills.isDateInBetweenIncludingEndPoints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

public class productsales_productbills extends AppCompatActivity {

    TextView startdate,enddate;
    String endDate,startDate;
    ImageView start,end;
    ListView li;
    String[] quantity_sold,cust_name;
    String[]  sold_cost,seller_name;
    String[] datetime;
    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;

    String name,field,cost;

    int taskleft1=0,taskleft2=0,taskleft3=0;

    ArrayList<String > filtered=new ArrayList<>();

    ArrayList<String > quantity_sold2=new ArrayList<>();
    ArrayList<String > cust_name2=new ArrayList<>();
    ArrayList<String > sold_cost2=new ArrayList<>();
    ArrayList<String > datetime2=new ArrayList<>();
    ArrayList<String > seller_name2=new ArrayList<>();
    private int sdate,smonth,syear,edate,emonth,eyear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productsales_productbills);
        startdate=findViewById(R.id.startdate);
        enddate=findViewById(R.id.enddate);
        start=this.findViewById(R.id.image_start);
        end=this.findViewById(R.id.image_end);
        Button fil=this.findViewById(R.id.but_filter);

        progressDialog=new ProgressDialog(this);


        Intent in =getIntent();
        field=in.getStringExtra("field name");
        name=in.getStringExtra("product name");
        cost=in.getStringExtra("product cost");
        TextView proname=findViewById(R.id.product_name);
        TextView procost=this.findViewById(R.id.product_cost);
        proname.setText(name);
        procost.setText(cost);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                sdate = cal.get(Calendar.DATE);
                smonth = cal.get(Calendar.MONTH);
                syear = cal.get(Calendar.YEAR);
                DatePickerDialog datestart = new DatePickerDialog(productsales_productbills.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        startdate.setText(i + "-" + (i1 + 1) + "-" + i2);

                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(i, i1, i2);

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        startDate = format.format(selectedCalendar.getTime());
                    }
                }, syear, smonth, sdate); // Use the sdate, smonth, and syear variables here

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
                DatePickerDialog dateend = new DatePickerDialog(productsales_productbills.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        enddate.setText(i + "-" + (i1 + 1) + "-" + i2);

                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(i, i1, i2);

                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        endDate = format.format(selectedCalendar.getTime());
                    }
                }, eyear, emonth, edate); // Use the edate, emonth, and eyear variables here

                // Set the maximum date to prevent selecting future dates
                dateend.getDatePicker().setMaxDate(System.currentTimeMillis());

                // Show the dialog
                dateend.show();
            }
        });


        fil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onCreate:                            startdate"+startDate+"         "+endDate);

                if (startdate != null && enddate != null) {
                    progressDialog.show();
                    SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.U_LOGIN, 0);
                    String user = sharedPreferences.getString("user", null);


                    SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
                    String prtbranch = sharedPreferences1.getString("branch", null);


                    firebaseFirestore = FirebaseFirestore.getInstance();
                    CollectionReference coll = firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("stock").document(field).collection("fieldstock").document(name).collection("productbills");


                    coll.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot document = task.getResult();
                                if (!document.isEmpty()) {
                                    for (DocumentSnapshot co : document) {

                                        String datetime = co.getString("datetime");
                                        Log.d(TAG, "product date and time : "+datetime);
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

                                    Log.d(TAG, "filtered dates are : "+filtered);





                                    final AtomicInteger taskCounter = new AtomicInteger(filtered.size());
                                    taskleft1=taskCounter.get();





                                    for (String s : filtered) {
                                        DocumentReference d=coll.document(s);
                                        Log.d(TAG, "in the for loop   ");



                                        Log.d(TAG, "initially  "+taskleft1);
                                        d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot dt=task.getResult();
                                                cust_name2.add(dt.getString("customername"));
                                                quantity_sold2.add(String.valueOf(dt.getLong("quantity")));
                                                sold_cost2.add(String.valueOf(dt.getLong("cost")));
                                                datetime2.add(dt.getString("datetime"));
                                                seller_name2.add(dt.getString("seller_name"));
                                                Log.d(TAG, "in the data getting   ");
                                                Log.d(TAG, "  "+taskleft1);


                                                if( taskleft1 == 0){
                                                    Log.d(TAG, "in the end   ");


                                                    int n = datetime2.size();


                                                    datetime = new String[n];
                                                    quantity_sold=new String[n];
                                                    sold_cost = new String[n];
                                                    cust_name = new String[n];
                                                    seller_name= new String[n];


                                                    for (int i = 0; i < n; i++) {
                                                        seller_name[i]=seller_name2.get(i);
                                                        datetime[i] = datetime2.get(i);
                                                        quantity_sold[i]=quantity_sold2.get(i);
                                                        sold_cost[i] = sold_cost2.get(i);
                                                        cust_name[i] = cust_name2.get(i);

                                                    }
                                                    Log.d(TAG, "feilds are   : "+datetime2+quantity_sold2+sold_cost2+cust_name2);

                                                    li = findViewById(R.id.history_list);
                                                    productsales_productbills.CustomBaseAdapter cust = new productsales_productbills.CustomBaseAdapter(getApplicationContext(), datetime,quantity_sold, sold_cost,cust_name,seller_name);
                                                    li.setAdapter(cust);
                                                    progressDialog.cancel();

                                                }
                                            }
                                        });

                                        Log.d(TAG, "finally  "+taskleft1);
                                        taskleft1-=1;

                                    }



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
                else {
                    Toast.makeText(view.getContext(), "PLEASE SELECT THE DATE", Toast.LENGTH_SHORT).show();
                }








            }
        });

    }
    class CustomBaseAdapter extends BaseAdapter {
        Context context;

        String[] datetime,sold_cost,quantity_sold,cust_name,seller_name;

        LayoutInflater inflater;

        public CustomBaseAdapter(Context applicationContext,String[] datetime,String[] quantity_sold,String[] sold_cost,String[] cust_name,String[] seller_name) {
            this.context=applicationContext;
            this.datetime=datetime;
            this.quantity_sold=quantity_sold;
            this.sold_cost=sold_cost;
            this.cust_name=cust_name;
            this.seller_name=seller_name;
            inflater=LayoutInflater.from(applicationContext);
        }

        @Override
        public int getCount() {
            return datetime.length;
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
            view=inflater.inflate(R.layout.activity_productsales_historylist,null);
            TextView dateandtime=view.findViewById(R.id.date_time);
            TextView quantity=view.findViewById(R.id.quantity_sold);
            TextView cost=view.findViewById(R.id.sold_cost);
            TextView cust=view.findViewById(R.id.cust_name);
            TextView sell=view.findViewById(R.id.seller_name);

            sell.setText(seller_name[i]);

            cust.setText(cust_name[i]);

           dateandtime.setText(datetime[i]);
           quantity.setText(quantity_sold[i]);
           cost.setText(sold_cost[i]);

            return view;
        }


    }
}