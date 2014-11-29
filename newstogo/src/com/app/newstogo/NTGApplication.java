package com.app.newstogo;

import android.app.Application;
import android.os.Bundle;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

public class NTGApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        LocationLibrary.initialiseLibrary(
            getBaseContext(),
            60 * 1000,
            2 * 60 * 1000,
            "com.app.newstogo"
        );
    }
}