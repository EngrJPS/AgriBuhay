package com.AgriBuhayProj.app.Printer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.AgriBuhayProj.app.Chef;
import com.AgriBuhayProj.app.ProducerPanel.ChefFinalOrders;
import com.AgriBuhayProj.app.R;
import com.bumptech.glide.util.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;


public class ProducerPrintOrder extends Activity implements Runnable {
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    private String producerID = "tFHeG3JY4DbOld2kNqISYw3C4ZJ3";
    private String randomId = "1fbf53c1-d360-4760-9d25-17d063ddbeb0";
    private String randomId1 = "f711dfc3-a0ef-4e07-b53c-c1504e852780";

    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.main_printer);
        mScan = (Button) findViewById(R.id.Scan);
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
                        ListPairedDevices();
                        Intent connectIntent = new Intent(ProducerPrintOrder.this,
                                DeviceListActivity.class);
                        startActivityForResult(connectIntent,
                                REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });

        mPrint = (Button) findViewById(R.id.mPrint);
        mPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                p1();
                p2();
            }
        });

        mDisc = (Button) findViewById(R.id.dis);
        mDisc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                if (mBluetoothAdapter != null)
                    mBluetoothAdapter.disable();
            }
        });

    }// onCreate

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

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
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
                    ListPairedDevices();
                    Intent connectIntent = new Intent(ProducerPrintOrder.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(ProducerPrintOrder.this, "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  " + mDevice.getAddress());
            }
        }
    }

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

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(ProducerPrintOrder.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

    public void p1() {
        Thread t = new Thread() {
            public void run() {
                try {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("Chef").child(producerID);
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final Chef chef = snapshot.getValue(Chef.class);
                            String producerName = chef.getFname() + " " + chef.getLname();
                            String province = chef.getState();
                            String address = chef.getCity() + " " + chef.getSuburban();
                            String num = "+63" + chef.getMobile();
                            try {
                                OutputStream os = mBluetoothSocket
                                        .getOutputStream();
                                String BILL = "";
                                BILL = BILL
                                        + "================================\n";


                                BILL = BILL + String.format("%1$-10s %2$10s", "Producer name: ", producerName);
                                BILL = BILL + "\n";
                                BILL = BILL + String.format("%1$-10s %2$10s", "Province: ", province);
                                BILL = BILL + "\n";
                                BILL = BILL + String.format("%1$-10s %2$10s", "Address: ", address);
                                BILL = BILL + "\n";
                                BILL = BILL + String.format("%1$-10s %2$10s", "MobileNo.: ", num);
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

    public void p2() {
        Thread t = new Thread() {
            public void run() {
                try {
                    DatabaseReference dataa = FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(producerID).child(randomId).child("OtherInformation");
                    dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final ChefFinalOrders chefFinalOrder = snapshot.getValue(ChefFinalOrders.class);
                            String retailerName = chefFinalOrder.getName();
                            String retailerAds = chefFinalOrder.getAddress();
                            DatabaseReference dataaa = FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(producerID).child(randomId).child("Dishes").child(randomId1);
                            dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    final OtherInformation otherInformation = snapshot.getValue(OtherInformation.class);
                                    String productName = otherInformation.getDishName();
                                    String price = otherInformation.getDishQuantity() + "kg " + otherInformation.getDishPrice();
                                    String totalPrice = otherInformation.getTotalPrice();
                                    try {
                                        OutputStream os = mBluetoothSocket
                                                .getOutputStream();
                                        String BILL = "";
                                        BILL = BILL
                                                + "================================\n";


                                        BILL = BILL + String.format("%1$-10s %2$10s", "Retailer name: ", retailerName);
                                        BILL = BILL + "\n";
                                        BILL = BILL + String.format("%1$-10s %2$10s", "Retailer Address: ", retailerAds);
                                        BILL = BILL + "\n";
                                        BILL = BILL + String.format("%1$-10s %2$10s", "Product name: ", productName);
                                        BILL = BILL + "\n";
                                        BILL = BILL + String.format("%1$-10s %2$10s", "Price/kg: ", price);
                                        BILL = BILL + "\n";
                                        BILL = BILL + String.format("%1$-10s %2$10s", "GrandTotal: ", totalPrice);
                                        BILL = BILL + "\n";
                                        BILL = BILL
                                                + "================================\n";
                                        BILL = BILL + "\n\n ";
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
}