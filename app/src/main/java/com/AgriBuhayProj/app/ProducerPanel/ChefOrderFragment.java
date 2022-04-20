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
public class ChefOrderFragment extends Fragment {

    TextView OrdertobePrepare, Preparedorders, deliveredOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Orders");
        View v = inflater.inflate(R.layout.fragment_chef_order, null);
        OrdertobePrepare = v.findViewById(R.id.ordertobe);
        Preparedorders = v.findViewById(R.id.prepareorder);
        deliveredOrders = v.findViewById(R.id.delivered);

        OrdertobePrepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(),ChefOrderTobePrepared.class);
                startActivity(i);
            }
        });

        Preparedorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),ChefPreparedOrder.class);
                startActivity(intent);
            }
        });

        deliveredOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),ProducerDeliveredOrdersList.class);
                startActivity(intent);
            }
        });

        return v;
    }
}