package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.io.IOException;
import java.util.Calendar;

import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.Room.AlarmHistory;
import id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup.ViewModel.AlarmHistoryViewModel;

public class AlarmReceiverActivity extends AppCompatActivity {
    private Ringtone ringtone;
    private MediaPlayer mediaPlayer;
    public final static String RECEIVER_ALARM_TIME = "receiver_alarm_time";
    public final static String RECEIVER_ALARM_SNOOZE = "receiver_alarm_snooze";
    public final static String RECEIVER_ALARM_LABEL= "receiver_alarm_label";
    public final static String RECEIVER_ALARM_DAY= "receiver_alarm_day";
    public final static String RECEIVER_ALARM_ID= "receiver_alarm_id";
    public final static String RECEIVER_ALARM_RINGTONE= "receiver_alarm_music";
    private Intent intent;
    private Bundle extras;

    private String finishStatus = "SKIPPED";


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AlarmHistoryViewModel model = ViewModelProviders.of(this).get(AlarmHistoryViewModel.class);
        AlarmHistory alarmHistory = new AlarmHistory(intent.getLongExtra(RECEIVER_ALARM_TIME,0L), finishStatus);
        model.insert(alarmHistory);
        stopMusic();
    }

    private void stopMusic() {
        if (ringtone != null) {
            ringtone.stop();
        } else {
            mediaPlayer.stop();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alarm_receiver);

        intent = getIntent();
        extras = intent.getExtras();

        Button stopAlarmButton = findViewById(R.id.stopAlarm);
        Button snoozeButton = findViewById(R.id.snoozeButton);
        TextView alarmTime = findViewById(R.id.alarmTime);
        TextView alarmLabel = findViewById(R.id.alarmLabel);

        stopAlarmButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                stopMusic();
                finish();
                finishStatus = "STOPPED";
                return false;
            }}
        );

        snoozeButton.setText(String.format("SNOOZE %d MIN", extras.getInt(RECEIVER_ALARM_SNOOZE)));
        alarmLabel.setText(extras.getString(RECEIVER_ALARM_LABEL));

        // parse time in long from calendar
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(extras.getLong(RECEIVER_ALARM_TIME));

        alarmTime.setText(String.format("%02d : %02d", calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE)));

        if (extras.getInt(RECEIVER_ALARM_SNOOZE, 0) != 0) {
            snoozeButton.setOnClickListener(new View.OnClickListener() {

                // will trigger to create one time alarm based on snoozed time
                @Override
                public void onClick(View view) {
                    snoozeAlarm();

                }
            });
        } else {
            snoozeButton.setVisibility(View.GONE);
        }

        try {
            playSound(this, extras.getString(RECEIVER_ALARM_RINGTONE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intentReceiver = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intentReceiver,PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    // To snooze the alarm, user needs to have internet connectivity
    private void snoozeAlarm() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Calendar snoozedCalendar = Calendar.getInstance();
            snoozedCalendar.add(Calendar.MINUTE, extras.getInt(RECEIVER_ALARM_SNOOZE));
            intent.putExtra(RECEIVER_ALARM_TIME, snoozedCalendar.getTimeInMillis());

            // send request create alarm with same generated request code
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    Integer.MAX_VALUE, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager am =
                    (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP,
                    snoozedCalendar.getTimeInMillis(),
                    pendingIntent);
            // turn off media and finish
            stopMusic();
            finishStatus = "SNOOZED";
            finish();
        } else {
            Toast.makeText(getApplicationContext(),"You have to be connected to internet to snooze this!", Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void playSound(Context context, String ringtonePath) throws IOException {
        Uri alarmUri;
        if (ringtonePath != null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(getApplicationContext(),Uri.parse(ringtonePath));
            mediaPlayer.prepare();
            mediaPlayer.setVolume(1,1);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            ringtone = RingtoneManager.getRingtone(context, alarmUri);
            ringtone.play();
            ringtone.setVolume(1);
        }
    }
}
