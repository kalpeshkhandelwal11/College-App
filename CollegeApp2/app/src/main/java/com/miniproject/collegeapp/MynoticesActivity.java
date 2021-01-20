package com.miniproject.collegeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.miniproject.collegeapp.model.Data;

public class MynoticesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private static FirebaseFirestore mDatabase;
    private Query query;
    private LinearLayoutManager linearLayoutManager;
    String id;
    String URL;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mynotices);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recycler_viewnotice);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mDatabase = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        id = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        //  mdialog.setMessage("Processing..");
        //mdialog.show();

        query = db.collection("Notices").whereEqualTo("id", id);
        FirestoreRecyclerOptions<Data> response = new FirestoreRecyclerOptions.Builder<Data>()
                .setQuery(query, Data.class)
                .build();
        //  mdialog.dismiss();

        adapter = new FirestoreRecyclerAdapter<Data, MynoticesActivity.FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(final MynoticesActivity.FriendsHolder holder, final int position, Data model) {
                holder.title.setText("Title:- " + model.getTitle());
                holder.description.setText("Description:- " + model.getDescription());
                holder.author.setText("By:- " + model.getName());
                holder.date.setText(model.getDate());
                holder.toDate.setText("Valid till:- " + model.getToDate());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                        snapshot.getId();
                        Log.d("kalpesh fs", "onClick get id: " + snapshot.getId());
                        Intent ViewNotice1 = new Intent(getApplicationContext(), Mynoticedetail.class);
                        Log.d("kalpesh kh", "onClick: " + snapshot.getId());
                        ViewNotice1.putExtra("noticeid", snapshot.getId());
                        startActivity(ViewNotice1);
                    }
                });

            }

            @Override
            public MynoticesActivity.FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_layout, group, false);


                return new MynoticesActivity.FriendsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("KalpeshError", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    public static class FriendsHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView author;
        TextView date;
        TextView toDate;


        public FriendsHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_ongoing);
            description = itemView.findViewById(R.id.description_ongoing);
            author = itemView.findViewById(R.id.id_ongoing);
            date = itemView.findViewById(R.id.date_ongoing);
            toDate = itemView.findViewById(R.id.todate_ongoing);


        }


    }
}