package com.kp.wakeup.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kp.wakeup.Room.AlarmHistory;
import com.kp.wakeup.Room.AlarmHistoryDao;
import com.kp.wakeup.Room.AlarmHistoryDatabase;

import java.util.List;

public class AlarmHistoryRepository {
    private AlarmHistoryDao alarmHistoryDao;

    public AlarmHistoryRepository(Application application) {
        AlarmHistoryDatabase database = AlarmHistoryDatabase.getInstance(application);
        alarmHistoryDao = database.alarmHistoryDao();
    }

    public void insertAlarmHistory(AlarmHistory alarm) { new InsertAlarmHistoryAsyncTask(alarmHistoryDao).execute(alarm);}

    public LiveData<List<AlarmHistory>> getAlarmHistories() {
        return alarmHistoryDao.getAllAlarmHistory();
    }


    private static class InsertAlarmHistoryAsyncTask extends AsyncTask<AlarmHistory,Void, Void> {
        private AlarmHistoryDao alarmHistoryDao;

        private InsertAlarmHistoryAsyncTask(AlarmHistoryDao alarmDao) {
            this.alarmHistoryDao = alarmDao;
        }

        @Override
        protected Void doInBackground(AlarmHistory... alarms) {
            alarmHistoryDao.insertAlarmHistory(alarms[0]);
            return null;
        }
    }
}
