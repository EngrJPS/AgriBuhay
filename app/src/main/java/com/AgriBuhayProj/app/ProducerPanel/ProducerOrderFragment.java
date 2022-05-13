package com.AgriBuhayProj.app.ProducerPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.AgriBuhayProj.app.R;

//ORDER FRAGMENT
public class ProducerOrderFragment extends Fragment {
    //VARIABLES
    TextView OrdertobePrepare, Preparedorders,DeliveredOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Orders");
        View v = inflater.inflate(R.layout.fragment_producer_order, null);

        //CONNECT XML
        OrdertobePrepare= v.findViewById(R.id.ordertobe);
        Preparedorders= v.findViewById(R.id.prepareorder);
        DeliveredOrders= v.findViewById(R.id.delivered);

        //BUTTON EVENTS
        OrdertobePrepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to pending orders
                Intent i=new Intent(getContext(), ProducerOrderTobePrepared.class);
                startActivity(i);
            }
        });

        Preparedorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to prepared orders
                Intent intent=new Intent(getContext(), ProducerPreparedOrder.class);
                startActivity(intent);
            }
        });

        DeliveredOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to delivered orders
                Intent intent=new Intent(getContext(), ProducerDeliveredOrdersList.class);
                startActivity(intent);
            }
        });

        return v;
    }
}