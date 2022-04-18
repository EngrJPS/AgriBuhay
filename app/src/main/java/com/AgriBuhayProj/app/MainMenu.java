package com.AgriBuhayProj.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.AgriBuhayProj.app.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenu extends AppCompatActivity {

    Button signinemail, signinphone, signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        signinemail = (Button) findViewById(R.id.SignwithEmail);
        signinphone = (Button) findViewById(R.id.SignwithPhone);
        signup = (Button) findViewById(R.id.SignUp);

        signinemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signemail = new Intent(MainMenu.this, ChooseOne.class);
                signemail.putExtra("Home", "Email");
                startActivity(signemail);
            }
        });

        signinphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signphone = new Intent(MainMenu.this, ChooseOne.class);
                signphone.putExtra("Home", "Phone");
                startActivity(signphone);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(MainMenu.this, ChooseOne.class);
                signup.putExtra("Home", "SignUp");
                startActivity(signup);
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
            /*super.onBackPressed();*/
            fAuth.signOut();
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
