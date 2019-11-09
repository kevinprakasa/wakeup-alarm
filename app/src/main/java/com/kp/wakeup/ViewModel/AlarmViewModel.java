package com.kp.wakeup.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.kp.wakeup.Repository.AlarmRepository;
import com.kp.wakeup.Room.Alarm;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AlarmViewModel extends AndroidViewModel {
    private AlarmRepository repository;
    private MutableLiveData<Integer> currentChoosedDayNum = new MutableLiveData<>();
    private LiveData<List<Alarm>> allAlarmByDay;


    public AlarmViewModel(@NonNull Application application) {
        super(application);
        repository = new AlarmRepository(application);

        allAlarmByDay = Transformations.switchMap(currentChoosedDayNum,
                new Function<Integer, LiveData<List<Alarm>>>() {
                    @Override
                    public LiveData<List<Alarm>> apply(Integer input) {
                        return repository.getAllAlarmFilteredByDay(input);
                    }
                }
        );
    }


    public long insert(Alarm alarm) throws ExecutionException, InterruptedException {
        return repository.insertAlarm(alarm);
    }

    public void update(Alarm alarm) {
        repository.updateAlarm(alarm);
    }

    public void delete(Alarm alarm) {
        repository.deleteAlarm(alarm);
    }

    public LiveData<List<Alarm>> getAllAlarmByDay() {
        return allAlarmByDay;
    }

    public void setAllAlarmByDay(int dayNum) {
        currentChoosedDayNum.postValue(dayNum);
    }
}
