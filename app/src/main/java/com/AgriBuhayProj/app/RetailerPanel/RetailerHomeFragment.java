package com.AgriBuhayProj.app.RetailerPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.AgriBuhayProj.app.ProducerPanel.UpdateProductModel;
import com.AgriBuhayProj.app.Models.Retailer;

import com.AgriBuhayProj.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//HOME FRAGMENT
public class RetailerHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    //VARIABLES
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;

    private List<UpdateProductModel> updateProductModelList;
    private RetailerHomeAdapter adapter;
    String province, city, baranggay;
    DatabaseReference dataaa, databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_retailerhome, null);
        getActivity().setTitle("AgriBuhay");
        setHasOptionsMenu(true);
        //RECYCLER VIEW
        recyclerView = v.findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);

        //PRODUCT LOAD ANIMATION
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);

        recyclerView.startAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //ARRAY LIST INSTNACE
        updateProductModelList = new ArrayList<>();

        //REFRESH
        swipeRefreshLayout = v.findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);

        //REFRESH LIST
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                //retailer db reference
                dataaa = FirebaseDatabase.getInstance().getReference("Retailer").child(userid);
                dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //get values
                        Retailer ret = dataSnapshot.getValue(Retailer.class);
                        province = ret.getProvince();
                        city = ret.getCity();
                        baranggay = ret.getBaranggay();
                        //list products
                        retailermenu();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return v;
    }


    @Override
    public void onRefresh() {
        retailermenu();
    }

    //list products
    private void retailermenu() {
        swipeRefreshLayout.setRefreshing(true);
        //ProductSupplyDetails db reference
        databaseReference = FirebaseDatabase.getInstance().getReference("ProductSupplyDetails").child(province).child(city).child(baranggay);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateProductModelList.clear();
                //add products to list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        UpdateProductModel updateProductModel = snapshot1.getValue(UpdateProductModel.class);
                        updateProductModelList.add(updateProductModel);
                    }
                }

                //set adapter
                adapter = new RetailerHomeAdapter(getContext(), updateProductModelList);
                recyclerView.setAdapter(adapter);

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //search product
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //search
                search(newText);
                return true;
            }
        });

    }

    //SEARCH PRODUCT
    private void search(final String searchtext) {
        ArrayList<UpdateProductModel> mylist = new ArrayList<>();
        for (UpdateProductModel object : updateProductModelList) {
            //list searched product
            if (object.getProducts().toLowerCase().contains(searchtext.toLowerCase())) {
                mylist.add(object);
            }
        }

        //set adapter
        adapter = new RetailerHomeAdapter(getContext(), mylist);
        recyclerView.setAdapter(adapter);

    }

    //DISPLAY SEARCH BAR
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.searchItem);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Find Product");
    }
}
