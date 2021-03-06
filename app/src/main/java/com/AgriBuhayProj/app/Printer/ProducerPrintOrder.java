package com.AgriBuhayProj.app.Printer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.AgriBuhayProj.app.Models.Producer;
import com.AgriBuhayProj.app.Models.Crops;
import com.AgriBuhayProj.app.Models.ProductData;
import com.AgriBuhayProj.app.Models.Sensors;
import com.AgriBuhayProj.app.ProducerPanel.ProducerFinalOrders;
import com.AgriBuhayProj.app.ProducerPanel.ProducerFinalOrders1;
import com.AgriBuhayProj.app.R;
import com.AgriBuhayProj.app.ReusableCode.ReusableCodeForAll;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

//PRINT ORDER
public class ProducerPrintOrder extends Activity implements Runnable {
    //VARIABLES
    protected static final String TAG = "TAG";

    //pair device
    private static final int REQUEST_CONNECT_DEVICE = 1;
    //enable bluetooth
    private static final int REQUEST_ENABLE_BT = 2;

    Button mSave,mScan,mPrint,mDisc;
    BluetoothAdapter mBluetoothAdapter;
    Spinner spinProduct;
    ProgressDialog progressDialog;

    //printer id
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;

    //bluetooth socket
    private BluetoothSocket mBluetoothSocket;

    //printer bluetooth
    BluetoothDevice mBluetoothDevice;

    //tracking number
    private String randomUIID;

    private TextInputLayout prodName, totalWeight, totalTemp, totalHumid, totalCO2;
    private String prod, weight, temp, humid, co2;
    private Double dW,dT,dH,dC;
    //decimal places
    private DecimalFormat df = new DecimalFormat("#.##");

    ArrayList<String> productList;
    ArrayAdapter<String> adapter;

    FirebaseAuth fbAuth;
    DatabaseReference dbRef;

