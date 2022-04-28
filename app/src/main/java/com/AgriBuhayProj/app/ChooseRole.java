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
                        finish();
                        break;
                    case "Phone":
                        startActivity(new Intent(ChooseRole.this, LoginPhoneProducer.class));
                        finish();
                        break;
                    case "SignUp":
                        startActivity(new Intent(ChooseRole.this, RegistrationProducer.class));
                        finish();
                        break;
                }
            }
        });

        Retailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(type){
                    case "Email":
                        startActivity(new Intent(ChooseRole.this, LoginEmailRetailer.class));
                        finish();
                        break;
                    case "Phone":
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

        DeliveryPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(type){
                    case "Email":
                        startActivity(new Intent(ChooseRole.this, LoginEmailLogistics.class));
                        finish();
                        break;
                    case "Phone":
                        startActivity(new Intent(ChooseRole.this, LoginPhoneLogistics.class));
                        finish();
                        break;
                    case "SignUp":
                        startActivity(new Intent(ChooseRole.this, RegistrationLogistics.class));
                        finish();
                        break;
                }
            }
        });
    }

    public void onBackPressed(){
        startActivity(new Intent(ChooseRole.this, MainMenu.class));
        finish();
    }
}
