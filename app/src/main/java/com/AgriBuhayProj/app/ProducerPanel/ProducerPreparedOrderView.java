package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.AgriBuhayProj.app.Models.Logistics;
import com.AgriBuhayProj.app.Models.Producer;

import com.AgriBuhayProj.app.Printer.ProducerPrintOrder;
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

//PREPARED ORDER VIEW
public class ProducerPreparedOrderView extends AppCompatActivity {
    //VARIABLES
    RecyclerView recyclerViewproduct;
    TextView grandtotal, address, name, number;
    TextView cAddress,cMobile;
    LinearLayout l1;
    Button Prepared;
    private Button printOrder;
    private ProgressDialog progressDialog;
    Spinner Shipper;

    private List<ProducerFinalOrders> producerFinalOrdersList;
    private ProducerPreparedOrderViewAdapter adapter;

    ArrayList<String> logisticsLIst;
    ArrayList<String> idList;
    ArrayAdapter<String> logAdapter;

    private APIService apiService;

    DatabaseReference reference;

    String RandomUID, userid, productid, logisticsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_prepared_order_view);
        //CONNECT XML
        recyclerViewproduct = findViewById(R.id.Recycle_viewOrder);
        grandtotal = findViewById(R.id.Gtotal);
        address = findViewById(R.id.Cadress);
        name = findViewById(R.id.Cname);
        number = findViewById(R.id.Cnumber);
        cAddress = findViewById(R.id.cAdd);
        cMobile = findViewById(R.id.cNum);
        l1 = findViewById(R.id.linear);
        Shipper = findViewById(R.id.shipper);
        Prepared = findViewById(R.id.prepared);
        printOrder = findViewById(R.id.print);

        //DB REFERENCE
        reference = FirebaseDatabase.getInstance().getReference();

        //NOTIFICATION
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        //PROGRESS DIALOG
        progressDialog = new ProgressDialog(ProducerPreparedOrderView.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        //RECYCLER VIEW
        recyclerViewproduct.setHasFixedSize(true);
        recyclerViewproduct.setLayoutManager(new LinearLayoutManager(ProducerPreparedOrderView.this));

        //ARRAY LIST INSTANCE
        producerFinalOrdersList = new ArrayList<>();

        //LIST AVAILABLE LOGISTICS
        logisticsLIst = new ArrayList<>();
        idList = new ArrayList<>();
        logAdapter = new ArrayAdapter<>(ProducerPreparedOrderView.this, android.R.layout.simple_spinner_dropdown_item,logisticsLIst);

        //LOGISTICS REFERENCE
        reference.child("Logistics").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                logisticsLIst.clear();
                idList.clear();
                //get logistics
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    final Logistics logistics = dataSnapshot.getValue(Logistics.class);
                    String logisticsName = logistics.getFullName();
                    String logID = logistics.getLogisticsID();
                    idList.add(logID);
                    logisticsLIst.add(logisticsName);
                    logAdapter.notifyDataSetChanged();
                }
                //set spinner adapter
                Shipper.setAdapter(logAdapter);
                //logistics selected
                Shipper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Object value = adapterView.getSelectedItemPosition();
                        int position = (int) value;
                        logisticsId = idList.get(position);
                        Toast.makeText(ProducerPreparedOrderView.this, logisticsId, Toast.LENGTH_SHORT).show();

                        //logistics reference
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Logistics");
                        dbRef.child(logisticsId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //display logistics info
                                Logistics courier = snapshot.getValue(Logistics.class);
                                cAddress.setText("Courier Address: "+courier.getHouse());
                                cMobile.setText("Mobile: "+courier.getMobile());
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //assign order to logistics
                        ProducerorderdishesView(logisticsId);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProducerPreparedOrderView.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //ASSIGN ORDER TO SELECTED LOGISTICS
    private void ProducerorderdishesView(String logisticsId) {
        //tracking number
        RandomUID = getIntent().getStringExtra("RandomUID");
        //producer products prepared order reference
        reference = FirebaseDatabase.getInstance().getReference("ProducerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Products");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                producerFinalOrdersList.clear();

                //list products ordered
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ProducerFinalOrders producerFinalOrders = snapshot.getValue(ProducerFinalOrders.class);
                    producerFinalOrdersList.add(producerFinalOrders);
                }
                //check order
                if (producerFinalOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);
                } else {
                    l1.setVisibility(View.VISIBLE);

                    //order prepared
                    Prepared.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressDialog.setMessage("Please wait...");
                            progressDialog.show();

                            //Producer db reference
                            DatabaseReference data = FirebaseDatabase.getInstance().getReference("Producer").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            data.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //get db values
                                    final Producer producer = dataSnapshot.getValue(Producer.class);
                                    final String ProducerName = producer.getFirstName() + " " + producer.getLastName();

                                    //ProducerFinalOrders product db reference
                                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("ProducerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Products");
                                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            //get all reference children
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                //get values
                                                final ProducerFinalOrders producerFinalOrders = dataSnapshot1.getValue(ProducerFinalOrders.class);
                                                HashMap<String, String> hashMap = new HashMap<>();
                                                productid = producerFinalOrders.getProductId();
                                                userid = producerFinalOrders.getUserId();
                                                hashMap.put("ProducerId", producerFinalOrders.getProducerId());
                                                hashMap.put("ProductId", producerFinalOrders.getProductId());
                                                hashMap.put("ProductName", producerFinalOrders.getProductName());
                                                hashMap.put("ProductPrice", producerFinalOrders.getProductPrice());
                                                hashMap.put("ProductQuantity", producerFinalOrders.getProductQuantity());
                                                hashMap.put("RandomUID", RandomUID);
                                                hashMap.put("TotalPrice", producerFinalOrders.getTotalPrice());
                                                hashMap.put("UserId", producerFinalOrders.getUserId());
                                                //save product value to LogisticsShipOrders db
                                                FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(RandomUID).child("Products").child(productid).setValue(hashMap);
                                            }
                                            //ProducerFinalOrders other info db reference
                                            DatabaseReference data = FirebaseDatabase.getInstance().getReference("ProducerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                                            data.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    //get data
                                                    final ProducerFinalOrders1 producerFinalOrders1 = dataSnapshot.getValue(ProducerFinalOrders1.class);
                                                    HashMap<String, String> hashMap1 = new HashMap<>();
                                                    String producerid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    hashMap1.put("Address", producerFinalOrders1.getAddress());
                                                    hashMap1.put("ProducerId", producerid);
                                                    hashMap1.put("ProducerName", ProducerName);
                                                    hashMap1.put("GrandTotalPrice", producerFinalOrders1.getGrandTotalPrice());
                                                    hashMap1.put("MobileNumber", producerFinalOrders1.getMobileNumber());
                                                    hashMap1.put("Name", producerFinalOrders1.getName());
                                                    hashMap1.put("RandomUID", RandomUID);
                                                    hashMap1.put("Status", "Order is Prepared");
                                                    hashMap1.put("UserId", userid);

                                                    //save other info value to LogisticsShipOrders db
                                                    FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            //RetailerFinalOrders change order status
                                                            FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(userid).child(RandomUID).child("OtherInformation").child("Status").setValue("Order is Prepared...").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    //retailer token
                                                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            //get token
                                                                            String usertoken = dataSnapshot.getValue(String.class);
                                                                            //notify retailer
                                                                            sendNotifications(usertoken, "Estimated Time", "Your Order is Prepared", "Prepared");
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                }
                                                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    //logistics token reference
                                                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(logisticsId).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            //get token
                                                                            String usertoken = dataSnapshot.getValue(String.class);
                                                                            //notify logistics
                                                                            sendNotifications(usertoken, "New Order", "You have a New Order", "DeliveryOrder");
                                                                            progressDialog.dismiss();
                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(ProducerPreparedOrderView.this);
                                                                            builder.setMessage("Order has been sent to shipper");
                                                                            builder.setCancelable(false);
                                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    dialog.dismiss();
                                                                                    //direct to prepared order list
                                                                                    Intent b = new Intent(ProducerPreparedOrderView.this, ProducerPreparedOrder.class);
                                                                                    startActivity(b);
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

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    });

                    //print order
                    printOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), ProducerPrintOrder.class);
                            //attach tracking number
                            intent.putExtra("RandomUIID", RandomUID);
                            startActivity(intent);
                        }
                    });

                }

                //set adapter
                adapter = new ProducerPreparedOrderViewAdapter(ProducerPreparedOrderView.this, producerFinalOrdersList);
                recyclerViewproduct.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //ProducerFinalOrders other info reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProducerFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //display db values
                ProducerFinalOrders1 producerFinalOrders1 = dataSnapshot.getValue(ProducerFinalOrders1.class);
                grandtotal.setText("₱ " + producerFinalOrders1.getGrandTotalPrice());
                address.setText("Delivery Address: "+producerFinalOrders1.getAddress());
                name.setText("Retailer Name: "+producerFinalOrders1.getName());
                number.setText("Mobile: "+ producerFinalOrders1.getMobileNumber());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //NOTIFY USER
    private void sendNotifications(String usertoken, String title, String message, String prepared) {
        Data data = new Data(title, message, prepared);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(ProducerPreparedOrderView.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
