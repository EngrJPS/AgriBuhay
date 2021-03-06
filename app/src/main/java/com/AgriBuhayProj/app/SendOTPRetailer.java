package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTPRetailer extends AppCompatActivity {
    //DECLARE VARIABLES
    //xml
    Button verify, Resend;
    TextView txt;
    EditText entercode;
    ProgressDialog progress;

    //strings
    String phonenumber;
    String verificationId;

    //firebase
    FirebaseAuth FAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_otp_retailer);
        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("OTP Verification");

        //PROGRESS DIALOG
        progress = new ProgressDialog(SendOTPRetailer.this);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        //GET MOBILE STRING
        phonenumber = getIntent().getStringExtra("phonenumber").trim();

        //CONNECT XML
        entercode = findViewById(R.id.phoneno);
        txt = findViewById(R.id.text);
        Resend = findViewById(R.id.Resendotp);
        verify = findViewById(R.id.Verify);

        //FIREBASE INSTANCE
        FAuth = FirebaseAuth.getInstance();

        //STARTUP (OTP)
        //send otp
        sendVerificationCode(phonenumber);
        //count down timer
        new CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                //hide resend button
                Resend.setVisibility(View.INVISIBLE);
                //show timer
                txt.setVisibility(View.VISIBLE);
                //counter
                txt.setText("Resend Code within " + millisUntilFinished/1000 + " Seconds");
            }
            @Override
            public void onFinish() {
                //hide timer
                txt.setVisibility(View.INVISIBLE);
                //show resend button
                Resend.setVisibility(View.VISIBLE);
            }
        }.start();

        //BUTTON EVENTS
        //verify clicked
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                hideKeyboard();

                //hide resend button
                Resend.setVisibility(View.INVISIBLE);

                //get string
                String code = entercode.getText().toString().trim();

                //check string validity
                if (code.isEmpty() && code.length() < 6) {
                    entercode.setError("Enter code");
                    entercode.requestFocus();
                    return;
                }

                //verify code
                verifyCode(code);
            }

        });

        //resend clicked
        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide resend button
                Resend.setVisibility(View.INVISIBLE);
                //resend OTP
                resendOTP(phonenumber);
                //timer
                new CountDownTimer(60000, 1000) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTick(long millisUntilFinished) {
                        txt.setVisibility(View.VISIBLE);
                        txt.setText("Resend Code within " + millisUntilFinished / 1000 + " Seconds");
                    }

                    @Override
                    public void onFinish() {
                        Resend.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.INVISIBLE);
                    }
                }.start();
            }
        });
    }

    //RESEND OTP
    private void resendOTP(String phonenumber) {
        sendVerificationCode(phonenumber);
    }

    //VERIFY CODE
    private void verifyCode(String code) {
        //get credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInwithCredential(credential);
    }

    //LOGIN
    private void signInwithCredential(PhoneAuthCredential credential) {
        progress.setMessage("Verifying....");
        progress.show();

        FAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //logged in
                            if (task.isSuccessful()) {
                                progress.dismiss();

                                //direct to retailer home
                                startActivity(new Intent(SendOTPRetailer.this, ProductPanelBottomNavigation_Retailer.class));
                                Toast.makeText(SendOTPRetailer.this, "Logged In", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                //not logged in
                                ReusableCodeForAll.ShowAlert(SendOTPRetailer.this,"Error",task.getException().getMessage());
                            }
                        }
                    });
    }

    //SEND VERIFICATION CODE
    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    //PHONE CALLBACKS
        private PhoneAuthProvider.OnVerificationStateChangedCallbacks
                mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                //otp
                verificationId=s;
            }
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //get otp
                String code = phoneAuthCredential.getSmsCode();

                //auto-fill
                if (code != null) {
                    entercode.setText(code);
                    /*verifyCode(code);*/
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                //otp error
                Toast.makeText(SendOTPRetailer.this, "THis 2:" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
    };

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