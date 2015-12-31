package com.icefeather.neko.database;

import java.util.Date;

/**
 * Created by IceFeather on 31/12/2015.
 */
public class Message {
    private int id;
    private long imei;
    private Date date;
    private String message;

    public Message(int id, long imei, Date date, String message){
        super();
        this.id = id;
        this.imei = imei;
        this.date = date;
        this.message = message;
    }

    public void setId(int id){ this.id = id; }

    public int getId(){ return this.id; }

    public void setImei(long imei){ this.imei = imei; }

    public long getImei(){ return this.imei; }

    public void setDate(Date date){ this.date = date; }

    public Date getDate(){ return this.date; }

    public void setMessage(String message){ this.message = message; }

    public String getMessage(){ return this.message; }

}
