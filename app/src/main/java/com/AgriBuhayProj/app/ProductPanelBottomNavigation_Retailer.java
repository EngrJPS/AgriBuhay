package com.AgriBuhayProj.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

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
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        UpdateToken();
        String name = getIntent().getStringExtra("PAGE");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (name != null) {
            if (name.equalsIgnoreCase("Homepage")) {
                loadFragment(new RetailerHomeFragment());
            } else if (name.equalsIgnoreCase("Preparingpage")) {
                loadFragment(new RetailerTrackFragment());
            } else if (name.equalsIgnoreCase("Preparedpage")) {
                loadFragment(new RetailerTrackFragment());
            } else if (name.equalsIgnoreCase("DeliverOrderpage")) {
                loadFragment(new RetailerTrackFragment());
            } else if (name.equalsIgnoreCase("ThankYoupage")) {
                loadFragment(new RetailerHomeFragment());
            }
        } else {
            loadFragment(new RetailerHomeFragment());
        }
    }

    private void UpdateToken() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
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
        return loadFragment(fragment);
    }
}
