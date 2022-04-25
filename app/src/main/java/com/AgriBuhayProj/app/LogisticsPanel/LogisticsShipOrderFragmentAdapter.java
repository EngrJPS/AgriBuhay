package com.AgriBuhayProj.app.LogisticsPanel;

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
import com.AgriBuhayProj.app.SendNotification.APIService;
import com.AgriBuhayProj.app.SendNotification.Client;
import com.AgriBuhayProj.app.SendNotification.Data;
import com.AgriBuhayProj.app.SendNotification.MyResponse;
import com.AgriBuhayProj.app.SendNotification.NotificationSender;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogisticsShipOrderFragmentAdapter extends RecyclerView.Adapter<LogisticsShipOrderFragmentAdapter.ViewHolder> {

    private Context context;
    private List<LogisticsShipFinalOrders1> logisticsShipFinalOrders1List;
    private APIService apiService;


    public LogisticsShipOrderFragmentAdapter(Context context, List<LogisticsShipFinalOrders1> logisticsShipFinalOrders1List) {
        this.logisticsShipFinalOrders1List = logisticsShipFinalOrders1List;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.logistics_shiporders, parent, false);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        return new LogisticsShipOrderFragmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final LogisticsShipFinalOrders1 logisticsShipFinalOrders1 = logisticsShipFinalOrders1List.get(position);
        holder.Address.setText(logisticsShipFinalOrders1.getAddress());
        holder.grandtotalprice.setText("Total Price: ₱" + logisticsShipFinalOrders1.getGrandTotalPrice());
        holder.mobilenumber.setText(logisticsShipFinalOrders1.getMobileNumber());
        final String random = logisticsShipFinalOrders1.getRandomUID();
        final String userid = logisticsShipFinalOrders1.getUserId();
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LogisticsShipOrderView.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
            }
        });

        holder.ShipOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(userid).child(random).child("OtherInformation").child("Status").setValue("Your Order is on the way...").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String usertoken = dataSnapshot.getValue(String.class);
                                sendNotifications(usertoken, "Estimated Time", "Your Order has been collected by Delivery Person, your product is on the way", "DeliverOrder");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(context, LogisticsShippingOrder.class);
                        intent.putExtra("RandomUID",random);
                        context.startActivity(intent);
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
        return logisticsShipFinalOrders1List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice, mobilenumber;
        Button Vieworder, ShipOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Address = itemView.findViewById(R.id.ad2);
            mobilenumber = itemView.findViewById(R.id.MB2);
            grandtotalprice = itemView.findViewById(R.id.TP2);
            Vieworder = itemView.findViewById(R.id.view2);
            ShipOrder = itemView.findViewById(R.id.ship2);
        }
    }
}
