package kr.co.uxn.agms.android.activity.device;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.mlkit.md.CommonUtil;

import kr.co.uxn.agms.android.CommonConstant;
import kr.co.uxn.agms.android.databinding.ActivityDeviceListBinding;
import kr.co.uxn.agms.android.databinding.ActivityManualInputBinding;
import kr.co.uxn.agms.android.service.BluetoothService;
import kr.co.uxn.agms.android.ui.BleActivity;

public class DeviceListActivity extends BleActivity {

    ActivityDeviceListBinding binding;
    RecyclerView recyclerView;
    BleDeviceAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeviceListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        recyclerView = binding.recyclerView;
        mAdapter = new BleDeviceAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setListener(new BleDeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, BleAdapterItem obj, int position) {
                setResult(Activity.RESULT_OK);
                Intent intent = new Intent();
                intent.putExtra(CommonUtil.RAW_VALUE, obj.getUuid());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        doScan();
    }


    @Override
    public void doWhenConnectFail() {
        if(!isFinishing()){
            doScan();
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void doWhenDeviceFound(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothService.EXTRA_DEVICE);


        if(device!=null){
            int rssi = intent.getIntExtra(BluetoothService.EXTRA_RSSI,0);
            Log.e("check","device:"+device.getName()+"/"+device.getAddress());
            BleAdapterItem item = new BleAdapterItem(device.getAddress(),
                    device.getName(), rssi);
            mAdapter.addDeviceItem(item);
        }
    }

    @Override
    public void doWhenDeviceConnected() {
        finish();
    }

    @Override
    public void doWhenDeviceDisconnected() {
        finish();
    }

}
