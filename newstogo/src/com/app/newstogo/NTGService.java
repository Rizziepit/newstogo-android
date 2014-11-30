package com.app.newstogo;

import org.json.*;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

public class NTGService extends IntentService
{
    private static final String TAG = "NTGService";
    private static final int RADIUS = 2000;
    private static final String ENDPOINT_URL = "http://10.52.152.78:5000/located_news";

    public NTGService() {super("com.app.newstogo");}

    @Override
    protected void onHandleIntent(Intent intent)
    {
        LocationInfo latestInfo = new LocationInfo(getBaseContext());
        Log.v(TAG, "Latest location " + latestInfo.toString());

        if (latestInfo.lastAccuracy <= RADIUS) {
            try {
                URL url = getQueryURL(latestInfo);
                Log.v(TAG, "Query: " + url);
                /*HttpURLConnection connection = (HttpURLConnection) (getQueryURL(latestInfo).openConnection());
                try {
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    processAPIResponse(in);
                }
                catch (Exception e) {Log.e(TAG, e.toString());}
                finally {connection.disconnect();}*/
                processAPIResponse(getMockDataStream());
            }
            catch (Exception e) {Log.e(TAG, e.toString());}
        }
    }

    protected void processAPIResponse(InputStream in) throws IOException, JSONException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        String all = "";
        while ((line = reader.readLine()) != null)
            all += line;
        JSONArray stories = new JSONObject(all).getJSONArray("stories");
        for (int i = 0; i < 5; i++) {
            JSONObject story = stories.getJSONObject(i);
            String link = story.getString("link");
            String summary = story.getString("summary");
            String title = story.getString("title");
            String id = story.getString("id");
            makeNotification(title, summary, link, id);
            Log.v(TAG, title);
        }
    }

    protected void makeNotification(String title, String text, String link, String id)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        Notification notification = new Notification.Builder(getApplicationContext())
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .build();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id, 0, notification);
        Log.v(TAG, "create notification");
    }

    protected InputStream getMockDataStream()
    {
        Resources res = getApplicationContext().getResources();
        int fileID = res.getIdentifier("mock_data", "raw", "com.app.newstogo");
        InputStream in = res.openRawResource(fileID);
        Log.v(TAG, "using mock data");
        return in;
    }

    protected URL getQueryURL(LocationInfo location) throws MalformedURLException
    {
        return new URL(ENDPOINT_URL + "?latitude=" + location.lastLat + "&longitude=" + location.lastLong + "&level=suburb&radius=2000");
    }
}