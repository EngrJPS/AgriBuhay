package com.AgriBuhayProj.app.RetailerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.AgriBuhayProj.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//PAYABLE ORDER LIST
public class PayableOrders extends AppCompatActivity {
    //VARIABLES
    RecyclerView recyclerView;
    Button payment;
    TextView grandtotal;
    private LinearLayout pay;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<RetailerPaymentOrders> retailerPaymentOrdersList;
    private PayableOrderAdapter adapter;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer_payable_orders);
        //CONNECT XML
        recyclerView = findViewById(R.id.recyclepayableorder);
        pay = findViewById(R.id.btn);
        grandtotal = findViewById(R.id.rs);
        payment = findViewById(R.id.paymentmethod);
        swipeRefreshLayout = findViewById(R.id.Swipe2);

        //RECYCLER VIEW
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PayableOrders.this));

        //ARRAY LIST INSTANCE
        retailerPaymentOrdersList = new ArrayList<>();

        //REFRESH
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);

        //SET ADAPTER
        adapter = new PayableOrderAdapter(PayableOrders.this, retailerPaymentOrdersList);
        recyclerView.setAdapter(adapter);

        //REFRESHED
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //list payable orders
                recyclerView = findViewById(R.id.recyclepayableorder);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(PayableOrders.this));
                retailerPaymentOrdersList = new ArrayList<>();
                RetailerpayableOrders();
            }
        });
        RetailerpayableOrders();
    }

    //LIST PAYABLE ORDERS
    private void RetailerpayableOrders() {
        //RetailerPaymentOrders db reference
        databaseReference = FirebaseDatabase.getInstance().getReference("RetailerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check retailer id
                if (dataSnapshot.exists()) {
                    retailerPaymentOrdersList.clear();
                    //get all data
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final String randomuid = snapshot.getKey();
                        //RetailerPaymentOrders product db reference
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("RetailerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Products");
                        data.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //list orders
                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                    RetailerPaymentOrders retailerPaymentOrders = snapshot1.getValue(RetailerPaymentOrders.class);
                                    retailerPaymentOrdersList.add(retailerPaymentOrders);
                                }
                                //check array size
                                if (retailerPaymentOrdersList.size() == 0) {
                                    pay.setVisibility(View.INVISIBLE);
                                } else {
                                    //show payment
                                    pay.setVisibility(View.VISIBLE);

                                    //payment mode
                                    payment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //direct to payment mode
                                            Intent intent = new Intent(PayableOrders.this, RetailerPayment.class);
                                            intent.putExtra("RandomUID", randomuid);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }

                                //set adapter
                                adapter = new PayableOrderAdapter(PayableOrders.this, retailerPaymentOrdersList);
                                recyclerView.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //RetailerPaymentOrders other info db
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RetailerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomuid).child("OtherInformation");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //check order
                                if(dataSnapshot.exists()) {
                                    //display values
                                    RetailerPaymentOrders1 retailerPaymentOrders1 = dataSnapshot.getValue(RetailerPaymentOrders1.class);
                                    grandtotal.setText("₱ " + retailerPaymentOrders1.getGrandTotalPrice());
                                    swipeRefreshLayout.setRefreshing(false);

                                } else {
                                    swipeRefreshLayout.setRefreshing(false);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
