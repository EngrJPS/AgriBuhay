package com.AgriBuhayProj.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {
    //DECLARE VARIABLES
    //xml
    ImageView pic1,pic2;
    Animation slide,fade;

    //firebase
    FirebaseAuth fAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //CONNECT XML
        pic1 = findViewById(R.id.logo1);
        pic2 = findViewById(R.id.logo2);

        //FIREBASE INSTANCE
        firebaseDatabase = FirebaseDatabase.getInstance();

        //SET ANIMATIONS
        slide = AnimationUtils.loadAnimation(this,R.anim.slide);
        fade = AnimationUtils.loadAnimation(this,R.anim.fadein);

        //HIDE LOGO AT START
        pic1.setVisibility(View.GONE);
        pic2.setVisibility(View.GONE);

        //SLIDE ANIMATION
        pic1.setVisibility(View.VISIBLE);
        pic1.setAnimation(slide);

        //SLIDE LISTENER
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                //FADE ANIMATION
                pic2.setVisibility(View.VISIBLE);
                pic2.setAnimation(fade);

                //AUTO SIGN-IN FOR CURRENT USER
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //firebase authentication instance
                        fAuth = FirebaseAuth.getInstance();

                        //check current user
                        if (fAuth.getCurrentUser() != null) {

                            //check user email verification
                            if (fAuth.getCurrentUser().isEmailVerified()) {
                                fAuth = FirebaseAuth.getInstance();

                                //user role reference
                                databaseReference = firebaseDatabase.getReference("User").child(FirebaseAuth.getInstance().getUid() + "/Role");

                                //data snapshot
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //get role using String model
                                        String role = dataSnapshot.getValue(String.class);

                                        //assume role
                                        assert  role != null;

                                        //check role
                                        if (role.equals("Retailer")) {
                                            //direct to retailer home
                                            startActivity(new Intent(SplashScreen.this, ProductPanelBottomNavigation_Retailer.class));
                                            finish();
                                        }
                                        if (role.equals("Producer")) {
                                            //direct to producer home
                                            startActivity(new Intent(SplashScreen.this, ProductPanelBottomNavigation_Producer.class));
                                            finish();
                                        }
                                        if (role.equals("Logistics")) {
                                            //direct to logistics home
                                            startActivity(new Intent(SplashScreen.this, ProductPanelBottomNavigation_Logistics.class));
                                            finish();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        //database error
                                        Toast.makeText(SplashScreen.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                //non-verified credentials
                                AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                                builder.setMessage("Check whether you have verified your details, Otherwise please verify");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        //direct to main menu
                                        startActivity(new Intent(SplashScreen.this, MainMenu.class));
                                        finish();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();

                                //sign-out current user
                                fAuth.signOut();
                            }
                        } else {
                            //new user
                            startActivity(new Intent(SplashScreen.this, MainMenu.class));
                            finish();
                        }

                    }
                }, 3000);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}