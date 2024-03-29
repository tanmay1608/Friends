package com.example.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    android.content.Context context;
    ArrayList<Messages> arrayList;
    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;


    public MessagesAdapter(Context context, ArrayList<Messages> arrayList) {
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
            View view= LayoutInflater.from(context).inflate(R.layout.receiverchatlayout,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages messages=arrayList.get(position);
        if (holder.getClass()==SenderViewHolder.class)
        {
            SenderViewHolder viewHolder=(SenderViewHolder)holder;
            viewHolder.textViewmessage.setText(messages.getMessage());
            viewHolder.timeofmessage.setText(messages.getCurrenttime());
        }
        else{
            ReceiverViewHolder viewHolder=(ReceiverViewHolder) holder;
            viewHolder.textViewmessage.setText(messages.getMessage());
            viewHolder.timeofmessage.setText(messages.getCurrenttime());
        }

    }

    @Override
    public int getItemViewType(int position) {
        Messages messages=arrayList.get(position);
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

    class SenderViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewmessage;
        TextView timeofmessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage=itemView.findViewById(R.id.sendermessage);
            timeofmessage=itemView.findViewById(R.id.timeofsendermessage);

        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewmessage;
        TextView timeofmessage;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage=itemView.findViewById(R.id.receivermessage);
            timeofmessage=itemView.findViewById(R.id.timeofreceivedmessage);

        }
    }
}
