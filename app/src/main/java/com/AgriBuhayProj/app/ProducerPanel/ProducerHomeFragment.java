package com.AgriBuhayProj.app.ProducerPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.AgriBuhayProj.app.Models.Producer;
import com.AgriBuhayProj.app.MainMenu;
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

//PRODUCTS LIST
public class ProducerHomeFragment extends Fragment {
    //VARIABLES
    RecyclerView recyclerView;
    private List<UpdateProductModel> updateProductModelList;
    private ProducerHomeAdapter adapter;
    DatabaseReference dataaa;
    private String province, city, baranggay;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_producer_home, null);
        getActivity().setTitle("AgriBuhay");

        setHasOptionsMenu(true);

        recyclerView = v.findViewById(R.id.Recycle_menu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateProductModelList = new ArrayList<>();
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Producer").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Producer producer = dataSnapshot.getValue(Producer.class);
                province = producer.getProvince();
                city = producer.getCity();
                baranggay = producer.getBaranggay();
                producerProducts();
            }
//
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//
            }
        });


        return v;
    }

    //DISPLAY PRODUCER POSTED PRODUCTS
    private void producerProducts() {

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //product supply reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProductSupplyDetails").child(province).child(city).child(baranggay).child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateProductModelList.clear();
                //display posted products
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UpdateProductModel updateProductModel = snapshot.getValue(UpdateProductModel.class);
                    updateProductModelList.add(updateProductModel);

                }
                adapter = new ProducerHomeAdapter(getContext(), updateProductModelList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //OPTIONS
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.logout, menu);
    }

    //OPTIONS CLICKED
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idd = item.getItemId();

        if (idd == R.id.LogOut) {
            Logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //LOGOUT USER
    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
    }
}
