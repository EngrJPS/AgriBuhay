package com.AgriBuhayProj.app.ProducerPanel;

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

public class ChefPendingOrdersAdapter extends RecyclerView.Adapter<ChefPendingOrdersAdapter.ViewHolder> {

    private Context context;
    private List<ChefPendingOrders1> chefPendingOrders1list;
    private APIService apiService;
    String userid, dishid;


    public ChefPendingOrdersAdapter(Context context, List<ChefPendingOrders1> chefPendingOrders1list) {
        this.chefPendingOrders1list = chefPendingOrders1list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chef_orders, parent, false);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        return new ChefPendingOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ChefPendingOrders1 chefPendingOrders1 = chefPendingOrders1list.get(position);
        holder.Address.setText(chefPendingOrders1.getAddress());
        holder.grandtotalprice.setText("GrandTotal: ₱ " + chefPendingOrders1.getGrandTotalPrice());
        final String random = chefPendingOrders1.getRandomUID();
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chef_order_dishes.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
            }
        });

        holder.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO this is the database for the ChefPendngOrders
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Dishes");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final ChefPendingOrders chefPendingOrders = snapshot.getValue(ChefPendingOrders.class);
                            HashMap<String, String> hashMap = new HashMap<>();
                            String chefid = chefPendingOrders.getChefId();
                            String dishid = chefPendingOrders.getDishId();
                            hashMap.put("ChefId", chefPendingOrders.getChefId());
                            hashMap.put("DishId", chefPendingOrders.getDishId());
                            hashMap.put("DishName", chefPendingOrders.getDishName());
                            hashMap.put("DishPrice", chefPendingOrders.getPrice());
                            hashMap.put("DishQuantity", chefPendingOrders.getDishQuantity());
                            hashMap.put("RandomUID", random);
                            hashMap.put("TotalPrice", chefPendingOrders.getTotalPrice());
                            hashMap.put("UserId", chefPendingOrders.getUserId());
                            //TODO this is the database for the ChefPaymentOrders
                            FirebaseDatabase.getInstance().getReference("ChefPaymentOrders").child(chefid).child(random).child("Dishes").child(dishid).setValue(hashMap);
                        }
                        //TODO this is the database for the ChefPendingOrders
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ChefPendingOrders1 chefPendingOrders1 = dataSnapshot.getValue(ChefPendingOrders1.class);
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("Address", chefPendingOrders1.getAddress());
                                hashMap1.put("GrandTotalPrice", chefPendingOrders1.getGrandTotalPrice());
                                hashMap1.put("MobileNumber", chefPendingOrders1.getMobileNumber());
                                hashMap1.put("Name", chefPendingOrders1.getName());
                                hashMap1.put("Note",chefPendingOrders1.getNote());
                                hashMap1.put("RandomUID", random);
                                //TODO this is the database for the ChefPaymentOrders
                                FirebaseDatabase.getInstance().getReference("ChefPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //TODO this is the database for the ChefPendingOrders
                                        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Dishes");
                                        Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    final ChefPendingOrders chefPendingOrders = snapshot.getValue(ChefPendingOrders.class);
                                                    HashMap<String, String> hashMap2 = new HashMap<>();
                                                    userid = chefPendingOrders.getUserId();
                                                    dishid = chefPendingOrders.getDishId();
                                                    hashMap2.put("ChefId", chefPendingOrders.getChefId());
                                                    hashMap2.put("DishId", chefPendingOrders.getDishId());
                                                    hashMap2.put("DishName", chefPendingOrders.getDishName());
                                                    hashMap2.put("DishPrice", chefPendingOrders.getPrice());
                                                    hashMap2.put("DishQuantity", chefPendingOrders.getDishQuantity());
                                                    hashMap2.put("RandomUID", random);
                                                    hashMap2.put("TotalPrice", chefPendingOrders.getTotalPrice());
                                                    hashMap2.put("UserId", chefPendingOrders.getUserId());
                                                    //TODO this is the database for the ChefPaymentOrders
                                                    FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(userid).child(random).child("Dishes").child(dishid).setValue(hashMap2);
                                                }
                                                DatabaseReference dataa = FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation");
                                                dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        ChefPendingOrders1 chefPendingOrders1 = dataSnapshot.getValue(ChefPendingOrders1.class);
                                                        HashMap<String, String> hashMap3 = new HashMap<>();
                                                        hashMap3.put("Address", chefPendingOrders1.getAddress());
                                                        hashMap3.put("GrandTotalPrice", chefPendingOrders1.getGrandTotalPrice());
                                                        hashMap3.put("MobileNumber", chefPendingOrders1.getMobileNumber());
                                                        hashMap3.put("Name", chefPendingOrders1.getName());
                                                        hashMap3.put("Note",chefPendingOrders1.getNote());
                                                        hashMap3.put("RandomUID", random);
                                                        //TODO this is the database for the CustomerPaymentOrders
                                                        FirebaseDatabase.getInstance().getReference("CustomerPaymentOrders").child(userid).child(random).child("OtherInformation").setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                //TODO this is the database for the CustomerPaymentOrders
                                                                FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(userid).child(random).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        //TODO this is the database for the CustomerPendingOrders
                                                                        FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(userid).child(random).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                //TODO this is the database for the ChefPendingOrders
                                                                                FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        //TODO this is the database for the ChefPendingOrders
                                                                                        FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                //TODO this is the database for the Tokens
                                                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                                                        sendNotifications(usertoken, "Order Accepted", "Your Order has been Accepted by the Producer, Now make Payment for Order", "Payment");
                                                                                                        ReusableCodeForAll.ShowAlert(context,"","Wait for the Customer to make Payment");

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

        holder.Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO this is the database for the ChefPendingOrders
                DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Dishes");
                Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final ChefPendingOrders chefPendingOrders = snapshot.getValue(ChefPendingOrders.class);
                            userid = chefPendingOrders.getUserId();
                            dishid = chefPendingOrders.getDishId();
                        }
                        //TODO this is the database for the tokens
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String usertoken = dataSnapshot.getValue(String.class);
                                sendNotifications(usertoken, "Order Rejected", "Your Order has been Rejected by the Chef due to some Circumstances", "Home");
                                //TODO this is the database for the CustomerPendingOrders
                                FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(userid).child(random).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //TODO this is the database for the CustomerPendingOrders
                                        FirebaseDatabase.getInstance().getReference("CustomerPendingOrders").child(userid).child(random).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                //TODO this is the database for the ChefPendingOrders
                                                FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        //TODO this is the database for the ChefPendingOrders
                                                        FirebaseDatabase.getInstance().getReference("ChefPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                //TODO this is the database for the AlreadyOrderd
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

    @Override
    public int getItemCount() {
        return chefPendingOrders1list.size();
    }

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