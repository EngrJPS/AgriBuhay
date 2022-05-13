package com.AgriBuhayProj.app.LogisticsPanel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AgriBuhayProj.app.R;
import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//ADAPTER FOR PENDING ORDERS
public class LogisticsPendingOrderFragmentAdapter extends RecyclerView.Adapter<LogisticsPendingOrderFragmentAdapter.ViewHolder> {
    //VARIABLES
    //context
    private Context context;
    //array list
    private List<LogisticsShipOrders1> logisticsShipOrders1List;
    //api = app notification
    private APIService apiService;
    //firebase
    FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    //strings
    String producerid;
    String logisticsId = fbAuth.getCurrentUser().getUid();

    //SET ARRAY LIST AND CONTEXT
    public LogisticsPendingOrderFragmentAdapter(Context context, List<LogisticsShipOrders1> logisticsShipOrders1List) {
        this.logisticsShipOrders1List = logisticsShipOrders1List;
        this.context = context;
    }

    //VIEW HOLDER
    @NonNull
    @Override
    public LogisticsPendingOrderFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.logistics_pendingorders, parent, false);
        //app notification service
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        return new LogisticsPendingOrderFragmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogisticsPendingOrderFragmentAdapter.ViewHolder holder, int position) {
        //get selected item postition
        LogisticsShipOrders1 logisticsShipOrders1 = logisticsShipOrders1List.get(position);
        //set values
        holder.Address.setText(logisticsShipOrders1.getAddress());
        holder.mobilenumber.setText(logisticsShipOrders1.getMobileNumber());
        holder.grandtotalprice.setText("Total Price: ₱" + logisticsShipOrders1.getGrandTotalPrice());
        //get order id
        final String randomuid = logisticsShipOrders1.getRandomUID();

        //BUTTON EVENTS
        //view clicked
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to pending order details
                Intent intent = new Intent(context, LogisticsPendingOrderView.class);
                //send order id
                intent.putExtra("Random", randomuid);
                context.startActivity(intent);
            }
        });

        //accept clicked
        holder.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pending orders reference
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(randomuid).child("Products");
                //get data
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //get specific data
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //get database values using model
                            LogisticsShipOrders logisticsShipOrderss = snapshot.getValue(LogisticsShipOrders.class);
                            //hashmap instance
                            HashMap<String, String> hashMap = new HashMap<>();
                            //get value from database
                            String productid = logisticsShipOrderss.getProductId();
                            producerid = logisticsShipOrderss.getProducerId();
                            //pending order values
                            hashMap.put("ProducerId", logisticsShipOrderss.getProducerId());
                            hashMap.put("ProductId", logisticsShipOrderss.getProductId());
                            hashMap.put("ProductName", logisticsShipOrderss.getProductName());
                            hashMap.put("ProductPrice", logisticsShipOrderss.getProductPrice());
                            hashMap.put("ProductQuantity", logisticsShipOrderss.getProductQuantity());
                            hashMap.put("RandomUID", logisticsShipOrderss.getRandomUID());
                            hashMap.put("TotalPrice", logisticsShipOrderss.getTotalPrice());
                            hashMap.put("UserId", logisticsShipOrderss.getUserId());
                            //add to final orders (products)
                            FirebaseDatabase.getInstance().getReference("LogisticsShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomuid).child("Products").child(productid).setValue(hashMap);
                        }

                        //other information reference
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(randomuid).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                LogisticsShipOrders1 logisticsShipOrders11 = dataSnapshot.getValue(LogisticsShipOrders1.class);
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                //other information values
                                hashMap1.put("Address", logisticsShipOrders11.getAddress());
                                hashMap1.put("ProducerId", logisticsShipOrders11.getProducerId());
                                hashMap1.put("ProducerName", logisticsShipOrders11.getProducerName());
                                hashMap1.put("GrandTotalPrice", logisticsShipOrders11.getGrandTotalPrice());
                                hashMap1.put("MobileNumber", logisticsShipOrders11.getMobileNumber());
                                hashMap1.put("Name", logisticsShipOrders11.getName());
                                hashMap1.put("RandomUID", randomuid);
                                hashMap1.put("UserId", logisticsShipOrders11.getUserId());
                                //add to final orders database (other information)
                                FirebaseDatabase.getInstance().getReference("LogisticsShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomuid).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //remove from pending orders (products)
                                        FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(randomuid).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                //remove from pending orders (other information)
                                                FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(randomuid).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //producer token reference
                                                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(producerid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                //get value from reference using model
                                                                String usertoken = dataSnapshot.getValue(String.class);
                                                                //Send app notification
                                                                sendNotifications(usertoken, "Order Accepted", "Your Order has been Accepted by the Delivery person", "AcceptOrder");
                                                                ReusableCodeForAll.ShowAlert(context, "", "Now you can check orders which are to be shipped");
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        //remove final order from producer (product)
                                                        FirebaseDatabase.getInstance().getReference("ProducerFinalOrders").child(producerid).child(randomuid).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                //remove final order from producer (product)
                                                                FirebaseDatabase.getInstance().getReference("ProducerFinalOrders").child(producerid).child(randomuid).child("OtherInformation").removeValue();
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

        //reject clicked
        holder.Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(randomuid).child("Products");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            LogisticsShipOrders logisticsShipOrders = dataSnapshot1.getValue(LogisticsShipOrders.class);
                            producerid = logisticsShipOrders.getProducerId();
                        }

                        //get producer token
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(producerid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String usertoken = dataSnapshot.getValue(String.class);
                                sendNotifications(usertoken, "Order Rejected", "Your Order has been Rejected by the Logistics due to unfortunate circumstances", "RejectOrder");
                                //remove order from selected logistics
                                FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(randomuid).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseDatabase.getInstance().getReference("LogisticsShipOrders").child(logisticsId).child(randomuid).child("OtherInformation").removeValue();
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

    //APP NOTIFICATIONS (API SERVICE)
    private void sendNotifications(String usertoken, String title, String message, String order) {
        //data instance + format
        Data data = new Data(title, message, order);
        //notification sender
        NotificationSender sender = new NotificationSender(data, usertoken);
        //send notification
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                //check response
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    //GET PENDING ORDERS
    @Override
    public int getItemCount() {
        return logisticsShipOrders1List.size();
    }

    //VIEW HOLDER XML
    public class ViewHolder extends RecyclerView.ViewHolder {
        //VALUES
        //xml
        TextView Address, grandtotalprice, mobilenumber;
        Button Vieworder, Accept, Reject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //connect xml
            Address = itemView.findViewById(R.id.textView1);
            mobilenumber = itemView.findViewById(R.id.textView2);
            grandtotalprice = itemView.findViewById(R.id.textView3);
            Vieworder = itemView.findViewById(R.id.btn1);
            Accept = itemView.findViewById(R.id.btn2);
            Reject = itemView.findViewById(R.id.btn3);
        }

        //get order address
        public TextView getAddress(){
            return Address;
        }
    }
}
