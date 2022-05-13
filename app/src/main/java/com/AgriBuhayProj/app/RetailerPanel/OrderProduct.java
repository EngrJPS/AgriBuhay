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
    //VARIABLES
    ImageView imageView;
    ElegantNumberButton additem;
    TextView Productname, ProducerName, ProducerLocation, ProductQuantity, ProductPrice, ProductDescription;
    Button textMessage;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, dataaa, producerdata, reference, data, dataref;

    int productprice;

    String State, City, Sub, productname;
    String RandomId, ProducerID, ProducerMobileNum;
    String retID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer_order_product);
        //CONNECT XML
        Productname = findViewById(R.id.product_namee);
        ProducerName = findViewById(R.id.producer_name);
        ProducerLocation = findViewById(R.id.producer_location);
        ProductQuantity = findViewById(R.id.product_quantity);
        ProductPrice = findViewById(R.id.product_price);
        ProductDescription = findViewById(R.id.product_description);
        imageView = findViewById(R.id.image);
        additem = findViewById(R.id.number_btn);
        textMessage = findViewById(R.id.sendText);

        //FIREBASE INSTANCE
        firebaseDatabase = FirebaseDatabase.getInstance();

        //GET RETAILER ID
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //RETAILER DB REFERENCE
        dataaa = FirebaseDatabase.getInstance().getReference("Retailer").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get values
                Retailer ret = dataSnapshot.getValue(Retailer.class);
                State = ret.getProvince();
                City = ret.getCity();
                Sub = ret.getBaranggay();

                //product id
                RandomId = getIntent().getStringExtra("ProductMenu");
                //producer id
                ProducerID = getIntent().getStringExtra("ProducerId");
                //produce mobile number
                ProducerMobileNum = getIntent().getStringExtra("ProducerPhoneNum");

                //product db reference
                databaseReference = FirebaseDatabase.getInstance().getReference("ProductSupplyDetails").child(State).child(City).child(Sub).child(ProducerID).child(RandomId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //get db values
                        UpdateProductModel updateProductModel = dataSnapshot.getValue(UpdateProductModel.class);
                        //display product description
                        Productname.setText(updateProductModel.getProducts());
                        String qua = "<b>" + "Quantity: " + "</b>" + updateProductModel.getQuantity();
                        ProductQuantity.setText(Html.fromHtml(qua));
                        String ss = "<b>" + "Description: " + "</b>" + updateProductModel.getDescription();
                        ProductDescription.setText(Html.fromHtml(ss));
                        String pri = "<b>" + "Price/kg: ₱ " + "</b>" + updateProductModel.getPrice();
                        ProductPrice.setText(Html.fromHtml(pri));
                        //product image
                        Glide.with(OrderProduct.this).load(updateProductModel.getImageURL()).into(imageView);

                        //producer db reference
                        producerdata = FirebaseDatabase.getInstance().getReference("Producer").child(ProducerID);
                        producerdata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //display producer info
                                Producer producer = dataSnapshot.getValue(Producer.class);
                                String name = "<b>" + "Producer Name: " + "</b>" + producer.getFullName();
                                ProducerName.setText(Html.fromHtml(name));
                                String loc = "<b>" + "Location: " + "</b>" + producer.getBaranggay();
                                ProducerLocation.setText(Html.fromHtml(loc));

                                //retailer id
                                retID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                //retailer cart db reference
                                databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(retID).child(RandomId);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //display product quantity
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

                //add item
                additem.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //cart items db reference
                        dataref = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Cart cart1=null;
                                //check existing products
                                if (dataSnapshot.exists()) {
                                    //product ordered quantity
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

                                    //check current orders
                                    if (ProducerID.equals(cart1.getProducerId())) {
                                        //producer product db reference
                                        data = firebaseDatabase.getReference("ProductSupplyDetails").child(State).child(City).child(Sub).child(ProducerID).child(RandomId);
                                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //get value
                                                UpdateProductModel update = dataSnapshot.getValue(UpdateProductModel.class);
                                                productname = update.getProducts();
                                                productprice = Integer.parseInt(update.getPrice());

                                                //order quantity
                                                int num = Integer.parseInt(additem.getNumber());
                                                //total price
                                                int totalprice = num * productprice;
                                                //check order quantity
                                                if (num != 0) {
                                                    //get values
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("ProductName", productname);
                                                    hashMap.put("ProductID", RandomId);
                                                    hashMap.put("ProductQuantity", String.valueOf(num));
                                                    hashMap.put("Price", String.valueOf(productprice));
                                                    hashMap.put("TotalPrice", String.valueOf(totalprice));
                                                    hashMap.put("ProducerId", ProducerID);
                                                    hashMap.put("ProducerPhone", ProducerMobileNum);

                                                    //retailer id
                                                    retID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    //add values to cart db
                                                    reference = firebaseDatabase.getReference("Cart").child("CartItems").child(retID).child(RandomId);
                                                    reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(OrderProduct.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                } else {
                                                    //remove product from cart db
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
                                        //cant add other producer products
                                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderProduct.this);
                                        builder.setMessage("You can't add product items of multiple producers at a time. Try to add items of same producers");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                //direct to home
                                                Intent intent = new Intent(OrderProduct.this, ProductPanelBottomNavigation_Retailer.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                } else {
                                    //producer product db reference
                                data = firebaseDatabase.getReference("ProductSupplyDetails").child(State).child(City).child(Sub).child(ProducerID).child(RandomId);
                                data.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        //get values
                                        UpdateProductModel update = dataSnapshot.getValue(UpdateProductModel.class);
                                        productname = update.getProducts();
                                        productprice = Integer.parseInt(update.getPrice());
                                        //get product quantity
                                        int num = Integer.parseInt(additem.getNumber());
                                        int totalprice = num * productprice;
                                        if (num != 0) {
                                            //values
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap.put("ProductName", productname);
                                            hashMap.put("ProductID", RandomId);
                                            hashMap.put("ProductQuantity", String.valueOf(num));
                                            hashMap.put("Price", String.valueOf(productprice));
                                            hashMap.put("TotalPrice", String.valueOf(totalprice));
                                            hashMap.put("ProducerId", ProducerID);
                                            hashMap.put("Mobile", ProducerMobileNum);
                                            //get retailer id
                                            retID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            //add to cart db
                                            reference = firebaseDatabase.getReference("Cart").child("CartItems").child(retID).child(RandomId);
                                            reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(OrderProduct.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        } else {
                                            //no existing product

                                            //remove product from cart db
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

        //text producer
        textMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check sms permission
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) ==
                        PackageManager.PERMISSION_GRANTED){
                    //send sms
                    sendSMS();
                }else{
                    ActivityCompat.requestPermissions(OrderProduct.this, new String[]{Manifest.permission.SEND_SMS}, 100);
                }
            }
        });
    }

    //SEND SMS
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


