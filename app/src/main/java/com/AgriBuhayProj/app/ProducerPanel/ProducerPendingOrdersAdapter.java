package com.AgriBuhayProj.app.ProducerPanel;

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

//PENDING ORDERS ADAPTER
public class ProducerPendingOrdersAdapter extends RecyclerView.Adapter<ProducerPendingOrdersAdapter.ViewHolder> {
    //VARIABLES
    private Context context;
    private List<ProducerPendingOrders1> producerPendingOrders1List;
    private ProgressDialog progressDialog;

    private APIService apiService;
    String userid, productid;

    //ADAPTER
    public ProducerPendingOrdersAdapter(Context context, List<ProducerPendingOrders1> producerPendingOrders1List) {
        this.producerPendingOrders1List = producerPendingOrders1List;
        this.context = context;
    }

    //VIEW HOLDER
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.producer_orders, parent, false);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        return new ProducerPendingOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProducerPendingOrders1 producerPendingOrders1 = producerPendingOrders1List.get(position);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        holder.Address.setText(producerPendingOrders1.getAddress());
        holder.grandtotalprice.setText("Total Price: ???" + producerPendingOrders1.getGrandTotalPrice());

        final String random = producerPendingOrders1.getRandomUID();

        //view order
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProducerOrderProducts.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
            }
        });

        //accept order
        holder.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Accepting Order...");
                progressDialog.show();

                //pending orders product reference
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProducerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Products");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //get all products ordered
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final ProducerPendingOrders producerPendingOrders = snapshot.getValue(ProducerPendingOrders.class);
                            HashMap<String, String> hashMap = new HashMap<>();
                            String producerid = producerPendingOrders.getProducerId();
                            String productid = producerPendingOrders.getProductId();
                            hashMap.put("ProducerId", producerPendingOrders.getProducerId());
                            hashMap.put("ProductId", producerPendingOrders.getProductId());
                            hashMap.put("ProductName", producerPendingOrders.getProductName());
                            hashMap.put("ProductPrice", producerPendingOrders.getPrice());
                            hashMap.put("ProductQuantity", producerPendingOrders.getProductQuantity());
                            hashMap.put("RandomUID", random);
                            hashMap.put("TotalPrice", producerPendingOrders.getTotalPrice());
                            hashMap.put("UserId", producerPendingOrders.getUserId());
                            //save product values to payment orders db
                            FirebaseDatabase.getInstance().getReference("ProducerPaymentOrders").child(producerid).child(random).child("Products").child(productid).setValue(hashMap);
                        }
                        //pending orders other info db reference
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("ProducerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //get values
                                ProducerPendingOrders1 producerPendingOrders1 = dataSnapshot.getValue(ProducerPendingOrders1.class);
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("Address", producerPendingOrders1.getAddress());
                                hashMap1.put("GrandTotalPrice", producerPendingOrders1.getGrandTotalPrice());
                                hashMap1.put("MobileNumber", producerPendingOrders1.getMobileNumber());
                                hashMap1.put("Name", producerPendingOrders1.getName());
                                hashMap1.put("Note", producerPendingOrders1.getNote());
                                hashMap1.put("RandomUID", random);
                                //save values to payment orders other info
                                FirebaseDatabase.getInstance().getReference("ProducerPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //producer pending order product reference
                                        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("ProducerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Products");
                                        Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //get all data from reference
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    final ProducerPendingOrders producerPendingOrders = snapshot.getValue(ProducerPendingOrders.class);
                                                    HashMap<String, String> hashMap2 = new HashMap<>();
                                                    userid = producerPendingOrders.getUserId();
                                                    productid = producerPendingOrders.getProductId();
                                                    hashMap2.put("ProducerId", producerPendingOrders.getProducerId());
                                                    hashMap2.put("ProductId", producerPendingOrders.getProductId());
                                                    hashMap2.put("ProductName", producerPendingOrders.getProductName());
                                                    hashMap2.put("ProductPrice", producerPendingOrders.getPrice());
                                                    hashMap2.put("ProductQuantity", producerPendingOrders.getProductQuantity());
                                                    hashMap2.put("RandomUID", random);
                                                    hashMap2.put("TotalPrice", producerPendingOrders.getTotalPrice());
                                                    hashMap2.put("UserId", producerPendingOrders.getUserId());
                                                    //save values to RetailerPaymentOrders products db
                                                    FirebaseDatabase.getInstance().getReference("RetailerPaymentOrders").child(userid).child(random).child("Products").child(productid).setValue(hashMap2);
                                                }
                                                //ProducerPendingOrders db (Other Information)
                                                DatabaseReference dataa = FirebaseDatabase.getInstance().getReference("ProducerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation");
                                                dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        //get values
                                                        ProducerPendingOrders1 producerPendingOrders1 = dataSnapshot.getValue(ProducerPendingOrders1.class);
                                                        HashMap<String, String> hashMap3 = new HashMap<>();
                                                        hashMap3.put("Address", producerPendingOrders1.getAddress());
                                                        hashMap3.put("GrandTotalPrice", producerPendingOrders1.getGrandTotalPrice());
                                                        hashMap3.put("MobileNumber", producerPendingOrders1.getMobileNumber());
                                                        hashMap3.put("Name", producerPendingOrders1.getName());
                                                        hashMap3.put("Note", producerPendingOrders1.getNote());
                                                        hashMap3.put("RandomUID", random);
                                                        //save values to RetailerPaymentOrders db (Other Information)
                                                        FirebaseDatabase.getInstance().getReference("RetailerPaymentOrders").child(userid).child(random).child("OtherInformation").setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                //RetailerPendingOrders remove Products values
                                                                FirebaseDatabase.getInstance().getReference("RetailerPendingOrders").child(userid).child(random).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        //RetailerPendingOrders remove OtherInformation values
                                                                        FirebaseDatabase.getInstance().getReference("RetailerPendingOrders").child(userid).child(random).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                //ProducerPendingOrders remove products
                                                                                FirebaseDatabase.getInstance().getReference("ProducerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        //ProducerPendingOrders remove other info
                                                                                        FirebaseDatabase.getInstance().getReference("ProducerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                //retailer token reference
                                                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                        progressDialog.dismiss();
                                                                                                        //get token
                                                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                                                        //send notification
                                                                                                        sendNotifications(usertoken, "Order Accepted", "Your Order has been Accepted by the Producer, Now make Payment for Order", "Payment");
                                                                                                        ReusableCodeForAll.ShowAlert(context,"Order Accepted","Wait for the Retailer to make Payment");
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

        //reject order
        holder.Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Rejecting Order...");
                progressDialog.show();

                //ProducerPendingOrders product reference
                DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("ProducerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Products");
                Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //get values
                            final ProducerPendingOrders producerPendingOrders = snapshot.getValue(ProducerPendingOrders.class);
                            userid = producerPendingOrders.getUserId();
                            productid = producerPendingOrders.getProductId();
                        }
                        //retailer token reference
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //get token
                                String usertoken = dataSnapshot.getValue(String.class);
                                //notify retailer
                                sendNotifications(usertoken, "Order Rejected", "Your Order has been Rejected by the producer due to some Circumstances", "Home");
                                //RetailerPendingOrders remove order
                                FirebaseDatabase.getInstance().getReference("RetailerPendingOrders").child(userid).child(random).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //ProducerPendingOrders remove order
                                        FirebaseDatabase.getInstance().getReference("ProducerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                //change order status
                                                FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(userid).child("isOrdered").setValue("false").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressDialog.dismiss();
                                                        ReusableCodeForAll.ShowAlert(context,"","You had rejected an order");
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressDialog.dismiss();
                                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                        Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        });
    }

    //NOTIFY USER
    private void sendNotifications(String usertoken, String title, String message, String order) {

        Data data = new Data(title, message, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
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

    //ARRAY LIST SIZE
    @Override
    public int getItemCount() {
        return producerPendingOrders1List.size();
    }

    //CONNECT XML
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice;
        Button Vieworder, Accept, Reject;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Address = itemView.findViewById(R.id.AD);
            grandtotalprice = itemView.findViewById(R.id.TP);
            Vieworder = itemView.findViewById(R.id.vieww);
            Accept = itemView.findViewById(R.id.accept);
            Reject = itemView.findViewById(R.id.reject);
        }
    }
}