package com.AgriBuhayProj.app.LogisticsPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AgriBuhayProj.app.Models.Producer;
import com.AgriBuhayProj.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogisticsShipOrderView extends AppCompatActivity {

    RecyclerView recyclerViewproduct;
    private List<LogisticsShipFinalOrders> logisticsShipFinalOrdersList;
    private LogisticsShipOrderViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID;
    private TextView grandtotal, address, name, number;
    private TextView producerName,producerAddress,producerMobile;
    LinearLayout l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_ship_order_view);
        recyclerViewproduct = findViewById(R.id.delishipvieworder);
        recyclerViewproduct.setHasFixedSize(true);
        recyclerViewproduct.setLayoutManager(new LinearLayoutManager(LogisticsShipOrderView.this));
        l1 = (LinearLayout) findViewById(R.id.linear2);
        grandtotal = (TextView) findViewById(R.id.Shiptotal);
        address = (TextView) findViewById(R.id.ShipAddress);
        name = (TextView) findViewById(R.id.ShipName);
        number = (TextView) findViewById(R.id.ShipNumber);
        producerName = (TextView) findViewById(R.id.shipPName);
        producerAddress = (TextView) findViewById(R.id.shipPAdd);
        producerMobile = (TextView) findViewById(R.id.shipPNum);
        logisticsShipFinalOrdersList = new ArrayList<>();
        logisticsfinalorders();
    }

    private void logisticsfinalorders() {

        RandomUID = getIntent().getStringExtra("RandomUID");

        reference = FirebaseDatabase.getInstance().getReference("LogisticsShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Products");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                logisticsShipFinalOrdersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LogisticsShipFinalOrders logisticsShipFinalOrders = snapshot.getValue(LogisticsShipFinalOrders.class);
                    logisticsShipFinalOrdersList.add(logisticsShipFinalOrders);
                }
                if (logisticsShipFinalOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);

                } else {
                    l1.setVisibility(View.VISIBLE);
                }
                adapter = new LogisticsShipOrderViewAdapter(LogisticsShipOrderView.this, logisticsShipFinalOrdersList);
                recyclerViewproduct.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LogisticsShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LogisticsShipFinalOrders1 logisticsShipFinalOrders1 = dataSnapshot.getValue(LogisticsShipFinalOrders1.class);
                grandtotal.setText("₱ " + logisticsShipFinalOrders1.getGrandTotalPrice());
                address.setText("Delivery Address: "+logisticsShipFinalOrders1.getAddress());
                name.setText("Retailer: "+logisticsShipFinalOrders1.getName());
                number.setText("Mobile: "+logisticsShipFinalOrders1.getMobileNumber());

                producerName.setText("Producer: " + logisticsShipFinalOrders1.getProducerName());
                final String prodID = logisticsShipFinalOrders1.getProducerId();
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Producer");
                dbRef.child(prodID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Producer producer = snapshot.getValue(Producer.class);
                        producerAddress.setText("Address: "+producer.getHouse()+", "+producer.getArea()+", "+producer.getBaranggay()+", "+producer.getCity()+", "+producer.getProvince());
                        producerMobile.setText("Mobile: "+producer.getMobile());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
