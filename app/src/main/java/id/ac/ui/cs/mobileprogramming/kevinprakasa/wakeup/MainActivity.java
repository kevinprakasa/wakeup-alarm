package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Adapter.AlarmAdapter;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Room.Alarm;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.ViewModel.AlarmViewModel;


public class MainActivity extends AppCompatActivity {

    private AlarmViewModel model;
    public static MainActivity instance;

    private Calendar calendar = Calendar.getInstance();
    private int currentDay = mapRealDayNumToDefinedDayNum(calendar.get(Calendar.DAY_OF_WEEK));

    public static MainActivity instance() {
        return instance;
    }

    @Override
    public void onStart() {
        super.onStart();

        instance = this;

        findViewById(R.id.createAlarmButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateCreateAlarm(null);
            }
        });

        findViewById(R.id.alarmHistoryNavigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAlarmHistory();
            }
        });

        ((TextView)findViewById(R.id.currentDay)).setText(String.format("%s %s",getResources().getString(R.string.today_is),convertFromDefinedDayNumToString(currentDay)));
        ((TextView)findViewById(R.id.dayPicked)).setText(convertFromDefinedDayNumToString(currentDay));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.alarmRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AlarmAdapter adapter = new AlarmAdapter(this);
        recyclerView.setAdapter(adapter);


        model = ViewModelProviders.of(this).get(AlarmViewModel.class);
        model.setAllAlarmByDay(currentDay);
        model.getAllAlarmByDay().observe(this, new Observer<List<Alarm>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<Alarm> alarms) {
                Toast.makeText(MainActivity.this, String.format("%s alarms on %s",String.valueOf(alarms.size()), convertFromDefinedDayNumToString(currentDay)), Toast.LENGTH_SHORT).show();
                adapter.setAlarms(alarms);
            }
        });

        adapter.setOnItemClickListener(new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Alarm alarm) {
                navigateCreateAlarm(alarm);
            }
        });
    }

    public void navigateToAlarmHistory() {
        Intent intent = new Intent(this, AlarmHistoryActivity.class);
        startActivity(intent);
    }

    public void onButtonDaysClicked(View view) {
        switch (view.getId()) {
            case R.id.monButton:
                currentDay = 1;
                model.setAllAlarmByDay(1);
                break;
            case R.id.tueButton:
                currentDay = 2;
                model.setAllAlarmByDay(2);
                break;
            case R.id.wedButton:
                currentDay = 3;
                model.setAllAlarmByDay(3);
                break;
            case R.id.thuButton:
                currentDay = 4;
                model.setAllAlarmByDay(4);
                break;
            case R.id.friButton:
                currentDay = 5;
                model.setAllAlarmByDay(5);
                break;
            case R.id.satButton:
                currentDay = 6;
                model.setAllAlarmByDay(6);
                break;
            case R.id.sunButton:
                currentDay = 7;
                model.setAllAlarmByDay(7);
                break;
        }
        ((TextView)findViewById(R.id.dayPicked)).setText(convertFromDefinedDayNumToString(currentDay));
    }

    public int mapRealDayNumToDefinedDayNum(int realDayNum) {
        switch (realDayNum) {
            case Calendar.SUNDAY:
                return 7;
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
        }
        return 0;
    }

    public String convertFromDefinedDayNumToString(int definedDayNum) {
        switch (definedDayNum) {
            case 1:
                return getResources().getString(R.string.mon);
            case 2:
                return getResources().getString(R.string.tue);
            case 3:
                return getResources().getString(R.string.wed);
            case 4:
                return getResources().getString(R.string.thu);
            case 5:
                return getResources().getString(R.string.fri);
            case 6:
                return getResources().getString(R.string.sat);
            case 7:
                return getResources().getString(R.string.sun);
            default:
                return "";
        }
    }

    public void triggerDeleteAlarm(Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(),AlarmReceiverActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.cancel(pendingIntent);
        model.delete(alarm);
    }

    public void navigateCreateAlarm(Alarm alarm) {
        Intent intent = new Intent(this, CreateAlarmActivity.class);
        if (alarm != null) {
            intent.putExtra(CreateAlarmActivity.ALARM_TIME, alarm.getTime());
            intent.putExtra(CreateAlarmActivity.ALARM_LABEL, alarm.getLabel());
            intent.putExtra(CreateAlarmActivity.ALARM_SNOOZE, alarm.getSnooze());
            intent.putExtra(CreateAlarmActivity.ALARM_DAY, alarm.getDay());
            intent.putExtra(CreateAlarmActivity.ALARM_ID, alarm.getId());
        }
        startActivity(intent);
    }

}
