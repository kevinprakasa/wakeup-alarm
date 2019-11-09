package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {AlarmHistory.class}, version=1)
public abstract class AlarmHistoryDatabase extends RoomDatabase {
    private static AlarmHistoryDatabase instance;
    public abstract AlarmHistoryDao alarmHistoryDao();

    public static synchronized AlarmHistoryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AlarmHistoryDatabase.class,
                    "alarm_history_database"
            ).fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }

}
