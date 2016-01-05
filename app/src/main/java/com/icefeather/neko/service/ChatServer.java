package com.icefeather.neko.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.icefeather.neko.ChatActivity;
import com.icefeather.neko.MainActivity;
import com.icefeather.neko.R;
import com.icefeather.neko.database.Contact;
import com.icefeather.neko.database.ContactDAO;
import com.icefeather.neko.database.Message;
import com.icefeather.neko.database.MessageDAO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ChatServer extends Service {

    private static final Integer SERVER_PORT = 9999;
    private ServerSocket serverSocket;
    private Message messageIn;
    private ContactDAO cdao;
    private MessageDAO mdao;


    @Override
    public void onCreate() {
        Log.d("ChatServer", "Create service");
        cdao = new ContactDAO(this);
        mdao = new MessageDAO(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        PowerManager.WakeLock wl = null;
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
        wl.acquire();
        startServer();
        if (wl != null) wl.release();

        return START_STICKY;
    }

    private Runnable thread = new Runnable() {

        @Override
        public synchronized void run() {
            try {
                serverSocket = new ServerSocket(SERVER_PORT);
                ObjectInputStream in = null;

                while (true) {

                    Socket client = serverSocket.accept();

                    Log.d("ChatServer", "Receiving...");
                    try {
                        in = new ObjectInputStream(client.getInputStream());
                        Message messageIn = (Message) in.readObject();
                        Log.d("REC", "from : "+messageIn.getImei());
                        Contact contact = cdao.select(messageIn.getImei());
                        if(contact != null){
                            storeMessage(messageIn, contact);
                        }

                    } catch (ClassNotFoundException e) {
                        System.out.println("TCP S: Error in MessageServer Listener");
                        e.printStackTrace();

                    } finally {
                        client.close();
                    }
                }

            } catch (IOException e) {
            }

        }

    };
    private Thread serverThread;

    private synchronized void startServer() {
        if (serverThread == null) {
            serverThread = new Thread(thread);
            serverThread.start();
        }
    }

    private synchronized void stopServer() {
        if (serverThread != null) {
            Thread t = serverThread;
            serverThread = null;
            t.interrupt();
        }

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("TCP", "Killing Service");
        if (serverSocket != null) {
            try {
                serverSocket.close();
                stopServer();
                Log.v("TCP", "Closed server socket");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void storeMessage(Message m, Contact c){
        m.setDirection(2);
        m.setDate(new Date());
        mdao.insert(m);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_chat)
                        .setContentTitle("Neko")
                        .setContentText("New message from "+c.getUsername());
        Intent resultIntent = new Intent(this, ChatActivity.class);
        resultIntent.putExtra(MainActivity.CONTACT_IMEI, c.getImei()); //Put your id to your next Intent
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ChatActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
