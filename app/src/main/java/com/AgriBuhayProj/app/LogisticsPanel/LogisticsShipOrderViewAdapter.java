package com.AgriBuhayProj.app.LogisticsPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AgriBuhayProj.app.R;
import java.util.List;

//SHIPPING ORDER ADAPTER
public class LogisticsShipOrderViewAdapter extends RecyclerView.Adapter<LogisticsShipOrderViewAdapter.ViewHolder> {
    //VARIABLES
    private Context mcontext;
    private List<LogisticsShipFinalOrders> logisticsShipFinalOrderslist;

    //SET ADAPTER
    public LogisticsShipOrderViewAdapter(Context context, List<LogisticsShipFinalOrders> logisticsShipFinalOrderslist) {
        this.logisticsShipFinalOrderslist = logisticsShipFinalOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.shiporderview, parent, false);
        return new LogisticsShipOrderViewAdapter.ViewHolder(view);
    }

    //DISPLAY VALUES
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final LogisticsShipFinalOrders logisticsShipFinalOrders = logisticsShipFinalOrderslist.get(position);
        holder.productname.setText(logisticsShipFinalOrders.getProductName());
        holder.price.setText("Price: ₱ " + logisticsShipFinalOrders.getProductPrice());
        holder.quantity.setText("× " + logisticsShipFinalOrders.getProductQuantity());
        holder.totalprice.setText("Total: ₱ " + logisticsShipFinalOrders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return logisticsShipFinalOrderslist.size();
    }

    //CONNECT XML
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productname = itemView.findViewById(R.id.product2);
            price = itemView.findViewById(R.id.Price2);
            totalprice = itemView.findViewById(R.id.Total2);
            quantity = itemView.findViewById(R.id.Qty2);
        }
    }
}
