package com.AgriBuhayProj.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    ImageView pic1,pic2;
    Animation slide,fade;

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

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashScreen.this, MainMenu.class));
                        finish();
                    }
                }, 2500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}