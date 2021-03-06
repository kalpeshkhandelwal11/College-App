package com.miniproject.collegeapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miniproject.collegeapp.model.Data;

import java.text.SimpleDateFormat;
import java.util.Date;


public class OnGoing extends Fragment {
    private FirebaseAuth mAuth;
    public LinearLayoutManager linearLayoutManager;
    private FirebaseFirestore db;
    private TextView temp;
    private FirestoreRecyclerAdapter adapter;
    private String UID;
    private ProgressDialog mdialog;

    String tempo;
    private Query query;
    private String Department;
    public RecyclerView recyclerView1;

    // Required empty public constructor
    public OnGoing() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_going, container, false);
        recyclerView1 = view.findViewById(R.id.recycler_ongoing);
        temp = view.findViewById(R.id.temp);
        mdialog = new ProgressDialog(getContext());
        // Inflate the layout for this fragment;

        init();
        getNotices();
        recyclerView1.setHasFixedSize(true);
        return view;
    }


    private void init() {
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        db = FirebaseFirestore.getInstance();
    }

    private void getNotices() {
        //  DocumentReference docRef = db.collection("cities").document(Department);
        // Query query = db.collection("Notices/"+Department);
        mdialog.setMessage("Processing..");
        mdialog.show();
        db.collection("Users")
                .whereEqualTo("uid", UID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Department = document.get("Department").toString().trim();
                                // Query query = db.collection("Notices")
                                //.whereEqualTo("department",Department);
                                temp.setText(Department);
                                temp.setText(document.get("Department").toString());
                                Log.d("kalpeshDepartment", document.getId() + " => " + Department);

                            }

                            String pattern = "dd-MM-yyyy";
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                            String date1 = simpleDateFormat.format(new Date());
                            Log.d("date", "check todays date" + date1);
                            Log.d("Dep", "Department inside if" + Department);

                            query = db.collection("Notices").whereArrayContains("adepartment", Department).whereGreaterThanOrEqualTo("toDate", date1).orderBy("toDate", Query.Direction.DESCENDING);
                            FirestoreRecyclerOptions<Data> response = new FirestoreRecyclerOptions.Builder<Data>()
                                    .setQuery(query, Data.class)
                                    .build();
                            mdialog.dismiss();

                            adapter = new FirestoreRecyclerAdapter<Data, OnGoing.FriendsHolder>(response) {
                                @Override
                                public void onBindViewHolder(final OnGoing.FriendsHolder holder, final int position, Data model) {
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
                                            Intent ViewNotice1 = new Intent(getContext(), viewNotice.class);
                                            Log.d("kalpesh kh", "onClick: " + snapshot.getId());
                                            ViewNotice1.putExtra("noticeid", snapshot.getId());
                                            startActivity(ViewNotice1);
                                        }
                                    });

                                }

                                @Override
                                public OnGoing.FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                                    View view = LayoutInflater.from(group.getContext())
                                            .inflate(R.layout.item_layout, group, false);


                                    return new FriendsHolder(view);
                                }

                                @Override
                                public void onError(FirebaseFirestoreException e) {
                                    Log.e("KalpeshError", e.getMessage());
                                }
                            };
                            adapter.notifyDataSetChanged();
                            recyclerView1.setAdapter(adapter);
                            adapter.startListening();

                        } else {
                            Log.d("Dep", "Department" + Department);
                            Log.d("kalpeshfs", "Error getting documents: ", task.getException());
                        }
                    }
                });
        tempo = temp.getText().toString().trim();
        Log.d("depvalue", "getNotices: " + tempo);
        Log.d("Query", "Query: " + query);


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





 /*   @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    } */
}