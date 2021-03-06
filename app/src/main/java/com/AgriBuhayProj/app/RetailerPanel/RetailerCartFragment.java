package com.AgriBuhayProj.app.RetailerPanel;

import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.AgriBuhayProj.app.Models.Retailer;
import com.AgriBuhayProj.app.R;
import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.AgriBuhayProj.app.SendNotification.APIService;
import com.AgriBuhayProj.app.SendNotification.Client;
import com.AgriBuhayProj.app.SendNotification.Data;
import com.AgriBuhayProj.app.SendNotification.MyResponse;
import com.AgriBuhayProj.app.SendNotification.NotificationSender;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//CART FRAGMENT
public class RetailerCartFragment extends Fragment {
    //VARIABLES
    RecyclerView recyclecart;
    private LinearLayout TotalBtns;
    public static TextView grandt;
    Button remove, placeorder;
    String address, Addnote;
    String ProductId, RandomUId, ProducerId;
    private ProgressDialog progressDialog;

    private List<Cart> cartModelList;

    private RetailerCartAdapter adapter;

    DatabaseReference databaseReference, data, reference, ref, getRef, dataa;

    private APIService apiService;

    String ProducerMobileNum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Cart");
        View v = inflater.inflate(R.layout.fragment_retailercart, null);
        recyclecart = v.findViewById(R.id.recyclecart);
        grandt = v.findViewById(R.id.GT);
        remove = v.findViewById(R.id.RM);
        placeorder = v.findViewById(R.id.PO);
        TotalBtns = v.findViewById(R.id.TotalBtns);

        //TODO phonenum getStringExtra
        recyclecart.setHasFixedSize(true);
        recyclecart.setLayoutManager(new LinearLayoutManager(getContext()));

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        cartModelList = new ArrayList<>();

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        //show products
        retailercart();

