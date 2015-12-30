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
    public static final String EMEI = "emei";
    public static final String USERNAME = "username";
    public static final String CURRENT_IP = "currentip";

    public static final String TABLE_CREATE =
            "CREATE TABLE "+TABLE_NAME+" (" +
            EMEI+" INTEGER PRIMARY KEY, "+
            USERNAME+" TEXT, "+
            CURRENT_IP+" TEXT);";

    public static final String TABLE_DROP =
            "DROP TABLE IF EXISTS "+TABLE_NAME+";";

    public ContactDAO(Context pContext) {
        super(pContext);
    }

    public void insert(Contact c){
        ContentValues value= new ContentValues();
        value.put(EMEI, c.getEmei());
        value.put(USERNAME, c.getUsername());
        value.put(CURRENT_IP, c.getCurrentIp());
        mDB.insert(TABLE_NAME, null, value);
    }

    public void delete(long emei){
        mDB.delete(TABLE_NAME, EMEI + " = ?", new String[]{String.valueOf(emei)});
    }

    public void update(Contact c){
        ContentValues value= new ContentValues();
        value.put(USERNAME, c.getUsername());
        value.put(CURRENT_IP, c.getCurrentIp());
        mDB.update(TABLE_NAME, value, EMEI + " = ?", new String[]{String.valueOf(c.getEmei())});
    }

    public Contact select(long emei){
        Cursor cursor = mDB.rawQuery(
                "select "+
                        USERNAME+"," +
                        CURRENT_IP+"," +
                "from " +
                        TABLE_NAME+
                "where " +
                        EMEI+" = ?"+
                ";"
        , new String[]{String.valueOf(emei)});
        return new Contact(emei,cursor.getString(0),cursor.getString(1));
    }

    public List<Contact> getContactList(){
        List<Contact> contactList = null;
        Cursor cursor = mDB.rawQuery(
                "select "+EMEI+","+USERNAME+","+CURRENT_IP+" from "+TABLE_NAME+" ;",null
        );
        while (cursor.moveToNext()){
            contactList.add(
                    new Contact(cursor.getLong(0),cursor.getString(1),cursor.getString(2))
            );
        }
        return contactList;
    }
}
