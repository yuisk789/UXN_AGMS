package kr.co.uxn.agms.android.ui.setting;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import kr.co.uxn.agms.android.CommonConstant;
import kr.co.uxn.agms.android.MainActivity;
import kr.co.uxn.agms.android.R;
import kr.co.uxn.agms.android.data.room.EventData;
import kr.co.uxn.agms.android.data.room.GlucoseData;
import kr.co.uxn.agms.android.data.room.PatientData;
import kr.co.uxn.agms.android.data.room.SensorRepository;
import kr.co.uxn.agms.android.ui.BaseFragment;
import kr.co.uxn.agms.android.ui.connect.WarmupActivity;


public class SettingFragment extends BaseFragment {



    Button buttonNewUser;
    Button buttonNewDevice;
    Button buttonNewSensor;
    Button buttonSave;
    Button buttonCancel;

    TextView adminId;
    TextView deviceSerial;
    TextView deviceBattery;
    TextView deviceSensorDate;

    private SensorRepository mRepository;

    private void setAdminData(){
        String dataAdminId = "";
        String dataDeviceMac = "";
        String dataDeviceBattery = "";
        String dataSensorDate = "";

        try {
            MainActivity activity = (MainActivity)getActivity();
            dataAdminId = activity.getAdminId();
            dataDeviceMac = activity.getDeviceMac();
            dataDeviceBattery = activity.getDeviceBattery();
            dataSensorDate = activity.getSensorDate();
        }catch (Exception e){}

        if(adminId!=null){
            adminId.setText(getString(R.string.setting_data_admin_id, dataAdminId));
        }
        if(deviceSerial !=null){
            deviceSerial.setText(getString(R.string.setting_data_device, dataDeviceMac));
        }
        if(deviceBattery !=null){
            deviceBattery.setText(getString(R.string.setting_data_device_battery, dataDeviceBattery));
        }
        if(deviceSensorDate !=null){
            deviceSensorDate.setText(getString(R.string.setting_data_sensor_date, dataSensorDate));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setAdminData();
        if(fileSavingNow.get()){
            showProgress();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        dismissProgress();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRepository = new SensorRepository(getActivity().getApplication());
        View root = null;
        if(CommonConstant.MODE_IS_MEDICAL){
            root = inflater.inflate(R.layout.fragment_admin_setting, container, false);
        } else {
            root = inflater.inflate(R.layout.fragment_new_setting, container, false);
        }
        adminId = root.findViewById(R.id.admin_id);
        deviceSerial = root.findViewById(R.id.device_serial);
        deviceBattery = root.findViewById(R.id.device_battery);
        deviceSensorDate = root.findViewById(R.id.device_sensor_date);


        buttonNewUser = root.findViewById(R.id.button_new_user);
        buttonNewDevice = root.findViewById(R.id.button_new_device);
        buttonNewSensor = root.findViewById(R.id.button_new_sensor);
        buttonSave = root.findViewById(R.id.button_save);
        buttonCancel = root.findViewById(R.id.button_cancel);

        buttonNewUser.setOnClickListener(view -> {
            buttonNewUser.setEnabled(false);
            buttonNewUser.postDelayed(() -> buttonNewUser.setEnabled(true),1000);
            doNewUser();
        });
        buttonNewDevice.setOnClickListener(view -> {
            buttonNewDevice.setEnabled(false);
            buttonNewDevice.postDelayed(() -> buttonNewDevice.setEnabled(true),1000);
            doNewDevice();
        });
        buttonNewSensor.setOnClickListener(view -> {
            buttonNewSensor.setEnabled(false);
            buttonNewSensor.postDelayed(() -> buttonNewSensor.setEnabled(true),1000);
            clickNewSensor();
        });

        buttonSave.setOnClickListener(view -> {
            buttonSave.setEnabled(false);
            buttonSave.postDelayed(() -> buttonSave.setEnabled(true),1000);
            doSave();
        });
        buttonCancel.setOnClickListener(view -> {
            buttonCancel.setEnabled(false);
            buttonCancel.postDelayed(() -> buttonCancel.setEnabled(true),1000);
            doOff();
        });

//        Intent intent;
//        intent = new Intent(getContext(), LoginAdminActivity.class);
//        startActivityForResult(intent,CommonConstant.REQUEST_CODE_CHECK_PASSWORD );


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CommonConstant.REQUEST_CODE_CHECK_PASSWORD){
            if(resultCode!= Activity.RESULT_OK){
                try {
                    ((MainActivity)getActivity()).goTabHome();
                }catch (Exception e){}
            }
        } else if(requestCode == CommonConstant.REQUEST_CREATE_FILE_FOR_DATA){
            if(resultCode == Activity.RESULT_OK){
                savePatientDataFile(data.getData());
            } else {
                createPatientEventFile();
            }
        } else if(requestCode == CommonConstant.REQUEST_CREATE_FILE_FOR_EVENT){

            if(resultCode == Activity.RESULT_OK){
                savePatientEventFile(data.getData());
            } else {
                createPatientDataFile();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CommonConstant.REQUEST_CODE_PERMISSION_WRITE) {
            boolean writeGranted = false;
            if(grantResults.length == permissions.length){
                for(int i=0;i<permissions.length;i++){
                    int grantResult = grantResults[i];
                    String permission = permissions[i];
                    if(permission.equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        if(grantResult==PackageManager.PERMISSION_GRANTED){
                            writeGranted = true;
                        }
                    }
                }
            }
            if (writeGranted) {
                doSave();
            } else {

                Toast.makeText(getContext(), R.string.write_permission_error_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void doSave(){
        if(fileSavingNow.get()){
            Toast.makeText(getContext(), R.string.file_save_now,Toast.LENGTH_SHORT).show();
            return;
        }
        fileSavingNow.set(true);
        mSaveIndex = -1;
        mPatientList = new ArrayList<>();

        showProgress();

        new GetPatientTask().execute();
        /*if(checkPermissions()){
            if(fileSavingNow.get()){
                Toast.makeText(getContext(), R.string.file_save_now,Toast.LENGTH_SHORT).show();
                return;
            }
            fileSavingNow.set(true);
            mSaveIndex = -1;
            mPatientList = new ArrayList<>();

            showProgress();

            new GetPatientTask().execute();
        } else {
            requestPermission();
        } */
   }
   private void showProgress(){
       try{
           ((MainActivity)getActivity()).showActivityProgress();
       }catch (Exception e){}
   }
   private void dismissProgress(){
       try{
           ((MainActivity)getActivity()).dismissActivityProgress();
       }catch (Exception e){}
   }

   private void clickNewSensor(){
        new AlertDialog.Builder(getContext())
                .setTitle("알림")
                .setMessage("새로운 센서를 연결하였습니까? Warm-up을 진행합니다.")
                .setPositiveButton("Warm-up", (dialogInterface, i) -> doNewSensor())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
   }

   private void doNewSensor(){
       PreferenceManager.getDefaultSharedPreferences(getContext())
               .edit()
               .putLong(CommonConstant.PREF_WARM_UP_START_DATE,System.currentTimeMillis())
               .putLong(CommonConstant.PREF_DEVICE_NEW_SENSOR_DATE,System.currentTimeMillis())
               .apply();

       Intent intent = new Intent(getContext(), WarmupActivity.class);
       startActivity(intent);
   }
   private void doNewUser(){
       new AlertDialog.Builder(getContext())
               .setTitle(R.string.title_dialog_in_progress)
               .setMessage(R.string.dialog_message_new_user)
               .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                   dialogInterface.dismiss();
                   createNewUserRequested.set(true);
                   doSave();

               })
               .setNegativeButton(android.R.string.cancel, null)
               .show();
   }
   private void doNewDevice(){

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_dialog)
                .setMessage(R.string.dialog_message_new_device)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    resetAndGoNewDevice();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();


   }
   private void doAppOff(){
       PreferenceManager.getDefaultSharedPreferences(getContext())
               .edit()
               .putString(CommonConstant.PREF_LAST_CONNECT_DEVICE, null)
               .putLong(CommonConstant.PREF_WARM_UP_START_DATE,0)
               .putLong(CommonConstant.PREF_DEVICE_FIRST_PAIRING_DATE,0)
               .putLong(CommonConstant.PREF_DEVICE_NEW_SENSOR_DATE,0)
               .putLong(CommonConstant.PREF_CURRENT_PATIENT_NUMBER,0)
               .putString(CommonConstant.PREF_CURRENT_PATIENT_NAME, null)
               .apply();

       try{
           ((MainActivity)getActivity()).doOff();
       }catch (Exception e){}
       getActivity().finishAffinity();

//       Intent intent;
//       if(CommonConstant.MODE_IS_MEDICAL){
//           intent = new Intent(getContext(), CreatePatientActivity.class);
//       } else {
//           intent = new Intent(getContext(), LoginActivity.class);
//           intent.putExtra(LoginActivity.ARGS_IS_CREATE_USER, true);
//       }
//       startActivity(intent);
   }

   private void createNewUser(){
       PreferenceManager.getDefaultSharedPreferences(getContext())
               .edit()
               .putString(CommonConstant.PREF_LAST_CONNECT_DEVICE, null)
               .putLong(CommonConstant.PREF_WARM_UP_START_DATE,0)
               .putLong(CommonConstant.PREF_DEVICE_FIRST_PAIRING_DATE,0)
               .putLong(CommonConstant.PREF_DEVICE_NEW_SENSOR_DATE,0)
               .putLong(CommonConstant.PREF_CURRENT_PATIENT_NUMBER,0)
               .putString(CommonConstant.PREF_CURRENT_PATIENT_NAME, null)
               .apply();

       try{
           ((MainActivity)getActivity()).disconnectAndReset();
       }catch (Exception e){}
       getActivity().finishAffinity();
       Intent intent;
//       intent = new Intent(getContext(), CreatePatientActivity.class);
//       startActivity(intent);


   }
   private void resetAndGoNewDevice(){


       try {
           ((MainActivity)getActivity()).disconnectAndReset();
       }catch (Exception e){}


   }

   private void doOff(){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.alert_title)
                .setMessage(R.string.dialog_app_off_confirm)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    appOffRequested.set(true);
                    doSave();
                }).show();



   }



