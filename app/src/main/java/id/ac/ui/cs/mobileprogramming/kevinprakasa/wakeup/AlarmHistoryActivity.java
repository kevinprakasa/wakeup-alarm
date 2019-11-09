package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Adapter.AlarmHistoryAdapter;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Room.AlarmHistory;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.ViewModel.AlarmHistoryViewModel;

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
                adapter.setAlarmHistories(alarmHistories);
            }
        });
    }
}
