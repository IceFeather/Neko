package com.icefeather.neko;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.icefeather.neko.database.Contact;

import java.util.ArrayList;

/**
 * Created by IceFeather on 02/01/2016.
 */
public class ContactAdapter extends ArrayAdapter<Contact>{
    private static LayoutInflater layoutInflater;
    private Activity activity;
    private ArrayList<Contact> contactArrayList;

    public ContactAdapter(Context context, int textViewRessourceId,
                          ArrayList<Contact> contactArrayList){
        super(context, textViewRessourceId, contactArrayList);
        try{
            this.activity = activity;
            this.contactArrayList = contactArrayList;
            layoutInflater = LayoutInflater.from(context);
        } catch (Exception e){

        }

    }

    public static class ViewHolder {
        public TextView usernameTextView;
        public TextView ipTextView;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = layoutInflater.inflate(R.layout.active_conversation_list, null);
                holder = new ViewHolder();

                holder.usernameTextView = (TextView) vi.findViewById(R.id.username);
                holder.ipTextView = (TextView) vi.findViewById(R.id.ip);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }



            holder.usernameTextView.setText(contactArrayList.get(position).getUsername());
            holder.ipTextView.setText(contactArrayList.get(position).getCurrentIp());


        } catch (Exception e) {


        }
        return vi;
    }


}
