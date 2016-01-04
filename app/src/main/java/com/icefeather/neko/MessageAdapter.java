package com.icefeather.neko;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.icefeather.neko.database.Contact;
import com.icefeather.neko.database.Message;

import java.util.ArrayList;

/**
 * Created by IceFeather on 04/01/2016.
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    private static LayoutInflater layoutInflater;
    private Activity activity;
    private ArrayList<Message> messageArrayList;

    public MessageAdapter(Context context, int textViewRessourceId,
                          ArrayList<Message> messageArrayList){
        super(context, textViewRessourceId, messageArrayList);
        try{
            this.activity = activity;
            this.messageArrayList = messageArrayList;
            layoutInflater = LayoutInflater.from(context);
        } catch (Exception e){

        }

    }

    public static class ViewHolder {
        public TextView messageTextView;
        public TextView dateTextView;
        public ImageView directionImageView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = layoutInflater.inflate(R.layout.message_list, null);
                holder = new ViewHolder();

                holder.messageTextView = (TextView) vi.findViewById(R.id.message);
                holder.dateTextView = (TextView) vi.findViewById(R.id.date);
                holder.directionImageView = (ImageView) vi.findViewById(R.id.sent_or_received_icon);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }


            holder.messageTextView.setText(messageArrayList.get(position).getMessage());
            holder.dateTextView.setText(String.valueOf(messageArrayList.get(position).getDate()));

            switch(messageArrayList.get(position).getDirection()){
                case 1:
                    holder.directionImageView.setImageResource(R.mipmap.ic_sent);
                    break;
                case 2:
                    holder.directionImageView.setImageResource(R.mipmap.ic_received);
                    break;
                default:
                    break;
            }


        } catch (Exception e) {


        }
        return vi;
    }
}
