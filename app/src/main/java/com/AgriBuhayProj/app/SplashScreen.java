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

    ImageView pic1,pic2;
    Animation slide,fade;

    FirebaseAuth fAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        pic1 = findViewById(R.id.logo1);
        pic2 = findViewById(R.id.logo2);

        slide = AnimationUtils.loadAnimation(this,R.anim.slide);
        fade = AnimationUtils.loadAnimation(this,R.anim.fadein);

        pic1.setVisibility(View.GONE);
        pic2.setVisibility(View.GONE);

        pic1.setVisibility(View.VISIBLE);
        pic1.setAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pic2.setVisibility(View.VISIBLE);
                pic2.setAnimation(fade);

                //AUTO SIGN-IN FOR CURRENT USER
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //check logged-in user
                        fAuth = FirebaseAuth.getInstance();
                        if (fAuth.getCurrentUser() != null) {
                            if (fAuth.getCurrentUser().isEmailVerified()) {
                                fAuth = FirebaseAuth.getInstance();
                                databaseReference = firebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getUid() + "/Role");
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String role = dataSnapshot.getValue(String.class);
                                        //Todo database name role path Customer, Chef, DeliveryPerson
                                        if (role.equals("Customer")) {
                                            Intent n = new Intent(SplashScreen.this, CustomerFoodPanel_BottomNavigation.class);
                                            startActivity(n);
                                            finish();
                                        }
                                        if (role.equals("Chef")) {
                                            Intent a = new Intent(SplashScreen.this, ChefFoodPanel_BottomNavigation.class);
                                            startActivity(a);
                                            finish();
                                        }
                                        if (role.equals("DeliveryPerson")) {
                                            Intent intent = new Intent(SplashScreen.this, Delivery_FoodPanelBottomNavigation.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(SplashScreen.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                });
                            } else {
                                //if user credentials aren't verified
                                AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                                builder.setMessage("Check whether you have verified your details, Otherwise please verify");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                        Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                                fAuth.signOut();
                            }
                        } else {
                            //new user
                            Intent intent = new Intent(SplashScreen.this, MainMenu.class);
                            startActivity(intent);
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