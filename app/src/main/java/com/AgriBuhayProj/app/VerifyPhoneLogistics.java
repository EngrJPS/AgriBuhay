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

public class VerifyPhoneLogistics extends AppCompatActivity {

    String verificationId;
    FirebaseAuth FAuth;
    Button verify, Resend;
    TextView txt;
    EditText entercode;
    String phonenumber;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_phone_logistics);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Verify");

        //PROGRESS DIALOG
        progress = new ProgressDialog(VerifyPhoneLogistics.this);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        phonenumber = getIntent().getStringExtra("phonenumber").trim();

        entercode = (EditText) findViewById(R.id.Pnumber);
        txt = (TextView) findViewById(R.id.textt);
        Resend = (Button) findViewById(R.id.Resendcode);
        FAuth = FirebaseAuth.getInstance();
        verify = (Button) findViewById(R.id.Verifycode);

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
                txt.setText("Resend Code within " + millisUntilFinished/1000 + " Seconds");
            }
            @Override
            public void onFinish() {
                //hide
                txt.setVisibility(View.INVISIBLE);
                //show resend button
                Resend.setVisibility(View.VISIBLE);
            }
        }.start();

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String code = entercode.getText().toString().trim();
                Resend.setVisibility(View.INVISIBLE);

                if (code.isEmpty() && code.length() < 6) {
                    entercode.setError("Enter code");
                    entercode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

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

    private void resendOTP(String phonenumber) {
        sendVerificationCode(phonenumber);
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        linkCredential(credential);
    }

    private void linkCredential(PhoneAuthCredential credential) {
        progress.setMessage("Verifying....");
        progress.show();
        FAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneLogistics.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progress.dismiss();
                            Intent intent = new Intent(VerifyPhoneLogistics.this, MainMenu.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progress.dismiss();
                            ReusableCodeForAll.ShowAlert(VerifyPhoneLogistics.this, "Error", task.getException().getMessage());
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

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                entercode.setText(code);
                /*verifyCode(code);*/

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progress.dismiss();
            Toast.makeText(VerifyPhoneLogistics.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
