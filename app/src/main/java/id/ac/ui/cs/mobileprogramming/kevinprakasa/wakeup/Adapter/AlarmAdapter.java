package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.MainActivity;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.R;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Room.Alarm;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {
    private List<Alarm> alarms = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener listener;

    public AlarmAdapter(Context context) {
        super();
        mContext = context;
    }

    @NonNull
    @Override
    public AlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_item, parent, false);

        return new AlarmHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlarmHolder holder, int position) {
        final Alarm currentAlarm = alarms.get(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentAlarm.getTime());

        holder.alarmTime.setText(String.format("%02d : %02d",calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

        holder.alarmEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), holder.alarmEditButton);
                popup.inflate(R.menu.alarm_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete_alarm:
                                ((MainActivity)mContext).triggerDeleteAlarm(currentAlarm);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAlarms(List<Alarm> alarms) {

        this.alarms = alarms;
        this.alarms.sort(new Comparator<Alarm>() {
            @Override
            public int compare(Alarm t1, Alarm t2) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(t1.getTime());
                int t1Hour = cal.get(Calendar.HOUR_OF_DAY);
                int t1Minute = cal.get(Calendar.MINUTE);

                cal.setTimeInMillis(t2.getTime());
                int t2Hour = cal.get(Calendar.HOUR_OF_DAY);
                int t2Minute = cal.get(Calendar.MINUTE);
                // first compare its hour, then its minute
                if (t1Hour > t2Hour) return 1;
                else if (t1Hour < t2Hour) return -1;
                else {
                    if (t1Minute > t2Minute) {
                        return 1;
                    } else if (t1Minute < t2Minute) {
                        return -1;
                    }
                    return 0;
                }
            }
        });
        notifyDataSetChanged();
    }

    class AlarmHolder extends RecyclerView.ViewHolder {
        private TextView alarmTime;
        private ImageButton alarmEditButton;

        public AlarmHolder(@NonNull View itemView) {
            super(itemView);
            alarmTime = itemView.findViewById(R.id.alarmTime);
            alarmEditButton = itemView.findViewById(R.id.alarmEdit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(alarms.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Alarm alarm);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
