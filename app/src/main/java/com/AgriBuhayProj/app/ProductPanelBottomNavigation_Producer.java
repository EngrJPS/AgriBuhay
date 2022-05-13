package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import com.AgriBuhayProj.app.ProducerPanel.ProducerHomeFragment;
import com.AgriBuhayProj.app.ProducerPanel.ProducerPendingOrdersFragment;
import com.AgriBuhayProj.app.ProducerPanel.ProducerProfileFragment;
import com.AgriBuhayProj.app.ProducerPanel.ProducerOrderFragment;

import com.AgriBuhayProj.app.SendNotification.Token;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class ProductPanelBottomNavigation_Producer extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_panel_bottom_navigation_producer);

        //CONNECT XML
        BottomNavigationView navigationView = findViewById(R.id.producer_bottom_navigation);

        //NAVIGATION LISTENER
        navigationView.setOnNavigationItemSelectedListener(this);

        //TOKEN
        UpdateToken();

        //GET PAGE VALUE
        String name = getIntent().getStringExtra("PAGE");

        //CHECK PAGE VALUE
        if (name != null) {
            if (name.equalsIgnoreCase("Orderpage")) {
                //load producer pending orders
                loadproducerfragment(new ProducerPendingOrdersFragment());
            } else if (name.equalsIgnoreCase("Confirmpage")) {
                //load producer orders
                loadproducerfragment(new ProducerOrderFragment());
            } else if (name.equalsIgnoreCase("AcceptOrderpage")) {
                //load producer home
                loadproducerfragment(new ProducerHomeFragment());
            } else if (name.equalsIgnoreCase("Deliveredpage")) {
                //load producer home
                loadproducerfragment(new ProducerHomeFragment());
            }
        } else {
            //load producer home
            loadproducerfragment(new ProducerHomeFragment());
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
    private boolean loadproducerfragment(Fragment fragment) {
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
            case R.id.producerHome:
                fragment = new ProducerHomeFragment();
                break;

            case R.id.PendingOrders:
                fragment = new ProducerPendingOrdersFragment();
                break;

            case R.id.Orders:
                fragment = new ProducerOrderFragment();
                break;
            case R.id.producerProfile:
                fragment = new ProducerProfileFragment();
                break;
        }

        //return fragment
        return loadproducerfragment(fragment);
    }

    //BACK PRESS
    private boolean pressedOnce = false;
    @Override
    public void onBackPressed() {
        BottomNavigationView navigationView = findViewById(R.id.producer_bottom_navigation);
        //check if home view
        if(navigationView.getSelectedItemId()==R.id.producerHome){
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
            navigationView.setSelectedItemId(R.id.producerHome); //direct to home view
        }
    }
}
