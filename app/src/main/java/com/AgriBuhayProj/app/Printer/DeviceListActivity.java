package com.AgriBuhayProj.app.Printer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.AgriBuhayProj.app.R;

import java.util.Set;

//CONNECT PHONE TO PRINTER VIA BLUETOOTH
public class DeviceListActivity extends Activity {
    //VARIABLES
    protected static final String TAG = "TAG";
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.devices);

        setResult(Activity.RESULT_CANCELED);

        //ADAPTER
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        //CONNECT XML
        ListView mPairedListView = findViewById(R.id.paired_devices);

        //SET ADAPTER
        mPairedListView.setAdapter(mPairedDevicesArrayAdapter);
        mPairedListView.setOnItemClickListener(mDeviceClickListener);

        //BLUETOOTH DEFAULT ADAPTER
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //GET CONNECTED DEVICES
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();

        //CHECK ARRAY LIST SIZE
        if (mPairedDevices.size() > 0) {
            //show device list
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);

            //list paired devices
            for (BluetoothDevice mDevice : mPairedDevices) {
                mPairedDevicesArrayAdapter.add(mDevice.getName() + "\n" + mDevice.getAddress());
            }
        } else {
            //no paired devices
            String mNoDevices = "None Paired";//getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(mNoDevices);
        }
    }

    //CANCEL DEVICE PAIRING
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    //CHOOSE DEVICE
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> mAdapterView, View mView, int mPosition, long mLong) {
            try {
                mBluetoothAdapter.cancelDiscovery();
                //get device info
                String mDeviceInfo = ((TextView) mView).getText().toString();
                //get device address
                String mDeviceAddress = mDeviceInfo.substring(mDeviceInfo.length() - 17);
                Log.v(TAG, "Device_Address " + mDeviceAddress);

                //attach device address
                Bundle mBundle = new Bundle();
                mBundle.putString("DeviceAddress", mDeviceAddress);

                //device connected
                Intent mBackIntent = new Intent();
                mBackIntent.putExtras(mBundle);
                setResult(Activity.RESULT_OK, mBackIntent);

                finish();
            } catch (Exception ex) {

            }
        }
    };

}
