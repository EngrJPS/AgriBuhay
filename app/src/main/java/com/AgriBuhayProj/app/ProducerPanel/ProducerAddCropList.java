package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.AgriBuhayProj.app.Models.Crops;
import com.AgriBuhayProj.app.R;
import com.AgriBuhayProj.app.SendNotification.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProducerAddCropList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FloatingActionButton add;
    RecyclerView cropView;
    SearchView searchView;

    private List<Crops> cropList;
    private ProducerAddCropAdapter adapter;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_add_crop_list);

        add = findViewById(R.id.floatBtn);
        cropView = findViewById(R.id.cropView);

        cropView.setHasFixedSize(true);
        cropView.setLayoutManager(new LinearLayoutManager(ProducerAddCropList.this));

        cropList = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference("Crops");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cropAdded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProducerAddCropList.this, ProducerAddCrop.class));
            }
        });


    }

    //DISPLAY CROPS
    private void cropAdded(){
        //database reference
        dbRef = FirebaseDatabase.getInstance().getReference("Crops");
        //update recyclerview
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cropList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Crops cropModel = snapshot.getValue(Crops.class);
                    cropList.add(cropModel);
                }
                adapter = new ProducerAddCropAdapter(ProducerAddCropList.this, cropList);
                cropView.setAdapter(adapter);

                //search item
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        searchCrop(newText);
                        return true;
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //SEARCH FUNCTION
    private void searchCrop(final String searchText){
        ArrayList<Crops> cropArray = new ArrayList<>();
        for(Crops crops : cropList){
            if(crops.getCrop().toLowerCase().contains(searchText.toLowerCase())){
                cropArray.add(crops);
            }
        }
        adapter = new ProducerAddCropAdapter(ProducerAddCropList.this,cropArray);
        cropView.setAdapter(adapter);
    }

    //SEARCH MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.searchItem);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Find Crop");
        return true;
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onBackPressed(){
        finish();
    }
}