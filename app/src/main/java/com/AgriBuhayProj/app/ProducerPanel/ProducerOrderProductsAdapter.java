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

//PENDING ORDERS ADAPTER
public class ProducerOrderProductsAdapter extends RecyclerView.Adapter<ProducerOrderProductsAdapter.ViewHolder> {
    //VARIABLES
    private Context mcontext;
    private List<ProducerPendingOrders> producerPendingOrderslist;

    //ADAPTER
    public ProducerOrderProductsAdapter(Context context, List<ProducerPendingOrders> producerPendingOrderslist) {
        this.producerPendingOrderslist = producerPendingOrderslist;
        this.mcontext = context;
    }

    //VIEW HOLDER
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.producer_order_products_adapter, parent, false);
        return new ProducerOrderProductsAdapter.ViewHolder(view);
    }

    //DISPLAY PENDING ORDER DETAILS
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ProducerPendingOrders producerPendingOrders = producerPendingOrderslist.get(position);
        holder.productname.setText(producerPendingOrders.getProductName());
        holder.price.setText("Price: ₱ " + producerPendingOrders.getPrice());
        holder.quantity.setText("× " + producerPendingOrders.getProductQuantity());
        holder.totalprice.setText("Total: ₱ " + producerPendingOrders.getTotalPrice());


    }

    //ARRAY LIST SIZE
    @Override
    public int getItemCount() {
        return producerPendingOrderslist.size();
    }

    //CONNECT XML
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
