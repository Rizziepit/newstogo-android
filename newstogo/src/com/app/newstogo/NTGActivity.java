package com.app.newstogo;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class NTGActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        refreshDisplay();
        final IntentFilter intentFilter = new IntentFilter(LocationLibraryConstants.getLocationChangedPeriodicBroadcastAction());
        registerReceiver(ntgBroadcastReceiver, intentFilter);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(ntgBroadcastReceiver);
    }

    private void refreshDisplay()
    {
        refreshDisplay(new LocationInfo(this));
    }

    private void refreshDisplay(final LocationInfo locationInfo)
    {
        final View locationTable = findViewById(R.id.location_table);
        final TextView locationTextView = (TextView) findViewById(R.id.location_title);

        if (locationInfo.anyLocationDataReceived()) {
            locationTable.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.location_timestamp)).setText(LocationInfo.formatTimeAndDay(locationInfo.lastLocationUpdateTimestamp, true));
            ((TextView)findViewById(R.id.location_latitude)).setText(Float.toString(locationInfo.lastLat));
            ((TextView)findViewById(R.id.location_longitude)).setText(Float.toString(locationInfo.lastLong));
            ((TextView)findViewById(R.id.location_accuracy)).setText(Integer.toString(locationInfo.lastAccuracy) + "m");
            ((TextView)findViewById(R.id.location_provider)).setText(locationInfo.lastProvider);
            if (locationInfo.hasLatestDataBeenBroadcast()) {
                locationTextView.setText("Latest location has been broadcast");
            }
            else {
                locationTextView.setText("Location broadcast pending (last " + LocationInfo.formatTimeAndDay(locationInfo.lastLocationUpdateTimestamp, true) + ")");
            }
        }
        else {
            locationTable.setVisibility(View.GONE);
            locationTextView.setText("No locations recorded yet");
        }
     }

    private final BroadcastReceiver ntgBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final LocationInfo locationInfo = (LocationInfo) intent.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
            refreshDisplay(locationInfo);
        }
    };
}
