package com.miniproject.collegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.miniproject.collegeapp.attendance_dateRVAdapter;

import com.miniproject.collegeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class attendance_display extends AppCompatActivity {

    private RecyclerView viewdatesRV;
    private List<String> items;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private com.miniproject.collegeapp.attendance_dateRVAdapter attendance_dateRVAdapter;
    private String finalstring="",FILE_NAME="attendance.csv";
    private String currentDate = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_display);

        viewdatesRV=findViewById(R.id.viewDate);

        database=FirebaseDatabase.getInstance();
        ref=database.getReference();
        auth=FirebaseAuth.getInstance();

        items= new ArrayList<>();
        attendance_dateRVAdapter=new attendance_dateRVAdapter(this,items);
        viewdatesRV.setAdapter(attendance_dateRVAdapter);
        viewdatesRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        displaydata();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.attendance_share, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id= item.getItemId();
//        if(id==R.id.a_share){
//            sharebutton();
//        }
//        if(id==R.id.a_delete){
//            ref.child("Main admin").addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.child("ID").getValue().toString().equals(auth.getUid())){
//                        new AlertDialog.Builder(attendance_display.this)
//                                .setTitle("Delete")
//                                .setMessage("Are you Sure? You want to delete the records?\n" +
//                                        "Records of previous months will be deleted and will not be recovered!")
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        deleteAttendance();
//                                    }
//                                })
//
//                                // A null listener allows the button to dismiss the dialog and take no further action.
//                                .setNegativeButton(android.R.string.no, null)
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
//                    }
//                    else {
//                        new AlertDialog.Builder(attendance_display.this)
//                                .setTitle("Alert")
//                                .setMessage("You don't have authority to delete the records!")
//                                .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                })
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
//        return super.onOptionsItemSelected(item);
//    }


//    private void sharebutton() {
//        finalstring = "";
//        final ProgressDialog progressBar;
//        progressBar = new ProgressDialog(this);
//        progressBar.setCancelable(true);//you can cancel it by pressing back button
//        progressBar.setMessage("Getting Data ...");
//        progressBar.show();
//
//        String[] months = {"January", "February", "March", "April", "May",
//                "June", "July", "August", "September", "October", "November", "December"};
//        for (int i = 0; i < 12; i++) {
//            final String m = months[i];
//            ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot mainNode) {
//                    if(mainNode.child("Attendance").exists()){
//                        for(DataSnapshot dates:mainNode.child("Attendance").getChildren()){
//                            if (dates.getKey().contains(m)) {
//                                for (final DataSnapshot uid : dates.getChildren()) {
//                                    final String intime, outtime, inloc, outloc, hrs, date,name,role;
//                                    intime = uid.child("IN").child("Time").getValue().toString();
//                                    inloc = uid.child("IN").child("Location").getValue().toString();
//
//                                    outtime = uid.child("OUT").child("Time").getValue().toString();
//                                    outloc = uid.child("OUT").child("Location").getValue().toString();
//
//                                    hrs = uid.child("Working Hours").getValue().toString();
//
//                                    date = dates.getKey();
//
//                                    name=mainNode.child("Users").child(uid.getKey()).child("username").getValue().toString();
//                                    role=mainNode.child("Users").child(uid.getKey()).child("role").getValue().toString();
//
//                                    String jsonstring =
//                                            "{\"Date\":\"" + date + "\"," +
//                                                    "\"Name\":\"" + name + "\"," +
//                                                    "\"Role\":\"" + role + "\"," +
//                                                    "\"Check In\":\"" + intime + "\"," +
//                                                    "\"Check In Location\":\"" + inloc + "\"," +
//                                                    "\"Check Out\":\"" + outtime + "\"," +
//                                                    "\"Check Out Location\":\"" + outloc + "\"," +
//                                                    "\"Working Hours\":\"" + hrs + "\"}";
//
//                                    if (finalstring.equals("")) {
//                                        finalstring = jsonstring;
//                                    } else {
//                                        finalstring = finalstring + "," + jsonstring;
//                                    }
//
//                                    Log.d("new share Final string:", finalstring);
//                                    StringBuilder addkey = new StringBuilder(finalstring);
//                                    addkey.insert(0, "{\"" + "attendance" + "\":[");
//                                    addkey.insert(addkey.length(), "]}");
//                                    Log.d(" 1.final string", finalstring);
//                                    Log.d("addkey", addkey.toString());
//                                    JSONObject output;
//                                    try {
//                                        String root = Environment.getExternalStorageDirectory().toString();
//                                        File myDir = new File(root + "/SAH/Attendance");
//                                        if (!myDir.exists()) {
//                                            Log.d("mkdir", myDir.getPath());
//                                            myDir.mkdirs();
//                                        }
//                                        if (myDir.exists()) {
//                                            Log.d("mkdir exist", myDir.getPath());
//                                        }
//                                        File file = new File(myDir, FILE_NAME);
//
//
//                                        output = new JSONObject(addkey.toString());
//                                        JSONArray docs = output.getJSONArray("attendance");
//                                        FileOutputStream fos = new FileOutputStream(file);
//                                        String csv = CDL.toString(docs);
//                                        Log.d("CSV", csv);
//                                        fos.write(csv.getBytes());
//                                        fos.close();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    } catch (IOException e) {
//                                        // TODO Auto-generated catch block
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }
//        progressBar.dismiss();
//        share();
//    }
//
//
//    private void share(){
//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root + "/SAH/Attendance");
//        File outputFile = new File (myDir, FILE_NAME);
//        //File outputFile = new File(FILE_NAME);
//        Uri path = FileProvider.getUriForFile(this, getPackageName(), outputFile);
//
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setDataAndType(path,"application/*");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "Sharing....");
//        shareIntent.putExtra(Intent.EXTRA_STREAM, path);
//        startActivity(shareIntent);
//
//    }
//
//    private void deleteAttendance(){
//        final String[] months={"January","February","March","April","May",
//                "June","July","August","September","October","November","December"};
//
//        for(int i =0;i<12;i++){
//            if(currentDate.contains(months[i])){
//                System.out.println(i);
//                for(int j=0;j<i;j++){
//                    final int finalJ = j;
//                    ref.child("Attendance").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if(dataSnapshot.exists()){
//                                for(DataSnapshot date:dataSnapshot.getChildren()){
//                                    if(date.getKey().contains(months[finalJ])){
//                                        ref.child("Attendance").child(date.getKey()).removeValue();
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//        }
//        displaydata();
//    }

    private void displaydata(){
        items.clear();
        //reshape
        ref.child("Attendance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> list = new ArrayList<String>();
                    Date list2[];
                    for (DataSnapshot date : dataSnapshot.getChildren()) {
                        list.add(date.getKey().toString());
                    }
                    list2= new Date[list.size()];
                    SimpleDateFormat sobj = new SimpleDateFormat("MMMM d, yyyy");// format specified in double quotes
                    for(int i=0;i<list.size();i++)
                    {
                        try {
                            list2[i]= sobj.parse(list.get(i));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("List1 :  ", String.valueOf(list.size()));
                    Arrays.sort(list2);
                    SimpleDateFormat main= new SimpleDateFormat("MMMM d, yyyy");
                    list.clear();
                    for(Date date1 : list2)                                  //Enchanced for loop, it loops through all element in date array
                    {                                                       //each time date[i] is copied to date1 like traditional for loop
                        list.add(main.format(date1));            //format the date1 to dd-MM-yyyy using sobj
                    }
                    Collections.reverse(list);
                    items.addAll(list);
                    attendance_dateRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}