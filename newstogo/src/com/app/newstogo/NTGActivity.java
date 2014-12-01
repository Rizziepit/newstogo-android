package com.app.newstogo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.text.Spanned;
import android.view.*;
import android.widget.*;
import android.util.Log;

import org.json.*;

public class NTGActivity extends Activity implements View.OnClickListener
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

    public View createStoryView(String title, String what, String when, String where, String url)
    {
        View view = getLayoutInflater().inflate(R.layout.list_item, null);
        ((TextView) view.findViewById(R.id.story_title)).setText(title);
        Spanned spannable = Html.fromHtml(
            "<strong>What:</strong> " + what + "<br/><strong>Where:</strong> " + where + "<strong>When:</strong> " + when
        );
        ((TextView) view.findViewById(R.id.story_meta)).setText(spannable);
        view.setTag(url);
        view.setOnClickListener(this);
        return view;
    }

    public View createSectionView(String day)
    {
        View view = getLayoutInflater().inflate(R.layout.list_title_item, null);
        ((TextView) view.findViewById(R.id.day)).setText("Today");
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        LinearLayout layout = (LinearLayout) findViewById(R.id.scroll_content);
        layout.removeAllViews();
        layout.addView(createSectionView("Today"));
        try
        {
            JSONObject[] stories = getRecentStories();
            for (int i = 0; i < stories.length; i++) {
                JSONObject story = stories[i];
                JSONArray suburbs = story.getJSONObject("places").getJSONArray("suburb");
                String placeName = NTGService.getClosestPlaceName(suburbs, null).split(",", 2)[0];
                layout.addView(createStoryView(
                    story.getString("title"),
                    story.getString("summary").substring(0, 50) + "...",
                    story.getString("pub_date").substring(11, 16),
                    placeName,
                    story.getString("link")
                ));
            }
        }
        catch (JSONException e) {Log.e("NTGActivity", e.toString());}
    }

    public JSONObject[] getRecentStories() throws JSONException
    {
        SQLiteDatabase db = new NTGStorageHelper(getApplicationContext()).getReadableDatabase();
        Cursor cursor = db.query("stories", new String[]{"data"}, null, null, null, null, "created DESC");
        JSONObject[] objects = new JSONObject[cursor.getCount()];
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            objects[i] = new JSONObject(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return objects;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse((String)v.getTag()));
        startActivity(intent);
    }
}
