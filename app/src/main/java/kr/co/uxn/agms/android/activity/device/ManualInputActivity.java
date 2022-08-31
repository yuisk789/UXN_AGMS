package kr.co.uxn.agms.android.activity.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.md.CommonUtil;

import kr.co.uxn.agms.android.CommonConstant;
import kr.co.uxn.agms.android.R;
import kr.co.uxn.agms.android.databinding.ActivityDeviceConnectBinding;
import kr.co.uxn.agms.android.databinding.ActivityManualInputBinding;
import kr.co.uxn.agms.android.service.BluetoothService;
import kr.co.uxn.agms.android.service.BluetoothUtil;
import kr.co.uxn.agms.android.ui.BleActivity;

public class ManualInputActivity extends BleActivity {
    private static final int REQUEST_DEVICE_LIST = 10824;
    ActivityManualInputBinding binding;
    private String mDeviceString = null;
    EditText macaddress;
    MaterialButton connect;
    MaterialButton deviceList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManualInputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        macaddress = binding.editTextMac;
        connect = binding.buttonConnect;
        deviceList = binding.buttonDeviceList;
        deviceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                },200);
                runDeviceList();
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                },200);
                if(TextUtils.isEmpty(macaddress.getText())){
                    Toast.makeText(ManualInputActivity.this,"일련번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    runDeviceConnect();
                }

            }
        });
        try {
            String tmp = getIntent().getStringExtra("address");
            if(tmp!=null){
                macaddress.setText(tmp);
                connect.performClick();
            }
        }catch (Exception e){}
    }
    private void runDeviceList(){
        Intent intent = new Intent(this,DeviceListActivity.class);
        startActivityForResult(intent,REQUEST_DEVICE_LIST );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_DEVICE_LIST){
            if(resultCode== Activity.RESULT_OK){
                if(!TextUtils.isEmpty(data.getStringExtra(CommonUtil.RAW_VALUE))){
                    mDeviceString = data.getStringExtra(CommonUtil.RAW_VALUE);
                    macaddress.setText(mDeviceString);
                    runDeviceConnect();
                }

            }
        }
    }

    private boolean runDeviceConnect(){
        if(TextUtils.isEmpty(macaddress.getText())){
            Toast.makeText(this,"Invalid address",Toast.LENGTH_SHORT).show();
            return false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().putLong(CommonConstant.PREF_WARM_UP_START_DATE,0)
                .putLong(CommonConstant.PREF_DEVICE_NEW_SENSOR_DATE,System.currentTimeMillis())
                .putLong(CommonConstant.PREF_DEVICE_FIRST_PAIRING_DATE, System.currentTimeMillis())
                .apply();
        boolean result = false;
        try {
            result = doDeviceClicked(true,macaddress.getText().toString());

        }catch (Exception e){}
        if(result){

        }
        return result;
    }
    private void startScan(){
        doScan();
    }

    @Override
    public void doWhenConnectFail() {
        if(!isFinishing()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connect.setEnabled(true);
                }
            });
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void doWhenDeviceFound(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothService.EXTRA_DEVICE);


        if(device!=null){
            int rssi = intent.getIntExtra(BluetoothService.EXTRA_RSSI,0);
            Log.e("check","device:"+device.getName()+"/"+device.getAddress());
            AdapterItem item = new AdapterItem(rssi,device.getName(), device.getAddress());
//            mAdapter.addScanResult(item);
//            if(TextUtils.isEmpty(editText.getText())){
//                editText.setText(device.getAddress());
//            }

        }
    }

    @Override
    public void doWhenDeviceConnected() {

        macaddress.setText(mBluetoothService.getConnectedAddress());
        connect.setEnabled(true);
        checkWarmup();
    }

    public void checkWarmup(){
        long warmUpStartDate = PreferenceManager.getDefaultSharedPreferences(this)
                .getLong(CommonConstant.PREF_WARM_UP_START_DATE,0);
        long currentTime = System.currentTimeMillis();
        if(mBluetoothService!=null && mBluetoothService.isDeviceConnected()){

            if(warmUpStartDate==0){
                goWarmup();

            } else {
                if(currentTime - warmUpStartDate < CommonConstant.WARM_UP_DELAY){
                    goWarmup();
                } else {
                    goHome();
                }
            }
        } else {

        }


    }

    @Override
    public void doWhenDeviceDisconnected() {
        checkWarmup();
    }

    static class AdapterItem {

        final String uuid;
        final String name;
        final int rssi;

        AdapterItem(int rssi, String name, String uuid) {
            this.name = name;
            this.uuid = uuid;
            this.rssi = rssi;
        }
    }

}
