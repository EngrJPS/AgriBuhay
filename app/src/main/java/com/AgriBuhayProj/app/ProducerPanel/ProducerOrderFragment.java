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
public class ProducerOrderFragment extends Fragment {

    TextView OrdertobePrepare, Preparedorders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Orders");
        View v = inflater.inflate(R.layout.fragment_producer_order, null);
        OrdertobePrepare=(TextView)v.findViewById(R.id.ordertobe);
        Preparedorders=(TextView)v.findViewById(R.id.prepareorder);

        OrdertobePrepare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), ProducerOrderTobePrepared.class);
                startActivity(i);
            }
        });

        Preparedorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ProducerPreparedOrder.class);
                startActivity(intent);
            }
        });


        return v;
    }
}