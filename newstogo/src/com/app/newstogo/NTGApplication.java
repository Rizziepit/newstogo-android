package com.app.newstogo;

import android.app.Application;
import android.os.Bundle;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

public class NTGApplication extends Application
{
    public static final int REPEAT_INTERVAL = 10 * 60  * 1000;

    @Override
    public void onCreate()
    {
        super.onCreate();
        LocationLibrary.initialiseLibrary(
            getBaseContext(),
            REPEAT_INTERVAL,
            2 * REPEAT_INTERVAL,
            "com.app.newstogo"
        );
    }
}