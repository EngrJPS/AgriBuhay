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

public class UpdateDeleteProduct extends AppCompatActivity {


    TextInputLayout desc, qty, pri;
    TextView Productname;
    ImageButton imageButton;
    Uri imageuri;
    String dburi;
    private Uri mCropimageuri;
    Button Update_product, Delete_product;
    String description, quantity, price, products, ProducerId;
    String RandomUId;
    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth FAuth;
    String ID;
    private ProgressDialog progressDialog;
    DatabaseReference dataaa;
    String State, City, Sub;
    //Added mobile
    String Mobile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer_post_product_update_delete);

        desc = (TextInputLayout) findViewById(R.id.description);
        qty = (TextInputLayout) findViewById(R.id.quantity);
        pri = (TextInputLayout) findViewById(R.id.price);
        Productname = (TextView) findViewById(R.id.product_name);
        imageButton = (ImageButton) findViewById(R.id.imageupload);
        Update_product = (Button) findViewById(R.id.Updateproduct);
        Delete_product = (Button) findViewById(R.id.Deleteproduct);
        ID = getIntent().getStringExtra("updatedeleteproduct");

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = firebaseDatabase.getInstance().getReference("Producer").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Producer producerc = dataSnapshot.getValue(Producer.class);
                State = producerc.getProvince();
                City = producerc.getCity();
                Sub = producerc.getBaranggay();

                Update_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        description = desc.getEditText().getText().toString().trim();
                        quantity = qty.getEditText().getText().toString().trim();
                        price = pri.getEditText().getText().toString().trim();


                        if (isValid()) {
                            if (imageuri != null) {
                                uploadImage();
                            } else {
                                updatedesc(dburi);
                            }
                        }
                    }
                });

                Delete_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDeleteProduct.this);
                        builder.setMessage("Are you sure you want to Delete product");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO this is the database for the FoosuplyDetails
                                firebaseDatabase.getInstance().getReference("ProductSupplyDetails").child(State).child(City).child(Sub).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID).removeValue();

                                AlertDialog.Builder product = new AlertDialog.Builder(UpdateDeleteProduct.this);
                                product.setMessage("Your Product has been Deleted");
                                product.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        startActivity(new Intent(UpdateDeleteProduct.this, ProductPanelBottomNavigation_Producer.class));
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


                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                progressDialog = new ProgressDialog(UpdateDeleteProduct.this);
                //TODO this is the database for the FoodSupplyDetails
                databaseReference = FirebaseDatabase.getInstance().getReference("ProductSupplyDetails").child(State).child(City).child(Sub).child(useridd).child(ID);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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


                FAuth = FirebaseAuth.getInstance();
                //TODO this is the database for the FoodSupplyDetails
                databaseReference = firebaseDatabase.getInstance().getReference("ProductSupplyDetails");
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
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


    private void uploadImage() {

        if (imageuri != null) {

            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            RandomUId = UUID.randomUUID().toString();
            ref = storageReference.child(RandomUId);
            ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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

                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }
    }

    private void updatedesc(String uri) {
        ProducerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //Added Mobile
        ProductSupplyDetails info = new ProductSupplyDetails(products, quantity, price, description, uri, ID, ProducerId, Mobile);
        firebaseDatabase.getInstance().getReference("ProductSupplyDetails").child(State).child(City).child(Sub)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID)
                .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(UpdateDeleteProduct.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void onSelectImageClick(View v) {

        CropImage.startPickImageActivity(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageuri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                mCropimageuri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            } else {

                startCropImageActivity(imageuri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ((ImageButton) findViewById(R.id.imageupload)).setImageURI(result.getUri());
                Toast.makeText(this, "Cropped Successfully" + result.getSampleSize(), Toast.LENGTH_SHORT).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "cropping failed" + result.getError(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (mCropimageuri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCropImageActivity(mCropimageuri);
        } else {
            Toast.makeText(this, "cancelling,required permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCropImageActivity(Uri imageuri) {

        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);


    }
}
