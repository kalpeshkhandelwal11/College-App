package com.miniproject.collegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.miniproject.collegeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class attendance extends AppCompatActivity implements LocationListener {
    private LocationManager locationManager;
    private ProgressBar bar;
    private TextClock clock;
    private TextView tvLocality,welcome,date,cint,cinl,cout,coutl,hrs,role;
    private Button checkin,checkout;
    private ConstraintLayout cinlo,coutlo,hrslo;
    private ImageView location_track;

    static String stvCity, stvState, stvCountry, stvPin, stvLocality,longitude,latitiude;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private String namee;
    private OffsetDateTime offset = OffsetDateTime.now();
    private String currentDate = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date());
    //private String currentDate ="April 8, 2020";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        ref=database.getReference();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

        tvLocality = findViewById(R.id.user_location);
        clock=findViewById(R.id.time);
        date=findViewById(R.id.loc_date);
        welcome=findViewById(R.id.welcometext);
        checkin=findViewById(R.id.checkin);
        checkout=findViewById(R.id.checkout);
        bar=findViewById(R.id.progressBar2);
        date.setText(currentDate+"  "+offset.getDayOfWeek());
        cinlo=findViewById(R.id.cindetailLayout);
        coutlo=findViewById(R.id.coutdetailLayout);
        cint=findViewById(R.id.checkinTime);
        cinl=findViewById(R.id.cinLocation2);
        cout=findViewById(R.id.checkoutTime);
        coutl=findViewById(R.id.coutLocation);
        hrs=findViewById(R.id.Workinghrs);
        role=findViewById(R.id.role);
        location_track=findViewById(R.id.location);

        checkin.setVisibility(View.INVISIBLE);
        checkout.setVisibility(View.INVISIBLE);
        cinlo.setVisibility(View.INVISIBLE);
        coutlo.setVisibility(View.INVISIBLE);
        hrs.setVisibility(View.INVISIBLE);
        location_track.setVisibility(View.INVISIBLE);
       namee = auth.getCurrentUser().getDisplayName();

