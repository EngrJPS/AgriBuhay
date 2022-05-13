package com.AgriBuhayProj.app.ProducerPanel;

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

//PREPARED ORDER LIST
public class ProducerPreparedOrder extends AppCompatActivity {
    //VARIABLES
    private RecyclerView recyclerView;
    private List<ProducerFinalOrders1> producerFinalOrders1List;
    private ProducerPreparedOrderAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_prepared_order);
        //RECYCLER VIEW
        recyclerView = findViewById(R.id.Recycle_preparedOrders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProducerPreparedOrder.this));
        //ARRAY LIST INSTANCE
        producerFinalOrders1List = new ArrayList<>();
        //LIST PREPARED ORDERS
        ProducerPrepareOrders();
    }

    //LIST PREPARED ORDERS
    private void ProducerPrepareOrders() {
        //prepared orders db reference
        databaseReference = FirebaseDatabase.getInstance().getReference("ProducerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //clear list
                producerFinalOrders1List.clear();
                //populate recycler view
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //prepared orders other info reference
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("ProducerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //get orders
                            ProducerFinalOrders1 producerFinalOrders1 = dataSnapshot.getValue(ProducerFinalOrders1.class);
                            //add orders to list
                            producerFinalOrders1List.add(producerFinalOrders1);
                            //set adapter
                            adapter = new ProducerPreparedOrderAdapter(ProducerPreparedOrder.this, producerFinalOrders1List);
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
