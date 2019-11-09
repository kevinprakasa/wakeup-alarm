package com.kp.wakeup.Room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Calendar;

@Database(entities = {Alarm.class}, version=1)
public abstract class AlarmDatabase extends RoomDatabase {
    private static AlarmDatabase instance;
    public abstract AlarmDao alarmDao();

    public static synchronized AlarmDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AlarmDatabase.class,
                    "alarm_database"
                    ).fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
    return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private AlarmDao alarmDao;

        private PopulateDbAsyncTask(AlarmDatabase db) {
            alarmDao = db.alarmDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Calendar calendar = Calendar.getInstance();
            alarmDao.insertAlarm(new Alarm(calendar.getTimeInMillis(), 15, "some ringtone", 1, "alarm label"));
            alarmDao.insertAlarm(new Alarm(calendar.getTimeInMillis(), 15, "some ringtone", 1, "alarm label"));
            alarmDao.insertAlarm(new Alarm(calendar.getTimeInMillis(), 15, "some ringtone", 1, "alarm label"));
            return null;
        }
    }
}
