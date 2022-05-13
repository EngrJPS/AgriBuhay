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

//TO BE PREPARED DETAILS ADAPTER
public class ProducerOrderToBePreparedViewAdapter extends RecyclerView.Adapter<ProducerOrderToBePreparedViewAdapter.ViewHolder> {
    //VARIABLES
    private Context mcontext;
    private List<ProducerWaitingOrders> producerWaitingOrderslist;

    //ADAPTER
    public ProducerOrderToBePreparedViewAdapter(Context context, List<ProducerWaitingOrders> producerWaitingOrderslist) {
        this.producerWaitingOrderslist = producerWaitingOrderslist;
        this.mcontext = context;
    }

    //VIEW HOLDER
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.producer_ordertobeprepared_view, parent, false);
        return new ProducerOrderToBePreparedViewAdapter.ViewHolder(view);
    }

    //DISPLAY DETAILS
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProducerWaitingOrders producerWaitingOrders = producerWaitingOrderslist.get(position);
        holder.productname.setText(producerWaitingOrders.getProductName());
        holder.price.setText("Price: ₱ " + producerWaitingOrders.getProductPrice());
        holder.quantity.setText("× " + producerWaitingOrders.getProductQuantity());
        holder.totalprice.setText("Total: ₱ " + producerWaitingOrders.getTotalPrice());
    }

    //ARRAY LIST SIZE
    @Override
    public int getItemCount() {
        return producerWaitingOrderslist.size();
    }

    //CONNECT XML
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
