package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.AgriBuhayProj.app.Models.Crops;
import com.AgriBuhayProj.app.Models.History;
import com.AgriBuhayProj.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProducerDeliveredOrdersList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView orderedView;

    private List<History> deliveredList;
    private ProducerDeliveredOrdersAdapter adapter;

    DatabaseReference dbRef;
    FirebaseAuth fbAuth;

    String producerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_delivered_orders_list);

        fbAuth = FirebaseAuth.getInstance();
        producerID = fbAuth.getCurrentUser().getUid();

        orderedView = findViewById(R.id.orderedView);

        orderedView.setHasFixedSize(true);
        orderedView.setLayoutManager(new LinearLayoutManager(ProducerDeliveredOrdersList.this));

        deliveredList = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference("Delivery History").child(producerID);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                deliveredOrders();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void deliveredOrders(){
        //database reference
        dbRef = FirebaseDatabase.getInstance().getReference("Delivery History").child(producerID);
        //update recyclerview
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveredList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    History history = snapshot.getValue(History.class);
                    deliveredList.add(history);
                }
                adapter = new ProducerDeliveredOrdersAdapter(ProducerDeliveredOrdersList.this, deliveredList);
                orderedView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onBackPressed(){
        finish();
    }
}