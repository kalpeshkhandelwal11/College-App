package com.miniproject.collegeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miniproject.collegeapp.R;
import com.miniproject.collegeapp.attendanceModleClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class attendance_dateRVAdapter extends RecyclerView.Adapter<attendance_dateRVAdapter.myViewHolder>{

    private Context context;
    private List<String> items;

    public attendance_dateRVAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.attendance_date, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        holder.date.setText(items.get(position));

        holder.database=FirebaseDatabase.getInstance();
        holder.ref=holder.database.getReference();

        holder.list= new ArrayList<attendanceModleClass>();
        holder.attendance_userdetailsRVAdapter=new attendance_userdetailsRVAdapter(context,holder.list);
        holder.rv.setAdapter(holder.attendance_userdetailsRVAdapter);
        holder.rv.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));

        holder.ref.child("Attendance").child(items.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot auth:dataSnapshot.getChildren()){
                        try {
                            String id, cintime, couttime, cinloc, coutloc, w_hrs, inlon, inlat, outlon, outlat;
                            id = auth.child("name").getValue().toString();
                            cintime = auth.child("IN").child("Time").getValue().toString();
                            couttime = auth.child("OUT").child("Time").getValue().toString();
                            cinloc = auth.child("IN").child("Location").getValue().toString();
                            coutloc = auth.child("OUT").child("Location").getValue().toString();
                            w_hrs = auth.child("Working Hours").getValue().toString();
                            inlon = auth.child("IN").child("Longitude").getValue().toString();
                            inlat = auth.child("IN").child("Latitude").getValue().toString();
                            outlon = auth.child("OUT").child("Longitude").getValue().toString();
                            outlat = auth.child("OUT").child("Latitude").getValue().toString();

                            attendanceModleClass p = new attendanceModleClass(items.get(position), id, cintime, couttime, cinloc, coutloc, w_hrs, inlon, inlat, outlon, outlat);
                            holder.list.add(p);
                            holder.attendance_userdetailsRVAdapter.notifyDataSetChanged();
                        }
                        catch (Exception e){}

                    }
                }
                holder.emp.setText("Emp.: "+holder.list.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.rv.setVisibility(View.GONE);
        holder.arrow.setRotation(0);
        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(holder.arrow.getRotation()).equals("0.0")) {
                    holder.arrow.setRotation(90);
                    holder.rv.setVisibility(View.VISIBLE);
                }
                else {
                    holder.arrow.setRotation(0);
                    holder.rv.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private TextView date,emp;
        private RecyclerView rv;
        private ImageView arrow;

        private List<attendanceModleClass> list;
        private attendance_userdetailsRVAdapter attendance_userdetailsRVAdapter;

        private FirebaseDatabase database;
        private DatabaseReference ref;

        public myViewHolder(View view) {
            super(view);
            date=view.findViewById(R.id.date);
            emp=view.findViewById(R.id.emp);
            rv=view.findViewById(R.id.user_detailsrv);
            arrow=view.findViewById(R.id.arrow2);
        }
    }
}
