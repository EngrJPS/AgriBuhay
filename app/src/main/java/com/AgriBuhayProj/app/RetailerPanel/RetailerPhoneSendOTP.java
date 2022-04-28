package com.AgriBuhayProj.app.RetailerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.AgriBuhayProj.app.MainMenu;
import com.AgriBuhayProj.app.Models.Retailer;
import com.AgriBuhayProj.app.R;
import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.AgriBuhayProj.app.SendOTPLogistics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RetailerPhoneSendOTP extends AppCompatActivity {

    String newMobile,oldMobile;
    EditText entercode;
    String retailerID,verificationId;
    Button verify, Resend;
    TextView txt;

    FirebaseAuth FAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer_phone_send_otp);

        newMobile = getIntent().getStringExtra("phonenumber").trim();

        entercode = (EditText) findViewById(R.id.phoneno);
        txt = (TextView) findViewById(R.id.text);
        Resend = (Button) findViewById(R.id.Resendotp);
        FAuth = FirebaseAuth.getInstance();
        Resend.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);
        verify = (Button) findViewById(R.id.Verify);

        FAuth = FirebaseAuth.getInstance();
        user = FAuth.getCurrentUser();

        oldMobile = user.getPhoneNumber();
        retailerID = FAuth.getCurrentUser().getUid();

        //STARTUP (OTP)
        //send otp
        sendVerificationCode(newMobile);
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
                Resend.setVisibility(View.INVISIBLE);
                String code = entercode.getText().toString().trim();
                if (code.isEmpty()){
                    entercode.setError("Empty Field");
                    entercode.requestFocus();
                } else if (code.length() < 6) {
                    entercode.setError("Invalid Code");
                    entercode.requestFocus();
                }
                verifyCode(code,oldMobile);
            }
        });

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resend.setVisibility(View.INVISIBLE);

                resendOTP(newMobile);

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

    private void verifyCode(String code, String oldMobile) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        updatePhone(credential,oldMobile);
    }

    private void updatePhone(PhoneAuthCredential credential, String oldMobile){
        ProgressDialog progressDialog = new ProgressDialog(RetailerPhoneSendOTP.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Verifying");
        progressDialog.show();

        user.updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    databaseReference = FirebaseDatabase.getInstance().getReference("Retailer");
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("Mobile", oldMobile);
                    databaseReference.child(retailerID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(RetailerPhoneSendOTP.this, "Mobile number updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RetailerPhoneSendOTP.this, "Update Failed", Toast.LENGTH_SHORT).show();
                            ReusableCodeForAll.ShowAlert(RetailerPhoneSendOTP.this,e.toString(),e.getMessage());
                        }
                    });
                    /*user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(RetailerPhoneSendOTP.this, "Mobile number updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                progressDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(RetailerPhoneSendOTP.this,task.getException().toString(),task.getException().getMessage());
                            }
                        }
                    });*/
                }else{
                    progressDialog.dismiss();
                    ReusableCodeForAll.ShowAlert(RetailerPhoneSendOTP.this,task.getException().toString(),task.getException().getMessage());
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
            Toast.makeText(RetailerPhoneSendOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() { }
}

