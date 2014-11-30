package com.app.newstogo;

import android.content.Context;
import android.database.sqlite.*;

public class NTGStorageHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "newstogo_db";

    NTGStorageHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS stories " +
                   "(data TEXT NOT NULL, " +
                   "id TEXT PRIMARY KEY NOT NULL, " + 
                   "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
