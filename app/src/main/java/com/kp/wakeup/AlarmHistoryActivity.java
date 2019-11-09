package com.kp.wakeup;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kp.wakeup.Adapter.AlarmHistoryAdapter;
import com.kp.wakeup.Room.AlarmHistory;
import com.kp.wakeup.ViewModel.AlarmHistoryViewModel;

import java.util.List;

public class AlarmHistoryActivity extends AppCompatActivity {

    private AlarmHistoryViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_history);
        RecyclerView recyclerView = findViewById(R.id.alarmHistoryRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AlarmHistoryAdapter adapter = new AlarmHistoryAdapter();
        recyclerView.setAdapter(adapter);


        model = ViewModelProviders.of(this).get(AlarmHistoryViewModel.class);
        model.getAllAlarmHistories().observe(this, new Observer<List<AlarmHistory>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<AlarmHistory> alarmHistories) {
                Toast.makeText(AlarmHistoryActivity.this, String.format("Check your alarm histories"), Toast.LENGTH_SHORT).show();
                adapter.setAlarmHistories(alarmHistories);
            }
        });
    }
}
