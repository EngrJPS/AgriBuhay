package com.AgriBuhayProj.app.ProducerPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.AgriBuhayProj.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ProducerPendingOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ProducerPendingOrders1> producerPendingOrders1List;
    private ProducerPendingOrdersAdapter adapter;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Pending Orders");
        v = inflater.inflate(R.layout.fragment_producer_pendingorders, null);

        recyclerView = v.findViewById(R.id.Recycle_orders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        producerPendingOrders1List = new ArrayList<>();
        swipeRefreshLayout = v.findViewById(R.id.Swipe_layoutt);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);

        adapter = new ProducerPendingOrdersAdapter(getContext(), producerPendingOrders1List);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                producerPendingOrders1List.clear();
                recyclerView = v.findViewById(R.id.Recycle_orders);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                producerPendingOrders1List = new ArrayList<>();
                producerorders();
            }
        });

        producerorders();
        return v;
    }

    private void producerorders() {
        //TODO this is the database for the ChefPendingOrders
        databaseReference = FirebaseDatabase.getInstance().getReference("ProducerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                producerPendingOrders1List.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //TODO this is the database for the ChefPendingOrders
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("ProducerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("OtherInformation");
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ProducerPendingOrders1 producerPendingOrders1 = dataSnapshot.getValue(ProducerPendingOrders1.class);
                            producerPendingOrders1List.add(producerPendingOrders1);
                            adapter = new ProducerPendingOrdersAdapter(getContext(), producerPendingOrders1List);
                            recyclerView.setAdapter(adapter);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}