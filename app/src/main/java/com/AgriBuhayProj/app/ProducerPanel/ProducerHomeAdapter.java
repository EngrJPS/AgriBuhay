package com.AgriBuhayProj.app.ProducerPanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AgriBuhayProj.app.R;

import java.util.List;

public class ProducerHomeAdapter extends RecyclerView.Adapter<ProducerHomeAdapter.ViewHolder> {

   private Context mcont;
   private List<UpdateProductModel> updateProductModellist;

   public ProducerHomeAdapter(Context context, List<UpdateProductModel> updateProductModellist)
   {
       this.updateProductModellist = updateProductModellist;
       this.mcont=context;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(mcont).inflate(R.layout.producer_post_product_adapter,parent,false);
       return new ProducerHomeAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final UpdateProductModel updateProductModel = updateProductModellist.get(position);
       holder.products.setText(updateProductModel.getProducts());
       updateProductModel.getRandomUID();
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(mcont, UpdateDeleteProduct.class);
               intent.putExtra("updatedeleteproduct", updateProductModel.getRandomUID());
               mcont.startActivity(intent);

           }
       });
    }


    @Override
    public int getItemCount() {
        return updateProductModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       TextView products;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            products =itemView.findViewById(R.id.product_name);

        }
    }
}
