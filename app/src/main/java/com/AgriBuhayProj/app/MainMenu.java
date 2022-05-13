package com.AgriBuhayProj.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity {
    //DECLARE VARIABLES
    Button signinemail, signinphone, signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        //CONNECT XML
        signinemail = (Button) findViewById(R.id.SignwithEmail);
        signinphone = (Button) findViewById(R.id.SignwithPhone);
        signup = (Button) findViewById(R.id.SignUp);

        //BUTTON EVENTS
        //email clicked
        signinemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to email login + home string
                startActivity(new Intent(MainMenu.this, ChooseRole.class).putExtra("Home", "Email"));
                finish();
            }
        });
        //phone clicked
        signinphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to choose role + home string
                startActivity(new Intent(MainMenu.this, ChooseRole.class).putExtra("Home", "Phone"));
                finish();
            }
        });
        //registration clicked
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to choose role + home string
                startActivity(new Intent(MainMenu.this, ChooseRole.class).putExtra("Home", "SignUp"));
                finish();
            }
        });


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    //EXIT APP
    private Boolean backPress = false;
    public void onBackPressed(){
        if(backPress){
            finishAffinity();
        }
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        backPress = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPress = false;
            }
        },2000);
    }
}
