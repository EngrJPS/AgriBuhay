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

public class RetailerForgotPassword extends AppCompatActivity {


    TextInputLayout emaillid;
    Button Reset;
    FirebaseAuth Fauth;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer_forgot_password);

        emaillid=(TextInputLayout)findViewById(R.id.email);
        Reset=(Button)findViewById(R.id.reset);

        Fauth=FirebaseAuth.getInstance();

        emaillid.setErrorEnabled(false);
        emaillid.setError("");

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();

                email = emaillid.getEditText().getText().toString();
                if(email.isEmpty()){
                    emaillid.setErrorEnabled(true);
                    emaillid.setError("Empty Field");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emaillid.setErrorEnabled(true);
                    emaillid.setError("Invalid email format");
                }else{
                    final ProgressDialog progressDialog = new ProgressDialog(RetailerForgotPassword.this);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("Sending password reset link...");
                    progressDialog.show();

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
                                        Fauth.signOut();
                                        startActivity(new Intent(RetailerForgotPassword.this, MainMenu.class));
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
