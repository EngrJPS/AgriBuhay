package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.AgriBuhayProj.app.Models.Producer;
import com.AgriBuhayProj.app.Models.Crops;
import com.AgriBuhayProj.app.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//POST PRODUCT
public class ProducerPostProduct extends AppCompatActivity {
    //VARIABLES
    ImageButton imageButton;
    Button post_product;
    Spinner Products;
    TextInputLayout desc, qty, pri, num;
    ProgressDialog progressDialog;

    Uri imageURI,cropURI;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,spinRef;
    DatabaseReference dataaa;
    FirebaseAuth fbAuth;
    StorageReference ref;

    String description,quantity,price, products, mobile;
    String producerID,randomUID;
    String province,city,baranggay;
    String imageURL;

    List<Crops> cropList;
    ArrayAdapter<Crops> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_post_product);
        //PROGRESS DIALOG
        progressDialog = new ProgressDialog(ProducerPostProduct.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        //CONNECT XML
        desc = findViewById(R.id.description);
        qty = findViewById(R.id.quantity);
        pri = findViewById(R.id.price);
        num = findViewById(R.id.mobile);
        post_product = findViewById(R.id.post);
        imageButton = findViewById(R.id.imageupload);
        Products = findViewById(R.id.products);

        //FIREBASE INSTANCE
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        fbAuth = FirebaseAuth.getInstance();

        //REFERENCE
        //product image reference
        storageReference = storage.getReference("ProductSupply");
        //product supply
        databaseReference = firebaseDatabase.getReference("ProductSupplyDetails");

        //SPINNER LIST
        //array
        cropList = new ArrayList<>();
        //adapter
        adapter = new ArrayAdapter<>(ProducerPostProduct.this, android.R.layout.simple_spinner_dropdown_item, cropList);
        //firebase
        spinRef = FirebaseDatabase.getInstance().getReference("Crops");
        //show data
        spinnerList();

        try {
            //get producer id
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            //producer reference
            dataaa = firebaseDatabase.getReference("Producer").child(userid);
            dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //get db values
                    Producer producer = dataSnapshot.getValue(Producer.class);
                    province = producer.getProvince();
                    city = producer.getCity();
                    baranggay = producer.getBaranggay();

                    //select image
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onSelectImageClick(v);
                        }
                    });

                    //post product
                    post_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //get string values
                            products = Products.getSelectedItem().toString().trim();
                            description = desc.getEditText().getText().toString().trim();
                            quantity = qty.getEditText().getText().toString().trim();
                            price = pri.getEditText().getText().toString().trim();
                            mobile = num.getEditText().getText().toString().trim();

                            //check validity
                            if (isValid()) {
                                //upload product
                                uploadImage();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            Log.e("Errrrrr: ", e.getMessage());
        }
    }

    //VALIDATION
    private boolean isValid() {
        desc.setErrorEnabled(false);
        desc.setError("");
        qty.setErrorEnabled(false);
        qty.setError("");
        pri.setErrorEnabled(false);
        pri.setError("");

        boolean isValiDescription = false, isValidPrice = false, isvalidQuantity = false, isvalid = false;
        if (TextUtils.isEmpty(description)) {
            desc.setErrorEnabled(true);
            desc.setError("Description is Required");
        } else {
            desc.setError(null);
            isValiDescription = true;
        }
        if (TextUtils.isEmpty(quantity)) {
            qty.setErrorEnabled(true);
            qty.setError("Quantity is Required");
        } else {
            isvalidQuantity = true;
        }
        if (TextUtils.isEmpty(price)) {
            pri.setErrorEnabled(true);
            pri.setError("Price is Required");
        } else {
            isValidPrice = true;
        }
        isvalid = (isValiDescription && isvalidQuantity && isValidPrice) ? true : false;

        return isvalid;
    }

    //UPLOAD IMAGE
    private void uploadImage() {
        //check image
        if (imageURI != null) {
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //product id
            randomUID = UUID.randomUUID().toString();

            //producer id
            producerID = fbAuth.getCurrentUser().getUid();

            //save image to db
            ref = storageReference.child(producerID).child(randomUID);
            ref.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //get image db link
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //convert link to string
                            imageURL = uri.toString();
                            //Added Mobile
                            ProductSupplyDetails info = new ProductSupplyDetails(products,quantity,price,description,imageURL,randomUID,producerID,mobile);
                            //ProductSupplyDetails reference
                            firebaseDatabase.getReference("ProductSupplyDetails").child(province).child(city).child(baranggay).child(producerID).child(randomUID)
                                    .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ProducerPostProduct.this, "Product posted successfully", Toast.LENGTH_SHORT).show();
                                    finish();
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
                    Toast.makeText(ProducerPostProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //progress dialog with percentage
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }
    }

    //SELECT IMAGE
    private void onSelectImageClick(View v) {
        CropImage.startPickImageActivity(this);
    }

    //CHOOSE SOURCE
    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //check source picked
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //get image
            imageURI = CropImage.getPickImageResultUri(this, data);
            //crop image permission
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageURI)) {
                cropURI = imageURI;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCropImageActivity(imageURI);
            }
        }
        //check crop activity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            //get cropped image
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //check result
            if (resultCode == RESULT_OK) {
                //display image
                ((ImageButton) findViewById(R.id.imageupload)).setImageURI(result.getUri());
                Toast.makeText(this, "Cropped Successfully", Toast.LENGTH_SHORT).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed" + result.getError(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //CROP PERMISSION
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (cropURI != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCropImageActivity(cropURI);
        } else {
            Toast.makeText(this, "cancelling,required permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    //CROP IMAGE
    private void startCropImageActivity(Uri imageuri) {
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }

    //SPINNER CONTENTS
    private void spinnerList(){
        spinRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //list crops added
                for(DataSnapshot item:snapshot.getChildren()){
                    Crops cropModel = item.getValue(Crops.class);
                    cropList.add(cropModel);
                }
                //set adapter
                Products.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


