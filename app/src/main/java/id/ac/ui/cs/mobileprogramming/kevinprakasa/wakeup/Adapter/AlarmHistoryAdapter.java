package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.R;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Room.AlarmHistory;

public class AlarmHistoryAdapter extends RecyclerView.Adapter<AlarmHistoryAdapter.AlarmHistoryHolder> {

    private List<AlarmHistory> alarmHistories = new ArrayList<>();

    @NonNull
    @Override
    public AlarmHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_history_item, parent, false);
        return new AlarmHistoryHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final AlarmHistoryHolder holder, int position) {
        final AlarmHistory currentAlarmHistory = alarmHistories.get(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentAlarmHistory.getTime());
        holder.alarmHistoryTime.setText(String.format("%02d : %02d",calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        holder.alarmHistoryStatus.setText(currentAlarmHistory.getStatus());

    }

    @Override
    public int getItemCount() {
        return alarmHistories.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAlarmHistories(List<AlarmHistory> alarmHistories) {

        this.alarmHistories = alarmHistories;
        this.alarmHistories.sort(new Comparator<AlarmHistory>() {
            @Override
            public int compare(AlarmHistory t1, AlarmHistory t2) {
                if (t1.getId() > t2.getId()) return -1;
                else if (t1.getId() < t2.getId()) return 1;
                return 0;
            }
        });
        notifyDataSetChanged();
    }

    class AlarmHistoryHolder extends RecyclerView.ViewHolder {
        private TextView alarmHistoryTime;
        private TextView alarmHistoryStatus;

        public AlarmHistoryHolder(@NonNull View itemView) {
            super(itemView);
            alarmHistoryTime = itemView.findViewById(R.id.alarmHistoryTime);
            alarmHistoryStatus = itemView.findViewById(R.id.alarmHistoryStatus);
        }
    }
}
