package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.AgriBuhayProj.app.R;
import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

//ADD CROP
public class ProducerAddCrop extends AppCompatActivity {

    private TextInputLayout cropName,tMin,tMax,hMin,hMax,cMin,cMax;
    private Button addCrop;

    String crop,tempMin,tempMax,humidMin,humidMax,carbonMin,carbonMax;

    FirebaseDatabase fbDB;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_add_crop);

        //XML
        cropName = findViewById(R.id.cropName);
        tMin = findViewById(R.id.tMin);
        tMax = findViewById(R.id.tMax);
        hMin = findViewById(R.id.hMin);
        hMax = findViewById(R.id.hMax);
        cMin = findViewById(R.id.cMin);
        cMax = findViewById(R.id.cMax);
        addCrop = findViewById(R.id.btnAddCrop);

        ProgressDialog progress = new ProgressDialog(ProducerAddCrop.this);
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(false);

        //DATABASE INSTANCE
        fbDB = FirebaseDatabase.getInstance();

        //ADD CROP EVENT
        addCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get string
                crop = cropName.getEditText().getText().toString();
                tempMin = tMin.getEditText().getText().toString();
                tempMax = tMax.getEditText().getText().toString();
                humidMin = hMin.getEditText().getText().toString();
                humidMax = hMax.getEditText().getText().toString();
                carbonMin = cMin.getEditText().getText().toString();
                carbonMax = cMax.getEditText().getText().toString();

                //check string validity
                if (isValid()){
                    progress.setMessage("Adding crop...");
                    progress.show();
                    //set database reference
                    dbRef = fbDB.getReference("Crops");
                    //find existing crop
                    dbRef.orderByChild("crop").equalTo(crop).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check crop exists
                            if(snapshot.exists()){
                                progress.dismiss();
                                cropName.setErrorEnabled(true);
                                cropName.setError("Crop existed");
                                Toast.makeText(ProducerAddCrop.this, "Crop already exists", Toast.LENGTH_LONG).show();
                            }else{
                                try {
                                    //set database
                                    dbRef = fbDB.getReference("Crops").child(crop);
                                    //add to database (Hashmap)
                                    HashMap<String,String> cropMap = new HashMap<>();
                                    cropMap.put("crop",crop);
                                    cropMap.put("tempMin",tempMin);
                                    cropMap.put("tempMax",tempMax);
                                    cropMap.put("humidMin",humidMin);
                                    cropMap.put("humidMax",humidMax);
                                    cropMap.put("carbonMin",carbonMin);
                                    cropMap.put("carbonMax",carbonMax);
                                    dbRef.setValue(cropMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progress.dismiss();
                                            Toast.makeText(ProducerAddCrop.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }catch (Exception e){
                                    progress.dismiss();
                                    ReusableCodeForAll.ShowAlert(ProducerAddCrop.this,"Error",e.getMessage());
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progress.dismiss();
                            ReusableCodeForAll.ShowAlert(ProducerAddCrop.this,"Error",error.getMessage());
                        }
                    });
                }else{
                    Toast.makeText(ProducerAddCrop.this, "All fields required", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //CHECK VALIDIDTY
    private boolean isValid(){
        final boolean isValid,validCrop,validtMin,validtMax,validhMin,validhMax,validcMin,validcMax;
        cropName.setErrorEnabled(false);
        cropName.setError("");
        tMin.setErrorEnabled(false);
        tMin.setError("");
        tMax.setErrorEnabled(false);
        tMax.setError("");
        hMin.setErrorEnabled(false);
        hMin.setError("");
        hMax.setErrorEnabled(false);
        hMax.setError("");
        cMin.setErrorEnabled(false);
        cMin.setError("");
        cMax.setErrorEnabled(false);
        cMax.setError("");
        //crop name
        if(crop.isEmpty()){
            cropName.setErrorEnabled(true);
            cropName.setError("Empty field");
            validCrop = false;
        }else{
            validCrop = true;
        }
        //temperature
        if(tempMin.isEmpty()){
            tMin.setErrorEnabled(true);
            tMin.setError("Empty field");
            validtMin = false;
        }else{
            validtMin = true;
        }
        if (tempMax.isEmpty()){
            tMax.setErrorEnabled(true);
            tMax.setError("Empty field");
            validtMax = false;
        }else{
            validtMax = true;
        }
        //humidity
        if(humidMin.isEmpty()){
            hMin.setErrorEnabled(true);
            hMin.setError("Empty field");
            validhMin = false;
        }else{
            validhMin = true;
        }
        if (humidMax.isEmpty()){
            hMax.setErrorEnabled(true);
            hMax.setError("Empty field");
            validhMax = false;
        }else{
            validhMax = true;
        }
        //carbon dioxide
        if(carbonMin.isEmpty()){
            cMin.setErrorEnabled(true);
            cMin.setError("Empty field");
            validcMin = false;
        }else{
            validcMin = true;
        }
        if (carbonMax.isEmpty()){
            cMax.setErrorEnabled(true);
            cMax.setError("Empty field");
            validcMax = false;
        }else{
            validcMax = true;
        }

        isValid = (validCrop&&validtMin&&validtMax&&validhMin&&validhMax&&validcMin&&validcMax);
        return isValid;
    }


}