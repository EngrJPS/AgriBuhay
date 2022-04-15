package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.AgriBuhayProj.app.Models.Crops;
import com.AgriBuhayProj.app.R;
import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProducerAddCropUpdate extends AppCompatActivity {
    private TextInputLayout minT,maxT,minH,maxH,minC,maxC;
    private TextView cName;
    private Button upCrop;
    private ProgressDialog prog;
    private Intent intent;

    private String crop,cropName,tempMin,tempMax,humidMin,humidMax,carbonMin,carbonMax;

    FirebaseDatabase fbDB;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_add_crop_update);
        //XML
        cName = findViewById(R.id.cName);
        minT = findViewById(R.id.tMin);
        maxT = findViewById(R.id.tMax);
        minH = findViewById(R.id.hMin);
        maxH = findViewById(R.id.hMax);
        minC = findViewById(R.id.cMin);
        maxC = findViewById(R.id.cMax);
        upCrop = findViewById(R.id.btnUpdateCrop);
        //STRING
        intent = getIntent();
        crop = intent.getStringExtra("cropName").trim();
        //PROGRESS DIALOG
        prog = new ProgressDialog(ProducerAddCropUpdate.this);
        prog.setCancelable(false);
        prog.setCanceledOnTouchOutside(false);
        //FIREBASE
        fbDB = FirebaseDatabase.getInstance();
        dbRef = fbDB.getReference("Crops").child(crop);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get database value
                Crops crops = snapshot.getValue(Crops.class);
                //display value
                cName.setText(crops.getCrop());
                minT.getEditText().setText(crops.getTempMin());
                maxT.getEditText().setText(crops.getTempMax());
                minH.getEditText().setText(crops.getHumidMin());
                maxH.getEditText().setText(crops.getHumidMax());
                minC.getEditText().setText(crops.getCarbonMin());
                maxC.getEditText().setText(crops.getCarbonMax());

                //BUTTON EVENTS
                upCrop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //get string value
                        cropName = cName.getText().toString();
                        tempMin = minT.getEditText().getText().toString();
                        tempMax = maxT.getEditText().getText().toString();
                        humidMin = minH.getEditText().getText().toString();
                        humidMax = maxH.getEditText().getText().toString();
                        carbonMin = minC.getEditText().getText().toString();
                        carbonMax = maxC.getEditText().getText().toString();
                        //validate fields
                        if(isValid()){
                            //progress dialog
                            prog.setMessage("Updating..");
                            prog.show();
                            //update current crop
                            Crops updateCrop = new Crops(cropName,tempMin,tempMax,humidMin,humidMax,carbonMin,carbonMax);
                            dbRef.setValue(updateCrop).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    prog.dismiss();
                                    Toast.makeText(ProducerAddCropUpdate.this, "Crop updated successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }else{
                            Toast.makeText(ProducerAddCropUpdate.this, "All fields required", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ReusableCodeForAll.ShowAlert(ProducerAddCropUpdate.this,"Database Error",error.getMessage());
            }
        });
    }

    private boolean isValid(){
        final boolean isValid,validtMin,validtMax,validhMin,validhMax,validcMin,validcMax;
        minT.setErrorEnabled(false);
        minT.setError("");
        maxT.setErrorEnabled(false);
        maxT.setError("");
        minH.setErrorEnabled(false);
        minH.setError("");
        maxH.setErrorEnabled(false);
        maxH.setError("");
        minC.setErrorEnabled(false);
        minC.setError("");
        maxC.setErrorEnabled(false);
        maxC.setError("");
        //temperature
        if(tempMin.isEmpty()){
            minT.setErrorEnabled(true);
            minT.setError("Empty field");
            validtMin = false;
        }else{
            validtMin = true;
        }
        if (tempMax.isEmpty()){
            maxT.setErrorEnabled(true);
            maxT.setError("Empty field");
            validtMax = false;
        }else{
            validtMax = true;
        }
        //humidity
        if(humidMin.isEmpty()){
            minH.setErrorEnabled(true);
            minH.setError("Empty field");
            validhMin = false;
        }else{
            validhMin = true;
        }
        if (humidMax.isEmpty()){
            maxH.setErrorEnabled(true);
            maxH.setError("Empty field");
            validhMax = false;
        }else{
            validhMax = true;
        }
        //carbon dioxide
        if(carbonMin.isEmpty()){
            minC.setErrorEnabled(true);
            minC.setError("Empty field");
            validcMin = false;
        }else{
            validcMin = true;
        }
        if (carbonMax.isEmpty()){
            maxC.setErrorEnabled(true);
            maxC.setError("Empty field");
            validcMax = false;
        }else{
            validcMax = true;
        }

        isValid = (validtMin&&validtMax&&validhMin&&validhMax&&validcMin&&validcMax);
        return isValid;
    }
}