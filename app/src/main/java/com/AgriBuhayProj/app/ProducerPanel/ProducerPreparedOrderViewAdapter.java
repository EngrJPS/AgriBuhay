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

//PREPARED ORDER ADAPTER
public class ProducerPreparedOrderViewAdapter extends RecyclerView.Adapter<ProducerPreparedOrderViewAdapter.ViewHolder> {
    //VARIABLES
    private Context mcontext;
    private List<ProducerFinalOrders> producerFinalOrderslist;

    //ADAPTER
    public ProducerPreparedOrderViewAdapter(Context context, List<ProducerFinalOrders> producerFinalOrderslist) {
        this.producerFinalOrderslist = producerFinalOrderslist;
        this.mcontext = context;
    }

    //VIEW HOLDER
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.producer_preparedorderview, parent, false);
        return new ProducerPreparedOrderViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //display product values
        final ProducerFinalOrders producerFinalOrders = producerFinalOrderslist.get(position);
        holder.productname.setText(producerFinalOrders.getProductName());
        holder.price.setText("Price: ₱ " + producerFinalOrders.getProductPrice());
        holder.quantity.setText("× " + producerFinalOrders.getProductQuantity());
        holder.totalprice.setText("Total: ₱ " + producerFinalOrders.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return producerFinalOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.Cproductname);
            price = itemView.findViewById(R.id.Cproductprice);
            totalprice = itemView.findViewById(R.id.Ctotalprice);
            quantity = itemView.findViewById(R.id.Cproductqty);
        }
    }
}
