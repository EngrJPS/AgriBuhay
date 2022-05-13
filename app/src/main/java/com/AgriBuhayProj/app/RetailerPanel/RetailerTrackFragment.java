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

//TRACK FRAGMENT
public class RetailerTrackFragment extends Fragment {
    //VARIABLES
    RecyclerView recyclerView;
    TextView grandtotal, Address,Status;
    LinearLayout total;

    private List<RetailerFinalOrders> retailerFinalOrdersList;

    private RetailerTrackAdapter adapter;

    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Track");
        View v = inflater.inflate(R.layout.fragment_retailertrack, null);
        //CONNECT XML
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

    //TRACK ORDER
    private void RetailerTrackOrder() {
        //RetailerFinalOrders reference
        databaseReference = FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                retailerFinalOrdersList.clear();
                //get reference children
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //RetailerFinalOrders product reference
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Products");
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //display delivery order
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                RetailerFinalOrders retailerFinalOrders = snapshot1.getValue(RetailerFinalOrders.class);
                                retailerFinalOrdersList.add(retailerFinalOrders);
                            }

                            //check order size
                            if (retailerFinalOrdersList.size() == 0) {
                                //no order
                                Address.setVisibility(View.INVISIBLE);
                                total.setVisibility(View.INVISIBLE);
                            } else {
                                //display order details
                                Address.setVisibility(View.VISIBLE);
                                total.setVisibility(View.VISIBLE);
                            }
                            //set adapter
                            adapter = new RetailerTrackAdapter(getContext(), retailerFinalOrdersList);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //RetailerFinalOrders other info reference
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            RetailerFinalOrders1 retailerFinalOrders1 = dataSnapshot.getValue(RetailerFinalOrders1.class);
                            try{
                                //display order other info
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
