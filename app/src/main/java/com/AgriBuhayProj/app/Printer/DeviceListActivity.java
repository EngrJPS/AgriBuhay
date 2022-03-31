package com.AgriBuhayProj.app.Printer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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


public class DeviceListActivity extends Activity {
    protected static final String TAG = "TAG";
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private static PrinterSelectedListener printerSelectedListener;

    @Override
    protected void onCreate(Bundle mSavedInstanceState) {
        super.onCreate(mSavedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.devices);


        newConnection();
    }

    private void newConnection() {
        try{
            setResult(Activity.RESULT_CANCELED);
            mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

            ListView mPairedListView = (ListView) findViewById(R.id.paired_devices);
            mPairedListView.setAdapter(mPairedDevicesArrayAdapter);
            mPairedListView.setOnItemClickListener(mDeviceClickListener);

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
                for (BluetoothDevice mDevice : mPairedDevices) {
                    mPairedDevicesArrayAdapter.add(mDevice.getName() + "\n" + mDevice.getAddress());
                }
            } else {
                String mNoDevices = "None Paired";//getResources().getText(R.string.none_paired).toString();
                mPairedDevicesArrayAdapter.add(mNoDevices);
            }
        } catch (Exception e) {
            Log.e("exception",e.toString());
        }
    }


    public static void setPrinterSelectedListener(PrinterSelectedListener printerSelectedListener_) {
        printerSelectedListener = printerSelectedListener_;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> mAdapterView, View mView, int mPosition, long mLong) {
            try {
                mBluetoothAdapter.cancelDiscovery();
                String mDeviceInfo = ((TextView) mView).getText().toString();
                String mDeviceAddress = mDeviceInfo.substring(mDeviceInfo.length() - 17);
                String mDeviceName = mDeviceInfo.substring(0, mDeviceInfo.length()- 17);
                Log.v(TAG, "Device_Address " + mDeviceAddress);

                Bundle mBundle = new Bundle();
                mBundle.putString("DeviceAddress", mDeviceAddress);

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("bluetooth_printer", mDeviceAddress); //
                editor.putString("bluetooth_name", mDeviceName);
                editor.apply();

                if(printerSelectedListener != null)
                {
                    printerSelectedListener.onPrinterSelected(mDeviceName);
                }

                Intent mBackIntent = new Intent();
                mBackIntent.putExtras(mBundle);
                setResult(Activity.RESULT_OK, mBackIntent);
                finish();
            } catch (Exception ex) {

            }
        }
    };

}
