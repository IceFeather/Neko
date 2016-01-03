package com.icefeather.neko.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by IceFeather on 30/12/2015.
 */
public abstract class DAOBase {
    protected final static int VERSION = 1;
    protected final static String NOM = "neko.db";

    protected SQLiteDatabase mDB = null;
    protected DatabaseHandler mHandler = null;

    public DAOBase(Context pContext){
        this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open(){
        mDB = mHandler.getWritableDatabase();
        return mDB;
    }

    public void close(){
        mDB.close();
    }

    public SQLiteDatabase getDb(){
        return mDB;
    }

}
