package com.AgriBuhayProj.app.ProducerPanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AgriBuhayProj.app.R;
import java.util.List;

//PREPARED ORDER ADAPTER
public class ProducerPreparedOrderAdapter extends RecyclerView.Adapter<ProducerPreparedOrderAdapter.ViewHolder> {
    //VARIABLES
    private Context context;
    private List<ProducerFinalOrders1> producerFinalOrders1List;

    //ADAPTER
    public ProducerPreparedOrderAdapter(Context context, List<ProducerFinalOrders1> producerFinalOrders1List) {
        this.producerFinalOrders1List = producerFinalOrders1List;
        this.context = context;
    }

    //CREATE VIEW HOLDER
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.producer_preparedorder, parent, false);
        return new ProducerPreparedOrderAdapter.ViewHolder(view);
    }

    //DISPLAY ORDER
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //display order
        final ProducerFinalOrders1 producerFinalOrders1 = producerFinalOrders1List.get(position);
        holder.Address.setText(producerFinalOrders1.getAddress());
        holder.grandtotalprice.setText("Total: ₱ " + producerFinalOrders1.getGrandTotalPrice());
        final String random = producerFinalOrders1.getRandomUID();

        //view order
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProducerPreparedOrderView.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
                ((ProducerPreparedOrder) context).finish();
            }
        });

    }

    //ARRAY LIST SIZE
    @Override
    public int getItemCount() {
        return producerFinalOrders1List.size();
    }

    //CONNECT XML
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice;
        Button Vieworder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Address = itemView.findViewById(R.id.customer_address);
            grandtotalprice = itemView.findViewById(R.id.customer_totalprice);
            Vieworder = itemView.findViewById(R.id.View);
        }
    }
}
