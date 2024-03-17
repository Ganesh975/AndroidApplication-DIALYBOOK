package com.example.dialybook;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dialybook.databinding.ActivityMyProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class my_profile extends AppCompatActivity {
    ArrayList<String> branch_name1 = new ArrayList<>();
    ArrayList<String> branch_type1 = new ArrayList<>();
    ListView list;
    ActivityMyProfileBinding binding;
    Uri image;
    StorageReference st;
    private final int gallery_req_code=1000;

    SharedPreferences sharedPreferences;
    String userimage1;


    FirebaseAuth fauth;


    String branch;

    String[] branch_name;
    String[] branch_type;

    ProgressDialog progressDialog;

    ImageView user_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       sharedPreferences = getSharedPreferences(MainActivity.pbranch, 0);
       userimage1 = sharedPreferences.getString("user", null);
       branch=sharedPreferences.getString("branch",null);


        progressDialog=new ProgressDialog(this);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference coll=firestore.collection("users").document(userimage1);


        TextView prbranch=this.findViewById(R.id.present_branch);
        Button select_image=findViewById(R.id.button1);
        prbranch.setText(MainActivity.user_branch);


        coll.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d(TAG, "                  "+branch);


                        binding.userFullname.setText(document.getString("fullname"));
                        binding.username.setText(document.getString("username"));
                        binding.userPresentBranch5.setText(branch);
                        binding.userMobile.setText(document.getString("mobile"));
                        binding.userEmail.setText(document.getString("email"));

                        coll.collection("branches").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                                QuerySnapshot collection=task.getResult();

                                for (DocumentSnapshot collect:collection){
                                    Toast.makeText(my_profile.this, "getting branch data", Toast.LENGTH_SHORT).show();
                                    branch_name1.add(collect.getId());
                                    branch_type1.add(collect.getString("branchtype"));



                                }

                                int n = branch_name1.size();

                                Log.d(TAG, "onCreate:                  "+n);
                                branch_name = new String[branch_name1.size()];
                                branch_type = new String[branch_type1.size()];
                                for(int i=0; i<n; i++) {
                                    branch_name[i] = branch_name1.get(i);
                                    branch_type[i]=branch_type1.get(i);
                                }




                                list=findViewById(R.id.branch_list);

                                CustomBaseAdapter cust=new CustomBaseAdapter(getApplicationContext(),branch_name,branch_type);
                                list.setAdapter(cust);
                                user_image=findViewById(R.id.userimage);




                            }
                        });


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });




        progressDialog.show();
        st= FirebaseStorage.getInstance().getReference("images/"+userimage1+".jpg");

        try {
            File localfile= File.createTempFile("tempfile",".jpg");
            st.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Bitmap bitmap= BitmapFactory.decodeFile(localfile.getAbsolutePath());
                    binding.userimage.setImageBitmap(bitmap);
                    progressDialog.cancel();
                    Toast.makeText(my_profile.this, "PROFILE RETRIVED", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    progressDialog.cancel();
                    Toast.makeText(my_profile.this, "no image uploaded ", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (IOException e) {
            progressDialog.cancel();
            throw new RuntimeException(e);
        }





        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent igall=new Intent(Intent.ACTION_PICK);
                igall.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(igall,gallery_req_code);






            }
        });


    }

    protected void onActivityResult(int req,int res, Intent data) {
        super.onActivityResult(req,res,data);
        if(res==RESULT_OK){
            if(req==gallery_req_code){
                image=data.getData();
                user_image.setImageURI(data.getData());

                uploadimage();
            }
        }
    }

    private void uploadimage() {

        progressDialog.show();
        st.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                binding.userimage.setImageURI(image);
                progressDialog.cancel();

                Toast.makeText(my_profile.this, "PROFILE SUCCESSFULLY UPDATED", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.cancel();
                Toast.makeText(my_profile.this, "NOT UPDATED", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
class CustomBaseAdapter extends BaseAdapter {
    Context context;
    String branchname[];
    String branchtype[];
    LayoutInflater inflater;
    public CustomBaseAdapter(Context c,String [] branch_namelist,String [] branch_typelist){
        this.context=c;
        this.branchname=branch_namelist;
        this.branchtype=branch_typelist;
        inflater=LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return branchname.length;
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
        view=inflater.inflate(R.layout.activity_custom_branch_listview,null);
        TextView brchname=view.findViewById(R.id.branch_name);
        TextView brchtype=view.findViewById(R.id.branch_type);
        brchname.setText(branchname[i]);
        brchtype.setText(branchtype[i]);
        return view;
    }
}
