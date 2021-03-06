package com.AgriBuhayProj.app.RetailerPanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import com.AgriBuhayProj.app.ProducerPanel.UpdateProductModel;
import com.AgriBuhayProj.app.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

//HOME FRAGMENT ADAPTER
public class RetailerHomeAdapter extends RecyclerView.Adapter<RetailerHomeAdapter.ViewHolder> {
    //VARIABLES
    private Context mcontext;
    private List<UpdateProductModel> updateProductModellist;
    DatabaseReference databaseReference;

    public RetailerHomeAdapter(Context context, List<UpdateProductModel> updateProductModellist)
    {
        this.updateProductModellist = updateProductModellist;
        this.mcontext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.retailer_menuproduct,parent,false);
        return new RetailerHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UpdateProductModel updateProductModel = updateProductModellist.get(position);
        //display product image
        Glide.with(mcontext).load(updateProductModel.getImageURL()).into(holder.imageView);
        //display product
        holder.Productname.setText(updateProductModel.getProducts());
        String randomUID = updateProductModel.getRandomUID();
        String producerID = updateProductModel.getProducerID();
        String mobile = updateProductModel.getMobile();
        holder.price.setText("Price: ₱ " + updateProductModel.getPrice());

        //item clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //direct to order product
                Intent intent=new Intent(mcontext, OrderProduct.class);
                //attach string
                //product id
                intent.putExtra("ProductMenu", randomUID);
                //producer id
                intent.putExtra("ProducerId", producerID);
                //producer mobile number
                intent.putExtra("ProducerPhoneNum", mobile);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return updateProductModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView Productname,price;
        ElegantNumberButton additem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.menu_image);
            Productname =itemView.findViewById(R.id.productname);
            price=itemView.findViewById(R.id.productprice);
            additem=itemView.findViewById(R.id.number_btn);


        }
    }
}
