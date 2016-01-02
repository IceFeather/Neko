package com.icefeather.neko.database;

/**
 * Created by IceFeather on 15/12/2015.
 */
public class Contact {
    
    private long imei;
    private String username;
    private String currentIp;

    public Contact(long imei, String username, String currentIp) {
        super();
        this.imei = imei;
        this.username = username;
        this.currentIp = currentIp;
    }

    public long getImei() {
        return imei;
    }

    public void setImei(long imei) {
        this.imei = imei;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentIp() {
        return currentIp;
    }

    public void setCurrentIp(String currentIp) {
        this.currentIp = currentIp;
    }
}
