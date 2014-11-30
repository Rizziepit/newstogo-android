package com.app.newstogo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

public class NTGActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, NTGService.class);
        PendingIntent pending = PendingIntent.getService(this, 0, alarmIntent, 0);
        int repeat_interval = NTGApplication.REPEAT_INTERVAL;
        if (NTGApplication.DEBUG) {
            repeat_interval = 10 * 1000;
        }
        alarmManager.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime(),
            repeat_interval,
            pending
        );
    }
}