        return v;
    }

    //LIST CART ITEMS
    private void retailercart() {
        //get retailer id
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //cart items db ref
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartModelList.clear();
                //add cart items to list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cart cart = snapshot.getValue(Cart.class);
                    cartModelList.add(cart);
                }
                //check array list size
                if (cartModelList.size() == 0) {
                    TotalBtns.setVisibility(View.INVISIBLE);
                } else {
                    //show total price
                    TotalBtns.setVisibility(View.VISIBLE);

                    //remove product
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Are you sure you want to remove the orders?");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //remove product from cart db
                                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                    FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                }
                            });
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    });


                    String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    //retailer db reference
                    data = FirebaseDatabase.getInstance().getReference("Retailer").child(UserID);
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //get value
                            final Retailer retailer = dataSnapshot.getValue(Retailer.class);

                            //place order
                            placeorder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //change order state
                                    FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isOrdered").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            //empty string
                                            String ss = "";

                                            //check user
                                            if (dataSnapshot.exists()) {
                                                ss = dataSnapshot.getValue(String.class);
                                            }

                                            //check user order status
                                            if (ss.trim().equalsIgnoreCase("false") || ss.trim().equalsIgnoreCase("")) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setTitle("Enter Address");

                                                //show dialog
                                                LayoutInflater inflater = getActivity().getLayoutInflater();
                                                View view = inflater.inflate(R.layout.retailer_enter_address, null);

                                                //connect xml
                                                final EditText localaddress = view.findViewById(R.id.LA);
                                                final EditText addnote = view.findViewById(R.id.addnote);
                                                RadioGroup group = view.findViewById(R.id.grp);
                                                final RadioButton home = view.findViewById(R.id.HA);
                                                final RadioButton other = view.findViewById(R.id.OA);

                                                builder.setView(view);

                                                //radio button checked
                                                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                        //check selection
                                                        if (home.isChecked()) {
                                                            //display local address
                                                            localaddress.setText(retailer.getLocalAddress() + ", " + retailer.getBaranggay());
                                                        } else if (other.isChecked()) {
                                                            //get string value
                                                            localaddress.getText().clear();
                                                            Toast.makeText(getContext(), "Address Saved", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                //order
                                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        progressDialog.setMessage("Please wait...");
                                                        progressDialog.show();

                                                        //cart items db reference
                                                        reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                //tracking number
                                                                RandomUId = UUID.randomUUID().toString();
                                                                //set values
                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                    final Cart cart1 = dataSnapshot1.getValue(Cart.class);

                                                                    ProductId = cart1.getProductID();

                                                                    address = localaddress.getText().toString().trim();
                                                                    Addnote = addnote.getText().toString().trim();

                                                                    final HashMap<String, String> hashMap = new HashMap<>();
                                                                    hashMap.put("ProducerId", cart1.getProducerId());
                                                                    hashMap.put("ProductID", cart1.getProductID());
                                                                    hashMap.put("ProductName", cart1.getProductName());
                                                                    hashMap.put("ProductQuantity", cart1.getProductQuantity());
                                                                    hashMap.put("Price", cart1.getPrice());
                                                                    hashMap.put("TotalPrice", cart1.getTotalPrice());

                                                                    //add order products to RetailerPendingOrders
                                                                    FirebaseDatabase.getInstance().getReference("RetailerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("Products").child(ProductId).setValue(hashMap);

                                                                }

                                                                //order total price reference
                                                                ref = FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal");
                                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        //set values
                                                                        String grandtotal = dataSnapshot.getValue(String.class);
                                                                        HashMap<String, String> hashMap1 = new HashMap<>();
                                                                        hashMap1.put("Address", address);
                                                                        hashMap1.put("GrandTotalPrice", String.valueOf(grandtotal));
                                                                        hashMap1.put("MobileNumber", retailer.getMobile());
                                                                        hashMap1.put("Name", retailer.getFirstName() + " " + retailer.getLastName());
                                                                        hashMap1.put("Note", Addnote);

                                                                        //add order other info to RetailerPendingOrders
                                                                        FirebaseDatabase.getInstance().getReference("RetailerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                //remove order items from cart
                                                                                FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        //remove order total price from cart
                                                                                        FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                //RetailerPendingOrders product reference
                                                                                                getRef = FirebaseDatabase.getInstance().getReference("RetailerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("Products");
                                                                                                getRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                        //get values
                                                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                                                                                            final RetailerPendingOrders retailerPendingOrders = dataSnapshot2.getValue(RetailerPendingOrders.class);
                                                                                                            String d = retailerPendingOrders.getProductID();
                                                                                                            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                                                            ProducerId = retailerPendingOrders.getProducerId();

                                                                                                            //set values
                                                                                                            final HashMap<String, String> hashMap2 = new HashMap<>();
                                                                                                            hashMap2.put("ProducerId", ProducerId);
                                                                                                            hashMap2.put("ProductId", retailerPendingOrders.getProductID());
                                                                                                            hashMap2.put("ProductName", retailerPendingOrders.getProductName());
                                                                                                            hashMap2.put("ProductQuantity", retailerPendingOrders.getProductQuantity());
                                                                                                            hashMap2.put("Price", retailerPendingOrders.getPrice());
                                                                                                            hashMap2.put("RandomUID", RandomUId);
                                                                                                            hashMap2.put("TotalPrice", retailerPendingOrders.getTotalPrice());
                                                                                                            hashMap2.put("UserId", userid);

                                                                                                            //add to ProducerPendingOrders products
                                                                                                            FirebaseDatabase.getInstance().getReference("ProducerPendingOrders").child(ProducerId).child(RandomUId).child("Products").child(d).setValue(hashMap2);
                                                                                                        }
                                                                                                        dataa = FirebaseDatabase.getInstance().getReference("RetailerPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("OtherInformation");
                                                                                                        dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                //get values
                                                                                                                RetailerPendingOrders1 retailerPendingOrders1 = dataSnapshot.getValue(RetailerPendingOrders1.class);

                                                                                                                //set values
                                                                                                                HashMap<String, String> hashMap3 = new HashMap<>();
                                                                                                                hashMap3.put("Address", retailerPendingOrders1.getAddress());
                                                                                                                hashMap3.put("GrandTotalPrice", retailerPendingOrders1.getGrandTotalPrice());
                                                                                                                hashMap3.put("MobileNumber", retailerPendingOrders1.getMobileNumber());
                                                                                                                hashMap3.put("Name", retailerPendingOrders1.getName());
                                                                                                                hashMap3.put("Note", retailerPendingOrders1.getNote());
                                                                                                                hashMap3.put("RandomUID", RandomUId);

                                                                                                                //add to ProducerPendingOrders other info
                                                                                                                FirebaseDatabase.getInstance().getReference("ProducerPendingOrders").child(ProducerId).child(RandomUId).child("OtherInformation").setValue(hashMap3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void aVoid) {
                                                                                                                        //change retailer order status
                                                                                                                        FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isOrdered").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(Void aVoid) {
                                                                                                                                //producer token reference
                                                                                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(ProducerId).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                                                    @Override
                                                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                                        //get token
                                                                                                                                        String usertoken = dataSnapshot.getValue(String.class);
                                                                                                                                        //notify producer
                                                                                                                                        sendNotifications(usertoken, "New Order", "You have a new Order", "Order");
                                                                                                                                        progressDialog.dismiss();
                                                                                                                                        ReusableCodeForAll.ShowAlert(getContext(), "", "Your order has been shifted to pending, please wait until the producer accepts your order.");
                                                                                                                                    }

                                                                                                                                    @Override
                                                                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                                                    }
                                                                                                                                });

                                                                                                                            }
                                                                                                                        });


                                                                                                                    }


                                                                                                                });
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                            }
                                                                                                        });
//                                                                                                            }
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });
//                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });

                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                AlertDialog aler = builder.create();
                                                aler.show();

                                            } else {
                                                //unfinished order
                                                ReusableCodeForAll.ShowAlert(getContext(), "Error", "It seems you have already placed the order, So you cannot place another order until the delivery of first order");
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
                }

                //set adapter
                adapter = new RetailerCartAdapter(getContext(), cartModelList);
                recyclecart.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //NOTIFY USER
    private void sendNotifications(String usertoken, String title, String message, String order) {
        Data data = new Data(title, message, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
