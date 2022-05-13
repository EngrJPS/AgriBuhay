package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class VerifyPhoneRetailer extends AppCompatActivity {
    //DECLARE VARIABLES
    //xml
    Button Resend;
    Button verify;
    EditText entercode;
    ProgressDialog progress;
    TextView txt;
    //strings
    String phonenumber;
    String verificationId;
    //firebase
    FirebaseAuth FAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_phone_retailer);
        //TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Verify Mobile Number");

        //PROGRESS DIALOG
        progress = new ProgressDialog(VerifyPhoneRetailer.this);
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
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                hideKeyboard();

                //get string
                String code = entercode.getText().toString().trim();

                //hide resend button
                Resend.setVisibility(View.INVISIBLE);

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
        linkCredential(credential);
    }

    //LINK MOBILE CREDENTIAL
    private void linkCredential(PhoneAuthCredential credential) {
        progress.setMessage("Verifying....");
        progress.show();

        //link mobile number
        FAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneRetailer.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //check linked account
                        if (task.isSuccessful()) {
                            progress.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(VerifyPhoneRetailer.this);
                            builder.setCancelable(false);
                            builder.setMessage("Mobile number verified successfully");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //direct to main menu
                                    startActivity(new Intent(VerifyPhoneRetailer.this,MainMenu.class));
                                    finish();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            progress.dismiss();
                            //not linked
                            ReusableCodeForAll.ShowAlert(VerifyPhoneRetailer.this,"Error",task.getException().getMessage());
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
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            //resend-otp
            super.onCodeSent(s, forceResendingToken);
            //otp
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //get otp
            String code = phoneAuthCredential.getSmsCode();

            //auto-fill
            if (code != null) {
                entercode.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progress.dismiss();
            //otp error
            Toast.makeText(VerifyPhoneRetailer.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
