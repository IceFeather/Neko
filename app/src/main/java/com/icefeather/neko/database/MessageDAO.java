package com.icefeather.neko.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IceFeather on 31/12/2015.
 */
public class MessageDAO extends DAOBase{
    public static final String TABLE_NAME = "Message";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMEI = "imei";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DIRECTION = "direction";
    public static final String COLUMN_MESSAGE = "message";
    private ContactDAO contactDAO;


    public static final String TABLE_CREATE =
            "CREATE TABLE "+TABLE_NAME+" ("+
                    COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_IMEI+" LONG " +
                        "REFERENCES "+ContactDAO.TABLE_NAME+"("+ContactDAO.COLUMN_IMEI+") "+
                        "ON DELETE CASCADE, "+
                    COLUMN_DATE+" LONG, "+
                    COLUMN_DIRECTION+" INTEGER, "+
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
        values.put(COLUMN_DATE, m.getDate().getTime());
        values.put(COLUMN_DIRECTION, m.getDirection());
        values.put(COLUMN_MESSAGE, m.getMessage());
        open();
        db.insert(TABLE_NAME, null, values);
        close();
    }

    public void delete(int id){
        open();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        close();
    }

    public ArrayList<Message> getMessageListFromImei(long imei){
        ArrayList<Message> messageList = new ArrayList<Message>();
        open();
        Cursor cursor = db.rawQuery(
                "SELECT "+
                        COLUMN_ID+" ,"+
                        COLUMN_DATE+" ,"+
                        COLUMN_DIRECTION+" ,"+
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
                            new Date(cursor.getLong(1)*1000),
                            cursor.getInt(2),
                            cursor.getString(3)
                    )
            );
        }
        close();
        return messageList;
    }

}
