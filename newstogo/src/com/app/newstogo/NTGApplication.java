package com.app.newstogo;

import android.app.Application;
import android.os.Bundle;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

public class NTGApplication extends Application
{
    public static final boolean DEBUG = false;
    public static final int REPEAT_INTERVAL = 10 * 60 * 1000;

    @Override
    public void onCreate()
    {
        super.onCreate();
        int repeat_interval = REPEAT_INTERVAL;
        if (DEBUG) {
            repeat_interval = 2 * 1000;
        }
        LocationLibrary.initialiseLibrary(
            getBaseContext(),
            repeat_interval,
            repeat_interval,
            "com.app.newstogo"
        );
    }
}