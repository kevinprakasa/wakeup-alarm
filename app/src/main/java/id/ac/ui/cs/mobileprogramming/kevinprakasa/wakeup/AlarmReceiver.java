package id.ac.ui.cs.mobileprogramming.kevinprakasa.wakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will send a notification message
        intent = new Intent(context, AlarmService.class);
        context.startService(intent);
    }


}