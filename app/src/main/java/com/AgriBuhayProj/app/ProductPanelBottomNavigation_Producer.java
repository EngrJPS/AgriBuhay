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
        BottomNavigationView navigationView = findViewById(R.id.producer_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        UpdateToken();
        String name = getIntent().getStringExtra("PAGE");
        if (name != null) {
            if (name.equalsIgnoreCase("Orderpage")) {
                loadproducerfragment(new ProducerPendingOrdersFragment());
            } else if (name.equalsIgnoreCase("Confirmpage")) {
                loadproducerfragment(new ProducerOrderFragment());
            } else if (name.equalsIgnoreCase("AcceptOrderpage")) {
                loadproducerfragment(new ProducerHomeFragment());
            } else if (name.equalsIgnoreCase("Deliveredpage")) {
                loadproducerfragment(new ProducerHomeFragment());
            }
        } else {
            loadproducerfragment(new ProducerHomeFragment());
        }
    }

    private void UpdateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    private boolean loadproducerfragment(Fragment fragment) {
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
        return loadproducerfragment(fragment);
    }

    private boolean pressedOnce = false;
    @Override
    public void onBackPressed() {
        BottomNavigationView navigationView = findViewById(R.id.producer_bottom_navigation);
        if(navigationView.getSelectedItemId()==R.id.producerHome){
            if(pressedOnce){
                super.onBackPressed();
            }

            this.pressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    pressedOnce = false;
                }
            },2000);
        }else{
            navigationView.setSelectedItemId(R.id.producerHome);
        }
    }
}
