package com.miniproject.collegeapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.miniproject.collegeapp.R;
import com.miniproject.collegeapp.attendanceModleClass;
import com.miniproject.collegeapp.user_location;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class attendance_userdetailsRVAdapter extends RecyclerView.Adapter<attendance_userdetailsRVAdapter.myViewHolder>{

    private Context context;
    private List<attendanceModleClass> items;

    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth auth=FirebaseAuth.getInstance();

    public attendance_userdetailsRVAdapter(Context context, List<attendanceModleClass> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.attendance_user_details, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        database=FirebaseDatabase.getInstance();
        ref=database.getReference();
//
//        ref.child("Users").child(items.get(position).getName()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    holder.name.setText(dataSnapshot.child("username").getValue().toString()+
//                            " ("+dataSnapshot.child("role").getValue().toString()+")");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        holder.name.setText(items.get(position).getName());

        holder.hrs.setText("Hours : "+items.get(position).getWhrs());

        holder.cinTime.setText("Time : "+items.get(position).getCinTime());
        holder.cinloc.setText(items.get(position).getCinLocation());

        holder.coutTime.setText("Time : "+items.get(position).getCoutTime());
        holder.coutloc.setText(items.get(position).getCoutLocation());


        holder.lv.setVisibility(View.GONE);
        holder.arrow.setRotation(0);
        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(holder.arrow.getRotation()).equals("0.0")) {
                    holder.arrow.setRotation(90);
                    holder.lv.setVisibility(View.VISIBLE);
                }
                else {
                    holder.arrow.setRotation(0);
                    holder.lv.setVisibility(View.GONE);
                }
            }
        });

        if(items.get(position).getOutlongitude().equals("") || items.get(position).getOutlatitude().equals("")){
            holder.outlocation.setVisibility(View.INVISIBLE);
        }

        if(items.get(position).getInlongitude().equals("") || items.get(position).getInlatitude().equals("")){
            holder.outlocation.setVisibility(View.INVISIBLE);
        }

        holder.inlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(context, user_location.class);
                i.putExtra("longitude", items.get(position).getInlongitude());
                i.putExtra("latitude", items.get(position).getInlatitude());
                i.putExtra("mes", "Check In time : "+items.get(position).getCinTime());
                context.startActivity(i);
            }
        });

        holder.outlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(context, user_location.class);
                i.putExtra("longitude", items.get(position).getOutlongitude());
                i.putExtra("latitude", items.get(position).getOutlatitude());
                i.putExtra("mes", "Check Out time : "+items.get(position).getCoutTime());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class myViewHolder extends RecyclerView.ViewHolder {
        private TextView name,cinTime,coutTime,cinloc,coutloc,hrs;
        private ConstraintLayout lv;
        private ImageView arrow,inlocation,outlocation;
        public myViewHolder(View view) {
            super(view);
            name=view.findViewById(R.id.emp_name);
            cinTime=view.findViewById(R.id.checkinTime);
            coutTime=view.findViewById(R.id.checkoutTime);
            cinloc=view.findViewById(R.id.cinLocation2);
            coutloc=view.findViewById(R.id.coutLocation);
            lv=view.findViewById(R.id.coutdetailLayout);
            arrow=view.findViewById(R.id.arrow2);
            hrs=view.findViewById(R.id.w_hrs);
            inlocation=view.findViewById(R.id.inlocation);
            outlocation=view.findViewById(R.id.outlocation);
        }
    }
}
