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

//TO BE PREPARED ADAPTER
public class ProducerOrderTobePreparedAdapter extends RecyclerView.Adapter<ProducerOrderTobePreparedAdapter.ViewHolder> {
    //VARIABLE
    private Context context;
    private List<ProducerWaitingOrders1> producerWaitingOrders1List;

    //ADAPTER
    public ProducerOrderTobePreparedAdapter(Context context, List<ProducerWaitingOrders1> producerWaitingOrders1List) {
        this.producerWaitingOrders1List = producerWaitingOrders1List;
        this.context = context;
    }

    //VIEW HOLDER
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.producer_ordertobeprepared, parent, false);
        return new ProducerOrderTobePreparedAdapter.ViewHolder(view);
    }

    //DISPLAY VALUES
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProducerWaitingOrders1 producerWaitingOrders1 = producerWaitingOrders1List.get(position);
        holder.Address.setText(producerWaitingOrders1.getAddress());
        holder.grandtotalprice.setText("Total Price: ₱ " + producerWaitingOrders1.getGrandTotalPrice());
        final String random = producerWaitingOrders1.getRandomUID();

        //adapter clicked
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to pending order details
                Intent intent = new Intent(context, ProducerOrdertobePrepareView.class);
                //attach tracking number
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
                ((ProducerOrderTobePrepared) context).finish();
            }
        });
    }

    //ARRAY LIST SIZE
    @Override
    public int getItemCount() {
        return producerWaitingOrders1List.size();
    }

    //CONNECT XML
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice;
        Button Vieworder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Address = itemView.findViewById(R.id.ret_address);
            grandtotalprice = itemView.findViewById(R.id.Grandtotalprice);
            Vieworder = itemView.findViewById(R.id.View_order);
        }
    }
}
