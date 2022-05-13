package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordRetailer extends AppCompatActivity {
    //DECLARE VARIABLES
    TextInputLayout forgetpassword;
    Button Reset;
    FirebaseAuth FAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_retailer);
        //CONNECT XML
        forgetpassword = (TextInputLayout) findViewById(R.id.Emailid);
        Reset = (Button) findViewById(R.id.button2);

        //FIREBASE AUTHENTICATION INSTANCE
        FAuth = FirebaseAuth.getInstance();

        //BUTTON EVENT
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                hideKeyboard();

                //show progress dialog
                final ProgressDialog mDialog = new ProgressDialog(ForgotPasswordRetailer.this);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setCancelable(false);
                mDialog.setMessage("Sending password reset link...");
                mDialog.show();

                //send reset link to email
                FAuth.sendPasswordResetEmail(forgetpassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //check if link was sent
                        if (task.isSuccessful()) {
                            //close progress dialog
                            mDialog.dismiss();

                            //show alert dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordRetailer.this);
                            builder.setCancelable(false);
                            builder.setMessage("Password reset link sent. Check your email");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    //direct to main menu
                                    startActivity(new Intent(ForgotPasswordRetailer.this, MainMenu.class));
                                    finish();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else {
                            //close progress dialog
                            mDialog.dismiss();
                            //show error
                            ReusableCodeForAll.ShowAlert(ForgotPasswordRetailer.this,"Error",task.getException().getMessage());
                        }
                    }
                });
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

    //BACK PRESS
    @Override
    public void onBackPressed() {finish();}
}
