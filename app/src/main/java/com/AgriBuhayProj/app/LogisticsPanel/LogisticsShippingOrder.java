package com.AgriBuhayProj.app.LogisticsPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AgriBuhayProj.app.Models.Producer;
import com.AgriBuhayProj.app.ProductPanelBottomNavigation_Logistics;

import com.AgriBuhayProj.app.R;
import com.AgriBuhayProj.app.SendNotification.APIService;
import com.AgriBuhayProj.app.SendNotification.Client;
import com.AgriBuhayProj.app.SendNotification.Data;
import com.AgriBuhayProj.app.SendNotification.MyResponse;
import com.AgriBuhayProj.app.SendNotification.NotificationSender;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogisticsShippingOrder extends AppCompatActivity {

    TextView address, ProducerName,producerMobile, total, MobileNumber, Retname;
    Button shipped;
    ImageButton imageProof;

    private APIService apiService;

    String randomuid;
    String userid,producerID,logisticsID;

    private Uri imageUri,cropUri;

    FirebaseAuth fbAuth;
    FirebaseDatabase fbDB;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_shipping_order);

        address = findViewById(R.id.ad3);
        ProducerName = findViewById(R.id.producername2);
        producerMobile = findViewById(R.id.pMobile);
        total = findViewById(R.id.Shiptotal1);
        MobileNumber = findViewById(R.id.ShipNumber1);
        Retname = findViewById(R.id.ShipName1);
        shipped = findViewById(R.id.shipped2);
        imageProof = findViewById(R.id.uploadProof);

        shipped.setVisibility(View.GONE);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseDatabase.getInstance();

        randomuid = getIntent().getStringExtra("RandomUID");
        logisticsID = fbAuth.getCurrentUser().getUid();

        dbRef = fbDB.getReference("LogisticsShipFinalOrders").child(logisticsID).child(randomuid).child("OtherInformation");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LogisticsShipFinalOrders1 finalOrders = snapshot.getValue(LogisticsShipFinalOrders1.class);
                total.setText("₱ " + finalOrders.getGrandTotalPrice());
                address.setText(finalOrders.getAddress());
                Retname.setText(finalOrders.getName());
                MobileNumber.setText(finalOrders.getMobileNumber());
                ProducerName.setText("Producer: " + finalOrders.getProducerName());
                userid = finalOrders.getUserId();
                producerID = finalOrders.getProducerId();

                dbRef = fbDB.getReference("Producer").child(producerID);
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Producer producer = snapshot.getValue(Producer.class);

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imageProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectImageClick(view);
            }
        });

        shipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri!=null) {
                    Toast.makeText(LogisticsShippingOrder.this, imageUri.toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LogisticsShippingOrder.this, "no image detected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*Shipped();*/
    }

    /*private void Shipped() {
        randomuid = getIntent().getStringExtra("RandomUID");
        logisticsID = fbAuth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LogisticsShipFinalOrders").child(logisticsID).child(randomuid).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LogisticsShipFinalOrders1 logisticsShipFinalOrders1 = dataSnapshot.getValue(LogisticsShipFinalOrders1.class);
                total.setText("₱ " + logisticsShipFinalOrders1.getGrandTotalPrice());
                address.setText(logisticsShipFinalOrders1.getAddress());
                Retname.setText(logisticsShipFinalOrders1.getName());
                MobileNumber.setText("+63" + logisticsShipFinalOrders1.getMobileNumber());
                ProducerName.setText("Producer " + logisticsShipFinalOrders1.getProducerName());
                userid = logisticsShipFinalOrders1.getUserId();
                producerID = logisticsShipFinalOrders1.getProducerId();
                Shipped.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(userid).child(randomuid).child("OtherInformation").child("Status").setValue("Your Order is delivered").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String usertoken = dataSnapshot.getValue(String.class);
                                        sendNotifications(usertoken, "Home Producer", "Thank you for Ordering", "ThankYou");
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(producerID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String usertoken = dataSnapshot.getValue(String.class);
                                        sendNotifications(usertoken, "Order Placed", "The product of your choice has been delivered to Retailer's Doorstep", "Delivered");
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LogisticsShippingOrder.this);
                                builder.setMessage("Order is delivered, Now you can check for new Orders");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(LogisticsShippingOrder.this, ProductPanelBottomNavigation_Logistics.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(userid).child(randomuid).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(userid).child(randomuid).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseDatabase.getInstance().getReference("LogisticsShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomuid).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        FirebaseDatabase.getInstance().getReference("LogisticsShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomuid).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(userid).child("isOrdered").setValue("false");
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    private void onSelectImageClick(View v) {
        CropImage.startPickImageActivity(this);
    }
    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                cropUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCropImageActivity(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((ImageButton) findViewById(R.id.uploadProof)).setImageURI(result.getUri());
                Toast.makeText(this, "Cropped Successfully", Toast.LENGTH_SHORT).show();
                shipped.setVisibility(View.VISIBLE);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping Failed" + result.getError(), Toast.LENGTH_SHORT).show();
                shipped.setVisibility(View.GONE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (cropUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCropImageActivity(cropUri);
        } else {
            Toast.makeText(this, "Cancelling,required permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    //CROP IMAGE
    private void startCropImageActivity(Uri imageuri) {
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    //NOTIFY USER
    private void sendNotifications(String usertoken, String title, String message, String order) {
        Data data = new Data(title, message, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(retrofit2.Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(LogisticsShippingOrder.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
