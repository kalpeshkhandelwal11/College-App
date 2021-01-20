package com.miniproject.collegeapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.miniproject.collegeapp.model.Data;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class createnotice extends AppCompatActivity {
    //firebase
    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DatePickerDialog picker;
    private EditText title;
    private EditText date;
    private EditText description;
    private String UID;
    private Button upload;
    private TextView notify, durl;
    private Uri pdfUri;
    String pdflink;
    List<String> mADepartment;
    private String acctodate;
    private String name;
    ProgressDialog mdialog;
    private String Department;
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    //notification
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAcz7I-Ls:APA91bFh_6HO-iTXwTnu55KFZCSba-mYN53vSmJRD94MnPjDocQfS3aMWO6Zkq5PPLqCJ2rVHXtqhNrHdsVg08_qciOCazmKoIFJ1diH4fsC0U6DNUXIYPImNywYnLMSiBGzwW2gt5Xl";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnotice);
        Toolbar toolbar = findViewById(R.id.toolbarCreatenotice);
        toolbar.setTitle("Create Notice");
        setSupportActionBar(toolbar);
        mdialog = new ProgressDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //firebase
        FirebaseStorage firebaseStorage;

        upload = findViewById(R.id.upload);
        notify = findViewById(R.id.acknoledgement);
        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        description = findViewById(R.id.description);
        durl = findViewById(R.id.durl);
        Button btnSave = findViewById(R.id.send);
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .whereEqualTo("uid", UID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                name = (String) document.get("name");
                                Department = (String) document.get("Department");
                                Log.d("kalpeshfs", document.getId() + " => " + name);
                                Log.d("kalpeshfs", document.getId() + " => " + Department);

                            }
                        } else {
                            Log.d("kalpeshfs", "Error getting documents: ", task.getException());
                        }
                    }
                });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
               /*   // date picker dialog
                picker = new DatePickerDialog(createnotice.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                picker.show(); */
                picker = new DatePickerDialog(createnotice.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                String dateString = format.format(calendar.getTime());
                                date.setText(dateString);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis());
                picker.show();

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(createnotice.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPdf();
                } else {
                    ActivityCompat.requestPermissions(createnotice.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mTitle = title.getText().toString().trim();
                String currdate = date.getText().toString().trim();
                String mDescription = description.getText().toString().trim();
                String mname = name;
                Log.d("fs", "onClick: " + mname);
                final String mDepartment = Department.trim();
                if (Department.contentEquals("Office")) {
                    mADepartment = Arrays.asList(Department, "BVOC", "IT", "BCOM", "BSC", "JC");
                } else {
                    mADepartment = Arrays.asList(Department);

                }
                if (TextUtils.isEmpty(mTitle)) {
                    title.setError("Required Field");
                } else if (TextUtils.isEmpty(currdate)) {
                    date.setError("Required Field");
                } else if (TextUtils.isEmpty(mDescription)) {
                    description.setError("Required Field");
                } else if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "name is " + name, Toast.LENGTH_SHORT).show();
                } else {
                    String pattern = "dd-MM-yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                    String mDate = simpleDateFormat.format(new Date());
                    mdialog.setMessage("processing...");
                    mdialog.show();
                    Data data = new Data(mTitle, mDate, currdate, mDescription, mAuth.getUid(), mname, mDepartment, durl.getText().toString(), mADepartment);

                    db.collection("Notices").document()
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("kalpeshfs", "DocumentSnapshot successfully written!");
                                    title.setText("");
                                    description.setText("");
                                    date.setText("");
                                    mdialog.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("kalpeshfs", "Error writing document", e);
                                }
                            });
                    Toast.makeText(getApplicationContext(), "Data Uploaded", Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(getApplicationContext(),Nav.class));
                }

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        } else {
            Toast.makeText(getApplicationContext(), "please give permission", Toast.LENGTH_SHORT).show();
        }

    }


    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            notify.setText("A file is selected");
            mdialog.setMessage("processing...");
            mdialog.show();
            uploadpdf();
        } else {
            Toast.makeText(getApplicationContext(), "please select file", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadpdf() {
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("NoticesPDF/" + System.currentTimeMillis() + ".pdf");
        if (pdfUri != null) {
            storageReference.putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            pdflink = downloadUrl.toString();
                            durl.setText(downloadUrl.toString());
                            mdialog.dismiss();
                            notify.setText("file uploaded");
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "problem" + e, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
