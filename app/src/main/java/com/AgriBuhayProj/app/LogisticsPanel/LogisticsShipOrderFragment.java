package com.AgriBuhayProj.app.LogisticsPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AgriBuhayProj.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//SHIP ORDERS FRAGMENT
public class LogisticsShipOrderFragment extends Fragment {
    //VARIABLES
    private RecyclerView recyclerView;
    private List<LogisticsShipFinalOrders1> logisticsShipFinalOrders1List;
    private LogisticsShipOrderFragmentAdapter adapter;
    private DatabaseReference databaseReference;

    //CREATE RECYCLER VIEW
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_logisticsshiporder, null);
        getActivity().setTitle("Ship Orders");
        recyclerView = view.findViewById(R.id.delishiporder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        logisticsShipFinalOrders1List = new ArrayList<>();
        LogisticsShipfinalOrder();
        return view;
    }

    //LIST CURRENT SHIPPING ORDERS
    private void LogisticsShipfinalOrder() {
        //order db reference
        databaseReference = FirebaseDatabase.getInstance().getReference("LogisticsShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                logisticsShipFinalOrders1List.clear();
                //list available orders
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("LogisticsShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            LogisticsShipFinalOrders1 logisticsShipFinalOrders1 = dataSnapshot.getValue(LogisticsShipFinalOrders1.class);
                            logisticsShipFinalOrders1List.add(logisticsShipFinalOrders1);
                            adapter = new LogisticsShipOrderFragmentAdapter(getContext(), logisticsShipFinalOrders1List);
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
