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

    Button signinemail, signinphone, signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        signinemail = (Button) findViewById(R.id.SignwithEmail);
        signinphone = (Button) findViewById(R.id.SignwithPhone);
        signup = (Button) findViewById(R.id.SignUp);

        signinemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, ChooseRole.class).putExtra("Home", "Email"));
                finish();
            }
        });

        signinphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, ChooseRole.class).putExtra("Home", "Phone"));
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    FirebaseAuth fAuth;
    private Boolean backPress = false;
    //EXIT APP
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
