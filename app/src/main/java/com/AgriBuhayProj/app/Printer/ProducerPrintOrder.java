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
import com.google.android.material.textfield.TextInputEditText;
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
    protected static final int BT_ON = 3;

    TextInputEditText inputTotal;
    Button mScan, mPrint, mDisc;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    protected BluetoothDevice mBluetoothDevice;
    protected OutputStream outputStream;

    public String BILL = "";
    protected String printerName = "";
    protected boolean isChangingName = false;
    protected boolean isTestingPrinter = false;

    private String RandomUID;

    @Override
    public void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        setContentView(R.layout.main_printer);

        //edit text xml
        inputTotal = findViewById(R.id.edtNetWt);

        isChangingName = false;
//        isTestingPrinter = true;
//        String test = "This is a test for the printer";
//        doPrint(test);

        mPrint = (Button) findViewById(R.id.mPrint);
        mPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isTestingPrinter = true;
                isChangingName = false;
                String test = "This is a test for the printer"+ "\n";
                String test1 = "Please work"+ "\n";
                doPrint(test);
                doPrint(test1);

//                printText(test);
//                doPrint("This");
//                printingProcess(test, "PT-210");
//                RandomUID = getIntent().getStringExtra("RandomUID");
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chef").child(FirebaseAuth.getInstance().getUid());
//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Chef chef = snapshot.getValue(Chef.class);
//                        String ProducerName = chef.getFname() + " " + chef.getLname();
//                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("ChefFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
//                        data.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                final ChefFinalOrders chefFinalOrders1 = snapshot.getValue(ChefFinalOrders.class);
//                                String ProducerId = chefFinalOrders1.getChefId();
//                                String Product = chefFinalOrders1.getDishName();
//                                String Price = "₱ " + chefFinalOrders1.getDishPrice();
//                                String RandomUID = chefFinalOrders1.getRandomUID();
//                                printingProcess("--------------------------------", "PT-210");
//                                printNewLine();
//                                printingProcess(RandomUID, "PT-210");
//                                printNewLine();
//                                printingProcess(ProducerName, "PT-210");
//                                printNewLine();
//                                printingProcess(ProducerId, "PT-210");
//                                printNewLine();
//                                printingProcess(Product, "PT-210");
//                                printNewLine();
//                                printingProcess(Price,"PT-210");
//                                printingProcess("--------------------------------", "PT-210");
//                                printNewLine();
//
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
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

    public void doPrint(final String job){
        String name = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("bluetooth_printer", "");
        printerName = name;
        this.BILL = job;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (name.equalsIgnoreCase(""))
        {

            if (this.mBluetoothAdapter == null)
            {
                Toast.makeText(getApplicationContext(), "Your Bluetooth adapter has issues", Toast.LENGTH_LONG).show();
                return;
            } else if (!this.mBluetoothAdapter.isEnabled())
            {
                //put on the bluetooth
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
                return;
            } else
            {
                introduceNewDevice();
                return;
            }
        }else
        {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,
                    BT_ON);
            return;
        }
    }

    private void introduceNewDevice() {
        ListPairedDevices();
        Intent connectIntent = new Intent(ProducerPrintOrder.this,
                DeviceListActivity.class);
        startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
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

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        try {
            switch (mRequestCode) {
                case REQUEST_CONNECT_DEVICE:
                    if (mResultCode == Activity.RESULT_OK) {
                        Bundle mExtra = mDataIntent.getExtras();
                        String mDeviceAddress = mExtra.getString("DeviceAddress");
                        Log.e(TAG, "Coming incoming address " + mDeviceAddress);
                        mBluetoothDevice = mBluetoothAdapter
                                .getRemoteDevice(mDeviceAddress);

                        mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                                "Connecting...", mBluetoothDevice.getName() + " : "
                                        + mBluetoothDevice.getAddress(), true, false);

                        mBluetoothAdapter.cancelDiscovery();
                        mHandler.sendEmptyMessage(0);

                        //don't print if we are just changing name
                        if (!isChangingName)
                            printingProcess(BILL, mDeviceAddress);
                        else {
                            Toast.makeText(ProducerPrintOrder.this, "Printer selected successfully!", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;

                case REQUEST_ENABLE_BT:
                    if (mResultCode == Activity.RESULT_OK) {
                        introduceNewDevice();
                    } else {
                        Toast.makeText(ProducerPrintOrder.this, "Request denied", Toast.LENGTH_SHORT).show();
                    }
                    break; //BT_ON
                case BT_ON:
                    if (mResultCode == Activity.RESULT_OK) {
                        if (isChangingName) {
                            introduceNewDevice();
                        } else {
                            printingProcess(BILL, printerName);
                        }
                    } else {
                        Toast.makeText(ProducerPrintOrder.this, "Request denied", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }catch(Exception ex){
            Log.e(TAG, ex.toString());
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
            Log.e("main run","inside the main run");
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
                    + com.AgriBuhayProj.app.Printer.UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

    public void printPhoto(int img) {
        try{
            Bitmap imge = BitmapFactory.decodeResource(getResources(), img);
            if(imge != null){
                byte[] command = Utils.decodeBitmap(imge);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    public void printUnicode(){
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            Log.e("printUnicodeProblem", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            // printNewLine();
        } catch (IOException e) {
            Log.e("printTextError",e.toString());
        }
    }


    public  void resetPrint() {
        try {
            outputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            outputStream.write(PrinterCommands.FS_FONT_ALIGN);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void printingProcess(final String BILL, String name) {
        this.mBluetoothDevice = this.mBluetoothAdapter.getRemoteDevice(name);

        try {
            this.mBluetoothSocket = this.mBluetoothDevice.createRfcommSocketToServiceRecord(this.applicationUUID);
            this.mBluetoothSocket.connect();
        } catch (IOException eConnectException) {
            Toast.makeText(ProducerPrintOrder.this, "The printer is not available. Check if it is on", Toast.LENGTH_SHORT).show();

        }

        new Thread() {
            public void run() {
                try { //outputStream
                    outputStream = mBluetoothSocket.getOutputStream();


                    if(isTestingPrinter){
                        //invoice details
                        printConfig(BILL, 2, 1,1);//align 1=center
                        printNewLine();
                    }
                    closeSocket(mBluetoothSocket); //close the connection

                } catch (Exception e) {
                    Log.e("SplashScreen", "Exe ", e);
                }
            }
        }.start();
    }

    protected void printConfig(String bill, int size, int style, int align)
    {
        //size 1 = large, size 2 = medium, size 3 = small
        //style 1 = Regular, style 2 = Bold
        //align 0 = left, align 1 = center, align 2 = right

        try{

            byte[] format = new byte[]{27,33, 0};
            byte[] change = new byte[]{27,33, 0};

            outputStream.write(format);

            //different sizes, same style Regular
            if (size==1 && style==1)  //large
            {
                change[2] = (byte) (0x10); //large
                outputStream.write(change);
            }else if(size==2 && style==1) //medium
            {
                //nothing to change, uses the default settings
            }else if(size==3 && style==1) //small
            {
                change[2] = (byte) (0x3); //small
                outputStream.write(change);
            }

            //different sizes, same style Bold
            if (size==1 && style==2)  //large
            {
                change[2] = (byte) (0x10 | 0x8); //large
                outputStream.write(change);
            }else if(size==2 && style==2) //medium
            {
                change[2] = (byte) (0x8);
                outputStream.write(change);
            }else if(size==3 && style==2) //small
            {
                change[2] = (byte) (0x3 | 0x8); //small
                outputStream.write(change);
            }


            switch (align) {
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(bill.getBytes());
            outputStream.write(PrinterCommands.LF);
        }catch(Exception ex){
            Log.e("error", ex.toString());
        }
    }

}

//mScan = (Button) findViewById(R.id.Scan);
//        mScan.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View mView) {
//                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                if (mBluetoothAdapter == null) {
//                    Toast.makeText(ProducerPrintOrder.this, "Message1", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (!mBluetoothAdapter.isEnabled()) {
//                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//                    } else {
//                        ListPairedDevices();
//                        Intent connectIntent = new Intent(ProducerPrintOrder.this,
//                                com.AgriBuhayProj.app.Printer.DeviceListActivity.class);
//                        startActivityForResult(connectIntent,
//                                REQUEST_CONNECT_DEVICE);
//                    }
//                }
//            }
//        });
//
//        mPrint = (Button) findViewById(R.id.mPrint);
//        mPrint.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View mView) {
//                Thread t = new Thread() {
//                    public void run() {
//                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chef").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                final Chef chef = snapshot.getValue(Chef.class);
//                                final String ProducerName  = chef.getFname() + " " + chef.getLname();
//                                try {
//                                    OutputStream os = mBluetoothSocket
//                                            .getOutputStream();
//                                    String BILL = "";
//
//                                    BILL = "                   XXXX MART    \n"
//                                            + "                   XX.AA.BB.CC.     \n " +
//                                            "                 NO 25 ABC ABCDE    \n" +
//                                            "                  XXXXX YYYYYY      \n" +
//                                            "                   MMM 590019091      \n";
//                                    BILL = BILL
//                                            + "-----------------------------------------------\n";
//
//
//                                    BILL = BILL + String.format("%1$-10s %2$10s %3$13s %4$10s", "Item", "Qty", "Rate", "Totel");
//                                    BILL = BILL + "\n";
//                                    BILL = BILL
//                                            + "-----------------------------------------------";
//                                    BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", ProducerName, "5", "10", "50.00");
//                                    BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-002", "10", "5", "50.00");
//                                    BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-003", "20", "10", "200.00");
//                                    BILL = BILL + "\n " + String.format("%1$-10s %2$10s %3$11s %4$10s", "item-004", "50", "10", "500.00");
//
//                                    BILL = BILL
//                                            + "\n-----------------------------------------------";
//                                    BILL = BILL + "\n\n ";
//
//                                    BILL = BILL + "                   Total Qty:" + "      " + "85" + "\n";
//                                    BILL = BILL + "                   Total Value:" + "     " + "700.00" + "\n";
//
//                                    BILL = BILL
//                                            + "-----------------------------------------------\n";
//                                    BILL = BILL + "\n\n ";
//                                    os.write(BILL.getBytes());
//                                    //This is printer specific code you can comment ==== > Start
//
//                                    // Setting height
//                                    int gs = 29;
//                                    os.write(intToByteArray(gs));
//                                    int h = 104;
//                                    os.write(intToByteArray(h));
//                                    int n = 162;
//                                    os.write(intToByteArray(n));
//
//                                    // Setting Width
//                                    int gs_width = 29;
//                                    os.write(intToByteArray(gs_width));
//                                    int w = 119;
//                                    os.write(intToByteArray(w));
//                                    int n_width = 2;
//                                    os.write(intToByteArray(n_width));
//
//
//                                } catch (Exception e) {
//                                    Log.e("ProducerPrintOrder", "Exe ", e);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                };
//                t.start();
//            }
//        });
//
//        mDisc = (Button) findViewById(R.id.dis);
//        mDisc.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View mView) {
//                if (mBluetoothAdapter != null)
//                    mBluetoothAdapter.disable();
//            }
//        });
