package com.example.dialybook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class sell_billitems extends AppCompatActivity {

     ArrayList<String> product_name1=new ArrayList<String>();
    ArrayList<Integer> quantity1=new ArrayList<Integer>();
    ArrayList<Integer> cost1=new ArrayList<Integer>();
    ArrayList<Integer> field1=new ArrayList<Integer>();

    String seller_name;

    EditText total_cost;
    String custname="";
    private  int totalcost;



    ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    ListView li;

    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sell_billitems);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Button cancelbutton=this.findViewById(R.id.but_cancelbill);
        TextView cust_name=this.findViewById(R.id.customer_name);

        progressDialog=new ProgressDialog(this);



        radioGroup = (RadioGroup)findViewById(R.id.Radio_payement);

        // Uncheck or reset the radio buttons initially
        radioGroup.clearCheck();

        // Add the Listener to the RadioGroup
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override

                    // The flow will come here when
                    // any of the radio buttons in the radioGroup
                    // has been clicked

                    // Check which radio button has been clicked
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId)
                    {

                        // Get the selected Radio Button
                        RadioButton
                                radioButton
                                = (RadioButton)group
                                .findViewById(checkedId);
                    }
                });








        cancelbutton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Intent in=new Intent(sell_billitems.this,MainActivity.class);
        product_name1.clear();
        quantity1.clear();
        cost1.clear();
        sell_additem.cust_name="";
        in.putExtra("branch name",MainActivity.user_branch);
        startActivity(in);
            sell_additem.items_clear();
            finish();

        }
        });

        li=findViewById(R.id.purchase_list);
        Intent in=getIntent();
        Bundle b=this.getIntent().getExtras();

         this.product_name1 = (ArrayList<String>) b.getSerializable("items list");
        this.quantity1= (ArrayList<Integer>) b.getSerializable("items quantity");
        this.cost1= (ArrayList<Integer>) b.getSerializable("items cost");
        this.field1=(ArrayList<Integer>) b.getSerializable("item field");
        this.custname=in.getStringExtra("customer name");
        cust_name.setText(custname);
        totalcost = 0;
        for(int i = 0; i < cost1.size(); i++)
            totalcost += cost1.get(i);
        total_cost=findViewById(R.id.total_cost);
        total_cost.setText(String.valueOf(totalcost));

        Button billadditems=this.findViewById(R.id.but_billadditem);
        billadditems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bsend=new Bundle();
                bsend.putSerializable("items list", product_name1);
                bsend.putSerializable("items cost",cost1);
                bsend.putSerializable("items quantity",quantity1);
                Intent in=new Intent(sell_billitems.this,sell_additem.class);
                in.putExtras(bsend);
                startActivity(in);
            }
        });

        sell_billitems.CustomBaseAdapter cust=new sell_billitems.CustomBaseAdapter(getApplicationContext(),product_name1,quantity1,cost1);
        li.setAdapter(cust);
        total_cost.setText(String.valueOf(totalcost));




        Button billdone=this.findViewById(R.id.but_billdone);
        billdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(sell_billitems.this,
                                    "No answer has been selected",
                                    Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    progressDialog.show();

                    RadioButton radioButton
                            = (RadioButton) radioGroup
                            .findViewById(selectedId);

                    // Now display the value of selected item
                    // by the Toast message
                    Toast.makeText(sell_billitems.this,
                                    radioButton.getText(),
                                    Toast.LENGTH_SHORT)
                            .show();

                    Log.d(TAG, "product   : "+product_name1+"    "+field1);


                    Toast.makeText(sell_billitems.this, "PURCHASE DONE SUCCESSFULLY ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(sell_billitems.this, MainActivity.class);


                    SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.U_LOGIN, 0);
                    String user = sharedPreferences.getString("user", null);

                    SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.pbranch, 0);
                    String prtbranch = sharedPreferences1.getString("branch", null);


                    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                    String date = df.format(Calendar.getInstance().getTime());

                    firebaseFirestore = FirebaseFirestore.getInstance();

                    SharedPreferences sharedPreferences2=getSharedPreferences(MainActivity.U_LOGIN,0);
                    boolean haslogedin=sharedPreferences2.getBoolean("hasUserLogIn",false);
                    boolean empLogIn=sharedPreferences2.getBoolean("hasEmpLogIn",false);

                    if(haslogedin){
                        seller_name="owner";

                    }else if(empLogIn) {
                        seller_name=sharedPreferences2.getString("employee",null);
                    }

                    DocumentReference coll = firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("bills").document(custname + date);
                    coll.set(new customermodel(custname, date, (String) radioButton.getText(), totalcost,seller_name));

                    CollectionReference coo=firebaseFirestore.collection("users").document(user).collection("branches").document(prtbranch).collection("stock");



                    Log.d(TAG, "seller          : "+seller_name);
                    progressDialog.show();
                    for (int i = 0; i < product_name1.size(); i++) {
                        progressDialog.show();
                        coll.collection("fields").document(String.valueOf(String.valueOf(i)+" "+field1.get(i))).set(new productfield_model(String.valueOf(field1.get(i))));
                        coll.collection("fields").document(String.valueOf(String.valueOf(i)+" "+field1.get(i))).collection("items").document(String.valueOf(i)+" "+product_name1.get(i)).set(new productmodel(product_name1.get(i), String.valueOf(quantity1.get(i)), String.valueOf(cost1.get(i))));
                        int Stockleft=Integer.parseInt(String.valueOf(quantity1.get(i)));

                        Log.d(TAG, "quantity selected    : "+Stockleft);

                        DocumentReference s= coo.document(String.valueOf(field1.get(i))).collection("fieldstock").document(product_name1.get(i));

                        s.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                progressDialog.show();
                                DocumentSnapshot document = task.getResult();

                                int sl=Integer.parseInt(document.getString("stockleft"));
                                Log.d(TAG, "actual quantity of that product     :    "+document.getString("itemname")+"    "+sl);

                                sl=sl-Stockleft;

                                Map<String, Object> updates = new HashMap<>();
                                updates.put("stockleft",String.valueOf(sl));

                                s.update(updates);
                                progressDialog.cancel();
                            }
                        });
                        s.collection("productbills").document(custname+date).set(new productbillsmodel(date,custname,quantity1.get(i),cost1.get(i),seller_name));


                    }

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    /***
                     Intent in= new Intent(sell_billitems.this, MainActivity.class);
                     product_name1.clear();
                     quantity1.clear();
                     cost1.clear();
                     sell_additem.cust_name="";
                     in.putExtra("branch name",MainActivity.user_branch);
                     startActivity(in);
                     sell_additem.items_clear();
                     finish();***/
                    sell_additem.cust_name = "";

                }
            }
        });

    }


    class CustomBaseAdapter extends BaseAdapter {
        Context context;
        ArrayList<String> product_name2 =new ArrayList<>();
        ArrayList<Integer> quantity2=new ArrayList<>();
        ArrayList<Integer> cost2=new ArrayList<>();

        LayoutInflater inflater;

        public CustomBaseAdapter(Context applicationContext,  ArrayList<String> product_name, ArrayList<Integer> quantity, ArrayList<Integer> cost) {

            this.context=applicationContext;
            this.product_name2=product_name;
            this.quantity2=quantity;
            this.cost2=cost;
            inflater=LayoutInflater.from(applicationContext);
        }

        @Override
        public int getCount() {
            return product_name2.size();
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
            view=inflater.inflate(R.layout.billitems_list,null);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            TextView name=view.findViewById(R.id.product_name);
            TextView quantity=view.findViewById(R.id.product_quantity);
            EditText cost=view.findViewById(R.id.quantity_cost);

            cost.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {


                }

                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    if(!(cost.getText().toString()).equals("")) {
                        try {
                            int tot = Integer.parseInt(s.toString());
                            totalcost -= cost2.get(i);
                            cost2.set(i, tot);
                            sell_additem.cost2.set(i,tot);
                            totalcost += tot;
                            total_cost.setText(String.valueOf(totalcost));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        totalcost -= cost2.get(i);
                        cost2.set(i, 0);
                        sell_additem.cost2.set(i,0);
                        totalcost += 0;
                        cost.setHint(String.valueOf(0));
                        total_cost.setText(String.valueOf(totalcost));
                        Toast.makeText(context, "enter cost of "+product_name2.get(i), Toast.LENGTH_SHORT).show();
                    }

                }
            });

            Button ac=view.findViewById(R.id.but_deleteitem);
            ac.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(sell_billitems.this, "THE ITEM "+product_name2.get(i)+" WAS REMOVED FROM CART ", Toast.LENGTH_SHORT).show();
                    sell_additem.delete_item(i);
                    product_name2.remove(i);
                    quantity2.remove(i);
                    totalcost-=cost2.get(i);
                    cost2.remove(i);
                    total_cost.setText(String.valueOf(totalcost));
                    notifyDataSetChanged();
                }
            });
            name.setText(product_name2.get(i));
            quantity.setText(String.valueOf(quantity2.get(i)));
            cost.setText(String.valueOf(cost2.get(i)));
            return view;
        }

    }
    public void onBackPressed(){
        Toast.makeText(this, "PLEASE CONFIRM THE TRANSACTION", Toast.LENGTH_SHORT).show();
    }
}