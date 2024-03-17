package com.example.dialybook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class history_billspurchase extends AppCompatActivity {

    ListView li;

    Boolean lock1=true,lock2=true,lock3=true;
    String[] product_name;
    String[] field_name;
    String[] product_quantity;
    String[] product_cost;
    String[] actual__cost;
    String[] quantity_cost;


    ArrayList<String > product_name2=new ArrayList<>();
    ArrayList<String > field_name2=new ArrayList<>();
    ArrayList<String > product_quantity2=new ArrayList<>();
    ArrayList<String > product_cost2=new ArrayList<>();
    ArrayList<DocumentReference > actual_cost2=new ArrayList<>();
    ArrayList<String > actual_cost3=new ArrayList<>();
    ArrayList<String > quantity_cost2=new ArrayList<>();


    FirebaseFirestore firebaseFirestore;


    CountDownTimer countDownTimer;
    ProgressDialog progressDialog;
    int tasksLeft1,tasksLeft2,tasksLeft3;
    int lock=0;

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_billspurchase);
        TextView custname=this.findViewById(R.id.customer_name);
        TextView totalbill=this.findViewById(R.id.total_bill);
        TextView dat_time=this.findViewById(R.id.date_time);
        TextView pay=this.findViewById(R.id.payement_method);
        TextView sell=this.findViewById(R.id.seller_name);
        Intent in=getIntent();
        String name=in.getStringExtra("customer name");
        String totbill=in.getStringExtra("total bill");
        String date=in.getStringExtra("date time");
        id=in.getStringExtra("customer id");
        String pay_method=in.getStringExtra("payement method");
        String seller_name=in.getStringExtra("seller_name");
        sell.setText(seller_name);
        pay.setText(pay_method);
        custname.setText(name);
        totalbill.setText(totbill);
        dat_time.setText(date);

        li=findViewById(R.id.product_list);



        progressDialog=new ProgressDialog(this);


        progressDialog.show();
        //while(getcollectionlist());
        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
        String user=sharedPreferences.getString("user",null);


        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);


        Log.d(TAG, "getting the data    started: "+prtbranch+"  "+user+"   ");
        firebaseFirestore= FirebaseFirestore.getInstance();
        DocumentReference coll=firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("bills").document(id);

        CollectionReference collection1=firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("stock");



        coll.collection("fields").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    progressDialog.show();
                    QuerySnapshot document = task.getResult();
                    if (!document.isEmpty()) {
                        progressDialog.show();
                        final AtomicInteger taskCounter1 = new AtomicInteger(document.size());

                        tasksLeft1 = taskCounter1.get();
                        for (DocumentSnapshot co : document) {
                            field_name2.add(co.getString("field_name"));
                            Log.d(TAG, "fields are     : "+co.getId());

                            coll.collection("fields").document(co.getId()).collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot document = task.getResult();
                                        if (!document.isEmpty()) {
                                            progressDialog.show();
                                            final AtomicInteger taskCounter2 = new AtomicInteger(document.size());
                                            tasksLeft2 = taskCounter2.get();
                                            for (DocumentSnapshot co1 : document) {
                                                product_name2.add(co1.getString("product_name"));
                                                product_cost2.add(co1.getString("product_cost"));
                                                product_quantity2.add(co1.getString("product_quantity"));

                                                int quantity = Integer.parseInt(co1.getString("product_quantity"));


                                                Log.d(TAG, "items are     : " + co1.getString("product_name"));
                                                Log.d(TAG, "cost are     : " + co1.getString("product_cost"));
                                                Log.d(TAG, "quantity are     : " + co1.getString("product_quantity"));
                                                DocumentReference do1 = collection1.document(co.getString("field_name")).collection("fieldstock").document(co1.getString("product_name"));
                                                actual_cost2.add(do1);
                                                progressDialog.show();

                                                final AtomicInteger taskCounter3 = new AtomicInteger(actual_cost2.size());
                                                tasksLeft3=taskCounter3.get();
                                                Log.d(TAG, "onComplete: "+tasksLeft3);


                                                do1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        DocumentSnapshot document = task.getResult();
                                                        actual_cost3.add(document.getString("itemcost"));





                                                        Log.d(TAG, "Actual product cost:      " + document.getString("itemcost"));
                                                        Log.d(TAG, "task are    : "+tasksLeft1+"   "+tasksLeft2+"   "+tasksLeft3);
                                                        Log.d(TAG, "onComplete: "+tasksLeft3);
                                                        int cost = Integer.parseInt(document.getString("itemcost"));
                                                        int totalCost = cost * quantity;
                                                        String totalCostString = Integer.toString(totalCost);
                                                        quantity_cost2.add(totalCostString);
                                                        tasksLeft3 = tasksLeft3-1;
                                                        Log.d(TAG, "task are    : "+tasksLeft1+"   "+tasksLeft2+"   "+tasksLeft3);
                                                        // If all tasks are completed, dismiss the ProgressDialog
                                                        if (tasksLeft2 == 0 && tasksLeft1 == 0 && tasksLeft3 == 0) {


                                                            int n = field_name2.size();
                                                            product_name=new String[n];
                                                            field_name=new String[n];
                                                            actual__cost=new String[n];
                                                            product_quantity=new String[n];
                                                            product_cost=new String[n];
                                                            quantity_cost=new String[n];

                                                            for (int i = 0; i < n; i++) {
                                                                product_cost[i]=product_cost2.get(i);
                                                                field_name[i]=field_name2.get(i);
                                                                product_name[i]=product_name2.get(i);
                                                                actual__cost[i]=actual_cost3.get(i);
                                                                product_quantity[i]=product_quantity2.get(i);
                                                                quantity_cost[i]=quantity_cost2.get(i);
                                                            }
                                                            Log.d(TAG, "field name : "+field_name2);
                                                            Log.d(TAG, "product name : "+product_name2);
                                                            Log.d(TAG, "actual cost: "+actual_cost3);
                                                            Log.d(TAG, "product quantity : "+product_quantity2);
                                                            Log.d(TAG, "product cost : "+product_cost2);
                                                            Log.d(TAG, "Quantity Cost : "+quantity_cost2);
                                                            CustomBaseAdapter cust=new CustomBaseAdapter(getApplicationContext(),field_name,product_name,product_cost,product_quantity,actual__cost,quantity_cost);
                                                            li.setAdapter(cust);
                                                            progressDialog.cancel();
                                                        }




                                                    }
                                                });



                                                tasksLeft2 = taskCounter2.decrementAndGet();

                                            }
                                        }
                                    }

                                }
                            });


                            tasksLeft1 = taskCounter1.decrementAndGet();

                        }

                    }
                }

            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

            }
        });






        /***

        progressDialog.show();
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                coll.collection("fields").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            QuerySnapshot document = task.getResult();
                            if (!document.isEmpty()) {
                                for (DocumentSnapshot co : document) {
                                    field_name2.add(co.getId());

                                    Thread t6=new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                    coll.collection("fields").document(co.getId()).
                                            collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        QuerySnapshot document = task.getResult();
                                                        if (!document.isEmpty()) {
                                                            for (DocumentSnapshot co1 : document) {
                                                                Log.d(TAG, "opro:     " + product_name2);
                                                                product_name2.add(co1.getString("product_name"));
                                                                String product = co1.getString("product_name");

                                                                Thread t5=new Thread(new Runnable() {
                                                                    @Override
                                                                    public void run() {

                                                                collection1.document(co1.getId()).collection("fieldstock").document(co.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                            DocumentSnapshot document = task.getResult();
                                                                            product_cost2.add(document.getString("itemcost"));

                                                                            Log.d(TAG, "product cost    : "+document.getString("itemcost"));

                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.d(TAG, "onFailure:             failed");
                                                                    }
                                                                });

                                                                    }
                                                                });
                                                                t5.start();

                                                                try {
                                                                    Thread.sleep(5000);
                                                                    t5.join();
                                                                } catch (InterruptedException e) {
                                                                    throw new RuntimeException(e);
                                                                }

                                                                product_quantity2.add(co1.getString("product_quantity"));
                                                                quantity_cost2.add(co1.getString("product_cost"));
                                                                Log.d(TAG, "product name   : "+product_name2);
                                                                Log.d(TAG, "product cost    : "+product_cost2);
                                                                Log.d(TAG, "quantity cost    : "+quantity_cost2);
                                                                Log.d(TAG, "product quantity     : "+product_quantity2);


                                                                int n = field_name2.size();


                                                                product_name = new String[n];
                                                                product_cost = new String[n];
                                                                quantity_cost = new String[n];
                                                                product_quantity = new String[n];

                                                                for (int i = 0; i < n; i++) {

                                                                    product_name[i] = product_name2.get(i);
                                                                    product_cost[i] = product_cost2.get(i);
                                                                    quantity_cost[i] = quantity_cost2.get(i);
                                                                    product_quantity[i] = product_quantity2.get(i);
                                                                }

                                                            }

                                                        }
                                                    }
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {







                                                }
                                            });


                                        }
                                    });
                                    t6.start();

                                    try {
                                        Thread.sleep(5000);
                                        t6.join();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }







                                }
                            }
                        }

                    }
                }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        progressDialog.cancel();
                        int n = field_name2.size();

                        field_name = new String[n];
                        for(int i=0; i<n; i++) {
                            field_name[i] = field_name2.get(i);
                        }
                        Log.d(TAG, "field name   : "+field_name2);



                    }
                });



            }
        });
        t1.start();
        try {



            Thread.sleep(1000);


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }




        Thread t3=new Thread(new Runnable() {
            @Override
            public void run() {


                Log.d(TAG, "product name   : "+product_name2);
                Log.d(TAG, "product cost    : "+product_cost2);
                Log.d(TAG, "quantity cost    : "+quantity_cost2);
                Log.d(TAG, "product quantity     : "+product_quantity2);




            }
        });

        t3.start();
        try {
                t3.sleep(5000);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

         ***/




        progressDialog.cancel();


    }



    public boolean getcollectionlist() {


        SharedPreferences sharedPreferences=getSharedPreferences(MainActivity.U_LOGIN,0);
        String user=sharedPreferences.getString("user",null);


        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
        String prtbranch = sharedPreferences1.getString("branch", null);


        Log.d(TAG, "getting the data    started: ");
        firebaseFirestore= FirebaseFirestore.getInstance();
        DocumentReference coll=firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("bills").document(id);

        CollectionReference collection1=firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("stock");



        progressDialog.show();

                coll.collection("fields").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            QuerySnapshot document = task.getResult();
                            if (!document.isEmpty()) {
                                for (DocumentSnapshot co : document) {
                                    field_name2.add(co.getId());

                                    Log.d(TAG, "retriving the product details   started: ");
                                            coll.collection("fields").document(co.getId()).
                                                    collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                QuerySnapshot document = task.getResult();
                                                                if (!document.isEmpty()) {
                                                                    for (DocumentSnapshot co1 : document) {
                                                                        Log.d(TAG, "opro:     " + product_name2);
                                                                        product_name2.add(co1.getString("product_name"));
                                                                        String product = co1.getString("product_name");



                                                                                DocumentReference do1=collection1.document(co.getId()).collection("fieldstock").document(co1.getId());

                                                                                do1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                        Log.d(TAG, "product cost retreiving started   ");
                                                                                        DocumentSnapshot document = task.getResult();
                                                                                        product_cost2.add(document.getString("itemcost"));

                                                                                        Log.d(TAG, "product cost  : "+product_cost2);
                                                                                        Log.d(TAG, "product cost:      "+document.getString("itemcost"));


                                                                                        int n = field_name2.size();


                                                                                        product_cost = new String[n];

                                                                                        for (int i = 0; i < n; i++) {
                                                                                            product_cost[i]=product_cost2.get(i);

                                                                                        }

                                                                                        history_billspurchase.CustomBaseAdapter cust=new history_billspurchase.CustomBaseAdapter(getApplicationContext(),field_name,product_name,product_cost,product_quantity,actual__cost,quantity_cost);
                                                                                        li.setAdapter(cust);

                                                                                    }
                                                                                }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {



                                                                                        Log.d(TAG, "onFailure:             failed");
                                                                                    }
                                                                                });





                                                                        try {
                                                                            Thread.sleep(5000);
                                                                        }catch (Exception e){

                                                                        }


                                                                        Log.d(TAG, "      lock 1 completed");





                                                                        /****
                                                                        progressDialog.cancel();
                                                                        Log.d(TAG, "getproductcost:      end");


                                                                        Log.d(TAG, "before   : "+getproductcost(do1));

                                                                        try {
                                                                            task.wait(5000);
                                                                        } catch (
                                                                                InterruptedException e) {
                                                                            throw new RuntimeException(e);
                                                                        }
                                                                        while (getproductcost(do1));


                                                                        Log.d(TAG, "after   : "+getproductcost(do1));***/



                                                                        product_quantity2.add(co1.getString("product_quantity"));
                                                                        quantity_cost2.add(co1.getString("product_cost"));
                                                                        Log.d(TAG, "product name   : "+product_name2);

                                                                        Log.d(TAG, "quantity cost    : "+quantity_cost2);
                                                                        Log.d(TAG, "product quantity     : "+product_quantity2);


                                                                        int n = field_name2.size();


                                                                        product_name = new String[n];

                                                                        quantity_cost = new String[n];
                                                                        product_quantity = new String[n];

                                                                        for (int i = 0; i < n; i++) {

                                                                            product_name[i] = product_name2.get(i);
                                                                            quantity_cost[i] = quantity_cost2.get(i);
                                                                            product_quantity[i] = product_quantity2.get(i);
                                                                        }

                                                                    }

                                                                }
                                                            }
                                                        }
                                                    }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                                        }
                                                    });







                                    try {
                                        Thread.sleep(5000);
                                    }catch (Exception e){

                                    }



                                    Log.d(TAG, "product details completed   : ");


                                }
                            }
                        }

                    }
                }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        progressDialog.cancel();
                        int n = field_name2.size();

                        field_name = new String[n];
                        for(int i=0; i<n; i++) {
                            field_name[i] = field_name2.get(i);
                        }
                        Log.d(TAG, "field name   : "+field_name2);


                        lock3=false;



                    }
                });









        try {
            Thread.sleep(5000);
        }catch (Exception e){

        }


        Log.d(TAG, "retriving the details completed lock 3 closed  ");

      /***  Log.d(TAG, "getcollectionlist:      completed");
        for(String field:field_name2){
            Log.d(TAG, "getcollectionlist:     "+field);
            for(String pro:product_name2){

                Log.d(TAG, "getcollectionlist:        "+pro);
                collection1.document(field).collection("fieldstock").document(pro).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        product_cost2.add(documentSnapshot.getString("itemcost"));

                        Log.d(TAG, "onSuccess:                successful");
                    }
                });
                Log.d(TAG, "product cost    : "+product_cost2);
            }
        }


        Log.d(TAG, "THE DETAILS ARE     ");
        Log.d(TAG, "product name:    "+product_name2);
        Log.d(TAG, "product cost :    "+product_cost2);
        Log.d(TAG, "field name :   "+field_name2);
        Log.d(TAG, "quantity cost  : "+quantity_cost2);
        Log.d(TAG, "product quantity   : "+product_quantity2);***/





        return false;


    }
