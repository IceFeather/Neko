package com.icefeather.neko;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.icefeather.neko.database.Message;
import com.icefeather.neko.database.MessageDAO;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private MessageDAO mdao;
    private ArrayList<Message> messageArrayList = new ArrayList<Message>();
    private static MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        Long imei = intent.getLongExtra(MainActivity.CONTACT_IMEI, 0L);
        Log.d("CHAT_IMEI", String.valueOf(imei));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton buttonSend = (ImageButton) findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        mdao = new MessageDAO(this);
        mdao.insert(new Message(0, 1L, new Date(), 1, "envoi"));
        mdao.insert(new Message(0, 1L, new Date(), 2, "reception"));

        messageArrayList = mdao.getMessageListFromImei(imei);

        messageAdapter = new MessageAdapter(this, 0, messageArrayList);

        ListView contactListView = (ListView) findViewById(R.id.message_list);
        contactListView.setAdapter(messageAdapter);


    }

    protected void sendMessage(){

    }

}
