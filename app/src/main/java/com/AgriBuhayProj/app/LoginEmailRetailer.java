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

public class LoginEmailRetailer extends AppCompatActivity {


    TextInputLayout email, pass;
    Button Signout,SignInphone;
    TextView Forgotpassword;
    TextView txt;
    FirebaseAuth FAuth;
    String em;
    String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_email_retailer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login As Retailer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginEmailRetailer.this, MainMenu.class));
                finish();
            }
        });

        try {
            email = (TextInputLayout) findViewById(R.id.Lemail);
            pass = (TextInputLayout) findViewById(R.id.Lpassword);
            Signout = (Button) findViewById(R.id.button4);
            txt = (TextView) findViewById(R.id.textView3);
            Forgotpassword=(TextView)findViewById(R.id.forgotpass);
            SignInphone=(Button)findViewById(R.id.btnphone);

            FAuth = FirebaseAuth.getInstance();

            Signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard();

                    em = email.getEditText().getText().toString().trim();
                    pwd = pass.getEditText().getText().toString().trim();
                    if (isValid()) {
                        final ProgressDialog mDialog = new ProgressDialog(LoginEmailRetailer.this);
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
                                            if(Objects.equals(snapshot.getValue(), "Retailer")){
                                                email.setErrorEnabled(false);
                                                if (FAuth.getCurrentUser().isEmailVerified()) {
                                                    mDialog.dismiss();
                                                    Toast.makeText(LoginEmailRetailer.this, "You are logged in", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(LoginEmailRetailer.this, ProductPanelBottomNavigation_Retailer.class));
                                                    finish();
                                                } else {
                                                    mDialog.dismiss();
                                                    ReusableCodeForAll.ShowAlert(LoginEmailRetailer.this,"","Please Verify your Email");
                                                }
                                            }else{
                                                mDialog.dismiss();
                                                email.setErrorEnabled(true);
                                                email.setError("Account is not a Retailer");
                                                FAuth.signOut();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            mDialog.dismiss();
                                            Toast.makeText(LoginEmailRetailer.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    mDialog.dismiss();
                                    ReusableCodeForAll.ShowAlert(LoginEmailRetailer.this,"Error",task.getException().getMessage());
                                }
                            }
                        });

                    }
                }
            });

            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginEmailRetailer.this, RegistrationRetailer.class));
                    finish();
                }
            });

            Forgotpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginEmailRetailer.this, ForgotPasswordRetailer.class));
                }
            });
            SignInphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginEmailRetailer.this, LoginPhoneRetailer.class));
                    finish();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValid() {
        email.setErrorEnabled(false);
        email.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");

        boolean isvalidemail=false,isvalidpassword=false,isvalid;
        if (TextUtils.isEmpty(em))
        {
            email.setErrorEnabled(true);
            email.setError("Email is required");
        }
        else {
            if (Patterns.EMAIL_ADDRESS.matcher(em).matches())
            {
                isvalidemail=true;
            }
            else {
                email.setErrorEnabled(true);
                email.setError("Enter a valid Email Address");
            }

        }
        if (TextUtils.isEmpty(pwd))
        {
            pass.setErrorEnabled(true);
            pass.setError("Password is required");
        }
        else {
            isvalidpassword=true;
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


