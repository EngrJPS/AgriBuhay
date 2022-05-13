package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import com.AgriBuhayProj.app.RetailerPanel.RetailerCartFragment;
import com.AgriBuhayProj.app.RetailerPanel.RetailerHomeFragment;
import com.AgriBuhayProj.app.RetailerPanel.RetailerOrderFragment;
import com.AgriBuhayProj.app.RetailerPanel.RetailerProfileFragment;
import com.AgriBuhayProj.app.RetailerPanel.RetailerTrackFragment;

import com.AgriBuhayProj.app.SendNotification.Token;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class ProductPanelBottomNavigation_Retailer extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_panel_bottom_navigation_retailer);

        //CONNECT XML
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);

        //NAVIGATION LISTENER
        navigationView.setOnNavigationItemSelectedListener(this);

        //TOKEN
        UpdateToken();

        //GET PAGE VALUE
        String name = getIntent().getStringExtra("PAGE");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //CHECK PAGE VALUE
        if (name != null) {
            if (name.equalsIgnoreCase("Homepage")) {
                //load retailer home
                loadFragment(new RetailerHomeFragment());
            } else if (name.equalsIgnoreCase("Preparingpage")) {
                //load retailer track order
                loadFragment(new RetailerTrackFragment());
            } else if (name.equalsIgnoreCase("Preparedpage")) {
                //load retailer track order
                loadFragment(new RetailerTrackFragment());
            } else if (name.equalsIgnoreCase("DeliverOrderpage")) {
                //load retailer track order
                loadFragment(new RetailerTrackFragment());
            } else if (name.equalsIgnoreCase("ThankYoupage")) {
                //load retailer home
                loadFragment(new RetailerHomeFragment());
            }
        } else {
            //load retailer home
            loadFragment(new RetailerHomeFragment());
        }
    }

    //GENERATE TOKEN
    private void UpdateToken() {
        //get current user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //generate token
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        //TODO (EDITED)
        //save token to database
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(token);

    }

    //LOAD FRAGMENT
    private boolean loadFragment(Fragment fragment) {
        //check fragment
        if (fragment != null) {
            //set fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    //BOTTOM NAVIGATION SELECTION
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        //get current fragment id
        switch (menuItem.getItemId()) {
            case R.id.Home:
                fragment = new RetailerHomeFragment();
                break;

            case R.id.Cart:
                fragment = new RetailerCartFragment();
                break;

            case R.id.Order:
                fragment = new RetailerOrderFragment();
                break;

            case R.id.Track:
                fragment = new RetailerTrackFragment();
                break;

            case R.id.Profile:
                fragment = new RetailerProfileFragment();
                break;

        }

        //return fragment
        return loadFragment(fragment);
    }

    //BACK PRESS
    private boolean pressedOnce = false;
    @Override
    public void onBackPressed() {
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        //check if home view
        if(navigationView.getSelectedItemId()==R.id.Home){
            //check if double pressed
            if(pressedOnce){
                super.onBackPressed();//exit app
            }

            //back pressed once
            this.pressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            //loop
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    pressedOnce = false;
                }
            },2000);
        }else{
            navigationView.setSelectedItemId(R.id.Home); //direct to home view
        }
    }
}
