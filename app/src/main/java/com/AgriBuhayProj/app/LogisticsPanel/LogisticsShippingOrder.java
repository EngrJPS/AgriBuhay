package com.AgriBuhayProj.app.LogisticsPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AgriBuhayProj.app.Models.History;
import com.AgriBuhayProj.app.Models.Logistics;
import com.AgriBuhayProj.app.Models.Producer;
import com.AgriBuhayProj.app.ProductPanelBottomNavigation_Logistics;

import com.AgriBuhayProj.app.R;
import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.AgriBuhayProj.app.SendNotification.APIService;
import com.AgriBuhayProj.app.SendNotification.Client;
import com.AgriBuhayProj.app.SendNotification.Data;
import com.AgriBuhayProj.app.SendNotification.MyResponse;
import com.AgriBuhayProj.app.SendNotification.NotificationSender;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//PROOF OF DELIVERY
public class LogisticsShippingOrder extends AppCompatActivity {
    //VARIABLES
    TextView address, ProducerName,prodmobile, total, MobileNumber, Retname;
    Button shipped;
    ImageButton imageProof;
    ProgressDialog progressDialog;

    private APIService apiService;

    String randomuid;
    String userid,producerID,logisticsID;
    String currentDate,logisticsName,logisticsMobile,totalPrice,shipAddress,retailerName,retailerMobile,producerName,producerMobile;

    //image
    private Uri imageUri,cropUri;

    //system date
    private SimpleDateFormat dateFormat;

