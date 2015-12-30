package com.icefeather.neko.database;

import android.provider.BaseColumns;

/**
 * Created by IceFeather on 15/12/2015.
 */
public final class ContactContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public ContactContract() {}

    /* Inner class that defines the table contents */
    public static abstract class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME_IMEI = "imei";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_CURRENT_IP = "currentip";
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                    ContactEntry._ID + " INTEGER PRIMARY KEY," +
                    ContactEntry.COLUMN_NAME_IMEI + TEXT_TYPE + COMMA_SEP +
                    ContactEntry.COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                    ContactEntry.COLUMN_NAME_CURRENT_IP + TEXT_TYPE + COMMA_SEP +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME;
}