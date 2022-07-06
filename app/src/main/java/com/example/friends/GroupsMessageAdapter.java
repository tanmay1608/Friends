package com.example.friends;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friends.Models.GroupMessages;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Random;


public class GroupsMessageAdapter extends RecyclerView.Adapter {

    android.content.Context context;
    ArrayList<GroupMessages> arrayList;
    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;

    public GroupsMessageAdapter(Context context, ArrayList<GroupMessages> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_SEND)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.senderchatlayout,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.receivergroupmessage_layout,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        GroupMessages messages=arrayList.get(position);
        if (holder.getClass()== SenderViewHolder.class)
        {
            SenderViewHolder viewHolder=(SenderViewHolder)holder;
            viewHolder.textViewmessage.setText(messages.getMessage());
            viewHolder.timeofmessage.setText(messages.getCurrenttime());
        }
        else{
            ReceiverViewHolder viewHolder=(ReceiverViewHolder) holder;
            viewHolder.textViewmessage.setText(messages.getMessage());
            viewHolder.timeofmessage.setText(messages.getCurrenttime());
            viewHolder.sendername.setText(messages.getSendername());
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            viewHolder.sendername.setTextColor(color);
        }
    }


    @Override
    public int getItemViewType(int position) {
        GroupMessages messages=arrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))
        {
            return ITEM_SEND;
        }
        else{
            return ITEM_RECEIVE;
        }
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

   private class SenderViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewmessage;
        TextView timeofmessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timeofsendermessage);

        }
    }

  private   class ReceiverViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewmessage;
        TextView timeofmessage;
        TextView sendername;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage=itemView.findViewById(R.id.receivermessage);
            timeofmessage=itemView.findViewById(R.id.timeofreceivedmessage);
            sendername=itemView.findViewById(R.id.sender_name);

        }
    }
}
