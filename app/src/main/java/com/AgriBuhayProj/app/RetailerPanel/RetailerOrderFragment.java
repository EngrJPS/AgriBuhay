package com.AgriBuhayProj.app.RetailerPanel;

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
public class RetailerOrderFragment extends Fragment {
    //VARIABLE
    TextView Pendingorder, Payableorder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Orders");
        View v = inflater.inflate(R.layout.fragment_retailerorder, null);

        //CONNECT XML
        Pendingorder = v.findViewById(R.id.pendingorder);
        Payableorder = v.findViewById(R.id.payableorder);

        //pending orders
        Pendingorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to pending orders
                Intent intent = new Intent(getContext(), PendingOrders.class);
                startActivity(intent);
            }
        });

        //payable orders
        Payableorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to payable orders
                Intent i = new Intent(getContext(), PayableOrders.class);
                startActivity(i);
            }
        });
        return v;
    }


}