package com.icefeather.neko.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

/**
 * Created by IceFeather on 30/12/2015.
 */
public class ContactDAO extends DAOBase{
    public static final String TABLE_NAME = "Contact";
    public static final String COLUMN_IMEI = "imei";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_CURRENT_IP = "currentip";

    public static final String TABLE_CREATE =
            "CREATE TABLE "+TABLE_NAME+" (" +
                    COLUMN_IMEI+" INTEGER PRIMARY KEY, "+
                    COLUMN_USERNAME+" TEXT, "+
                    COLUMN_CURRENT_IP+" TEXT);";

    public static final String TABLE_DROP =
            "DROP TABLE IF EXISTS "+TABLE_NAME+";";

    public ContactDAO(Context pContext) {
        super(pContext);
    }

    public void insert(Contact c){
        ContentValues values= new ContentValues();
        values.put(COLUMN_IMEI, c.getImei());
        values.put(COLUMN_USERNAME, c.getUsername());
        values.put(COLUMN_CURRENT_IP, c.getCurrentIp());
        mDB.insert(TABLE_NAME, null, values);
    }

    public void delete(long emei){
        mDB.delete(
                TABLE_NAME, COLUMN_IMEI + " = ?",
                new String[]{String.valueOf(emei)}
        );
    }

    public void update(Contact c){
        ContentValues values= new ContentValues();
        values.put(COLUMN_USERNAME, c.getUsername());
        values.put(COLUMN_CURRENT_IP, c.getCurrentIp());
        mDB.update(
                TABLE_NAME, values, COLUMN_IMEI + " = ?",
                new String[]{String.valueOf(c.getImei())}
        );
    }

    public Contact select(long emei){
        Cursor cursor = mDB.rawQuery(
                "select "+
                        COLUMN_USERNAME+","+
                        COLUMN_CURRENT_IP+
                " from " +
                        TABLE_NAME+
                " where " +
                        COLUMN_IMEI+" = ?;",
                new String[]{String.valueOf(emei)}
        );
        return new Contact(emei,cursor.getString(0),cursor.getString(1));
    }

    public List<Contact> getContactList(){
        List<Contact> contactList = null;
        Cursor cursor = mDB.rawQuery(
                "select "+COLUMN_IMEI+","+COLUMN_USERNAME+","+COLUMN_CURRENT_IP+
                        " from "+TABLE_NAME+" ;",
                null
        );
        while (cursor.moveToNext()){
            contactList.add(
                    new Contact(cursor.getLong(0),cursor.getString(1),cursor.getString(2))
            );
        }
        return contactList;
    }
}