//        ref.child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    welcome.setText("Welcome "+dataSnapshot.child("username").getValue().toString().toUpperCase());
//                    role.setText(dataSnapshot.child("role").getValue().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationEnabled();
        getLocation();


        ref.child("Attendance").child(currentDate).child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("IN").exists()){
                    cinlo.setVisibility(View.VISIBLE);
                    hrs.setVisibility(View.INVISIBLE);
                }
                else{
                    cinlo.setVisibility(View.INVISIBLE);
                    coutlo.setVisibility(View.INVISIBLE);
                    hrs.setVisibility(View.INVISIBLE);
                }
                if(dataSnapshot.child("OUT").exists()){
                    if(!dataSnapshot.child("OUT").child("Time").getValue().equals("") &&
                            !dataSnapshot.child("OUT").child("Location").getValue().equals("")){
                        cinlo.setVisibility(View.VISIBLE);
                        coutlo.setVisibility(View.VISIBLE);
                        hrs.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ref.child("Attendance").child(currentDate).child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    try {
                        cint.setText("Time : "+ dataSnapshot.child("IN").child("Time").getValue().toString());
                        cinl.setText(dataSnapshot.child("IN").child("Location").getValue().toString());
                        cout.setText("Time : "+dataSnapshot.child("OUT").child("Time").getValue().toString());
                        coutl.setText(dataSnapshot.child("OUT").child("Location").getValue().toString());
                        hrs.setText("Total Hours : "+dataSnapshot.child("Working Hours").getValue().toString());
                    }catch (Exception e){}

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        location_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(attendance.this, user_location.class);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitiude);
                intent.putExtra("mes", "You are here");
                startActivity(intent);
            }
        });


    }

    private void locationEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(attendance.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 200, 2, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            stvCity=addresses.get(0).getLocality();
            stvState=addresses.get(0).getAdminArea();
            stvCountry=addresses.get(0).getCountryName();
            stvPin=addresses.get(0).getPostalCode();
            stvLocality=addresses.get(0).getAddressLine(0);

            tvLocality.setText(stvLocality);

            longitude=String.valueOf(addresses.get(0).getLongitude());
            latitiude=String.valueOf(addresses.get(0).getLatitude());


            ref.child("Attendance").child(currentDate).child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("IN").exists()){
                        checkin.setVisibility(View.VISIBLE);
                        checkout.setVisibility(View.VISIBLE);
                    }
                    else{
                        checkin.setVisibility(View.VISIBLE);
                        checkout.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            bar.setVisibility(View.INVISIBLE);
            location_track.setVisibility(View.VISIBLE);


        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Attendance").child(currentDate).child(auth.getUid()).child("IN").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            new AlertDialog.Builder(attendance.this)
                                    .setTitle("Alert")
                                    .setMessage("You have already Checked IN. Clicking OK will result in overwriting the data!")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            CHECKIN();
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        else{
                            new AlertDialog.Builder(attendance.this)
                                    .setTitle("Check In")
                                    .setMessage("Are you Sure?")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            CHECKIN();
                                            checkout.setVisibility(View.VISIBLE);
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Attendance").child(currentDate).child(auth.getUid()).child("OUT").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            if(dataSnapshot.child("Time").getValue().toString().equals("") && dataSnapshot.child("Location").getValue().toString().equals("")){
                                new AlertDialog.Builder(attendance.this)
                                        .setTitle("Check Out")
                                        .setMessage("Are you Sure?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                CHECKOUT();
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(android.R.string.no, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                            else{
                                new AlertDialog.Builder(attendance.this)
                                        .setTitle("Alert")
                                        .setMessage("You have already Checked OUT. Clicking OK will result in overwriting the data!")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                CHECKOUT();
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
                                        .setNegativeButton(android.R.string.no, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    private void CHECKIN(){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Location",tvLocality.getText().toString().trim());
        hashMap.put("Time",clock.getText());
        hashMap.put("Longitude",longitude);
        hashMap.put("Latitude",latitiude);

        ref.child("Attendance").child(currentDate).child(auth.getUid()).child("IN").setValue(hashMap);

        HashMap<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("Location","");
        hashMap2.put("Time","");
        hashMap2.put("Longitude","");
        hashMap2.put("Latitude","");

        ref.child("Attendance").child(currentDate).child(auth.getUid()).child("OUT").setValue(hashMap2);

        ref.child("Attendance").child(currentDate).child(auth.getUid()).child("Working Hours").setValue("");
        ref.child("Attendance").child(currentDate).child(auth.getUid()).child("name").setValue(namee);
    }

    private void CHECKOUT(){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Location",tvLocality.getText().toString().trim());
        hashMap.put("Time",clock.getText());
        hashMap.put("Longitude",longitude);
        hashMap.put("Latitude",latitiude);
        ref.child("Attendance").child(currentDate).child(auth.getUid()).child("OUT").setValue(hashMap);


        ref.child("Attendance").child(currentDate).child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    final String from,till;
                    from=dataSnapshot.child("IN").child("Time").getValue().toString();
                    till=dataSnapshot.child("OUT").child("Time").getValue().toString();
                    workinghrs(from,till);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void workinghrs(String from, String till){
        String[] in = from.split(":");
        String[] out = till.split(":");

        int x=(Integer.parseInt(in[0])*60)+(Integer.parseInt(in[1]));
        int y=(Integer.parseInt(out[0])*60)+(Integer.parseInt(out[1]));

        int totalTime=y-x;
        String total_time=String.valueOf((int)totalTime/60)+"h:"+String.valueOf((int)totalTime%60)+"m";
        ref.child("Attendance").child(currentDate).child(auth.getUid()).child("Working Hours").setValue(total_time);

    }

}