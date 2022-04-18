package com.AgriBuhayProj.app.RetailerPanel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class RetailerTrackFragment extends Fragment {

    RecyclerView recyclerView;
    private List<RetailerFinalOrders> retailerFinalOrdersList;
    private RetailerTrackAdapter adapter;
    DatabaseReference databaseReference;
    TextView grandtotal, Address,Status;
    LinearLayout total;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Track");
        View v = inflater.inflate(R.layout.fragment_retailertrack, null);
        recyclerView = v.findViewById(R.id.recyclefinalorders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        grandtotal = v.findViewById(R.id.Rs);
        Address = v.findViewById(R.id.addresstrack);
        Status=v.findViewById(R.id.status);
        total = v.findViewById(R.id.btnn);
        retailerFinalOrdersList = new ArrayList<>();
        RetailerTrackOrder();

        return v;
    }

    private void RetailerTrackOrder() {

        databaseReference = FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                retailerFinalOrdersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Products");
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                RetailerFinalOrders retailerFinalOrders = snapshot1.getValue(RetailerFinalOrders.class);
                                retailerFinalOrdersList.add(retailerFinalOrders);
                            }

                            if (retailerFinalOrdersList.size() == 0) {
                                Address.setVisibility(View.INVISIBLE);
                                total.setVisibility(View.INVISIBLE);
                            } else {
                                Address.setVisibility(View.VISIBLE);
                                total.setVisibility(View.VISIBLE);
                            }
                            adapter = new RetailerTrackAdapter(getContext(), retailerFinalOrdersList);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            RetailerFinalOrders1 retailerFinalOrders1 = dataSnapshot.getValue(RetailerFinalOrders1.class);
                            try{
                                grandtotal.setText("₱ " + retailerFinalOrders1.getGrandTotalPrice());
                                Address.setText(retailerFinalOrders1.getAddress());
                                Status.setText(retailerFinalOrders1.getStatus());
                            }catch (Exception e){
                                Log.d("RetailerTrackFragment", "onDataChange: "+e);
                            }

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