    private void requestPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(),R.string.write_permission_error_message, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(),R.string.permission_error_message, Toast.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
        }
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                CommonConstant.REQUEST_CODE_PERMISSION_WRITE);
    }
    private boolean checkPermissions() {

        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }


    final AtomicInteger glucoseSaveCount = new AtomicInteger(0);
    final AtomicInteger eventSaveCount = new AtomicInteger(0);
    final AtomicBoolean requestGlucoseSave = new AtomicBoolean(false);
    final AtomicBoolean requestEventSave = new AtomicBoolean(false);
    final AtomicBoolean fileSavingNow = new AtomicBoolean(false);
    final AtomicBoolean appOffRequested = new AtomicBoolean(false);
    final AtomicBoolean createNewUserRequested = new AtomicBoolean(false);
    SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");






    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();

    }

    private List<PatientData> mPatientList;

    private int mSaveIndex;

    class GetPatientTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            mPatientList = mRepository.getAllPatientList();

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            createPatientDataFile();
        }
    }

    private void createPatientEventFile(){

        SimpleDateFormat formatter = new SimpleDateFormat("MMdd_HHmm", Locale.KOREA);
        String time = formatter.format(new Date());
        PatientData patientData = mPatientList.get(mSaveIndex);
        String fileName = patientData.getName()+"_"+patientData.getPatientNumber()+"_event_"+time+".csv";
        createFile(null, fileName, CommonConstant.REQUEST_CREATE_FILE_FOR_EVENT);
    }

    private void createPatientDataFile(){

        mSaveIndex++;
        if(mPatientList==null || mPatientList.isEmpty() || (mSaveIndex >= mPatientList.size())){
            doWhenSaveOver();
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MMdd_HHmm", Locale.KOREA);
        String time = formatter.format(new Date());
        PatientData patientData = mPatientList.get(mSaveIndex);
        String fileName = patientData.getName()+"_"+patientData.getPatientNumber()+"_glucose_"+time+".csv";
        createFile(null, fileName, CommonConstant.REQUEST_CREATE_FILE_FOR_DATA);
    }
    private void savePatientDataFile(Uri uri){

        new SavePatientDataTask(uri).execute();
    }
    private void savePatientEventFile(Uri uri){

        new SavePatientEventTask(uri).execute();
    }

    class SavePatientDataTask extends AsyncTask<Void, Void, Void> {
        Uri mUri;
        public SavePatientDataTask(Uri uri){
            mUri = uri;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            PatientData patientData = mPatientList.get(mSaveIndex);

            List<GlucoseData>glucoseDataList = mRepository.getGlucoseAllDataArrayList(patientData.getPatientNumber());

            OutputStream file = null;
            OutputStreamWriter opStream = null;
            BufferedOutputStream buffer = null;
            try {
                ContentResolver contentResolver = getContext().getContentResolver();
                file = contentResolver.openOutputStream(mUri);
                buffer = new BufferedOutputStream(file);
                opStream = new OutputStreamWriter(buffer);

                // Write File
                String COMMA = ", ";
                String END = "\n";
                opStream.write("patient_name" + COMMA + "patient_number"+ COMMA + "device_address" + COMMA +
                        "date" + COMMA + "current" + COMMA + "battery_level" + COMMA + END);
                if(glucoseDataList!=null && glucoseDataList.size()>0){
                    for(int i=0 ; i<glucoseDataList.size() ; i++){
                        GlucoseData data = glucoseDataList.get(i);
                        opStream.write(data.getUser() + COMMA + data.getPatientNumber() + COMMA
                                + data.getDeviceAddress() + COMMA + fullDateFormat.format(new Date(data.getData_date()))  + COMMA
                                + data.getWe_current() + COMMA + data.getBatteryLevel()  + END);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if(opStream!=null){
                        opStream.close();
                    }
                }catch (Exception e){}
                try {
                    if(buffer!=null){
                        buffer.close();
                    }

                }catch (Exception e){}
                try {
                    if(file!=null){
                        file.close();
                    }
                }catch (Exception e){}
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            createPatientEventFile();
        }
    }
    class SavePatientEventTask extends AsyncTask<Void, Void, Void> {
        Uri mUri;

        public SavePatientEventTask(Uri uri) {
            mUri = uri;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            PatientData patientData = mPatientList.get(mSaveIndex);

            List<EventData> eventDataList = mRepository.getEventDataAllArrayList(patientData.getPatientNumber());

            OutputStream file2 = null;
            BufferedOutputStream buffer2 = null;
            OutputStreamWriter opStream2 = null;
            try {

                ContentResolver contentResolver = getContext().getContentResolver();
                file2 = contentResolver.openOutputStream(mUri);
                buffer2 = new BufferedOutputStream(file2);
                opStream2 = new OutputStreamWriter(buffer2);

                // Write File
                String COMMA = ", ";
                String END = "\n";
                opStream2.write("patient_name" + COMMA + "patient_number"+ COMMA +
                        "date" + COMMA + "event" + COMMA + "glucose"  + END);
                if(eventDataList!=null && !eventDataList.isEmpty()){
                    for(int i=0 ; i<eventDataList.size() ; i++){
                        EventData data = eventDataList.get(i);
                        opStream2.write(data.getPatientName() + COMMA + data.getPatientNumber() + COMMA
                                + fullDateFormat.format(new Date(data.getEventDate()))  + COMMA + data.getEventContent() + COMMA
                                + data.getEventGlucose()  + END);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if(opStream2!=null){
                        opStream2.close();
                    }
                }catch (Exception e){}
                try {
                    if(buffer2!=null){
                        buffer2.close();
                    }

                }catch (Exception e){}
                try {
                    if(file2!=null){
                        file2.close();
                    }
                }catch (Exception e){}
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            createPatientDataFile();
        }
    }

    private void doWhenSaveOver(){

        fileSavingNow.set(false);
        if(getActivity()==null || isRemoving()){
            return;
        }
        dismissProgress();
        if(appOffRequested.get()){
            doAppOff();
        }
        if(createNewUserRequested.get()){
            createNewUser();
        }
    }


    private void createFile(Uri pickerInitialUri, String title, int request) {

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
        startActivityForResult(intent, request);
    }


}