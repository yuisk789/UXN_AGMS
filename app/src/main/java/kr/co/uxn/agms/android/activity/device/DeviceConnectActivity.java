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
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.md.CommonUtil;
import com.google.mlkit.md.LiveBarcodeScanningActivity;

import kr.co.uxn.agms.android.CommonConstant;
import kr.co.uxn.agms.android.MainActivity;
import kr.co.uxn.agms.android.R;
import kr.co.uxn.agms.android.databinding.ActivityDeviceConnectBinding;
import kr.co.uxn.agms.android.service.BluetoothService;
import kr.co.uxn.agms.android.service.BluetoothUtil;

public class DeviceConnectActivity extends AppCompatActivity {
    private ActivityDeviceConnectBinding binding;
    private static final int REQUEST_BARCODE = 1;
    private String mDeviceString = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        binding = ActivityDeviceConnectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mLayout = binding.getRoot();
        binding.buttonDoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                },200);
                manualInput();
            }
        });
        binding.buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setClickable(true);
                    }
                },200);
                doQRCodeScan();
            }
        });
        if(!checkPermissions()){
            Log.e(TAG,"2");
            requestPermission();

        }
    }
    private void doQRCodeScan(){
        Intent intent = new Intent(this, LiveBarcodeScanningActivity.class);
        startActivityForResult(intent,REQUEST_BARCODE);
    }
    private void manualInput(){
        Intent intent = new Intent(this,ManualInputActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_BARCODE){
            if(resultCode== Activity.RESULT_OK){
                Toast.makeText(this,data.getStringExtra(CommonUtil.RAW_VALUE), Toast.LENGTH_SHORT).show();
                mDeviceString = data.getStringExtra(CommonUtil.RAW_VALUE);
                Intent intent = new Intent(this,ManualInputActivity.class);
                intent.putExtra("address",intent);
                startActivity(intent);
            }
        }
    }


    private static final String TAG = "MainActivity";

    private boolean waitForConnect = false;
    private BluetoothService mBluetoothService = null;
    private boolean mBluetoothBound = false;
    int permissionEvent =0;
    private View mLayout;
    private Handler mHandler = null;

    private static final int PERMISSION_BLUETOOTH = 1;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_BLUETOOTH) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(!mBluetoothBound){

                }
            } else {
                // Permission request was denied.
                Toast.makeText(this, R.string.permission_error_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermissions() {

        boolean location = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_ADMIN);
        return location ;

    }

    private void requestPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.BLUETOOTH_ADMIN)) {

            Snackbar.make(mLayout, R.string.permission_error_message,
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(DeviceConnectActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            PERMISSION_BLUETOOTH);
                }
            }).show();

        } else {
            Snackbar.make(mLayout, R.string.permission_error_message, Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_BLUETOOTH);
        }
    }

}
