package kr.co.uxn.agms.android.data.room;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class SensorRepository {
    private kr.co.uxn.agms.android.data.room.SensorDataDao mSensorDataDao;
    private SensorLogDao mSensorLogDao;
    private UserDataDao mUserDao;
    private kr.co.uxn.agms.android.data.room.GlucoseDataDao mGlucoseDataDao;
    private kr.co.uxn.agms.android.data.room.CalibrationDataDao mCalibrationDataDao;
    private PatientDataDao mPatientDataDao;
    private kr.co.uxn.agms.android.data.room.DeviceDataDao mDeviceDataDao;
    private kr.co.uxn.agms.android.data.room.EventDataDao mEventDataDao;
    private kr.co.uxn.agms.android.data.room.AdminDataDao mAdminDataDao;

    public SensorRepository(Application application){
        this(application.getApplicationContext());
    }
    public SensorRepository(Context context){
        kr.co.uxn.agms.android.data.room.AppDatabase db = kr.co.uxn.agms.android.data.room.AppDatabase.getDatabase(context.getApplicationContext());
        mSensorDataDao = db.sensorDataDao();
        mSensorLogDao = db.sensorLogDao();
        mUserDao = db.userDataDao();
        mGlucoseDataDao = db.glucoseDataDao();
        mCalibrationDataDao = db.calibrationDataDao();
        mPatientDataDao = db.patientDataDao();
        mDeviceDataDao = db.deviceDataDao();
        mEventDataDao = db.eventDataDao();
        mAdminDataDao = db.adminDataDao();
    }


    public void createAdminData(kr.co.uxn.agms.android.data.room.AdminData data){
        new insertAdminDataAsyncTask(mAdminDataDao).execute(data);
    }
    public void updateAdminData(kr.co.uxn.agms.android.data.room.AdminData data){
        new updateAdminDataAsyncTask(mAdminDataDao).execute(data);
    }


    public kr.co.uxn.agms.android.data.room.AdminData getAdminData(String id){
        return mAdminDataDao.getData(id);
    }

    public LiveData<kr.co.uxn.agms.android.data.room.AdminData> getLastLiveAdminData(){
        return mAdminDataDao.getLiveDataLastUser();
    }


    public void createEventData(EventData data){
        new insertEventDataAsyncTask(mEventDataDao).execute(data);
    }



    public kr.co.uxn.agms.android.data.room.PatientData getPatientData(long number){
        return mPatientDataDao.getData(number);
    }

    public LiveData<kr.co.uxn.agms.android.data.room.PatientData> getLastUser(){
        return mPatientDataDao.getLastUser();
    }
    public LiveData<List<kr.co.uxn.agms.android.data.room.PatientData>> getAllPatient(){
        return mPatientDataDao.getAll();
    }

    public List<kr.co.uxn.agms.android.data.room.PatientData> getAllPatientList(){
        return mPatientDataDao.getAllList();
    }

    public void createPatient(String name, long number){
        kr.co.uxn.agms.android.data.room.PatientData patientData = new kr.co.uxn.agms.android.data.room.PatientData(0,name,number,System.currentTimeMillis(), System.currentTimeMillis(), 0);
        new insertPatientAsyncTask(mPatientDataDao).execute(patientData);
    }

    public LiveData<kr.co.uxn.agms.android.data.room.SensorLog> getLastLog(){
        return mSensorLogDao.getLastData();
    }
    public LiveData<kr.co.uxn.agms.android.data.room.SensorLog> getSensorCurrentState(String address){
        return mSensorLogDao.getCurrentState(address);
    }

    public LiveData<kr.co.uxn.agms.android.data.room.GlucoseData> getLastGlucoseData(){
        return mGlucoseDataDao.getLastData();
    }



    public LiveData<List<GlucoseData>> getGlucoseDataList(String address, long start, long end){
        return mGlucoseDataDao.getDataList(address, start,end);
    }
    public LiveData<List<GlucoseData>> getGlucoseDataList(long patientNumber, long start, long end){
        return mGlucoseDataDao.getDataList(patientNumber, start,end);
    }
    public LiveData<List<GlucoseData>> getGlucoseDataAllList(long patientNumber){
        return mGlucoseDataDao.getAllDataList(patientNumber);
    }
    public List<GlucoseData> getGlucoseAllDataArrayList(long patientNumber){
        return mGlucoseDataDao.getGlucoseAllDataArrayList(patientNumber);
    }

    public LiveData<List<EventData>> getEventDataAllList(long patientNumber){
        return mEventDataDao.getLiveData(patientNumber);
    }

    public List<EventData> getEventDataAllArrayList(long patientNumber){
        return mEventDataDao.getAllList(patientNumber);
    }

    public void insertSensorData(SensorData sensorData){
        new insertSensorDataAsyncTask(mSensorDataDao).execute(sensorData);
    }
    public void insertSensorLog(SensorLog sensorLog){
        new insertSensorLogAsyncTask(mSensorLogDao).execute(sensorLog);
    }
    public void insertGluecoseData(GlucoseData glucoseData){
        new insertGluecoseDataAsyncTask(mGlucoseDataDao).execute(glucoseData);
    }


    private static class insertSensorDataAsyncTask extends AsyncTask<SensorData, Void, Void> {
        private SensorDataDao mAsyncTaskDao;

        public insertSensorDataAsyncTask(SensorDataDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(SensorData... sensorData) {
            mAsyncTaskDao.insert(sensorData[0]);

            return null;
        }
    }

    private static class insertGluecoseDataAsyncTask extends AsyncTask<GlucoseData, Void, Void> {
        private GlucoseDataDao mAsyncTaskDao;

        public insertGluecoseDataAsyncTask(GlucoseDataDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(GlucoseData... sensorData) {
            mAsyncTaskDao.insert(sensorData[0]);
            return null;
        }
    }
    private static class insertSensorLogAsyncTask extends AsyncTask<SensorLog, Void, Void> {
        private SensorLogDao mAsyncTaskDao;

        public insertSensorLogAsyncTask(SensorLogDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(SensorLog... sensorData) {
            mAsyncTaskDao.insert(sensorData[0]);

            return null;
        }
    }


    private static class insertPatientAsyncTask extends AsyncTask<PatientData, Void, Void> {
        private PatientDataDao mAsyncTaskDao;

        public insertPatientAsyncTask(PatientDataDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(PatientData... data) {
            mAsyncTaskDao.insert(data[0]);

            return null;
        }
    }

    private static class insertEventDataAsyncTask extends AsyncTask<EventData, Void, Void> {
        private EventDataDao mAsyncTaskDao;

        public insertEventDataAsyncTask(EventDataDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(EventData... data) {
            mAsyncTaskDao.insert(data[0]);

            return null;
        }
    }

    private static class insertAdminDataAsyncTask extends AsyncTask<AdminData, Void, Void> {
        private AdminDataDao mAsyncTaskDao;

        public insertAdminDataAsyncTask(AdminDataDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(AdminData... data) {
            mAsyncTaskDao.insert(data[0]);
            return null;
        }
    }

    private static class updateAdminDataAsyncTask extends AsyncTask<AdminData, Void, Void> {
        private AdminDataDao mAsyncTaskDao;

        public updateAdminDataAsyncTask(AdminDataDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }

        @Override
        protected Void doInBackground(AdminData... data) {
            mAsyncTaskDao.update(data[0]);
            return null;
        }
    }
}
