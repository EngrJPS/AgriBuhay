package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginEmailProducer extends AppCompatActivity {

    TextInputLayout email, pass;
    Button Signout, Signinphone;
    TextView Forgotpassword;
    TextView txt;
    FirebaseAuth FAuth;
    String em;
    String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_email_producer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login As Producer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            email = (TextInputLayout) findViewById(R.id.Lemail);
            pass = (TextInputLayout) findViewById(R.id.Lpassword);
            Signout = (Button) findViewById(R.id.button4);
            txt = (TextView) findViewById(R.id.textView3);
            Forgotpassword = (TextView) findViewById(R.id.forgotpass);
            Signinphone = (Button) findViewById(R.id.btnphone);


            FAuth = FirebaseAuth.getInstance();

            Signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    em = email.getEditText().getText().toString().trim();
                    pwd = pass.getEditText().getText().toString().trim();
                    if (isValid()) {

                        final ProgressDialog mDialog = new ProgressDialog(LoginEmailProducer.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("Logging in...");
                        mDialog.show();

                        FAuth.signInWithEmailAndPassword(em, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final String loginID = FAuth.getCurrentUser().getUid();
                                    final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");
                                    dbRef.child(loginID).child("Role").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(Objects.equals(snapshot.getValue(), "Producer")){
                                                email.setErrorEnabled(false);
                                                if (FAuth.getCurrentUser().isEmailVerified()) {
                                                    mDialog.dismiss();
                                                    Toast.makeText(LoginEmailProducer.this, "You are logged in", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(LoginEmailProducer.this, ProductPanelBottomNavigation_Producer.class));
                                                    finish();
                                                } else {
                                                    mDialog.dismiss();
                                                    ReusableCodeForAll.ShowAlert(LoginEmailProducer.this, "", "Please Verify your Email");
                                                }
                                            }else{
                                                mDialog.dismiss();
                                                email.setErrorEnabled(true);
                                                email.setError("Account is not a Producer");
                                                FAuth.signOut();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            mDialog.dismiss();
                                            Toast.makeText(LoginEmailProducer.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    mDialog.dismiss();
                                    ReusableCodeForAll.ShowAlert(LoginEmailProducer.this, "Error", task.getException().getMessage());
                                }
                            }
                        });
                    }
                }
            });

            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginEmailProducer.this, RegistrationProducer.class));
                    finish();
                }
            });

            Forgotpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginEmailProducer.this, ForgotPasswordProducer.class));
                    finish();
                }
            });

            Signinphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginEmailProducer.this, LoginPhoneProducer.class));
                    finish();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValid() {
        email.setErrorEnabled(false);
        email.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");

        boolean isvalidemail = false, isvalidpassword = false, isvalid;
        if (TextUtils.isEmpty(em)) {
            email.setErrorEnabled(true);
            email.setError("Email is required");
        } else {
            if (Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
                isvalidemail = true;
            } else {
                email.setErrorEnabled(true);
                email.setError("Enter a valid Email Address");
            }

        }
        if (TextUtils.isEmpty(pwd)) {
            pass.setErrorEnabled(true);
            pass.setError("Password is required");
        } else {
            isvalidpassword = true;
        }
        isvalid = (isvalidemail && isvalidpassword) ? true : false;
        return isvalid;
    }

    public void onBackPressed(){ }
}

