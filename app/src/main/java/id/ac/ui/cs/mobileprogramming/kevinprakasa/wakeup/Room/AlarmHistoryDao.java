package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AlarmHistoryDao {
    @Query("SELECT * FROM alarmhistories")
    LiveData<List<AlarmHistory>> getAllAlarmHistory();

    @Insert
    void insertAlarmHistory(AlarmHistory alarmHistory);

}
