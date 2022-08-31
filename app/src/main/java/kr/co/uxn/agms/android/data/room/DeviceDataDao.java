package kr.co.uxn.agms.android.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DeviceDataDao {
    @Query("SELECT * from device_data where address = :deviceAddress order by uid desc limit 1")
    LiveData<kr.co.uxn.agms.android.data.room.DeviceData> getLiveData(String deviceAddress);

    @Query("SELECT * from device_data where address = :deviceAddress order by uid desc limit 1")
    kr.co.uxn.agms.android.data.room.DeviceData getData(String deviceAddress);

    @Insert
    void insert(kr.co.uxn.agms.android.data.room.DeviceData dat);

    @Update
    void update(kr.co.uxn.agms.android.data.room.DeviceData data);

    @Delete
    void delete(kr.co.uxn.agms.android.data.room.DeviceData data);
}
