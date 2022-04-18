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

public class ProducerOrderToBePreparedViewAdapter extends RecyclerView.Adapter<ProducerOrderToBePreparedViewAdapter.ViewHolder> {

    private Context mcontext;
    private List<ProducerWaitingOrders> producerWaitingOrderslist;

    public ProducerOrderToBePreparedViewAdapter(Context context, List<ProducerWaitingOrders> producerWaitingOrderslist) {
        this.producerWaitingOrderslist = producerWaitingOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.producer_ordertobeprepared_view, parent, false);
        return new ProducerOrderToBePreparedViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ProducerWaitingOrders producerWaitingOrders = producerWaitingOrderslist.get(position);
        holder.productname.setText(producerWaitingOrders.getProductName());
        holder.price.setText("Price: ₱ " + producerWaitingOrders.getProductPrice());
        holder.quantity.setText("× " + producerWaitingOrders.getProductQuantity());
        holder.totalprice.setText("Total: ₱ " + producerWaitingOrders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return producerWaitingOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.Pname);
            price = itemView.findViewById(R.id.Dprice);
            totalprice = itemView.findViewById(R.id.Tprice);
            quantity = itemView.findViewById(R.id.Dqty);
        }
    }
}
