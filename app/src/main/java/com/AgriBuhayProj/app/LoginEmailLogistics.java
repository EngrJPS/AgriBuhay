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
    //DECLARE VARIABLES
    //xml
    TextInputLayout email, pass;
    Button Signin, Signinphone;
    TextView Forgotpassword;
    TextView txt;

    //firebase
    FirebaseAuth FAuth;

    //strings
    private String em,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_email_delivery);

        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login As Logistics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //direct to main menu
                startActivity(new Intent(LoginEmailLogistics.this, MainMenu.class));
                finish();
            }
        });

        //CONNECT XML
        email = findViewById(R.id.Demail);
        pass = findViewById(R.id.Dpassword);
        Signin = findViewById(R.id.Loginbtn);
        txt = findViewById(R.id.donot);
        Forgotpassword = findViewById(R.id.Dforgotpass);
        Signinphone = findViewById(R.id.Dphonebtn);

        //FIREBASE INSTANCE
        FAuth = FirebaseAuth.getInstance();

        //BUTTON EVENTS
        //login clicked
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                hideKeyboard();

                //get input strings
                em = email.getEditText().getText().toString().trim();
                pwd = pass.getEditText().getText().toString().trim();

                //check input validity
                if (isValid()) {

                    //show progress dialog
                    final ProgressDialog mDialog = new ProgressDialog(LoginEmailLogistics.this);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                    mDialog.setMessage("Logging in...");
                    mDialog.show();

                    //login account
                    FAuth.signInWithEmailAndPassword(em, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //check if logged in
                                if (task.isSuccessful()) {
                                    //get user ID
                                    final String loginID = FAuth.getCurrentUser().getUid();

                                    //database reference
                                    final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");

                                    //get data
                                    dbRef.child(loginID).child("Role").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            //check if logistics account
                                            if(Objects.equals(snapshot.getValue(), "Logistics")){
                                                //disable error
                                                email.setErrorEnabled(false);

                                                //check if email verified
                                                if (FAuth.getCurrentUser().isEmailVerified()) {
                                                    mDialog.dismiss();

                                                    Toast.makeText(LoginEmailLogistics.this, "You are logged in", Toast.LENGTH_SHORT).show();

                                                    //direct to logistics home
                                                    startActivity(new Intent(LoginEmailLogistics.this, ProductPanelBottomNavigation_Logistics.class));
                                                    finish();
                                                } else {
                                                    mDialog.dismiss();
                                                    //not verified
                                                    ReusableCodeForAll.ShowAlert(LoginEmailLogistics.this, "", "Please Verify your Email");
                                                }
                                            }else{
                                                mDialog.dismiss();

                                                //not logistics
                                                email.setErrorEnabled(true);
                                                email.setError("Account is not a Courier");

                                                //sign out current user
                                                FAuth.signOut();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            mDialog.dismiss();

                                            //database error
                                            Toast.makeText(LoginEmailLogistics.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    mDialog.dismiss();

                                    //login failed
                                    ReusableCodeForAll.ShowAlert(LoginEmailLogistics.this, "Error", task.getException().getMessage());
                                }
                            }
                        });
                    }
                }
        });

        //registration clicked
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to registration
                startActivity(new Intent(LoginEmailLogistics.this, RegistrationLogistics.class));
                finish();
            }
        });

        //forgot password clicked
        Forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to forgot password
                startActivity(new Intent(LoginEmailLogistics.this, ForgotPasswordLogistics.class));
            }
        });

        //phone login clicked
        Signinphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to phone login
                startActivity(new Intent(LoginEmailLogistics.this, LoginPhoneLogistics.class));
                finish();
            }
        });
    }

    //CHECK INPUT VALIDITY
    public boolean isValid() {
        email.setErrorEnabled(false);
        email.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");

        boolean isvalidemail = false, isvalidpassword = false, isvalid;

        //check if empty email
        if (TextUtils.isEmpty(em)) {
            email.setErrorEnabled(true);
            email.setError("Email is required");
        } else {
            //check email pattern
            if (Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
                isvalidemail = true;
            } else {
                email.setErrorEnabled(true);
                email.setError("Enter a valid Email Address");
            }

        }

        //check if empty password
        if (TextUtils.isEmpty(pwd)) {
            pass.setErrorEnabled(true);
            pass.setError("Password is required");
        } else {
            isvalidpassword = true;
        }

        //return true/false
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

    //DISABLE BACK PRESS
    public void onBackPressed(){ }
}
