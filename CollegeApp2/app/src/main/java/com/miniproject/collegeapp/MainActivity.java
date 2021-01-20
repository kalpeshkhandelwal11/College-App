package com.miniproject.collegeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button btnLogin;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog mdialog;
    private TextView fp;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mdialog = new ProgressDialog(this);
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_Register);
        fp = findViewById(R.id.fplabel);
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Nav.class));
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memail = email.getText().toString().trim();
                String mpass = password.getText().toString().trim();
                if (TextUtils.isEmpty(memail)) {
                    email.setError("required Field...");
                    return;
                } else if (TextUtils.isEmpty(mpass)) {
                    password.setError("Required Field");
                    return;
                } else {
                    mdialog.setMessage("Processing..");
                    mdialog.show();
                    mAuth.signInWithEmailAndPassword(memail, mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mdialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), Nav.class));
                                Toast.makeText(getApplicationContext(), "login complete", Toast.LENGTH_SHORT).show();
                            } else {
                                mdialog.dismiss();
                                Toast.makeText(getApplicationContext(), "problem", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
        //going to registration page
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register_activity.class));
            }
        });

        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), forgot_passwordActivity.class));
            }
        });

    }
}
