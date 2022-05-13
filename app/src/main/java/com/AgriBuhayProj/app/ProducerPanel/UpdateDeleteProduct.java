package com.AgriBuhayProj.app.ProducerPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.AgriBuhayProj.app.Models.Producer;
import com.bumptech.glide.Glide;
import com.AgriBuhayProj.app.ProductPanelBottomNavigation_Producer;
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

import java.util.UUID;

//PRODUCT UPDATE/DELETE
public class UpdateDeleteProduct extends AppCompatActivity {
    //VARIABLES
    TextInputLayout desc, qty, pri;
    TextView Productname;
    ImageButton imageButton;
    Button Update_product, Delete_product;
    private ProgressDialog progressDialog;

    //image
    private Uri mCropimageuri;
    Uri imageuri;

    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dataaa;
    FirebaseAuth FAuth;

    String dburi;
    String description, quantity, price, products, ProducerId;
    String RandomUId;
    String ID;
    String State, City, Sub;
    String Mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_post_product_update_delete);
        //CONNECT XML
        desc = findViewById(R.id.description);
        qty = findViewById(R.id.quantity);
        pri = findViewById(R.id.price);
        Productname = findViewById(R.id.product_name);
        imageButton = findViewById(R.id.imageupload);
        Update_product = findViewById(R.id.Updateproduct);
        Delete_product = findViewById(R.id.Deleteproduct);

        //FIREBASE INSTANCE
        firebaseDatabase = FirebaseDatabase.getInstance();

        //GET PRODUCT ID
        ID = getIntent().getStringExtra("updatedeleteproduct");

        //GET PRODUCER ID
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //PRODUCER DB REFERENCE
        dataaa = firebaseDatabase.getReference("Producer").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get producer db values
                Producer producerc = dataSnapshot.getValue(Producer.class);
                State = producerc.getProvince();
                City = producerc.getCity();
                Sub = producerc.getBaranggay();

                //update product
                Update_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get string values
                        description = desc.getEditText().getText().toString().trim();
                        quantity = qty.getEditText().getText().toString().trim();
                        price = pri.getEditText().getText().toString().trim();

                        //check validity
                        if (isValid()) {
                            //check image
                            if (imageuri != null) {
                                //upload image
                                uploadImage();
                            } else {
                                //update product description
                                updatedesc(dburi);
                            }
                        }
                    }
                });

                //delete product
                Delete_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDeleteProduct.this);
                        builder.setMessage("Are you sure you want to Delete product");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //ProductSupplyDetails remove product
                                firebaseDatabase.getReference("ProductSupplyDetails").child(State).child(City).child(Sub).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID).removeValue();
                                AlertDialog.Builder product = new AlertDialog.Builder(UpdateDeleteProduct.this);
                                product.setMessage("Your Product has been Deleted");
                                product.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //startActivity(new Intent(UpdateDeleteProduct.this, ProductPanelBottomNavigation_Producer.class));
                                        finish();
                                    }
                                });
                                AlertDialog alertt = product.create();
                                alertt.show();
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

                //get producer id
                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();

                //progress dialog
                progressDialog = new ProgressDialog(UpdateDeleteProduct.this);

                //ProductSupplyDetails product reference
                databaseReference = FirebaseDatabase.getInstance().getReference("ProductSupplyDetails").child(State).child(City).child(Sub).child(useridd).child(ID);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //display db values
                        UpdateProductModel updateProductModel = dataSnapshot.getValue(UpdateProductModel.class);
                        desc.getEditText().setText(updateProductModel.getDescription());
                        qty.getEditText().setText(updateProductModel.getQuantity());
                        Productname.setText("Product name: " + updateProductModel.getProducts());
                        products = updateProductModel.getProducts();
                        pri.getEditText().setText(updateProductModel.getPrice());
                        Glide.with(UpdateDeleteProduct.this).load(updateProductModel.getImageURL()).into(imageButton);
                        dburi = updateProductModel.getImageURL();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //authentication instance
                FAuth = FirebaseAuth.getInstance();

                //ProductSupplyDetails reference
                databaseReference = firebaseDatabase.getReference("ProductSupplyDetails");

                storage = FirebaseStorage.getInstance();
                //product image db reference
                storageReference = storage.getReference("ProductSupply");
                //select image
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSelectImageClick(v);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //STRING VALIDATION
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
        if (imageuri != null) {
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //get producer id
            String producerID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
            //get tracking number
            RandomUId = UUID.randomUUID().toString();

            //product image db reference
            ref = storageReference.child(producerID).child(RandomUId);
            //save image to db
            ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //get image db address
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            updatedesc(String.valueOf(uri));
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateDeleteProduct.this, "Failed : " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //progress dialog w/ percent
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }
    }

    //UPADATE PRODUCT DESCRIPTION
    private void updatedesc(String uri) {
        ProducerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //product db values format
        ProductSupplyDetails info = new ProductSupplyDetails(products, quantity, price, description, uri, ID, ProducerId, Mobile);
        //add product values to db
        firebaseDatabase.getReference("ProductSupplyDetails").child(State).child(City).child(Sub)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID)
                .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(UpdateDeleteProduct.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    //IMAGE SELECTED
    private void onSelectImageClick(View v) {
        //crop image
        CropImage.startPickImageActivity(this);
    }

    //CHOOSE IMAGE
    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //check result
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageuri = CropImage.getPickImageResultUri(this, data);

            //check permissions
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                //image to be cropped
                mCropimageuri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                //crop image
                startCropImageActivity(imageuri);
            }
        }

        //check crop status
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //check result
            if (resultCode == RESULT_OK) {
                //display cropped image
                ((ImageButton) findViewById(R.id.imageupload)).setImageURI(result.getUri());
                Toast.makeText(this, "Cropped Successfully" + result.getSampleSize(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "cropping failed" + result.getError(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //REQUEST PERMISSIONS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mCropimageuri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCropImageActivity(mCropimageuri);
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
}