    //firebase
    FirebaseAuth fbAuth;
    FirebaseDatabase fbDB;
    DatabaseReference dbRef;
    FirebaseStorage fbStorage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_shipping_order);
        //CONNECT XML
        address = findViewById(R.id.ad3);
        ProducerName = findViewById(R.id.producername2);
        prodmobile = findViewById(R.id.pMobile);
        total = findViewById(R.id.Shiptotal1);
        MobileNumber = findViewById(R.id.ShipNumber1);
        Retname = findViewById(R.id.ShipName1);
        shipped = findViewById(R.id.shipped2);
        imageProof = findViewById(R.id.uploadProof);

        //PROGRESS DIALOG
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        //HIDE SHIPPED BUTTON
        shipped.setVisibility(View.GONE);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        //FIREBASE INSTANCES
        fbAuth = FirebaseAuth.getInstance();
        fbDB = FirebaseDatabase.getInstance();
        fbStorage = FirebaseStorage.getInstance();

        randomuid = getIntent().getStringExtra("RandomUID"); //tracking number
        logisticsID = fbAuth.getCurrentUser().getUid();

        //LOGISTICS REFERENCE
        dbRef = fbDB.getReference("Logistics").child(logisticsID);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //logistics info
                Logistics logistics = snapshot.getValue(Logistics.class);
                logisticsName = logistics.getFullName();
                logisticsMobile = logistics.getMobile();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        //SHIPPING ORDER INFORMATION REFERENCE
        dbRef = fbDB.getReference("LogisticsShipFinalOrders").child(logisticsID).child(randomuid).child("OtherInformation");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //show values
                LogisticsShipFinalOrders1 finalOrders = snapshot.getValue(LogisticsShipFinalOrders1.class);
                totalPrice =  finalOrders.getGrandTotalPrice();
                total.setText("??? "+totalPrice);
                shipAddress = finalOrders.getAddress();
                address.setText(shipAddress);
                retailerName = finalOrders.getName();
                Retname.setText(retailerName);
                retailerMobile = finalOrders.getMobileNumber();
                MobileNumber.setText(retailerMobile);
                producerName = finalOrders.getProducerName();
                ProducerName.setText("Producer: " + producerName);
                userid = finalOrders.getUserId();
                producerID = finalOrders.getProducerId();

                //producer reference
                dbRef = fbDB.getReference("Producer").child(producerID);
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //producer info
                        Producer producer = snapshot.getValue(Producer.class);
                        producerMobile = producer.getMobile();
                        prodmobile.setText(producerMobile);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //BUTTON EVENTS
        imageProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectImageClick(view);
            }
        });

        shipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUri!=null) {
                    Shipped(logisticsName,logisticsMobile,totalPrice,shipAddress,retailerName,retailerMobile,producerName,producerMobile,logisticsID,producerID,userid,imageUri);
                }else{
                    Toast.makeText(LogisticsShippingOrder.this, "no image detected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //CONFIRM ORDER
    @SuppressLint("SimpleDateFormat")
    private void Shipped(String logisticsName, String logisticsMobile, String totalPrice, String shipAddress, String retailerName, String retailerMobile, String producerName, String producerMobile, String logisticsID, String producerID, String userid, Uri imageUri){
        progressDialog.setMessage("Order confirmation in progress...");
        progressDialog.show();

        randomuid = getIntent().getStringExtra("RandomUID"); //tracking number

        //set delivery date
        dateFormat = new SimpleDateFormat("'Date: 'MM.dd.yyyy 'Time: 'hh:mm:ss aa");
        currentDate = dateFormat.format(new Date());

        //delivery proof image reference
        storageRef = fbStorage.getReference("DeliveryProof").child(producerID).child(randomuid);
        //upload file to db
        storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //get image db link
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //image link to string
                        String imageURL = uri.toString();

                        //use model to upload values
                        History history = new History(randomuid,currentDate,shipAddress,totalPrice,producerName,producerMobile,retailerName,retailerMobile,logisticsName,logisticsMobile,imageURL,producerID,userid,logisticsID);

                        //delivery history reference
                        dbRef = fbDB.getReference("DeliveryHistory").child(producerID).child(randomuid);

                        //upload to db
                        dbRef.setValue(history).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //change delivery status to delivered
                                FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(userid).child(randomuid).child("OtherInformation").child("Status").setValue("Your Order is delivered").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //retailer token reference
                                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //get token
                                                String usertoken = dataSnapshot.getValue(String.class);
                                                //product delivered
                                                sendNotifications(usertoken, "Home Producer", "Thank you for Ordering", "ThankYou");
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //producer token reference
                                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(producerID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //get token
                                                String usertoken = dataSnapshot.getValue(String.class);
                                                //product delivered
                                                sendNotifications(usertoken, "Order Placed", "Product has been delivered", "Delivered");
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(LogisticsShippingOrder.this);
                                        builder.setMessage("Order is delivered, Now you can check for new Orders");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                //direct to logistics home
                                                Intent intent = new Intent(LogisticsShippingOrder.this, ProductPanelBottomNavigation_Logistics.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //remove order from retailer db
                                        FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(userid).child(randomuid).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseDatabase.getInstance().getReference("RetailerFinalOrders").child(userid).child(randomuid).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        //remove order from logistics db
                                                        FirebaseDatabase.getInstance().getReference("LogisticsShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomuid).child("Products").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                FirebaseDatabase.getInstance().getReference("LogisticsShipFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomuid).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        //change retailer order status
                                                                        FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(userid).child("isOrdered").setValue("false");
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                //upload failed
                Toast.makeText(LogisticsShippingOrder.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            //progress dialog by percent
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                progressDialog.setCanceledOnTouchOutside(false);
            }
        });
    }

    //PICK IMAGE
    private void onSelectImageClick(View v) {
        CropImage.startPickImageActivity(this);
    }
    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //check image option
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = CropImage.getPickImageResultUri(this, data);
            //set image for cropping
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                cropUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                //crop image
                startCropImageActivity(imageUri);
            }
        }

        //check crop status
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((ImageButton) findViewById(R.id.uploadProof)).setImageURI(result.getUri());
                Toast.makeText(this, "Cropped Successfully", Toast.LENGTH_SHORT).show();
                //show shipped button
                shipped.setVisibility(View.VISIBLE);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping Failed" + result.getError(), Toast.LENGTH_SHORT).show();
                //hide shipped button
                shipped.setVisibility(View.GONE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //CHECK CROPPING PERMISSIONS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (cropUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCropImageActivity(cropUri);
        } else {
            Toast.makeText(this, "Cancelling,required permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    //CROP IMAGE
    private void startCropImageActivity(Uri imageuri) {
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    //NOTIFY USER
    private void sendNotifications(String usertoken, String title, String message, String order) {
        Data data = new Data(title, message, order);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(retrofit2.Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(LogisticsShippingOrder.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
