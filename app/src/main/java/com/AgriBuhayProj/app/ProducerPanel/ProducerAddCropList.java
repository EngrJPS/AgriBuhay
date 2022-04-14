package com.AgriBuhayProj.app.ProducerPanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.AgriBuhayProj.app.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

public class ProducerAddCropList extends AppCompatActivity {

    FloatingActionButton add;
    RecyclerView cropView;
    SearchView searchView;

    /*private List<> updateCrop;*/

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_add_crop_list);

        add = findViewById(R.id.floatBtn);
        cropView = findViewById(R.id.cropView);

        cropView.setHasFixedSize(true);
        cropView.setLayoutManager(new LinearLayoutManager(ProducerAddCropList.this));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProducerAddCropList.this, ProducerAddCrop.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        return true;
    }
}