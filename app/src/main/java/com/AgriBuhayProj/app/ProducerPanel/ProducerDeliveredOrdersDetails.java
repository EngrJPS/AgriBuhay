package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AgriBuhayProj.app.Models.History;
import com.AgriBuhayProj.app.Models.Logistics;
import com.AgriBuhayProj.app.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

//DELIVERY HISTORY
public class ProducerDeliveredOrdersDetails extends AppCompatActivity {
    //VARIABLES
    private TextView retailAddress,trackNum,date,total,logName,logMobile,retailName,retailMobile;
    private ImageView proof;

    private String trackingNumber,producerID;

    FirebaseAuth fbAuth;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_delivered_orders_details);

        //FIREBASE
        fbAuth = FirebaseAuth.getInstance();

        //ID
        producerID = fbAuth.getCurrentUser().getUid();
        trackingNumber =getIntent().getStringExtra("trackingNumber");

        //XML
        retailAddress = findViewById(R.id.rAddress);
        trackNum = findViewById(R.id.tNum);
        date = findViewById(R.id.date);
        total = findViewById(R.id.tPrice);
        logName = findViewById(R.id.lName);
        logMobile = findViewById(R.id.lMobile);
        retailName = findViewById(R.id.rName);
        retailMobile = findViewById(R.id.rMobile);
        proof = findViewById(R.id.imProof);

        //delivery details
        dbRef = FirebaseDatabase.getInstance().getReference("DeliveryHistory");
        dbRef.child(producerID).child(trackingNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get db values
                History history = snapshot.getValue(History.class);

                //display text
                retailAddress.setText(history.getRetailerAddress());
                trackNum.setText(history.getTrackingNumber());
                date.setText(history.getDeliveryDate());
                total.setText("Total Price: ₱"+history.getTotalPrice());
                retailName.setText("Buyer: "+history.getRetailerName());
                retailMobile.setText("Mobile Number: "+history.getRetailerMobile());
                logName.setText("Logistics: "+history.getLogisticsName());
                logMobile.setText("Mobile Number: "+history.getLogisticsMobile());

                //display image
                Uri imageURI = Uri.parse(history.getDeliveryImage());
                Glide.with(ProducerDeliveredOrdersDetails.this)
                        .load(imageURI)
                        .fitCenter()
                        .into(proof);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}