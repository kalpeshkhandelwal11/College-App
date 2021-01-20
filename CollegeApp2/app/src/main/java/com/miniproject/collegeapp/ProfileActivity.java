package com.miniproject.collegeapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity {
    private TextView vName, vEmail, vPhone, vDepartment, vPost;
    private ImageView imageView;
    String id;
    String URL;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        vName = findViewById(R.id.name1);
        vEmail = findViewById(R.id.email1);
        vPhone = findViewById(R.id.phone1);
        imageView = findViewById(R.id.UserPhoto1);
        vDepartment = findViewById(R.id.department1);
        vPost = findViewById(R.id.post1);
        mAuth = FirebaseAuth.getInstance();
        vEmail.setText(mAuth.getCurrentUser().getEmail());
        id = mAuth.getCurrentUser().getUid();
        try {
            db = FirebaseFirestore.getInstance();
            db.collection("Users")
                    .whereEqualTo("uid", id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    vName.setText(document.get("name").toString().trim());
                                    vPost.setText(document.get("type").toString().trim());
                                    vDepartment.setText(document.get("Department").toString().trim());
                                    vPhone.setText(document.get("phone").toString());
                                    try {
                                        URL = document.get("profile").toString();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("fs", "onComplete: " + URL);
                                    try {
                                        Glide.with(getApplicationContext()).load(URL).into(imageView);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                Log.d("Failed", "get failed with ", task.getException());
                            }
                        }

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
