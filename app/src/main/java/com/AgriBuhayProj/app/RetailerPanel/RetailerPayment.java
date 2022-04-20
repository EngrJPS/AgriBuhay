package com.AgriBuhayProj.app.RetailerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.AgriBuhayProj.app.ProductPanelBottomNavigation_Retailer;
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

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetailerPayment extends AppCompatActivity {

    TextView OnlinePayment, CashPayment;
    String RandomUID, ProducerID;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer_payment);

        OnlinePayment = (TextView) findViewById(R.id.online);
        CashPayment = (TextView) findViewById(R.id.cash);
        RandomUID = getIntent().getStringExtra("RandomUID");
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        OnlinePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RetailerPayment.this, RetailerOnlinePayment.class);
                intent.putExtra("randomUID", RandomUID);
                startActivity(intent);
            }
        });


        CashPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("RetailerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Products");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            final RetailerPaymentOrders retailerPaymentOrders = dataSnapshot1.getValue(RetailerPaymentOrders.class);
                            HashMap<String, String> hashMap = new HashMap<>();
                            String productid = retailerPaymentOrders.getProductId();
                            hashMap.put("ProducerId", retailerPaymentOrders.getProducerId());
                            hashMap.put("ProductId", retailerPaymentOrders.getProductId());
                            hashMap.put("ProductName", retailerPaymentOrders.getProductName());
                            hashMap.put("ProductPrice", retailerPaymentOrders.getProductPrice());
                            hashMap.put("ProductQuantity", retailerPaymentOrders.getProductQuantity());
                            hashMap.put("RandomUID", RandomUID);
                            hashMap.put("TotalPrice", retailerPaymentOrders.getTotalPrice());
                            hashMap.put("UserId", retailerPaymentOrders.getUserId());
                            FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Products").child(productid).setValue(hashMap);
                        }
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("RetailerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final RetailerPaymentOrders1 retailerPaymentOrders1 = dataSnapshot.getValue(RetailerPaymentOrders1.class);
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("Address", retailerPaymentOrders1.getAddress());
                                hashMap1.put("GrandTotalPrice", retailerPaymentOrders1.getGrandTotalPrice());
                                hashMap1.put("MobileNumber", retailerPaymentOrders1.getMobileNumber());
                                hashMap1.put("Name", retailerPaymentOrders1.getName());
                                hashMap1.put("Note", retailerPaymentOrders1.getNote());
                                hashMap1.put("RandomUID", RandomUID);
                                hashMap1.put("Status", "Your order is waiting to be prepared by Farmers...");
                                FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("RetailerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Products");
                                        Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    final RetailerPaymentOrders retailerPaymentOrderss = snapshot.getValue(RetailerPaymentOrders.class);
                                                    HashMap<String, String> hashMap2 = new HashMap<>();
                                                    String productid = retailerPaymentOrderss.getProductId();
                                                    ProducerID = retailerPaymentOrderss.getProducerId();
                                                    hashMap2.put("ProducerId", retailerPaymentOrderss.getProducerId());
                                                    hashMap2.put("ProductId", retailerPaymentOrderss.getProductId());
                                                    hashMap2.put("ProductName", retailerPaymentOrderss.getProductName());
                                                    hashMap2.put("ProductPrice", retailerPaymentOrderss.getProductPrice());
                                                    hashMap2.put("ProductQuantity", retailerPaymentOrderss.getProductQuantity());
                                                    hashMap2.put("RandomUID", RandomUID);
                                                    hashMap2.put("TotalPrice", retailerPaymentOrderss.getTotalPrice());
                                                    hashMap2.put("UserId", retailerPaymentOrderss.getUserId());
                                                    FirebaseDatabase.getInstance().getReference("ProducerWaitingOrders").child(ProducerID).child(RandomUID).child("Products").child(productid).setValue(hashMap2);
                                                }
                                                DatabaseReference dataa = FirebaseDatabase.getInstance().getReference("RetailerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                                                dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        RetailerPaymentOrders1 retailerPaymentOrders11 = dataSnapshot.getValue(RetailerPaymentOrders1.class);
                                                        HashMap<String, String> hashMap3 = new HashMap<>();
                                                        hashMap3.put("Address", retailerPaymentOrders11.getAddress());
                                                        hashMap3.put("GrandTotalPrice", retailerPaymentOrders11.getGrandTotalPrice());
                                                        hashMap3.put("MobileNumber", retailerPaymentOrders11.getMobileNumber());
                                                        hashMap3.put("Name", retailerPaymentOrders11.getName());
                                                        hashMap3.put("Note", retailerPaymentOrders11.getNote());
                                                        hashMap3.put("RandomUID", RandomUID);
                                                        hashMap3.put("Status", "Your order is waiting to be prepared by Farmers...");
                                                        FirebaseDatabase.getInstance().getReference("ProducerWaitingOrders").child(ProducerID).child(RandomUID).child("OtherInformation").setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                FirebaseDatabase.getInstance().getReference("ProducerPaymentOrders").child(ProducerID).child(RandomUID).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        FirebaseDatabase.getInstance().getReference("ProducerPaymentOrders").child(ProducerID).child(RandomUID).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                FirebaseDatabase.getInstance().getReference("RetailerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        FirebaseDatabase.getInstance().getReference("RetailerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(ProducerID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                                                        sendNotifications(usertoken, "Order Confirmed", "Payment mode is confirmed by user, Now you can start the order", "Confirm");
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                    }
                                                                                                });

                                                                                            }
                                                                                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                AlertDialog.Builder builder = new AlertDialog.Builder(RetailerPayment.this);
                                                                                                builder.setMessage("Payment mode confirmed, Now you can track your order.");
                                                                                                builder.setCancelable(false);
                                                                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                                    @Override
                                                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                                                        dialog.dismiss();
                                                                                                        Intent b = new Intent(RetailerPayment.this, ProductPanelBottomNavigation_Retailer.class);
                                                                                                        b.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                                                        startActivity(b);
                                                                                                        finish();

                                                                                                    }
                                                                                                });
                                                                                                AlertDialog alert = builder.create();
                                                                                                alert.show();
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

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void sendNotifications(String usertoken, String title, String message, String confirm) {
        Data data = new Data(title, message, confirm);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(RetailerPayment.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
