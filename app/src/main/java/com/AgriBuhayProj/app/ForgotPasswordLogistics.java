package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordLogistics extends AppCompatActivity {

    TextInputLayout forgetpassword;
    Button Reset;
    FirebaseAuth FAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_logistics);

        forgetpassword = (TextInputLayout) findViewById(R.id.forgotEmailid);
        Reset = (Button) findViewById(R.id.forgotreset);

        FAuth = FirebaseAuth.getInstance();
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(ForgotPasswordLogistics.this);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setMessage("Logging in...");
                mDialog.show();


                FAuth.sendPasswordResetEmail(forgetpassword.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mDialog.dismiss();
                            ReusableCodeForAll.ShowAlert(ForgotPasswordLogistics.this, "", "Password has been sent to your Email");
                        } else {
                            mDialog.dismiss();
                            ReusableCodeForAll.ShowAlert(ForgotPasswordLogistics.this, "Error", task.getException().getMessage());
                        }
                    }
                });
            }
        });

    }
}
