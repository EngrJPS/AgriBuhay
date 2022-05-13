package com.AgriBuhayProj.app.RetailerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.AgriBuhayProj.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//PENDING ORDERS LIST
public class PendingOrders extends AppCompatActivity {
    //VARIABLES
    RecyclerView recyclerView;
    private List<RetailerPendingOrders> retailerPendingOrdersList;
    private PendingOrdersAdapter adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);
        recyclerView = findViewById(R.id.Recycleorders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PendingOrders.this));
        retailerPendingOrdersList = new ArrayList<>();
        RetailerpendingOrders();
    }

    //LIST PENDING ORDERS
    private void RetailerpendingOrders() {
        //RetailerPendingOrders db reference
        databaseReference = FirebaseDatabase.getInstance().getReference("RetailerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                retailerPendingOrdersList.clear();
                //list pending orders
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //RetailerPendingOrders products reference
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("RetailerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Products");
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //list products ordered
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                RetailerPendingOrders retailerPendingOrders = snapshot1.getValue(RetailerPendingOrders.class);
                                retailerPendingOrdersList.add(retailerPendingOrders);
                            }
                            //set adapter
                            adapter = new PendingOrdersAdapter(PendingOrders.this, retailerPendingOrdersList);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
