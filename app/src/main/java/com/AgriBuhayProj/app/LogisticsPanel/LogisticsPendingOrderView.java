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

//PENDING ORDER DETAILS DISPLAY
public class LogisticsPendingOrderView extends AppCompatActivity {
    //DECLARE VARIABLES
    //recycler view
    RecyclerView recyclerViewproduct;
    //array list
    private List<LogisticsShipOrders> logisticsShipOrdersList;
    //adapter
    private LogisticsPendingOrderViewAdapter adapter;
    //firebase
    DatabaseReference reference;
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    //strings
    String RandomUID;
    String logisticsId = fbAuth.getCurrentUser().getUid();
    //xml
    TextView grandtotal, address, name, number;
    TextView producerName, producerAddress, producerNumber;
    LinearLayout l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_pending_order_view);
        //xml
        recyclerViewproduct = findViewById(R.id.delivieworder);
        l1 = findViewById(R.id.linear1);
        grandtotal = findViewById(R.id.Dtotal);
        address = findViewById(R.id.DAddress);
        producerName = findViewById(R.id.prodName);
        producerAddress = findViewById(R.id.prodAdd);
        producerNumber = findViewById(R.id.prodNum);
        name = findViewById(R.id.DName);
        number = findViewById(R.id.DNumber);

        //recycler view
        recyclerViewproduct.setHasFixedSize(true);
        recyclerViewproduct.setLayoutManager(new LinearLayoutManager(LogisticsPendingOrderView.this));

        //array list instance
        logisticsShipOrdersList = new ArrayList<>();

        logisticsorders();
    }

    //PENDING ORDER DETAILS
    private void logisticsorders() {
        //get transaction id
        RandomUID = getIntent().getStringExtra("Random");

        //pending orders reference (products)
        reference = FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(RandomUID).child("Products");
        //get db data
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //clear products ordered list
                logisticsShipOrdersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //get products ordered
                    LogisticsShipOrders logisticsShipOrders = snapshot.getValue(LogisticsShipOrders.class);
                    //add to array list
                    logisticsShipOrdersList.add(logisticsShipOrders);
                }
                //check array list contents
                if (logisticsShipOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);
                } else {
                    l1.setVisibility(View.VISIBLE);
                }
                //get adapter value
                adapter = new LogisticsPendingOrderViewAdapter(LogisticsPendingOrderView.this, logisticsShipOrdersList);
                //set recycle view adapter
                recyclerViewproduct.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //pending orders reference (other information)
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(RandomUID).child("OtherInformation");
        //db snapshot
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get data using model
                LogisticsShipOrders1 logisticsShipOrders1 = dataSnapshot.getValue(LogisticsShipOrders1.class);
                //show data from db
                grandtotal.setText("₱ " + logisticsShipOrders1.getGrandTotalPrice());
                address.setText("Delivery Address: "+logisticsShipOrders1.getAddress());
                name.setText("Retailer: "+logisticsShipOrders1.getName());
                number.setText("Mobile Number: "+logisticsShipOrders1.getMobileNumber());
                producerName.setText("Producer: "+ logisticsShipOrders1.getProducerName());

                //get producer id
                String prodID = logisticsShipOrders1.getProducerId();

                //producer reference
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Producer");
                dbRef.child(prodID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get value from db using producer model
                        Producer producer = snapshot.getValue(Producer.class);
                        //display value
                        producerAddress.setText("Address: "+producer.getBaranggay()+", "+producer.getCity()+", "+producer.getProvince());
                        producerNumber.setText("Mobile Number: "+producer.getMobile());
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
