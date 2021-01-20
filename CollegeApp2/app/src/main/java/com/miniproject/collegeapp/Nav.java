package com.miniproject.collegeapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class Nav extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private EditText selectDate;
    private TextView uname;
    private ImageView profile;
    String name;
    String UID;
    String url1;
    String ty;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private AppBarConfiguration mAppBarConfiguration;
    //firebase
    private FloatingActionButton floatingActionButton;
    private FirebaseAuth mAuth;
    private String Dept;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profile = findViewById(R.id.profileImage);
        mAuth = FirebaseAuth.getInstance();
        floatingActionButton = findViewById(R.id.fab);
        UID = mAuth.getCurrentUser().getUid();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("token", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d("token my", "onComplete: " + token);

                        //  Toast.makeText(Nav.this,token, Toast.LENGTH_SHORT).show();
                    }
                });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

        /*
         * Displaying a notification locally
         */
        //MyNotificationManager.getInstance(this).displayNotification("Greetings", "Hello how are you?");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("uid", UID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                name = (String) document.get("name");
                                Dept = document.get("Department").toString();
                                FirebaseMessaging.getInstance().subscribeToTopic(Dept);
                                //    Toast.makeText(getApplicationContext(), "Topic Subscribed", Toast.LENGTH_LONG).show();
                                try {
                                    url1 = document.get("profile").toString();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Log.d("kalpeshfs", document.getId() + " => " + name);
                                View header = navigationView.getHeaderView(0);
                                uname = header.findViewById(R.id.uname_header);
                                uname.setText(name);
                                 ty = document.get("type").toString().trim();
                                if (ty.contentEquals("Student")) {
                                    floatingActionButton.hide();
                                }
                                try {
                                    Glide.with(getApplicationContext()).load(url1).into(profile);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                uname.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    }
                                });
                                Log.d("kalpeshfs", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("kalpeshfs", "Error getting documents: ", task.getException());
                        }
                    }
                });
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager Pager = findViewById(R.id.viewpager);
        tabpagerAdapter TabpagerAdapter = new tabpagerAdapter(getSupportFragmentManager());
        Pager.setAdapter(TabpagerAdapter);
        tabLayout.setupWithViewPager(Pager);
        //firebase

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Notices").child(UID);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                startActivity(new Intent(getApplicationContext(), createnotice.class));

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_home:
                        //Do some thing here
                        // add navigation drawer item onclick method here
                        break;
                    case R.id.nav_users:
                        Intent i = new Intent(getApplicationContext(), UsersActivity.class);
                        startActivity(i);
                        break;
                    case R.id.Mynotices:
                        Intent a = new Intent(getApplicationContext(), MynoticesActivity.class);
                        startActivity(a);
                        break;
                        case R.id.myprofile:
                        Intent b = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(b);
                        break;
                    case R.id.chat:
                        Intent c = new Intent(getApplicationContext(), chat_activity.class);
                        startActivity(c);
                        break;
                    case R.id.collegeloc:
                        Intent k = new Intent(getApplicationContext(), CollegeLocation.class);
                        startActivity(k);
                        break;
                    case R.id.atten:
                        Intent f = new Intent(getApplicationContext(), attendance.class);
                        startActivity(f);
                        break;

                    case R.id.ShowAttendance:
                        if (ty.contentEquals("Student")) {
                            Toast.makeText(Nav.this, "Insufficient privilages", Toast.LENGTH_SHORT).show();
                        }else
                            {
                                Intent m = new Intent(getApplicationContext(), attendance_display.class);
                                startActivity(m);
                            }

                        break;




                }
                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log: {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Dept);
                // Toast.makeText(getApplicationContext(),"unsubscribed"+Dept,Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
