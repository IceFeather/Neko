package com.icefeather.neko.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.sql.Date;
import java.util.List;

/**
 * Created by IceFeather on 31/12/2015.
 */
public class MessageDAO extends DAOBase{
    public static final String TABLE_NAME = "Message";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMEI = "imei";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_MESSAGE = "message";
    private ContactDAO contactDAO;


    public static final String TABLE_CREATE =
            "CREATE TABLE "+TABLE_NAME+" ("+
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_IMEI+" LONG " +
                        "REFERENCES "+ContactDAO.TABLE_NAME+"("+ContactDAO.COLUMN_IMEI+") "+
                        "ON DELETE CASCADE, "+
                    COLUMN_DATE+" TEXT, "+
                    COLUMN_MESSAGE+" TEXT" +
            ");";

    public static final String TABLE_DROP =
            "DROP TABLE IF EXISTS "+TABLE_NAME+";";

    public MessageDAO(Context pContext) {
        super(pContext);
        contactDAO = new ContactDAO(pContext);
    }

    public void insert(Message m){
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMEI, m.getImei());
        values.put(COLUMN_DATE, String.valueOf(m.getDate()));
        values.put(COLUMN_MESSAGE, m.getMessage());
        mDB.insert(TABLE_NAME, null, values);
    }

    public void delete(int id){
        mDB.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public List<Message> getMessageListFromImei(long imei){
        List<Message> messageList = null;
        Cursor cursor = mDB.rawQuery(
                "SELECT "+
                        COLUMN_ID+" ,"+
                        COLUMN_DATE+" ,"+
                        COLUMN_MESSAGE+
                " FROM "+
                        TABLE_NAME+
                " WHERE "+
                        COLUMN_IMEI+" = ?" +
                " ORDER BY date("+COLUMN_DATE+") ASC;",
                new String[]{String.valueOf(imei)}
        );
        while (cursor.moveToNext()){
            messageList.add(
                    new Message(
                            cursor.getInt(0),
                            imei,
                            Date.valueOf(cursor.getString(1)),
                            cursor.getString(2)
                    )
            );
        }
        return messageList;
    }

}
