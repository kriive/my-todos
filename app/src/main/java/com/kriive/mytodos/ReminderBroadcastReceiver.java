package com.kriive.mytodos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String title;
        if ((title = intent.getStringExtra("Title")) == null) {
            title = "Test";
        }

        NotificationCompat.Builder notiBuild = new NotificationCompat.Builder(context, "mytodoReminder").setSmallIcon(R.drawable.ic_baseline_calendar_today_24).setContentTitle("MyTODOs").setContentText(title).setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1337, notiBuild.build());
    }
}
