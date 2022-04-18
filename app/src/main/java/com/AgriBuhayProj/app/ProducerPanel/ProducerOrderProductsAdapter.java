package com.AgriBuhayProj.app.ProducerPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AgriBuhayProj.app.R;
import java.util.List;

public class ProducerOrderProductsAdapter extends RecyclerView.Adapter<ProducerOrderProductsAdapter.ViewHolder> {


    private Context mcontext;
    private List<ProducerPendingOrders> producerPendingOrderslist;

    public ProducerOrderProductsAdapter(Context context, List<ProducerPendingOrders> producerPendingOrderslist) {
        this.producerPendingOrderslist = producerPendingOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.producer_order_products, parent, false);
        return new ProducerOrderProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ProducerPendingOrders producerPendingOrders = producerPendingOrderslist.get(position);
        holder.productname.setText(producerPendingOrders.getProductName());
        holder.price.setText("Price: ₱ " + producerPendingOrders.getPrice());
        holder.quantity.setText("× " + producerPendingOrders.getProductQuantity());
        holder.totalprice.setText("Total: ₱ " + producerPendingOrders.getTotalPrice());


    }

    @Override
    public int getItemCount() {
        return producerPendingOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.PN);
            price = itemView.findViewById(R.id.PR);
            totalprice = itemView.findViewById(R.id.TR);
            quantity = itemView.findViewById(R.id.QY);
        }
    }
}
