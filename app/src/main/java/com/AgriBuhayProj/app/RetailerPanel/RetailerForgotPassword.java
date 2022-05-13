package com.AgriBuhayProj.app.RetailerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.AgriBuhayProj.app.MainMenu;
import com.AgriBuhayProj.app.R;
import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

//CHANGE PASSWORD
public class RetailerForgotPassword extends AppCompatActivity {
    //VARIABLES
    TextInputLayout emaillid;
    Button Reset;

    FirebaseAuth Fauth;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer_forgot_password);
        //CONNECT XML
        emaillid = findViewById(R.id.email);
        Reset = findViewById(R.id.reset);

        //INSTANCE
        Fauth=FirebaseAuth.getInstance();

        //SET ERROR
        emaillid.setErrorEnabled(false);
        emaillid.setError("");

        //RESET PASSWORD
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                hideKeyboard();

                //get string value
                email = emaillid.getEditText().getText().toString();

                //string validation
                if(email.isEmpty()){
                    emaillid.setErrorEnabled(true);
                    emaillid.setError("Empty Field");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emaillid.setErrorEnabled(true);
                    emaillid.setError("Invalid email format");
                }else{
                    //progress dialog
                    final ProgressDialog progressDialog = new ProgressDialog(RetailerForgotPassword.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Sending password reset link...");
                    progressDialog.show();

                    //send reset password
                    Fauth.sendPasswordResetEmail(emaillid.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                AlertDialog.Builder builder = new AlertDialog.Builder(RetailerForgotPassword.this);
                                builder.setMessage("Password has been sent to your Email");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        //logout user
                                        Fauth.signOut();
                                        //direct to main menu
                                        startActivity(new Intent(RetailerForgotPassword.this, MainMenu.class));
                                        finish();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                progressDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(RetailerForgotPassword.this,"Error",task.getException().getMessage());
                            }
                        }
                    });
                }
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
    public void onBackPressed() {
        finish();
    }
}
