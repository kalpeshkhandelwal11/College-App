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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class userdetailActivity extends AppCompatActivity {
    private TextView vName, vEmail, vPhone, vDepartment, vPost;
    private ImageView imageView;
    String id;
    String URL;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        vName = findViewById(R.id.name);
        vEmail = findViewById(R.id.email);
        vPhone = findViewById(R.id.phone);
        imageView = findViewById(R.id.UserPhoto);
        vDepartment = findViewById(R.id.department);
        vPost = findViewById(R.id.post);
        mAuth = FirebaseAuth.getInstance();
        vEmail.setText(mAuth.getCurrentUser().getEmail());
        id = getIntent().getExtras().get("UserID").toString();
        String url = "https://www.gstatic.com/webp/gallery3/1.png";

        Log.d("kalpesh fs", "check User id " + id);
        try {
            db = FirebaseFirestore.getInstance();
            db.collection("Users")
                    .document(id)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String TAG = "kalpesh fs";
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            vName.setText(document.get("name").toString().trim());
                            vPost.setText(document.get("type").toString().trim());
                            vDepartment.setText(document.get("Department").toString().trim());
                            try {
                                URL = document.get("profile").toString();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "onComplete: " + URL);
                            try {
                                Glide.with(getApplicationContext()).load(URL).into(imageView);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
