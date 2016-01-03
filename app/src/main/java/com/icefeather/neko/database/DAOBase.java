package com.icefeather.neko.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by IceFeather on 30/12/2015.
 */
public abstract class DAOBase {
    protected final static int VERSION = 1;
    protected final static String NOM = "neko.db";

    protected SQLiteDatabase db = null;
    protected DatabaseHandler mHandler = null;

    public DAOBase(Context pContext){
        this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
    }

    public void open(){
        db = mHandler.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

}
