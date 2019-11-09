package com.kp.wakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("BROADCASST RECEIVER", "ON RECEIVE");
        //this will send a notification message
        intent = new Intent(context, AlarmService.class);
        context.startService(intent);
    }


}