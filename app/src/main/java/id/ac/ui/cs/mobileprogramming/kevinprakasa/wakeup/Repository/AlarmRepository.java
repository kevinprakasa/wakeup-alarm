package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Room.Alarm;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Room.AlarmDao;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Room.AlarmDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlarmRepository {
    private AlarmDao alarmDao;

    public AlarmRepository(Application application) {
        AlarmDatabase database = AlarmDatabase.getInstance(application);
        alarmDao = database.alarmDao();
    }

    public long insertAlarm(Alarm alarm) throws ExecutionException, InterruptedException {
        Long aLong = new InsertAlarmAsyncTask(alarmDao).execute(alarm).get();
        return aLong;
    }

    public void updateAlarm(Alarm alarm) {
        new UpdateAlarmAsyncTask(alarmDao).execute(alarm);
    }

    public void deleteAlarm(Alarm alarm) {
        new DeleteAlarmAsyncTask(alarmDao).execute(alarm);
    }

    // this method will be received defined daynum
    public LiveData<List<Alarm>> getAllAlarmFilteredByDay(int dayNum) {
        return alarmDao.getAlarmByDay(dayNum);
    }

    private static class InsertAlarmAsyncTask extends AsyncTask<Alarm,Void, Long> {
        private AlarmDao alarmDao;

        private InsertAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }

        @Override
        protected Long doInBackground(Alarm... alarms) {
            return alarmDao.insertAlarm(alarms[0]);
        }
    }

    private static class UpdateAlarmAsyncTask extends AsyncTask<Alarm,Void, Void> {
        private AlarmDao alarmDao;

        private UpdateAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }

        @Override
        protected Void doInBackground(Alarm... alarms) {
            alarmDao.updateAlarm(alarms[0]);
            return null;
        }
    }

    private static class DeleteAlarmAsyncTask extends AsyncTask<Alarm,Void, Void> {
        private AlarmDao alarmDao;

        private DeleteAlarmAsyncTask(AlarmDao alarmDao) {
            this.alarmDao = alarmDao;
        }

        @Override
        protected Void doInBackground(Alarm... alarms) {
            alarmDao.deleteAlarm(alarms[0]);
            return null;
        }
    }
}

