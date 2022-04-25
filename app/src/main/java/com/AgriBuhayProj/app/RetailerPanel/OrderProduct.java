package com.AgriBuhayProj.app.RetailerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AgriBuhayProj.app.Models.Producer;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.AgriBuhayProj.app.ProducerPanel.UpdateProductModel;
import com.AgriBuhayProj.app.Models.Retailer;
import com.AgriBuhayProj.app.ProductPanelBottomNavigation_Retailer;

import com.AgriBuhayProj.app.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OrderProduct extends AppCompatActivity {


    String RandomId, ProducerID, ProducerMobileNum;
    ImageView imageView;
    ElegantNumberButton additem;
    TextView Productname, ProducerName, ProducerLocation, ProductQuantity, ProductPrice, ProductDescription;
    DatabaseReference databaseReference, dataaa, producerdata, reference, data, dataref;
    String State, City, Sub, productname;
    int productprice;
    String retID;
    FirebaseDatabase firebaseDatabase;
    Button textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer_order_product);

        Productname = (TextView) findViewById(R.id.product_namee);
        ProducerName = (TextView) findViewById(R.id.producer_name);
        ProducerLocation = (TextView) findViewById(R.id.producer_location);
        ProductQuantity = (TextView) findViewById(R.id.product_quantity);
        ProductPrice = (TextView) findViewById(R.id.product_price);
        ProductDescription = (TextView) findViewById(R.id.product_description);
        imageView = (ImageView) findViewById(R.id.image);
        additem = (ElegantNumberButton) findViewById(R.id.number_btn);
        textMessage = (Button) findViewById(R.id.sendText);

        firebaseDatabase = FirebaseDatabase.getInstance();

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Retailer").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Retailer ret = dataSnapshot.getValue(Retailer.class);
                State = ret.getProvince();
                City = ret.getCity();
                Sub = ret.getBaranggay();

                RandomId = getIntent().getStringExtra("ProductMenu");
                //TODO ChefID getStringExtra
                ProducerID = getIntent().getStringExtra("ProducerId");
                //TODO Added getIntent Here
                ProducerMobileNum = getIntent().getStringExtra("ProducerPhoneNum");


                databaseReference = FirebaseDatabase.getInstance().getReference("ProductSupplyDetails").child(State).child(City).child(Sub).child(ProducerID).child(RandomId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UpdateProductModel updateProductModel = dataSnapshot.getValue(UpdateProductModel.class);
                        Productname.setText(updateProductModel.getProducts());
                        String qua = "<b>" + "Quantity: " + "</b>" + updateProductModel.getQuantity();
                        ProductQuantity.setText(Html.fromHtml(qua));
                        String ss = "<b>" + "Description: " + "</b>" + updateProductModel.getDescription();
                        ProductDescription.setText(Html.fromHtml(ss));
                        String pri = "<b>" + "Price/kg: ₱ " + "</b>" + updateProductModel.getPrice();
                        ProductPrice.setText(Html.fromHtml(pri));
                        Glide.with(OrderProduct.this).load(updateProductModel.getImageURL()).into(imageView);

                        producerdata = FirebaseDatabase.getInstance().getReference("Producer").child(ProducerID);
                        producerdata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Producer producer = dataSnapshot.getValue(Producer.class);

                                String name = "<b>" + "Producer Name: " + "</b>" + producer.getFullName();
                                ProducerName.setText(Html.fromHtml(name));
                                String loc = "<b>" + "Location: " + "</b>" + producer.getBaranggay();
                                ProducerLocation.setText(Html.fromHtml(loc));
                                retID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(retID).child(RandomId);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Cart cart = dataSnapshot.getValue(Cart.class);
                                        if (dataSnapshot.exists()) {
                                            additem.setNumber(cart.getProductQuantity());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                additem.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dataref = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Cart cart1=null;
                                if (dataSnapshot.exists()) {
                                    int totalcount=0;
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        totalcount++;
                                    }
                                    int i=0;
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    i++;
                                        if(i==totalcount){
                                            cart1= snapshot.getValue(Cart.class);
                                        }
                                    }

                                    if (ProducerID.equals(cart1.getProducerId())) {
                                        data = firebaseDatabase.getReference("ProductSupplyDetails").child(State).child(City).child(Sub).child(ProducerID).child(RandomId);
                                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                UpdateProductModel update = dataSnapshot.getValue(UpdateProductModel.class);
                                                productname = update.getProducts();
                                                productprice = Integer.parseInt(update.getPrice());

                                                int num = Integer.parseInt(additem.getNumber());
                                                int totalprice = num * productprice;
                                                if (num != 0) {
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("ProductName", productname);
                                                    hashMap.put("ProductID", RandomId);
                                                    hashMap.put("ProductQuantity", String.valueOf(num));
                                                    hashMap.put("Price", String.valueOf(productprice));
                                                    hashMap.put("TotalPrice", String.valueOf(totalprice));
                                                    hashMap.put("ProducerId", ProducerID);
                                                    hashMap.put("ProducerPhone", ProducerMobileNum);
                                                    //TODO Add Mobile number here
                                                    retID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    reference = firebaseDatabase.getReference("Cart").child("CartItems").child(retID).child(RandomId);
                                                    reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(OrderProduct.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                } else {
                                                    firebaseDatabase.getInstance().getReference("Cart").child(retID).child(RandomId).removeValue();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderProduct.this);
                                        builder.setMessage("You can't add product items of multiple producers at a time. Try to add items of same producers");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(OrderProduct.this, ProductPanelBottomNavigation_Retailer.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                } else {
                                data = firebaseDatabase.getReference("ProductSupplyDetails").child(State).child(City).child(Sub).child(ProducerID).child(RandomId);
                                data.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        UpdateProductModel update = dataSnapshot.getValue(UpdateProductModel.class);
                                        productname = update.getProducts();
                                        productprice = Integer.parseInt(update.getPrice());
                                        int num = Integer.parseInt(additem.getNumber());
                                        int totalprice = num * productprice;
                                        if (num != 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap.put("ProductName", productname);
                                            hashMap.put("ProductID", RandomId);
                                            hashMap.put("ProductQuantity", String.valueOf(num));
                                            hashMap.put("Price", String.valueOf(productprice));
                                            hashMap.put("TotalPrice", String.valueOf(totalprice));
                                            hashMap.put("ProducerId", ProducerID);
                                            hashMap.put("Mobile", ProducerMobileNum);
                                            //TODO Add Mobile number here
                                            retID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            reference = firebaseDatabase.getReference("Cart").child("CartItems").child(retID).child(RandomId);
                                            reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(OrderProduct.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        } else {
                                            firebaseDatabase.getReference("Cart").child(retID).child(RandomId).removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        textMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) ==
                        PackageManager.PERMISSION_GRANTED){
                    sendSMS();
                }else{
                    ActivityCompat.requestPermissions(OrderProduct.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }
            }
        });
    }

    private void sendSMS() {
        try{
            SmsManager smgr = SmsManager.getDefault();
            smgr.sendTextMessage(ProducerMobileNum, null, "A customer has sent you an order", null,null);
            Toast.makeText(getApplicationContext(), "The message sent successfully", Toast.LENGTH_SHORT).show();
        }catch(Exception ex){
            Toast.makeText(getApplicationContext(), "SMS Failed to send. Please try again", Toast.LENGTH_SHORT).show();
        }
    }
}


