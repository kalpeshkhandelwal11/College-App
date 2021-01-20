package com.miniproject.collegeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register_activity extends AppCompatActivity {
    ImageView ImgUserPhoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    private StorageReference mStorageRef;
    Uri pickedImgUrl;
    private FirebaseFirestore db;
    private EditText email, phone, name;
    private EditText password, confirm_pass;
    private Button btnLoginreg;
    private Button btnRegisterreg;
    String profileImgUrl;
    String UID;

    private FirebaseAuth mAuth;
    private ProgressDialog mdialog;
    private String department;
    private String position1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mdialog = new ProgressDialog(this);
        email = findViewById(R.id.email_registration);
        password = findViewById(R.id.password_registration);
        confirm_pass = findViewById(R.id.confirm_password_registration);
        name = findViewById(R.id.name_registration);
        phone = findViewById(R.id.phone_register);
        btnLoginreg = findViewById(R.id.btn_login_reg);
        btnRegisterreg = findViewById(R.id.btn_Register_reg);
        final Spinner spinner = findViewById(R.id.department);

        Spinner spinner1 = findViewById(R.id.type);
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Post");
        arrayList1.add("Student");
        arrayList1.add("HOD");
        arrayList1.add("Professor");
        arrayList.add("Department");
        arrayList.add("IT");
        arrayList.add("COMS");
        arrayList.add("Mechanical");
        arrayList.add("CIVIL");
        arrayList.add("EXTC");
        arrayList.add("Office");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + department, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position1 = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + position1, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ImgUserPhoto = findViewById(R.id.regUserPhoto);
        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();
                openGallery();

            }
        });
        btnRegisterreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString().trim();
                String mpass = password.getText().toString().trim();
                if (TextUtils.isEmpty(mEmail)) {
                    email.setError("required Field...");
                    return;
                }
                if (TextUtils.isEmpty(mpass)) {
                    password.setError("Required Field");
                    return;
                }
                if (department.contentEquals("Department")) {
                    Toast.makeText(getApplicationContext(), "select a department", Toast.LENGTH_SHORT).show();
                }
                if (position1.contentEquals("Post")) {
                    Toast.makeText(getApplicationContext(), "select a Post", Toast.LENGTH_SHORT).show();
                }

                if (phone.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter a phone no.", Toast.LENGTH_SHORT).show();

                }
                if(mpass!=(confirm_pass.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(), "Password and confirm password should be same.", Toast.LENGTH_SHORT).show();

                }

                mdialog.setMessage("Processing...");
                mdialog.show();

                mAuth.createUserWithEmailAndPassword(mEmail, mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser mUser = mAuth.getCurrentUser();

                            UID = mUser.getUid();
                            Saveuserinfo();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            //set Users display name
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name.getText().toString()).build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    startActivity(new Intent(getApplicationContext(),Nav.class));
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), Nav.class));

                            Toast.makeText(getApplicationContext(), "Registration complete", Toast.LENGTH_SHORT).show();

                            mdialog.dismiss();
                        } else {
                            mdialog.dismiss();
                            Toast.makeText(getApplicationContext(), "problem", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

        });

//Redirect to login page
        btnLoginreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    private void Saveuserinfo() {
        String mName = name.getText().toString().trim();
        String mDepartment = department.toString().trim();
        String mPhone = phone.getText().toString();
        String mtype = position1.toString().trim();
        Map<String, Object> user = new HashMap<>();
        user.put("name", mName);
        user.put("Department", mDepartment);
        user.put("phone", mPhone);
        user.put("type", mtype);
        user.put("profile", profileImgUrl);
        user.put("uid", UID);
        db.collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("kalpeshFS", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("kalpeshFSE", "Error adding document", e);
                    }
                });


    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "select profile image"), REQUESCODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESCODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pickedImgUrl = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pickedImgUrl);
                ImgUserPhoto.setImageBitmap(bitmap);
                uploadImagetoFirebase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImagetoFirebase() {
        final StorageReference profilepicRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        if (pickedImgUrl != null) {
            profilepicRef.putFile(pickedImgUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                    profilepicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            profileImgUrl = downloadUrl.toString();
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
