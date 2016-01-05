package com.icefeather.neko;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.icefeather.neko.database.Contact;
import com.icefeather.neko.database.ContactDAO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Locale;

public class NFCReaderActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private ContactDAO cdao;
    private int SIMU = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcreader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        resoudreIntent(getIntent());

        /* SIMU RECEPTION TAG */
        simulation();
        /**/
    }


    @Override
    protected void onResume() {
        super.onResume();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, this.getClass()).addFlags(
                        Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        NdefRecord record = creerRecord(MainActivity.moi_serial);
        NdefMessage message = creerMessage(record);
        nfcAdapter.setNdefPushMessage(message, this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //nfcAdapter.disableForegroundNdefPush(this);
        //nfcAdapter.setNdefPushMessage(ndefMessage, this);
    }



    protected void resoudreIntent(Intent intent){
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] messages;
            if (rawMsgs != null) {
                messages = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    messages[i] = (NdefMessage) rawMsgs[i];
                    NdefRecord record = messages[i].getRecords()[i];
                    byte[] id = record.getId();
                    short tnf = record.getTnf();
                    byte[] type = record.getType();
                    String message = getTextData(record.getPayload());
                    Log.d("TAG RECEIVED", message);
                    parseNFCmessage(message);
                }
            }
        }
    }

    public static NdefRecord creerRecord(String message) {
        byte[] langBytes = Locale.ENGLISH.getLanguage().getBytes(Charset.forName("US-ASCII"));
        byte[] textBytes = message.getBytes(Charset.forName("UTF-8"));
        char status = (char) (langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    public static NdefMessage creerMessage(NdefRecord record)
    {
        NdefRecord[] records = new NdefRecord[1];
        records[0] = record;
        NdefMessage message = new NdefMessage(records);
        return message;
    }

    String getTextData(byte[] payload) {
        String s = "";
        String texteCode = ((payload[0] & 0200) == 0) ? (String) "UTF-8" : (String) "UTF-16";
        int langageCodeTaille = payload[0] & 0077;
        try {
            s = new String(payload, langageCodeTaille + 1, payload.length - langageCodeTaille - 1, texteCode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public void parseNFCmessage(String message){
        try{
            Contact NFCContact = (Contact) MainActivity.fromString(message);
            cdao = new ContactDAO(this);
            cdao.insert(NFCContact);
            Toast toast = Toast.makeText(getApplicationContext(), R.string.contact_added, Toast.LENGTH_SHORT);
            toast.show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void simulation(){
        if(SIMU == 0) {
            SIMU = 1;
            final Intent intent = new Intent(NfcAdapter.ACTION_TAG_DISCOVERED);
            NdefMessage[] messages = new NdefMessage[1];
            messages[0] = creerMessage(creerRecord(MainActivity.moi_serial));
            intent.putExtra(NfcAdapter.EXTRA_NDEF_MESSAGES, messages);
            startActivity(intent);
        }
    }

}
