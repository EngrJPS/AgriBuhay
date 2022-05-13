package com.AgriBuhayProj.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseRole extends AppCompatActivity {
    //DECLARE VARIABLES
    Button Producer, Retailer, DeliveryPerson;
    Intent intent;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_role);

        //CONNECT XML
        Producer = findViewById(R.id.producer);
        DeliveryPerson = findViewById(R.id.delivery);
        Retailer = findViewById(R.id.retailer);

        //GET STRING FROM MAIN MENU
        intent = getIntent();
        type = intent.getStringExtra("Home").toString().trim();

        //BUTTON EVENTS
        //producer clicked
        Producer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check string from main menu
                switch(type){
                    case "Email":
                        //direct to email login
                        startActivity(new Intent(ChooseRole.this, LoginEmailProducer.class));
                        finish();
                        break;
                    case "Phone":
                        //direct to phone login
                        startActivity(new Intent(ChooseRole.this, LoginPhoneProducer.class));
                        finish();
                        break;
                    case "SignUp":
                        //direct to registration
                        startActivity(new Intent(ChooseRole.this, RegistrationProducer.class));
                        finish();
                        break;
                }
            }
        });
        //retailer clicked
        Retailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check string from main menu
                switch(type){
                    case "Email":
                        //direct to email login
                        startActivity(new Intent(ChooseRole.this, LoginEmailRetailer.class));
                        finish();
                        break;
                    case "Phone":
                        //direct to phone login
                        startActivity(new Intent(ChooseRole.this, LoginPhoneRetailer.class));
                        finish();
                        break;
                    case "SignUp":
                        startActivity(new Intent(ChooseRole.this, RegistrationRetailer.class));
                        finish();
                        break;
                }
            }
        });
        //logistics clicked
        DeliveryPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check string from main menu
                switch(type){
                    case "Email":
                        //direct to email login
                        startActivity(new Intent(ChooseRole.this, LoginEmailLogistics.class));
                        finish();
                        break;
                    case "Phone":
                        //direct to phone login
                        startActivity(new Intent(ChooseRole.this, LoginPhoneLogistics.class));
                        finish();
                        break;
                    case "SignUp":
                        //direct to registration
                        startActivity(new Intent(ChooseRole.this, RegistrationLogistics.class));
                        finish();
                        break;
                }
            }
        });
    }

    //BACK PRESS EVENT
    public void onBackPressed(){
        //direct to main menu
        startActivity(new Intent(ChooseRole.this, MainMenu.class));
        finish();
    }
}
