package com.AgriBuhayProj.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseOne extends AppCompatActivity {

    Button Producer, Retailer, DeliveryPerson;
    Intent intent;
    String type;
    ConstraintLayout bgimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_one);
        Producer = (Button) findViewById(R.id.producer);
        DeliveryPerson = (Button) findViewById(R.id.delivery);
        Retailer = (Button) findViewById(R.id.retailer);
        intent = getIntent();
        type = intent.getStringExtra("Home").toString().trim();

        //TODO Find something that can replace with this line of code
        Producer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("Email")) {
                    Intent loginemail = new Intent(ChooseOne.this, ProducerLogin.class);
                    startActivity(loginemail);
                    /*finish();*/
                }
                if (type.equals("Phone")) {
                    Intent loginphone = new Intent(ChooseOne.this, ProducerLoginPhone.class);
                    startActivity(loginphone);
                    /*finish();*/
                }
                if (type.equals("SignUp")) {
                    Intent Register = new Intent(ChooseOne.this, RegistrationProducer.class);
                    startActivity(Register);

                }

            }
        });

        Retailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("Email")) {
                    Intent loginemailcust = new Intent(ChooseOne.this, Login.class);
                    startActivity(loginemailcust);
                    /*finish();*/
                }
                if (type.equals("Phone")) {
                    Intent loginphonecust = new Intent(ChooseOne.this, LoginPhone.class);
                    startActivity(loginphonecust);
                    /*finish();*/
                }
                if (type.equals("SignUp")) {
                    Intent Registercust = new Intent(ChooseOne.this, RegistrationRetailer.class);
                    startActivity(Registercust);
                }
            }
        });

        DeliveryPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("SignUp")) {
                    Intent Registerdelivery = new Intent(ChooseOne.this, RegistrationLogistics.class);
                    startActivity(Registerdelivery);
                }
                if (type.equals("Phone")) {
                    Intent loginphone = new Intent(ChooseOne.this, LogisticsLoginPhone.class);
                    startActivity(loginphone);
                    /*finish();*/
                }
                if (type.equals("Email")) {
                    Intent loginemail = new Intent(ChooseOne.this, LogisticsLogin.class);
                    startActivity(loginemail);
                    /*finish();*/
                }
            }
        });
    }

    public void onBackPressed(){
        finish();
    }
}
