package com.example.rabdos7.muestreowifi;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by rabdos7 on 24/11/18.
 */

public class DatabaseOpenHelper extends SQLiteAssetHelper{
    private static final String DATABASE_NAME = "muestreoWiFi.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
