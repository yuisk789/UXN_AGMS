package kr.co.uxn.agms.android.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.atomic.AtomicReference;

@Database(entities = {kr.co.uxn.agms.android.data.room.UserData.class, SensorLog.class, kr.co.uxn.agms.android.data.room.SensorData.class, GlucoseData.class,
        kr.co.uxn.agms.android.data.room.CalibrationData.class, kr.co.uxn.agms.android.data.room.DeviceData.class, PatientData.class, EventData.class, AdminData.class}, version = kr.co.uxn.agms.android.data.room.RoomConstants.DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SensorLogDao sensorLogDao();
    public abstract SensorDataDao sensorDataDao();
    public abstract UserDataDao userDataDao();
    public abstract kr.co.uxn.agms.android.data.room.GlucoseDataDao glucoseDataDao();
    public abstract kr.co.uxn.agms.android.data.room.CalibrationDataDao calibrationDataDao();
    public abstract PatientDataDao patientDataDao();
    public abstract DeviceDataDao deviceDataDao();
    public abstract EventDataDao eventDataDao();
    public abstract kr.co.uxn.agms.android.data.room.AdminDataDao adminDataDao();

    private static final AtomicReference<AppDatabase> INSTANCE = new AtomicReference<>();

    static AppDatabase getDatabase(final Context context){
        if(INSTANCE.get() == null){
            synchronized (AppDatabase.class){
                if(INSTANCE.get() == null){
                    INSTANCE.set(Room.databaseBuilder(context.getApplicationContext()
                            , AppDatabase.class, kr.co.uxn.agms.android.data.room.RoomConstants.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build());
                }
            }
        }
        return INSTANCE.get();
    }

}
