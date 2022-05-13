package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

//TO BE PREPARED LIST
public class ProducerOrderTobePrepared extends AppCompatActivity {
    //VARIALES
    private RecyclerView recyclerView;
    private List<ProducerWaitingOrders1> producerWaitingOrders1List;
    private ProducerOrderTobePreparedAdapter adapter;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_order_to_be_prepared_list);
        //RECYCLER VIEW
        recyclerView = findViewById(R.id.Recycle_orderstobeprepared);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProducerOrderTobePrepared.this));
        //ARRAY LIST INSTANCE
        producerWaitingOrders1List = new ArrayList<>();
        //REFRESH
        swipeRefreshLayout = findViewById(R.id.Swipe1);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
        //ADAPTER
        adapter = new ProducerOrderTobePreparedAdapter(ProducerOrderTobePrepared.this, producerWaitingOrders1List);
        //SET ADAPTER
        recyclerView.setAdapter(adapter);

        //REFRESH LIST
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                producerWaitingOrders1List.clear();
                recyclerView = findViewById(R.id.Recycle_orderstobeprepared);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(ProducerOrderTobePrepared.this));
                producerWaitingOrders1List = new ArrayList<>();
                producerorderstobePrepare();
            }
        });
        producerorderstobePrepare();
    }

    //LIST PENDING ORDERS
    private void producerorderstobePrepare() {
        //pending orders reference
        databaseReference = FirebaseDatabase.getInstance().getReference("ProducerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check orders
                if (dataSnapshot.exists()) {
                    producerWaitingOrders1List.clear();
                    //list orders
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //pending orders reference
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("ProducerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ProducerWaitingOrders1 producerWaitingOrders1 = dataSnapshot.getValue(ProducerWaitingOrders1.class);
                                producerWaitingOrders1List.add(producerWaitingOrders1);
                                adapter = new ProducerOrderTobePreparedAdapter(ProducerOrderTobePrepared.this, producerWaitingOrders1List);
                                recyclerView.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);
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
