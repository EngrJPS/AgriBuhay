package com.AgriBuhayProj.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class LoginPhoneLogistics extends AppCompatActivity {


    EditText num;
    Button sendotp, signinemail;
    TextView txtsignup;
    CountryCodePicker cpp;
    FirebaseAuth FAuth;
    String numberr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_phone_delivery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login As Logistics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        num = (EditText) findViewById(R.id.Dphonenumber);
        sendotp = (Button) findViewById(R.id.Sendotp);
        cpp = (CountryCodePicker) findViewById(R.id.countrycode);
        signinemail = (Button) findViewById(R.id.DbtnEmail);
        txtsignup = (TextView) findViewById(R.id.Signupif);

        FAuth = FirebaseAuth.getInstance();

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberr = num.getText().toString().trim();
                String phonenumber = cpp.getSelectedCountryCodeWithPlus() + numberr;
                Intent b = new Intent(LoginPhoneLogistics.this, SendOTPLogistics.class);
                b.putExtra("phonenumber", phonenumber);
                startActivity(b);
                finish();

            }
        });


        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(LoginPhoneLogistics.this, RegistrationLogistics.class);
                startActivity(a);
                finish();
            }
        });

        signinemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent em = new Intent(LoginPhoneLogistics.this, LoginEmailLogistics.class);
                startActivity(em);
                finish();
            }
        });

    }

    public void onBackPressed(){ finish(); }
}
