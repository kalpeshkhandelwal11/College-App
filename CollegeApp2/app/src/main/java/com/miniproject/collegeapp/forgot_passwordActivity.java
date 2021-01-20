    package com.miniproject.collegeapp;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.FirebaseAuth;

    public class forgot_passwordActivity extends AppCompatActivity {
        private Button Forgotpassword;
        private EditText email;
        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_forgot_password);
            Forgotpassword = findViewById(R.id.btn_fp);
            email = findViewById(R.id.email_fp);
            mAuth = FirebaseAuth.getInstance();
            Forgotpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String fpemail = email.getText().toString().trim();

                    if (TextUtils.isEmpty(fpemail)) {
                        email.setError("Required Field");
                        Toast.makeText(getApplicationContext(), "Enter Proper Email", Toast.LENGTH_SHORT).show();
                    }

                    mAuth.sendPasswordResetEmail(fpemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Mail has been sent to your mail id to reset your Password", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));


                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(getApplicationContext(), "Error Occured" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            });
        }

    }
