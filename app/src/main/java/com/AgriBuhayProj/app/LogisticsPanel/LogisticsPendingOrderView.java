package com.AgriBuhayProj.app.LogisticsPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AgriBuhayProj.app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogisticsPendingOrderView extends AppCompatActivity {

    RecyclerView recyclerViewproduct;
    private List<LogisticsShipOrders> logisticsShipOrdersList;
    private LogisticsPendingOrderViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID;
    TextView grandtotal, address, name, number, ProducerName;
    LinearLayout l1;
    String logisticsId = "oCpc4SwLVFbKO0fPdtp4R6bmDmI3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_pending_order_view);
        recyclerViewproduct = findViewById(R.id.delivieworder);
        recyclerViewproduct.setHasFixedSize(true);
        recyclerViewproduct.setLayoutManager(new LinearLayoutManager(LogisticsPendingOrderView.this));
        l1 = (LinearLayout) findViewById(R.id.linear1);
        grandtotal = (TextView) findViewById(R.id.Dtotal);
        address = (TextView) findViewById(R.id.DAddress);
        ProducerName =(TextView)findViewById(R.id.producername);
        name = (TextView) findViewById(R.id.DName);
        number = (TextView) findViewById(R.id.DNumber);
        logisticsShipOrdersList = new ArrayList<>();
        logisticsorders();
    }

    private void logisticsorders() {
        RandomUID = getIntent().getStringExtra("Random");

        reference = FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(RandomUID).child("Products");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                logisticsShipOrdersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LogisticsShipOrders logisticsShipOrders = snapshot.getValue(LogisticsShipOrders.class);
                    logisticsShipOrdersList.add(logisticsShipOrders);
                }
                if (logisticsShipOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);

                } else {
                    l1.setVisibility(View.VISIBLE);
                }
                adapter = new LogisticsPendingOrderViewAdapter(LogisticsPendingOrderView.this, logisticsShipOrdersList);
                recyclerViewproduct.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LogisticsShipOrders1 logisticsShipOrders1 = dataSnapshot.getValue(LogisticsShipOrders1.class);
                grandtotal.setText("₱ " + logisticsShipOrders1.getGrandTotalPrice());
                address.setText(logisticsShipOrders1.getAddress());
                name.setText(logisticsShipOrders1.getName());
                number.setText("+63" + logisticsShipOrders1.getMobileNumber());
                ProducerName.setText("Producer "+ logisticsShipOrders1.getProducerName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
