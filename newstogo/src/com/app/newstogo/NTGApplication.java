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
        LocationLibrary.initialiseLibrary(getBaseContext(), "com.app.newstogo");
    }
}