package com.icefeather.neko.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by IceFeather on 15/12/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String CONTACT_KEY = "imei";
    public static final String CONTACT_USERNAME = "username";
    public static final String CONTACT_CURRENT_IP = "currentip";

    public static final String CONTACT_TABLE_NAME = "Contact";
    public static final String CONTACT_TABLE_CREATE =
            "CREATE TABLE " + CONTACT_TABLE_NAME + " (" +
                    CONTACT_KEY + " PRIMARY KEY, " +
                    CONTACT_USERNAME + " TEXT, " +
                    CONTACT_CURRENT_IP + " TEXT);";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CONTACT_TABLE_CREATE);
    }

    public static final String CONTACT_TABLE_DROP = "DROP TABLE IF EXISTS " + CONTACT_TABLE_NAME + ";";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CONTACT_TABLE_DROP);
        onCreate(db);
    }

}
