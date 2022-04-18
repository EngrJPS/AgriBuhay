package com.AgriBuhayProj.app.RetailerPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AgriBuhayProj.app.R;
import java.util.List;

public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.ViewHolder> {

    private Context context;
    private List<RetailerPendingOrders> retailerPendingOrderslist;

    public PendingOrdersAdapter(Context context, List<RetailerPendingOrders> retailerPendingOrderslist) {
        this.retailerPendingOrderslist = retailerPendingOrderslist;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_order_products, parent, false);
        return new PendingOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final RetailerPendingOrders retailerPendingOrders = retailerPendingOrderslist.get(position);
        holder.Productname.setText(retailerPendingOrders.getProductName());
        holder.Price.setText("Price: ₱ " + retailerPendingOrders.getPrice());
        holder.Quantity.setText("× " + retailerPendingOrders.getProductQuantity());
        holder.Totalprice.setText("Total: ₱ " + retailerPendingOrders.getTotalPrice());

    }

    @Override
    public int getItemCount() {
        return retailerPendingOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Productname, Price, Quantity, Totalprice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Productname = itemView.findViewById(R.id.Dishh);
            Price = itemView.findViewById(R.id.pricee);
            Quantity = itemView.findViewById(R.id.qtyy);
            Totalprice = itemView.findViewById(R.id.total);

        }
    }
}