/***
    public boolean getproductcost(DocumentReference do1){


        Log.d(TAG, "getproductcost:      start");

        Log.d(TAG, "getproductcost:        "+do1);

        progressDialog.show();
                do1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        Log.d(TAG, "prokjskdf                     sdhffsdh: ");
                        DocumentSnapshot document = task.getResult();
                        product_cost2.add(document.getString("itemcost"));

                        Log.d(TAG, "product cost:      "+document.getString("itemcost"));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG, "onFailure:             failed");
                    }
                });


        progressDialog.cancel();
        Log.d(TAG, "getproductcost:      end");
        return false;


    }***/



    class CustomBaseAdapter extends BaseAdapter {
        Context context;
        String[] field_name,product_name,product_cost,product_quantity,quantity_cost,actual_cost;
        LayoutInflater inflater;

        public CustomBaseAdapter(Context applicationContext, String[] field_name, String[] product_name, String[] product_cost,String[] product_quantity,String[] actual_cost,String[] quantity_cost) {
            this.context=applicationContext;
            this.field_name=field_name;
            this.product_name=product_name;
            this.product_cost=product_cost;
            this.product_quantity=product_quantity;
            this.quantity_cost=quantity_cost;
            this.actual_cost=actual_cost;
            inflater=LayoutInflater.from(applicationContext);
        }

        @Override
        public int getCount() {
            return field_name.length;
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
            view=inflater.inflate(R.layout.history_billspurchase_list,null);
            TextView name=view.findViewById(R.id.field_name);
            TextView product=view.findViewById(R.id.product_name);

            TextView product_quantity1=view.findViewById(R.id.product_quantity);
            TextView actual=view.findViewById(R.id.actual_cost);
            TextView salecost=view.findViewById(R.id.sale_cost);
            name.setText(field_name[i]);
            product.setText(product_name[i]);

            product_quantity1.setText(product_quantity[i]);
            salecost.setText(quantity_cost[i]+" / "+product_cost[i]);
            actual.setText(actual_cost[i]);
            return view;
        }


    }

        }

