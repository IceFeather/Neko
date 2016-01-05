package com.icefeather.neko.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

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
        open();
        db.insert(TABLE_NAME, null, values);
        close();
    }

    public void delete(long imei){
        open();
        db.delete(
                TABLE_NAME, COLUMN_IMEI + " = ?",
                new String[]{String.valueOf(imei)}
        );
        close();
    }

    public void update(Contact c){
        ContentValues values= new ContentValues();
        values.put(COLUMN_USERNAME, c.getUsername());
        values.put(COLUMN_CURRENT_IP, c.getCurrentIp());
        open();
        db.update(
                TABLE_NAME, values, COLUMN_IMEI + " = ?",
                new String[]{String.valueOf(c.getImei())}
        );
        close();
    }

    public Contact select(long imei){
        open();
        Cursor cursor = db.rawQuery(
                "select "+
                        COLUMN_USERNAME+","+
                        COLUMN_CURRENT_IP+
                " from " +
                        TABLE_NAME+
                " where " +
                        COLUMN_IMEI+" = ?;",
                new String[]{String.valueOf(imei)}
        );
        cursor.moveToFirst();
        Contact contact = new Contact(imei,cursor.getString(0),cursor.getString(1));
        close();
        return contact;
    }

    public ArrayList<Contact> getContactList(){
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        open();
        Cursor cursor = db.rawQuery(
                "select "+COLUMN_IMEI+","+COLUMN_USERNAME+","+COLUMN_CURRENT_IP+
                        " from "+TABLE_NAME+" ;",
                null
        );
        if(cursor.getCount() > 0){
            Log.d("CONTACTS_NB", String.valueOf(cursor.getCount()));
            while (cursor.moveToNext()){
                contactList.add(
                        new Contact(cursor.getLong(0),cursor.getString(1),cursor.getString(2))
                );
            }
        }
        close();
        return contactList;
    }
}
