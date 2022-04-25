package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerOrdertobePrepareView extends AppCompatActivity {

    RecyclerView recyclerViewproduct;
    private List<ProducerWaitingOrders> producerWaitingOrdersList;
    private ProducerOrderToBePreparedViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID, userid;
    TextView grandtotal, note, address, name, number;
    LinearLayout l1;
    Button Preparing;
    private ProgressDialog progressDialog;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_ordertobe_prepare_view);
        recyclerViewproduct = findViewById(R.id.Recycle_viewOrder);
        grandtotal = findViewById(R.id.rupees);
        note = findViewById(R.id.NOTE);
        address = findViewById(R.id.ad);
        name = findViewById(R.id.nm);
        number = findViewById(R.id.num);
        l1 = findViewById(R.id.button1);
        Preparing = findViewById(R.id.preparing);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        progressDialog = new ProgressDialog(ProducerOrdertobePrepareView.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        recyclerViewproduct.setHasFixedSize(true);
        recyclerViewproduct.setLayoutManager(new LinearLayoutManager(ProducerOrdertobePrepareView.this));
        producerWaitingOrdersList = new ArrayList<>();
        CheforderdishesView();
    }

    private void CheforderdishesView() {
        RandomUID = getIntent().getStringExtra("RandomUID");

        //ProducerWaitingOrders
        reference = FirebaseDatabase.getInstance().getReference("ProducerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Products");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                producerWaitingOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProducerWaitingOrders producerWaitingOrders = snapshot.getValue(ProducerWaitingOrders.class);
                    producerWaitingOrdersList.add(producerWaitingOrders);
                }
                if (producerWaitingOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);

                } else {
                    l1.setVisibility(View.VISIBLE);
                    Preparing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            progressDialog.setMessage("Please wait...");
                            progressDialog.show();

                            //ProducerWaitingOrders
                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("ProducerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Products");
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        final ProducerWaitingOrders producerWaitingOrders = dataSnapshot1.getValue(ProducerWaitingOrders.class);
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        String productid = producerWaitingOrders.getProductId();
                                        userid = producerWaitingOrders.getUserId();
                                        hashMap.put("ProducerId", producerWaitingOrders.getProducerId());
                                        hashMap.put("ProductId", producerWaitingOrders.getProductId());
                                        hashMap.put("ProductName", producerWaitingOrders.getProductName());
                                        hashMap.put("ProductPrice", producerWaitingOrders.getProductPrice());
                                        hashMap.put("ProductQuantity", producerWaitingOrders.getProductQuantity());
                                        hashMap.put("RandomUID", RandomUID);
                                        hashMap.put("TotalPrice", producerWaitingOrders.getTotalPrice());
                                        hashMap.put("UserId", producerWaitingOrders.getUserId());
                                        //TODO this is the database for the ChefFinalOrders
                                        FirebaseDatabase.getInstance().getReference("ProducerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Products").child(productid).setValue(hashMap);
                                    }
                                    //ProducerWaitingOrders
                                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("ProducerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            final ProducerWaitingOrders1 producerWaitingOrders1 = dataSnapshot.getValue(ProducerWaitingOrders1.class);
                                            HashMap<String, String> hashMap1 = new HashMap<>();
                                            hashMap1.put("Address", producerWaitingOrders1.getAddress());
                                            hashMap1.put("GrandTotalPrice", producerWaitingOrders1.getGrandTotalPrice());
                                            hashMap1.put("MobileNumber", producerWaitingOrders1.getMobileNumber());
                                            hashMap1.put("Name", producerWaitingOrders1.getName());
                                            hashMap1.put("RandomUID", RandomUID);
                                            hashMap1.put("Status", "Producer is preparing your Order...");
                                            //ProducerFinalOrders
                                            FirebaseDatabase.getInstance().getReference("ProducerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    //RetailerFinalOrders
                                                    FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(userid).child(RandomUID).child("OtherInformation").child("Status").setValue("Producer is preparing your order...").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            //ProducerWaitingOrders
                                                            FirebaseDatabase.getInstance().getReference("ProducerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    //ProducerWaitingOrders
                                                                    FirebaseDatabase.getInstance().getReference("ProducerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            //Tokens
                                                                            FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                    String usertoken = dataSnapshot.getValue(String.class);
                                                                                    sendNotifications(usertoken, "Estimated Time", "Producer is Preparing your Order", "Preparing");
                                                                                    progressDialog.dismiss();
                                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ProducerOrdertobePrepareView.this);
                                                                                    builder.setMessage("See Prepared Orders");
                                                                                    builder.setCancelable(false);
                                                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                                            dialog.dismiss();
                                                                                            finish();

                                                                                        }
                                                                                    });
                                                                                    AlertDialog alert = builder.create();
                                                                                    alert.show();
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(@NonNull DatabaseError databaseError) {

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
                adapter = new ProducerOrderToBePreparedViewAdapter(ProducerOrdertobePrepareView.this, producerWaitingOrdersList);
                recyclerViewproduct.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //TODO this is the database for the ChefWaitingOrders
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProducerWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProducerWaitingOrders1 producerWaitingOrders1 = dataSnapshot.getValue(ProducerWaitingOrders1.class);
                grandtotal.setText("₱ " + producerWaitingOrders1.getGrandTotalPrice());
                note.setText(producerWaitingOrders1.getNote());
                address.setText(producerWaitingOrders1.getAddress());
                name.setText(producerWaitingOrders1.getName());
                number.setText(producerWaitingOrders1.getMobileNumber());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifications(String usertoken, String title, String message, String preparing) {
        Data data = new Data(title, message, preparing);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(ProducerOrdertobePrepareView.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