    String productName;

    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.main_printer);
        //CONNECT XML
        prodName = findViewById(R.id.edtNameProduct);
        totalWeight = findViewById(R.id.edtNetWt);
        totalTemp = findViewById(R.id.edtTemperature);
        totalHumid = findViewById(R.id.edtHumidity);
        totalCO2 = findViewById(R.id.edtCO2);
        spinProduct = findViewById(R.id.spinPName);

        //PROGRESS DIALOG
        progressDialog = new ProgressDialog(ProducerPrintOrder.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        //INSTANCE
        fbAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();

        //USER ID
        String uID = fbAuth.getCurrentUser().getUid();
        //TRACKING NUMBER
        randomUIID = getIntent().getStringExtra("RandomUIID");

        //ARRAY LIST
        productList = new ArrayList<>();
        //ADAPTER
        adapter = new ArrayAdapter<>(ProducerPrintOrder.this, android.R.layout.simple_spinner_dropdown_item,productList);

        //ORDERED PRODUCTS REFERENCE
        dbRef.child("ProducerFinalOrders").child(uID).child(randomUIID).child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //LIST ORDERED PRODUCTS
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    final ProducerFinalOrders productFinalOrders = dataSnapshot.getValue(ProducerFinalOrders.class);

                    productName = productFinalOrders.getProductName();

                    productList.add(productName);

                    spinProduct.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    //SELECTED ORDERED PRODUCT
                    spinProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Object value = adapterView.getItemAtPosition(i);
                            String spinProduct = value.toString().trim();

                            //set product name
                            prodName.getEditText().setText(spinProduct);

                            //crop referemce
                            dbRef.child("Crops").child(spinProduct).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //GET VALUES
                                    Crops crops = snapshot.getValue(Crops.class);
                                    Double minT = Double.parseDouble(crops.getTempMin());
                                    Double maxT = Double.parseDouble(crops.getTempMax());
                                    Double minH = Double.parseDouble(crops.getHumidMin());
                                    Double maxH = Double.parseDouble(crops.getHumidMax());
                                    Double minC = Double.parseDouble(crops.getCarbonMin());
                                    Double maxC = Double.parseDouble(crops.getCarbonMax());

                                    //compare sensor measurements to reference
                                    compareData(minT,maxT,minH,maxH,minC,maxC);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //save sensor measurement values
        mSave = findViewById(R.id.btnSave);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Saving value..");
                progressDialog.show();

                //get string value
                prod = prodName.getEditText().getText().toString();
                weight = totalWeight.getEditText().getText().toString();
                temp = totalTemp.getEditText().getText().toString();
                humid = totalHumid.getEditText().getText().toString();
                co2 = totalCO2.getEditText().getText().toString();

                //set values
                ProductData productData = new ProductData(prod,weight,temp,humid,co2);

                //add product values to ProductData
                dbRef.child("ProductData").child(randomUIID).child(prod).setValue(productData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(ProducerPrintOrder.this, "Product value saved", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //scan device
        mScan = findViewById(R.id.Scan);
        mScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (mBluetoothAdapter == null) {
                    Toast.makeText(ProducerPrintOrder.this, "Message1", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    } else {
                        //list paired devices
                        ListPairedDevices();
                        //show paired devices
                        Intent connectIntent = new Intent(ProducerPrintOrder.this,
                                DeviceListActivity.class);
                        //request connection
                        startActivityForResult(connectIntent,
                                REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });

        //PRINT RECEIPT
        mPrint = findViewById(R.id.mPrint);
        mPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                p2();
                p1();
                p3();
            }
        });

        //DISABLE CONNECTION
        mDisc = findViewById(R.id.dis);
        mDisc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                if (mBluetoothAdapter != null)
                    //disable bluetooth
                    mBluetoothAdapter.disable();
            }
        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    //PAIR DEVICE
    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        //check request
        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    //get device name + address
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    //get device
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    //connect device
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    //display paired devices
                    ListPairedDevices();
                    Intent connectIntent = new Intent(ProducerPrintOrder.this,
                            DeviceListActivity.class);
                    //device connected
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(ProducerPrintOrder.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //LIST PAIRED DEVICES
    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0) {
            //get device name + address
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  " + mDevice.getAddress());
            }
        }
    }

    //FIND BLUETOOTH DEVICES
    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    //DISABLE BLUETOOTH PRINTER
    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    //DEVICE CONNECTED TO PRINTER
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(ProducerPrintOrder.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
        }
    };

    //ARRAY VALUES TO BYTE
    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    //PRINTER BUFFER
    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

    //RETAILER DETAILS RECEIPT LINE
    public void p1() {
        Thread t = new Thread() {
            public void run() {
                try {
                    randomUIID = getIntent().getStringExtra("RandomUIID");
                    DatabaseReference dataa = FirebaseDatabase.getInstance().getReference("ProducerFinalOrders").child(fbAuth.getCurrentUser().getUid()).child(randomUIID).child("OtherInformation");
                    dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final ProducerFinalOrders1 producerFinalOrders1 = snapshot.getValue(ProducerFinalOrders1.class);
                            String retailerName = producerFinalOrders1.getName();
                            String retailerAds = producerFinalOrders1.getAddress();
                            String totalPrice = producerFinalOrders1.getGrandTotalPrice();
                            try {
                                OutputStream os = mBluetoothSocket.getOutputStream();
                                String BILL = "";
                                BILL = BILL + "\n";
                                BILL = BILL + String.format("%1$-10s %2$10s", "Transaction Number: ", randomUIID);
                                BILL = BILL + "\n";
                                BILL = BILL + "================================\n";
                                BILL = BILL + String.format("%1$-10s %2$10s", "Retailer Name: ", retailerName);
                                BILL = BILL + "\n";
                                BILL = BILL + String.format("%1$-10s %2$10s", "Retailer Address: ", retailerAds);
                                BILL = BILL + "\n";
                                BILL = BILL + String.format("%1$-10s %2$10s", "Total Price: ", "PHP"+totalPrice);
                                BILL = BILL + "\n";
                                BILL = BILL + "================================\n";
                                BILL = BILL + "\n";
                                os.write(BILL.getBytes());
                                //This is printer specific code you can comment ==== > Start

                                // Setting height
                                int gs = 29;
                                os.write(intToByteArray(gs));
                                int h = 104;
                                os.write(intToByteArray(h));
                                int n = 162;
                                os.write(intToByteArray(n));

                                // Setting Width
                                int gs_width = 29;
                                os.write(intToByteArray(gs_width));
                                int w = 119;
                                os.write(intToByteArray(w));
                                int n_width = 2;
                                os.write(intToByteArray(n_width));

                            } catch (Exception e) {
                                Log.e("MainActivity", "Exe ", e);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch (Exception e) {
                    Log.e("MainActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

    //PRODUCER DETAILS RECEIPT LINE
    public void p2() {
        Thread t = new Thread() {
            public void run() {
                try {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("Producer").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final Producer producer = snapshot.getValue(Producer.class);
                            String producerName = producer.getFirstName() + " " + producer.getLastName();
                            String province = producer.getProvince();
                            String address = producer.getCity() + " " + producer.getBaranggay();
                            String num = producer.getMobile();
                            try {
                                OutputStream os = mBluetoothSocket.getOutputStream();
                                String BILL = "";
                                BILL = BILL + "\n";
                                BILL = BILL + "================================\n";
                                BILL = BILL + String.format("%1$-10s %2$10s", "Producer Name :", producerName);
                                BILL = BILL + "\n";
                                BILL = BILL + String.format("%1$-10s %2$10s", "Province :", province);
                                BILL = BILL + "\n";
                                BILL = BILL + String.format("%1$-10s %2$10s", "Address :", address);
                                BILL = BILL + "\n";
                                BILL = BILL + String.format("%1$-10s %2$10s", "Mobile Number: ", num);
                                BILL = BILL + "\n";
                                BILL = BILL + "================================\n";
                                BILL = BILL + "\n\n";
                                os.write(BILL.getBytes());
                                //This is printer specific code you can comment ==== > Start

                                // Setting height
                                int gs = 29;
                                os.write(intToByteArray(gs));
                                int h = 104;
                                os.write(intToByteArray(h));
                                int n = 162;
                                os.write(intToByteArray(n));

                                // Setting Width
                                int gs_width = 29;
                                os.write(intToByteArray(gs_width));
                                int w = 119;
                                os.write(intToByteArray(w));
                                int n_width = 2;
                                os.write(intToByteArray(n_width));

                            } catch (Exception e) {
                                Log.e("MainActivity", "Exe ", e);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch (Exception e) {
                    Log.e("MainActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

    //PRODUCT DETAILS
    public void p3(){
        //product data reference
        dbRef.child("ProductData").child(randomUIID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get values
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    final ProductData productData = dataSnapshot.getValue(ProductData.class);
                    prod = productData.getProductName();
                    weight = productData.getTotalWeight();
                    temp = productData.getTemperatureLevel();
                    humid = productData.getHumidityPercentage();
                    co2 = productData.getCo2Level();

                    //product details receipt line
                    p4(prod,weight,temp,humid,co2);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //PRODUCT DETAILS RECEIPT LINE
    private void p4(String prod,String weight,String temp,String humid,String co2){
        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket.getOutputStream();
                    String BILL = "";
                    BILL = BILL + "================================\n";
                    BILL = BILL + String.format("%1$-10s %2$10s", "Product Name :", prod);
                    BILL = BILL + "\n";
                    BILL = BILL + String.format("%1$-10s %2$10s", "Total Net Weight :", weight);
                    BILL = BILL + "\n";
                    BILL = BILL + String.format("%1$-10s %2$10s", "Temperature Value :", temp);
                    BILL = BILL + "\n";
                    BILL = BILL + String.format("%1$-10s %2$10s", "Humidity Percentage :", humid);
                    BILL = BILL + "\n";
                    BILL = BILL + String.format("%1$-10s %2$10s", "CO2 Value :", co2);
                    BILL = BILL + "\n";
                    BILL = BILL + "================================\n";
                    BILL = BILL + "\n";

                    os.write(BILL.getBytes());
                    //This is printer specific code you can comment ==== > Start

                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 104;
                    os.write(intToByteArray(h));
                    int n = 162;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));


                } catch (Exception e) {
                    Log.e("MainActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

    //VALIDATION
    public boolean isValid(){
        prodName.setErrorEnabled(false);
        prodName.setError("");
        totalWeight.setErrorEnabled(false);
        totalWeight.setError("");
        totalCO2.setErrorEnabled(false);
        totalCO2.setError("");
        totalHumid.setErrorEnabled(false);
        totalHumid.setError("");
        totalTemp.setErrorEnabled(false);
        totalTemp.setError("");
        boolean isValidname = false, isValidWeight = false, isValidHumid = false, isValidTemp = false, isValidCO2 = false, isvalid = false;
        if(TextUtils.isEmpty(prod)){
            prodName.setErrorEnabled(true);
            prodName.setError("Product name is Required");
        }else{
            isValidname = true;
        }
        if(TextUtils.isEmpty(weight)){
            totalWeight.setErrorEnabled(true);
            totalWeight.setError("Total Weight is Required");
        }else{
            isValidWeight = true;
        }
        if(TextUtils.isEmpty(temp)){
            totalTemp.setErrorEnabled(true);
            totalTemp.setError("Temperature is Required");
        }else{
            isValidTemp = true;
        }
        if(TextUtils.isEmpty(humid)){
            totalHumid.setErrorEnabled(true);
            totalHumid.setError("Humidity is Required");
        }else{
            isValidHumid = true;
        }
        if(TextUtils.isEmpty(co2)){
            totalCO2.setErrorEnabled(true);
            totalCO2.setError("CO2 is Required");
        }else{
            isValidCO2 = true;
        }

        isvalid = (isValidname && isValidWeight && isValidHumid && isValidTemp && isValidCO2) ? true : false;
        return isValid();
    }

    //COMPARE SENSOR DATA TO PRODUCT REFERENCE
    public void compareData(Double minT,Double maxT,Double minH,Double maxH,Double minC, Double maxC){
        //sensor data reference
        dbRef.child("Sensors").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get db value
                Sensors sensors = snapshot.getValue(Sensors.class);
                assert sensors != null;
                //double value
                dW = sensors.getWeight_Value();
                dT = sensors.getTemp_Value();
                dH = sensors.getHumid_Value();
                dC = sensors.getCO2_PPM();
                //display values
                totalWeight.getEditText().setText(Double.toString(Double.parseDouble(df.format(dW)))+" kg");
                totalTemp.getEditText().setText(Double.toString(Double.parseDouble(df.format(dT)))+" ??C");
                totalHumid.getEditText().setText(Double.toString(Double.parseDouble(df.format(dH)))+" %");
                totalCO2.getEditText().setText(Double.toString(Double.parseDouble(df.format(dC)))+" ppm");

                //check temperature
                if(dT<minT){
                    totalTemp.setErrorEnabled(true);
                    totalTemp.setError("Low Temperature");
                    totalTemp.setErrorIconDrawable(R.drawable.ic_error_red);
                    totalTemp.setErrorIconOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //display preferred values
                            ReusableCodeForAll.ShowAlert(ProducerPrintOrder.this,"Preferred Temperature Level","Minimum: "+df.format(minT)+"??C"+"\nMaximum: "+df.format(maxT)+"??C");
                        }
                    });
                }else if(dT>maxT){
                    totalTemp.setErrorEnabled(true);
                    totalTemp.setError("High Temperature");
                    totalTemp.setErrorIconDrawable(R.drawable.ic_error_red);
                    totalTemp.setErrorIconOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //display preferred values
                            ReusableCodeForAll.ShowAlert(ProducerPrintOrder.this,"Preferred Temperature Level","Minimum: "+df.format(minT)+"??C"+"\nMaximum: "+df.format(maxT)+"??C");
                        }
                    });
                }else{
                    totalTemp.setErrorEnabled(false);
                    totalTemp.setErrorIconDrawable(null);
                    totalTemp.setError("");
                }

                //check humidity
                if(dH<minH){
                    totalHumid.setErrorEnabled(true);
                    totalHumid.setError("Low Humidity Percentage");
                    totalHumid.setErrorIconDrawable(R.drawable.ic_error_red);
                    totalHumid.setErrorIconOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //display preferred values
                            ReusableCodeForAll.ShowAlert(ProducerPrintOrder.this,"Preferred Humidity Percentage","Minimum: "+df.format(minH)+"%"+"\nMaximum: "+df.format(maxH)+"%");
                        }
                    });
                }else if (dH>maxH){
                    totalHumid.setErrorEnabled(true);
                    totalHumid.setError("High Humidity Percentage");
                    totalHumid.setErrorIconDrawable(R.drawable.ic_error_red);
                    totalHumid.setErrorIconOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //display preferred values
                            ReusableCodeForAll.ShowAlert(ProducerPrintOrder.this,"Preferred Humidity Percentage","Minimum: "+df.format(minH)+"%"+"\nMaximum: "+df.format(maxH)+"%");
                        }
                    });
                }else{
                    totalHumid.setErrorEnabled(false);
                    totalHumid.setErrorIconDrawable(null);
                    totalHumid.setError("");
                }

                //check co2
                if(dC<minC){
                    totalCO2.setErrorEnabled(true);
                    totalCO2.setError("Low CO2 Level");
                    totalCO2.setErrorIconDrawable(R.drawable.ic_error_red);
                    totalCO2.setErrorIconOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //display preferred values
                            ReusableCodeForAll.ShowAlert(ProducerPrintOrder.this,"Preferred CO2 Level","Minimum: "+df.format(minC)+" ppm"+"\nMaximum: "+df.format(maxC)+" ppm");
                        }
                    });
                }else if (dC>maxC){
                    totalCO2.setErrorEnabled(true);
                    totalCO2.setError("High CO2 Level");
                    totalCO2.setErrorIconDrawable(R.drawable.ic_error_red);
                    totalCO2.setErrorIconOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //display preferred values
                            ReusableCodeForAll.ShowAlert(ProducerPrintOrder.this,"Preferred CO2 Level","Minimum: "+df.format(minC)+" ppm"+"\nMaximum: "+df.format(maxC)+" ppm");
                        }
                    });
                }else{
                    totalCO2.setErrorEnabled(false);
                    totalCO2.setErrorIconDrawable(null);
                    totalCO2.setError("");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}