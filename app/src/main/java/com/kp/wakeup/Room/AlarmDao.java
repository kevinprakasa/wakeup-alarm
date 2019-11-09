package com.kp.wakeup.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlarmDao {
    @Query("SELECT * FROM alarms")
    LiveData<List<Alarm>>getAllAlarm();

    @Query("SELECT * FROM alarms WHERE day=:dayCode")
    LiveData<List<Alarm>> getAlarmByDay(int dayCode);

    @Insert
    long insertAlarm(Alarm alarm);

    @Update
    void updateAlarm(Alarm alarm);

    @Delete
    void deleteAlarm(Alarm alarm);

}
