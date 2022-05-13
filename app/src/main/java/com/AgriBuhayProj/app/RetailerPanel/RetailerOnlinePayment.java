package com.AgriBuhayProj.app.RetailerPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.AgriBuhayProj.app.R;
import com.google.android.material.textfield.TextInputLayout;

//ONLINE CARD PAYMENT
public class RetailerOnlinePayment extends AppCompatActivity {
    //VARIABLES
    TextInputLayout cardname, cardnumber, expirydate, cvv;
    Button Addcard;
    String name, number, date, CVV;
    String RandomUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer_online_payment);
        //CONNECT XML
        Addcard = findViewById(R.id.addcard);
        cardname = findViewById(R.id.nameoncard);
        cardnumber = findViewById(R.id.cardnumber);
        expirydate = findViewById(R.id.expirydate);
        cvv = findViewById(R.id.CVV);

        //TRACKING NUMBER
        RandomUID = getIntent().getStringExtra("randomUID");

        //ADD CREDIT/DEBIT CARD
        Addcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET STRING VALUES
                name = cardname.getEditText().getText().toString().trim();
                number = cardnumber.getEditText().getText().toString().trim();
                date = expirydate.getEditText().getText().toString().trim();
                CVV = cvv.getEditText().getText().toString().trim();

                //CHECK VALIDITY
                if (valid()) {
                    //direct to payment otp
                    Intent intent = new Intent(RetailerOnlinePayment.this, RetailerPaymentOTP.class);
                    intent.putExtra("RandomUID",RandomUID);
                    startActivity(intent);
                }

            }
        });
    }

    //VALIDATION
    private boolean valid() {
        cardname.setErrorEnabled(false);
        cardname.setError("");
        cardnumber.setErrorEnabled(false);
        cardnumber.setError("");
        expirydate.setErrorEnabled(false);
        expirydate.setError("");
        cvv.setErrorEnabled(false);
        cvv.setError("");


        boolean isValidname = false, isValidlnumber = false, isValidexpiry = false, isValidcvv = false, isvalid = false;
        if (TextUtils.isEmpty(name)) {
            cardname.setErrorEnabled(true);
            cardname.setError("Cardname is required");
        } else {
            isValidname = true;
        }
        if (TextUtils.isEmpty(number)) {
            cardnumber.setErrorEnabled(true);
            cardnumber.setError("Cardnumber is required");
        } else {
            if (number.length() < 16) {
                cardnumber.setErrorEnabled(true);
                cardnumber.setError("Invalid number");
            } else {
                isValidlnumber = true;
            }
        }
        if (TextUtils.isEmpty(date)) {
            expirydate.setErrorEnabled(true);
            expirydate.setError("Expiry date is required");
        } else {
            isValidexpiry = true;

        }
        if (TextUtils.isEmpty(CVV)) {
            cvv.setErrorEnabled(true);
            cvv.setError("CVV is required");
        } else {
            if (CVV.length() < 3) {
                cvv.setErrorEnabled(true);
                cvv.setError("Invalid CVV number");
            } else {
                isValidcvv = true;
            }
        }
        isvalid = (isValidname && isValidlnumber && isValidexpiry && isValidcvv) ? true : false;
        return isvalid;

    }
}
