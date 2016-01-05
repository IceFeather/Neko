package com.icefeather.neko;

import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.icefeather.neko.database.Contact;
import com.icefeather.neko.database.ContactDAO;
import com.icefeather.neko.database.Message;
import com.icefeather.neko.database.MessageDAO;
import com.icefeather.neko.service.ChatClient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private static ContactDAO cdao;
    private static MessageDAO mdao;
    private static Contact contact;
    private static ArrayList<Message> messageArrayList = new ArrayList<Message>();
    private LauncherActivity.ListItem messageListItem = new LauncherActivity.ListItem();
    public static MessageAdapter messageAdapter;
    private ListView messageListView;
    private EditText messageEditText;
    private static final Integer PORT = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        final Long imei = intent.getLongExtra(MainActivity.CONTACT_IMEI, 0L);
        Log.d("CHAT_IMEI", String.valueOf(imei));


        cdao = new ContactDAO(this);
        contact = cdao.select(imei);

        mdao = new MessageDAO(this);

        TextView contactUsernameTextView = (TextView) findViewById(R.id.contact_username);
        TextView contactIpTextView = (TextView) findViewById(R.id.contact_ip);

        contactUsernameTextView.setText(contact.getUsername());
        contactIpTextView.setText(contact.getCurrentIp());

        messageArrayList = mdao.getMessageListFromImei(imei);
        messageAdapter = new MessageAdapter(this, 0, mdao.getMessageListFromImei(contact.getImei()));
        messageListView = (ListView) findViewById(R.id.message_list);
        messageListView.setAdapter(messageAdapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        messageEditText = (EditText) findViewById(R.id.message_edit);
        ImageButton buttonSend = (ImageButton) findViewById(R.id.button_send);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageEditText.getText().toString();
                if (messageText != "") {
                    Message message = new Message(
                            0, imei, new Date(), 1, messageText
                    );
                    try {
                        sendMessage(message, contact);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    protected void sendMessage(Message message, Contact contact) throws IOException {
        Log.d("SEND_MESSAGE_TO", contact.getCurrentIp() + ":" + PORT);
        ChatClient cc = new ChatClient(contact, message);
        cc.execute();
        mdao.insert(message);
        messageArrayList = mdao.getMessageListFromImei(contact.getImei());
        updateMessageList();
        messageEditText.setText("");
    }

    public static void updateMessageList(){
        messageAdapter.clear();
        messageAdapter.addAll(mdao.getMessageListFromImei(contact.getImei()));
        messageAdapter.notifyDataSetChanged();
    }

}
