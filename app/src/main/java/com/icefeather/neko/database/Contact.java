package com.icefeather.neko.database;

/**
 * Created by IceFeather on 15/12/2015.
 */
public class Contact {
    
    private long emei;
    private String username;
    private String currentIp;

    public Contact(long emei, String username, String currentIp) {
        super();
        this.emei = emei;
        this.username = username;
        this.currentIp = currentIp;
    }

    public long getEmei() {
        return emei;
    }

    public void setEmei(long emei) {
        this.emei = emei;
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
