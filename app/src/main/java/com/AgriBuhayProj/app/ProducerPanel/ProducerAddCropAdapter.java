package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.AgriBuhayProj.app.Models.Crops;
import com.AgriBuhayProj.app.R;

import java.util.List;

public class ProducerAddCropAdapter extends RecyclerView.Adapter<ProducerAddCropAdapter.ViewHolder> {

    private Context context;
    private List<Crops>cropModel;

    public ProducerAddCropAdapter(Context context, List<Crops> cropModel) {
        this.context = context;
        this.cropModel = cropModel;
    }

    @NonNull
    @Override
    public ProducerAddCropAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.producer_add_crop_adapter,parent,false);
        return new ProducerAddCropAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProducerAddCropAdapter.ViewHolder holder, int position) {
        final Crops updateCrop = cropModel.get(position);
        holder.cropName.setText(updateCrop.getCrop());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProducerAddCropUpdate.class);
                intent.putExtra("cropName",updateCrop.getCrop());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cropModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cropName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cropName = itemView.findViewById(R.id.txtCrop);
        }
    }
}