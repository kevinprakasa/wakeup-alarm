package com.kp.wakeup.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kp.wakeup.Repository.AlarmHistoryRepository;
import com.kp.wakeup.Room.AlarmHistory;

import java.util.List;

public class AlarmHistoryViewModel extends AndroidViewModel {
    private AlarmHistoryRepository repository;
    private LiveData<List<AlarmHistory>> allAlarmHistories;

    public AlarmHistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new AlarmHistoryRepository(application);
        allAlarmHistories = repository.getAlarmHistories();
    }

    public void insert(AlarmHistory alarm){repository.insertAlarmHistory(alarm);}

    public LiveData<List<AlarmHistory>> getAllAlarmHistories() {
        return allAlarmHistories;
    }
}
