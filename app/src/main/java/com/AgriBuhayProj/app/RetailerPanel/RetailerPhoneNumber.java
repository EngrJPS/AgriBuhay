package com.AgriBuhayProj.app.RetailerPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.AgriBuhayProj.app.R;
import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

//CHANGE PHONE NUMBER
public class RetailerPhoneNumber extends AppCompatActivity {
    //VARIABLES
    EditText num;
    CountryCodePicker cpp;
    Button SendOTP;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer_phonenumber);
        //CONNECT XML
        num = findViewById(R.id.phonenumber);
        cpp = findViewById(R.id.Countrycode);
        SendOTP = findViewById(R.id.sendotp);

        //SEND OTP
        SendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                hideKeyboard();

                //get string value
                number=num.getText().toString().trim();
                String phonenumber= cpp.getSelectedCountryCodeWithPlus() + number;

                //string validation
                if(number.isEmpty()){
                    num.setError("Empty Field");
                }else if(number.length() <10){
                    num.setError("Invalid mobile number");
                }else{
                    //direct to send otp
                    startActivity(new Intent(RetailerPhoneNumber.this, RetailerPhoneSendOTP.class).putExtra("phonenumber",phonenumber));
                    finish();
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
