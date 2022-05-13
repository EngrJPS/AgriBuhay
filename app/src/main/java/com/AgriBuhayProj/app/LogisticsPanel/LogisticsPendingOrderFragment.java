package com.AgriBuhayProj.app.LogisticsPanel;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.AgriBuhayProj.app.MainMenu;
import com.AgriBuhayProj.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogisticsPendingOrderFragment extends Fragment {
    //VARIABLES
    //xml
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipeRefreshLayout;
    //array list
    private List<LogisticsShipOrders1> logisticsShipOrders1List;
    //fragment adapter
    private LogisticsPendingOrderFragmentAdapter adapter;
    //firebase
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    //string
    String logisticsId = fbAuth.getCurrentUser().getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_logisticspendingorder, null);
        getActivity().setTitle("Pending Orders");
        setHasOptionsMenu(true);
        //RECYCLER VIEW
        recyclerView = view.findViewById(R.id.delipendingorder);
        recyclerView.setHasFixedSize(true);

        //REFRESH
        swipeRefreshLayout = view.findViewById(R.id.Swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);

        //SET LAYOUT
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //ARRAY INSTANCE
        logisticsShipOrders1List = new ArrayList<>();

        //ARRAY VALUES
        adapter = new LogisticsPendingOrderFragmentAdapter(getContext(), logisticsShipOrders1List);

        //SET ADAPTER
        recyclerView.setAdapter(adapter);

        //REFRESH EVENT
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //clear list
                logisticsShipOrders1List.clear();
                //connect xml
                recyclerView = view.findViewById(R.id.delipendingorder);
                //size
                recyclerView.setHasFixedSize(true);
                //set layout
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                //array instance
                logisticsShipOrders1List = new ArrayList<>();

                //list orders
                LogisticsPendingOrders();
            }
        });

        //list orders
        LogisticsPendingOrders();

        return view;
    }

    //LIST PENDING ORDERS
    private void LogisticsPendingOrders() {
        //pending orders reference
        databaseReference = FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId);

        //get data
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check data existence
                if (dataSnapshot.exists()) {
                    //clear ship list
                    logisticsShipOrders1List.clear();

                    //get all data available on reference
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //other information reference
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(snapshot.getKey()).child("OtherInformation");

                        //get data from reference
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //get model
                                LogisticsShipOrders1 logisticsShipOrders1 = dataSnapshot.getValue(LogisticsShipOrders1.class);

                                //fill data on database using specified model
                                logisticsShipOrders1List.add(logisticsShipOrders1);

                                //set array adapter
                                adapter = new LogisticsPendingOrderFragmentAdapter(getContext(), logisticsShipOrders1List);

                                //show data
                                recyclerView.setAdapter(adapter);

                                //disable reference
                                swipeRefreshLayout.setRefreshing(false);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                //database error
                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else {
                    //data doesn't exist
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //MENU
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //set menu items
        inflater.inflate(R.menu.logout, menu);
    }

    //MENU ITEM SELECTION
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get menu item selected
        int idd = item.getItemId();

        //check id
        if (idd == R.id.LogOut) {
            //logout user
            Logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //USER LOGOUT
    private void Logout() {
        //logout user
        FirebaseAuth.getInstance().signOut();

        //direct to main menu
        Intent intent = new Intent(getActivity(), MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
