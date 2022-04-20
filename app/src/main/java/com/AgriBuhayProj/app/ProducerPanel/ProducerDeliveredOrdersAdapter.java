package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.AgriBuhayProj.app.Models.History;
import com.AgriBuhayProj.app.R;

import java.util.List;

public class ProducerDeliveredOrdersAdapter extends RecyclerView.Adapter<ProducerDeliveredOrdersAdapter.ViewHolder> {
    private Context context;
    private List<History> historyModel;

    public ProducerDeliveredOrdersAdapter(Context context, List<History> historyModel) {
        this.context = context;
        this.historyModel = historyModel;
    }

    @NonNull
    @Override
    public ProducerDeliveredOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.producer_delivered_orders_adapter,parent,false);
        return new ProducerDeliveredOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProducerDeliveredOrdersAdapter.ViewHolder holder, int position) {
        final History display = historyModel.get(position);
        holder.trackingNumber.setText(display.getTrackingNumber());
        holder.logisticsName.setText(display.getDeliveryID());
        holder.retailerName.setText(display.getRetailerName());
        holder.totalPrice.setText(display.getTotalPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProducerDeliveredOrdersDetails.class);
                intent.putExtra("trackingNumber",display.getTrackingNumber());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyModel.size();
    }

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