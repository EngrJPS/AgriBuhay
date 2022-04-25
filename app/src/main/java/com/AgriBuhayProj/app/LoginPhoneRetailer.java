package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    EditText num;
    TextView txtsignup;
    Button sendotp,signinemail;
    CountryCodePicker cpp;
    ProgressDialog progressDialog;
    FirebaseAuth FAuth;
    DatabaseReference dbRef;
    String numberr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_phone_retailer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login As Retailer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        num=(EditText)findViewById(R.id.number);
        sendotp=(Button)findViewById(R.id.otp);
        cpp=(CountryCodePicker)findViewById(R.id.CountryCode);
        signinemail=(Button)findViewById(R.id.btnEmail);
        txtsignup=(TextView)findViewById(R.id.acsignup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        FAuth=FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Mobile");

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                numberr=num.getText().toString().trim();
                String phonenumber= cpp.getSelectedCountryCodeWithPlus() + numberr;

                if(!numberr.isEmpty()){ //field not empty
                    if(!(numberr.length()<10)){ //number not <10
                        progressDialog.setMessage("Verifying...");
                        progressDialog.show();
                        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child(phonenumber).exists()){ //number exists
                                    if(snapshot.child(phonenumber).child("role").getValue().equals("Retailer")){ //retailer number
                                        progressDialog.dismiss();
                                        startActivity(new Intent(LoginPhoneRetailer.this, SendOTPRetailer.class).putExtra("phonenumber",phonenumber));
                                        finish();
                                    }else{ //non-retailer number
                                        progressDialog.dismiss();
                                        num.setError("Account is not a Retailer");
                                    }
                                }else{ //number doesn't exists
                                    progressDialog.dismiss();
                                    num.setError("Number Doesn't Exists");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressDialog.dismiss();
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

        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(LoginPhoneRetailer.this, RegistrationRetailer.class);
                startActivity(a);
                finish();
            }
        });

        signinemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent em=new Intent(LoginPhoneRetailer.this, LoginEmailRetailer.class);
                startActivity(em);
                finish();
            }
        });

    }

    public void onBackPressed(){  }
}