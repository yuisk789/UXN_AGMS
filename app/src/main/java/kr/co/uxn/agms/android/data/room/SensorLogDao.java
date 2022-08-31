package kr.co.uxn.agms.android.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SensorLogDao {
    @Query("SELECT * from sensor_log where address = :deviceAddress order by uid desc limit 1")
    LiveData<kr.co.uxn.agms.android.data.room.SensorLog> getCurrentState(String deviceAddress);

    @Query("SELECT * from sensor_log order by uid desc limit 1")
    LiveData<kr.co.uxn.agms.android.data.room.SensorLog> getLastData();

    @Insert
    void insert(kr.co.uxn.agms.android.data.room.SensorLog log);

    @Update
    void update(kr.co.uxn.agms.android.data.room.SensorLog log);

    @Delete
    void delete(kr.co.uxn.agms.android.data.room.SensorLog log);
}
