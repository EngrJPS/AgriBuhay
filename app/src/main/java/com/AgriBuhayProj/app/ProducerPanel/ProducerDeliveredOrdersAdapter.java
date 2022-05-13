package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.AgriBuhayProj.app.Models.History;
import com.AgriBuhayProj.app.R;

import java.util.List;

//DELIVERED ORDERS ADAPTER
public class ProducerDeliveredOrdersAdapter extends RecyclerView.Adapter<ProducerDeliveredOrdersAdapter.ViewHolder> {
    //VARIABLES
    private Context context;
    private List<History> historyModel;

    //ADAPTER
    public ProducerDeliveredOrdersAdapter(Context context, List<History> historyModel) {
        this.context = context;
        this.historyModel = historyModel;
    }

    //CREATE VIEW HOLDER
    @NonNull
    @Override
    public ProducerDeliveredOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.producer_delivered_orders_adapter,parent,false);
        return new ProducerDeliveredOrdersAdapter.ViewHolder(view);
    }

    //DISPLAY FINISHED ORDERS
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProducerDeliveredOrdersAdapter.ViewHolder holder, int position) {
        final History display = historyModel.get(position);
        holder.trackingNumber.setText("Tracking Number: \n"+display.getTrackingNumber());
        holder.logisticsName.setText("Logistics: "+display.getLogisticsName());
        holder.retailerName.setText("Retailer: "+display.getRetailerName());
        holder.totalPrice.setText("₱"+display.getTotalPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to delivered order details
                Intent intent = new Intent(context,ProducerDeliveredOrdersDetails.class);
                //attached tracking number
                intent.putExtra("trackingNumber",display.getTrackingNumber());
                context.startActivity(intent);
            }
        });
    }

    //ARRAY SIZE
    @Override
    public int getItemCount() {
        return historyModel.size();
    }

    //CONNECT XML
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView trackingNumber,logisticsName,retailerName,totalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trackingNumber = itemView.findViewById(R.id.trackNum);
            logisticsName = itemView.findViewById(R.id.delName);
            retailerName = itemView.findViewById(R.id.retName);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }
    }
}