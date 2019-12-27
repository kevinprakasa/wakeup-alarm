package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;
    public final String CHANNEL_NAME = "ALARM_CHANNEL_NAME";
    public final String CHANNEL_ID = "ALARM_CHANNEL_ID";
    public final String CHANNEL_DESC = "ALARM_CHANNEL_DESC";

    Handler handler;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        sendNotification("Wake Up! Wake Up!");
    }

    private void sendNotification(String msg) {
        final int[] counter = {0};
        createNotificationChannel();
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        final NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Wakeup!")
                .setContentText(String.format("This notification has shown for %d seconds", counter[0]))
                .setChannelId(CHANNEL_ID);


        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
        // create a counter for a notification
        handler = new Handler();
        Timer T=new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                counter[0]++;
                Log.d("COUNTER", String.valueOf(counter[0]));
                alarmNotificationBuilder.setContentText(String.format("This notification has shown for %d seconds", counter[0]));
                alarmNotificationManager.notify(1,alarmNotificationBuilder.build());
            }
        }, 0, 1000);
        Log.d("AlarmService", "Notification sent.");
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_NAME;
            String description = CHANNEL_DESC;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}