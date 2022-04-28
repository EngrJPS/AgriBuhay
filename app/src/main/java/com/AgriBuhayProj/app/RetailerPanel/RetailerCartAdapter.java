package com.AgriBuhayProj.app.RetailerPanel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.AgriBuhayProj.app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class RetailerCartAdapter extends RecyclerView.Adapter<RetailerCartAdapter.ViewHolder> {

    private Context mcontext;
    private List<Cart> cartModellist;
    static int total = 0;

    public RetailerCartAdapter(Context context, List<Cart> cartModellist) {
        this.cartModellist = cartModellist;
        this.mcontext = context;
        total = 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.retailer_cart_placeorder, parent, false);
        return new RetailerCartAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Cart cart = cartModellist.get(position);
        holder.productname.setText(cart.getProductName());
        holder.PricePhp.setText("Price: ₱ " + cart.getPrice());
        holder.Qty.setText("× " + cart.getProductQuantity());
        holder.Totalphp.setText("Total: ₱ " + cart.getTotalPrice());
        total += Integer.parseInt(cart.getTotalPrice());
        holder.elegantNumberButton.setNumber(cart.getProductQuantity());
        final int productprice = Integer.parseInt(cart.getPrice());

        holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                int num = newValue;
                int totalprice = num * productprice;
                if (num != 0) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("ProductID", cart.getProductID());
                    hashMap.put("ProductName", cart.getProductName());
                    hashMap.put("ProductQuantity", String.valueOf(num));
                    hashMap.put("Price", String.valueOf(productprice));
                    hashMap.put("TotalPrice", String.valueOf(totalprice));
                    hashMap.put("ProducerId",cart.getProducerId());
                    hashMap.put("Mobile",cart.getMobile());

                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getProductID()).setValue(hashMap);
                } else {
                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(cart.getProductID()).removeValue();
                }
            }
        });
        RetailerCartFragment.grandt.setText("Total: ₱ " + total);
        FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal").setValue(String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return cartModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productname, PricePhp, Qty, Totalphp;
        ElegantNumberButton elegantNumberButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.Productname);
            PricePhp = itemView.findViewById(R.id.pricephp);
            Qty = itemView.findViewById(R.id.qty);
            Totalphp = itemView.findViewById(R.id.totalphp);
            elegantNumberButton = itemView.findViewById(R.id.elegantbtn);
        }
    }
}
