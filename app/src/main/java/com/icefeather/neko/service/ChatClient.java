package com.icefeather.neko.service;

import android.os.AsyncTask;

import com.icefeather.neko.MainActivity;
import com.icefeather.neko.database.Contact;
import com.icefeather.neko.database.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by IceFeather on 05/01/2016.
 */
public class ChatClient extends AsyncTask<Void, Void, Void> {
    Contact contact;
    Message message;

    public ChatClient(Contact contact, Message message) {
        this.contact = contact;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            socket = new Socket(contact.getCurrentIp(), 9999);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            message.setImei(MainActivity.moi.getImei());
            oos.writeObject(message);
            oos.close();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

}
