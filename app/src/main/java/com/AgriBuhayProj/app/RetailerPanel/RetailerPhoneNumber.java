package com.AgriBuhayProj.app.RetailerPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.AgriBuhayProj.app.R;
import com.hbb20.CountryCodePicker;

public class RetailerPhoneNumber extends AppCompatActivity {

    EditText num;
    CountryCodePicker cpp;
    Button SendOTP;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer_phonenumber);

        num=(EditText)findViewById(R.id.phonenumber);
        cpp=(CountryCodePicker)findViewById(R.id.Countrycode);
        SendOTP=(Button)findViewById(R.id.sendotp);

        SendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number=num.getText().toString().trim();
                String phonenumber= cpp.getSelectedCountryCodeWithPlus() + number;
                Intent intent=new Intent(RetailerPhoneNumber.this, RetailerPhoneSendOTP.class);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);
                finish();
            }
        });
    }
}
