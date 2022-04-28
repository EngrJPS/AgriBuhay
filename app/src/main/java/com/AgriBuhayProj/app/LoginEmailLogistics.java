package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class LoginEmailLogistics extends AppCompatActivity {

    TextInputLayout email, pass;
    Button Signin, Signinphone;
    TextView Forgotpassword;
    TextView txt;

    FirebaseAuth FAuth;

    private String em,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_email_delivery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login As Logistics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginEmailLogistics.this, MainMenu.class));
                finish();
            }
        });

        email = (TextInputLayout) findViewById(R.id.Demail);
        pass = (TextInputLayout) findViewById(R.id.Dpassword);
        Signin = (Button) findViewById(R.id.Loginbtn);
        txt = (TextView) findViewById(R.id.donot);
        Forgotpassword = (TextView) findViewById(R.id.Dforgotpass);
        Signinphone = (Button) findViewById(R.id.Dphonebtn);

        FAuth = FirebaseAuth.getInstance();

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();

                em = email.getEditText().getText().toString().trim();
                pwd = pass.getEditText().getText().toString().trim();
                if (isValid()) {
                    final ProgressDialog mDialog = new ProgressDialog(LoginEmailLogistics.this);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                    mDialog.setMessage("Logging in...");
                    mDialog.show();

                    //login
                    FAuth.signInWithEmailAndPassword(em, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    final String loginID = FAuth.getCurrentUser().getUid();
                                    final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");
                                    dbRef.child(loginID).child("Role").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(Objects.equals(snapshot.getValue(), "Logistics")){
                                                email.setErrorEnabled(false);
                                                if (FAuth.getCurrentUser().isEmailVerified()) {
                                                    mDialog.dismiss();
                                                    Toast.makeText(LoginEmailLogistics.this, "You are logged in", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(LoginEmailLogistics.this, ProductPanelBottomNavigation_Logistics.class));
                                                    finish();
                                                } else {
                                                    mDialog.dismiss();
                                                    ReusableCodeForAll.ShowAlert(LoginEmailLogistics.this, "", "Please Verify your Email");
                                                }
                                            }else{
                                                mDialog.dismiss();
                                                email.setErrorEnabled(true);
                                                email.setError("Account is not a Courier");
                                                FAuth.signOut();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            mDialog.dismiss();
                                            Toast.makeText(LoginEmailLogistics.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    mDialog.dismiss();
                                    ReusableCodeForAll.ShowAlert(LoginEmailLogistics.this, "Error", task.getException().getMessage());
                                }
                            }
                        });
                    }
                }
        });

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginEmailLogistics.this, RegistrationLogistics.class));
                finish();
            }
        });

        Forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginEmailLogistics.this, ForgotPasswordLogistics.class));
            }
        });

        Signinphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginEmailLogistics.this, LoginPhoneLogistics.class));
                finish();
            }
        });
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
        isvalid = isvalidemail && isvalidpassword;
        return isvalid;
    }

    //HIDE KEYBOARD
    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager hide = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            hide.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onBackPressed(){ }
}
