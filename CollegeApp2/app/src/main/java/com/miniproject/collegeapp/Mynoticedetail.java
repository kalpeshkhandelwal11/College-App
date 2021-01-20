package com.miniproject.collegeapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Mynoticedetail extends AppCompatActivity {
    private TextView vTitle, vAuthor, vDate, vTodate, vDescription;
    private String id, url, type, UID;
    private Button fetch, del;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mynoticedetail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vTitle = findViewById(R.id.view_title1);
        vAuthor = findViewById(R.id.view_author1);
        vDate = findViewById(R.id.view_date1);
        vTodate = findViewById(R.id.view_todate1);
        vDescription = findViewById(R.id.view_Description1);
        fetch = findViewById(R.id.fetchpdf1);
        del = findViewById(R.id.deleteNotice1);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getUid();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            id = getIntent().getExtras().get("noticeid").toString();

            Log.d("kalpesh fs", "check notice id " + id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        db.collection("Users")
                .whereEqualTo("uid", UID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                type = document.get("type").toString().trim();
                                // Query query = db.collection("Notices")
                                //.whereEqualTo("department",Department);


                            }
                            if (type.contentEquals("Student")) {
                                del.setVisibility(View.INVISIBLE);
                            }

                        } else {
                            Log.d("Failed", "get failed with ", task.getException());
                        }
                    }

                });
        db.collection("Notices").document(id)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("ViewNotice", "DocumentSnapshot data: " + document.getData());

                        vDate.setText(document.get("date").toString());
                        vTitle.setText(document.get("title").toString());
                        vAuthor.setText(document.get("name").toString());
                        vTodate.setText(document.get("toDate").toString());
                        vDescription.setText(document.get("description").toString());
                        url = document.get("pdfDoc").toString();
                        if (url.contentEquals("empty")) {
                            fetch.setVisibility(View.INVISIBLE);
                        }
                        fetch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(url));
                                startActivity(i);
                            }
                        });

                    } else {
                        Log.d("ViewNotice", "No such document");
                    }
                } else {
                    Log.d("ViewNotice", "get failed with ", task.getException());
                }
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Notices").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Del", "DocumentSnapshot successfully deleted!");
                                startActivity(new Intent(getApplicationContext(), Nav.class));
                                Toast.makeText(getApplicationContext(), "Notice deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Del", "Error deleting document", e);
                                Toast.makeText(getApplicationContext(), "Notice cannot be deleted", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });

    }
}

