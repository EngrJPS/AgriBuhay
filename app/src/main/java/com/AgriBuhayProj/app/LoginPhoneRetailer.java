package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class LoginPhoneRetailer extends AppCompatActivity {
    //DECLARE VARIABLES
    //xml
    EditText num;
    TextView txtsignup;
    Button sendotp,signinemail;
    CountryCodePicker cpp;
    ProgressDialog progressDialog;

    //firebase
    FirebaseAuth FAuth;
    DatabaseReference dbRef;

    //string
    String numberr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_phone_retailer);

        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login As Retailer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //direct to main menu
                startActivity(new Intent(LoginPhoneRetailer.this, MainMenu.class));
                finish();
            }
        });

        //CONNECT XML
        num = findViewById(R.id.number);
        sendotp = findViewById(R.id.otp);
        cpp = findViewById(R.id.CountryCode);
        signinemail = findViewById(R.id.btnEmail);
        txtsignup = findViewById(R.id.pRReg);

        //PROGRESS DIALOG
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        //DATABASE INSTANCE
        FAuth=FirebaseAuth.getInstance();
        //database reference
        dbRef = FirebaseDatabase.getInstance().getReference("Mobile");

        //BUTTON EVENTS
        //send otp
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                hideKeyboard();

                //get string
                numberr=num.getText().toString().trim();
                String phonenumber= cpp.getSelectedCountryCodeWithPlus() + numberr;

                //check if empty
                if(!numberr.isEmpty()){ //field not empty
                    //check length
                    if(!(numberr.length()<10)){ //number not <10
                        progressDialog.setMessage("Verifying...");
                        progressDialog.show();

                        //get data from reference
                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //check if mobile exists
                                if(snapshot.child(phonenumber).exists()){ //number exists

                                    //check if logistics account
                                    if(snapshot.child(phonenumber).child("role").getValue().equals("Retailer")){ //retailer number
                                        progressDialog.dismiss();
                                        //direct to otp verification w/ mobile
                                        startActivity(new Intent(LoginPhoneRetailer.this, SendOTPRetailer.class).putExtra("phonenumber",phonenumber));
                                        finish();
                                    }else{ //non-retailer number
                                        progressDialog.dismiss();
                                        num.setError("Account is not a Retailer");
                                    }
                                }else{ //number doesn't exists
                                    progressDialog.dismiss();
                                    //database error
                                    num.setError("Number Doesn't Exists");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog.dismiss();
                                //database error
                                Toast.makeText(LoginPhoneRetailer.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{ //number is <10
                        progressDialog.dismiss();
                        num.setError("must be 10 characters long");
                    }
                }else{ //empty field
                    progressDialog.dismiss();
                    num.setError("Field is empty");
                }

            }
        });

        //registration clicked
        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to registration
                startActivity(new Intent(LoginPhoneRetailer.this, RegistrationRetailer.class));
                finish();
            }
        });

        //email login clicked
        signinemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to email login
                startActivity(new Intent(LoginPhoneRetailer.this, LoginEmailRetailer.class));
                finish();
            }
        });

    }

    //HIDE KEYBOARD
    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager hide = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            hide.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //DISABLE BACK PRES
    public void onBackPressed(){  }
}