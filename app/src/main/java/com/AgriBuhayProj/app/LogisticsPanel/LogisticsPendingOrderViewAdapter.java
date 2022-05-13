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

public class LogisticsPendingOrderViewAdapter extends RecyclerView.Adapter<LogisticsPendingOrderViewAdapter.ViewHolder> {
    //VARIABLES
    //context
    private Context mcontext;
    //array list
    private List<LogisticsShipOrders> logisticsShipOrderslist;

    //adapter
    public LogisticsPendingOrderViewAdapter(Context context, List<LogisticsShipOrders> logisticsShipOrderslist) {
        this.logisticsShipOrderslist = logisticsShipOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.logistics_pendingorder, parent, false);
        return new LogisticsPendingOrderViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get position of item
        final LogisticsShipOrders logisticsShipOrders = logisticsShipOrderslist.get(position);
        //display values from db
        holder.productname.setText(logisticsShipOrders.getProductName());
        holder.price.setText("Price: ₱ " + logisticsShipOrders.getProductPrice());
        holder.quantity.setText("× " + logisticsShipOrders.getProductQuantity());
        holder.totalprice.setText("Total: ₱ " + logisticsShipOrders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return logisticsShipOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //xml
        TextView productname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //connect xml
            productname = itemView.findViewById(R.id.Product1);
            price = itemView.findViewById(R.id.Price1);
            totalprice = itemView.findViewById(R.id.Total1);
            quantity = itemView.findViewById(R.id.Quantity1);
        }
    }
}
