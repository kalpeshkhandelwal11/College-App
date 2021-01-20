package com.miniproject.collegeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniproject.collegeapp.model.user;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private static FirebaseFirestore mDatabase;
    private FirestoreRecyclerAdapter<user, UserViewHolder> adapter;
    private Query query;
    private LinearLayoutManager linearLayoutManager;
    String id;
    String URL;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_user);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid();
        final CollectionReference datasRef = mDatabase.collection("Users");
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("uid", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String Department = document.get("Department").toString().trim();
                                query = datasRef.orderBy("name", Query.Direction.ASCENDING).whereEqualTo("Department", Department);
                                FirestoreRecyclerOptions<user> options = new FirestoreRecyclerOptions.Builder<user>()
                                        .setQuery(query, user.class)
                                        .build();
                                adapter = new FirestoreRecyclerAdapter<user, UserViewHolder>(options) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int i, @NonNull user data) {
                                        userViewHolder.setData(data.getName(), data.getPost());
                                        userViewHolder.itemView
                                                .setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(userViewHolder.getAdapterPosition());
                                                        snapshot.getId();
                                                        Log.d("kalpesh fs", "onClick get id: " + snapshot.getId());
                                                        Intent UserDetail = new Intent(getApplicationContext(), userdetailActivity.class);
                                                        UserDetail.putExtra("UserID", snapshot.getId());
                                                        startActivity(UserDetail);
                                                    }
                                                });
                                    }

                                    @NonNull
                                    @Override
                                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_layout, parent, false);

                                        return new UserViewHolder(view);
                                    }
                                };
                                recyclerView.setAdapter(adapter);
                                adapter.startListening();
                                // Query query = db.collection("Notices")
                                //.whereEqualTo("department",Department);

                                Log.d("kalpeshDepartment", document.getId() + " => " + Department);

                            }
                        } else {

                            Log.d("kalpeshfs", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    private class UserViewHolder extends RecyclerView.ViewHolder {
        private View view;

        UserViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setData(String name1, String post1) {
            TextView name = view.findViewById(R.id.user_name);
            TextView post = view.findViewById(R.id.user_post);
            name.setText(name1);
            post.setText(post1);
        }
    }
}