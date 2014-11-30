package com.app.newstogo;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class NTGService extends IntentService
{
    private static final String TAG = "NTGService";

    public NTGService() {super("NewsToGo");}

    @Override
    protected void onHandleIntent(Intent intent)
    {
        LocationInfo latestInfo = new LocationInfo(getBaseContext());
        Log.v(TAG, "Latest location " + latestInfo.toString());
        // NOTE: push location to service here
    }
}