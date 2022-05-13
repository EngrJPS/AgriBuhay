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

//TRACK ORDER ADAPTER
public class RetailerTrackAdapter extends RecyclerView.Adapter<RetailerTrackAdapter.ViewHolder> {
    //VARIABLES
    private Context context;
    private List<RetailerFinalOrders> retailerFinalOrderslist;

    //ADAPTER
    public RetailerTrackAdapter(Context context, List<RetailerFinalOrders> retailerFinalOrderslist) {
        this.retailerFinalOrderslist = retailerFinalOrderslist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.retailer_trackorder, parent, false);
        return new RetailerTrackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //display order details
        final RetailerFinalOrders retailerFinalOrders = retailerFinalOrderslist.get(position);
        holder.Productname.setText(retailerFinalOrders.getProductName());
        holder.Quantity.setText(retailerFinalOrders.getProductQuantity() + "× ");
        holder.Totalprice.setText("₱ " + retailerFinalOrders.getTotalPrice());

    }

    @Override
    public int getItemCount() {
        return retailerFinalOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Productname, Quantity, Totalprice;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Productname = itemView.findViewById(R.id.productnm);
            Quantity = itemView.findViewById(R.id.productqty);
            Totalprice = itemView.findViewById(R.id.totRS);
        }
    }
}
