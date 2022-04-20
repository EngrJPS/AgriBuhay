package com.AgriBuhayProj.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseRole extends AppCompatActivity {

    Button Producer, Retailer, DeliveryPerson;
    Intent intent;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_role);
        Producer = (Button) findViewById(R.id.producer);
        DeliveryPerson = (Button) findViewById(R.id.delivery);
        Retailer = (Button) findViewById(R.id.retailer);
        intent = getIntent();
        type = intent.getStringExtra("Home").toString().trim();

        Producer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(type){
                    case "Email":
                        startActivity(new Intent(ChooseRole.this, LoginEmailProducer.class));
                        break;
                    case "Phone":
                        startActivity(new Intent(ChooseRole.this, LoginPhoneProducer.class));
                        break;
                    case "SignUp":
                        startActivity(new Intent(ChooseRole.this, RegistrationProducer.class));
                        break;
                }
                /*if (type.equals("Email")) {
                    Intent emailProducer = new Intent(ChooseRole.this, LoginEmailProducer.class);
                    startActivity(emailProducer);
                }
                if (type.equals("Phone")) {
                    Intent phoneProducer = new Intent(ChooseRole.this, LoginPhoneProducer.class);
                    startActivity(phoneProducer);
                }
                if (type.equals("SignUp")) {
                    Intent registerProducer = new Intent(ChooseRole.this, RegistrationProducer.class);
                    startActivity(registerProducer);
                }*/

            }
        });

        Retailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(type){
                    case "Email":
                        startActivity(new Intent(ChooseRole.this, LoginEmailRetailer.class));
                        break;
                    case "Phone":
                        startActivity(new Intent(ChooseRole.this, LoginPhoneRetailer.class));
                        break;
                    case "SignUp":
                        startActivity(new Intent(ChooseRole.this, RegistrationRetailer.class));
                        break;
                }
                /*if (type.equals("Email")) {
                    Intent emailRetailer = new Intent(ChooseRole.this, LoginEmailRetailer.class);
                    startActivity(emailRetailer);
                }
                if (type.equals("Phone")) {
                    Intent phoneRetailer = new Intent(ChooseRole.this, LoginPhoneRetailer.class);
                    startActivity(phoneRetailer);
                }
                if (type.equals("SignUp")) {
                    Intent registerRetailer = new Intent(ChooseRole.this, RegistrationRetailer.class);
                    startActivity(registerRetailer);
                }*/
            }
        });

        DeliveryPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(type){
                    case "Email":
                        startActivity(new Intent(ChooseRole.this, LoginEmailLogistics.class));
                        break;
                    case "Phone":
                        startActivity(new Intent(ChooseRole.this, LoginPhoneLogistics.class));
                        break;
                    case "SignUp":
                        startActivity(new Intent(ChooseRole.this, RegistrationLogistics.class));
                        break;
                }
                /*if (type.equals("SignUp")) {
                    Intent registerLogistics = new Intent(ChooseRole.this, RegistrationLogistics.class);
                    startActivity(registerLogistics);
                }
                if (type.equals("Phone")) {
                    Intent phoneLogistics = new Intent(ChooseRole.this, LoginPhoneLogistics.class);
                    startActivity(phoneLogistics);
                }
                if (type.equals("Email")) {
                    Intent emailLogistics = new Intent(ChooseRole.this, LoginEmailLogistics.class);
                    startActivity(emailLogistics);
                }*/
            }
        });
    }

    public void onBackPressed(){
        finish();
    }
}
