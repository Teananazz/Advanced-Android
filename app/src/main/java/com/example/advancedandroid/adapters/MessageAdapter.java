package com.example.advancedandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advancedandroid.R;
import com.example.advancedandroid.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> list;
    // we can we two different xmls depending on who sent the message.
    private LayoutInflater MessageInflater;

    private static final int SENDING_MESSAGE = 1;
    private static final int RECEIVE_MESSAGE = 2;


    public MessageAdapter(Context context, List<Message> List) {
        MessageInflater= LayoutInflater.from(context);
        list = List;
    }

    public MessageAdapter(List<Message> messagesList) {
        this.list = messagesList;
    }


    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ItemView;
        if (viewType == SENDING_MESSAGE) {
             ItemView = MessageInflater.inflate(R.layout.sender_message, parent, false);
        }
        else {
            ItemView = MessageInflater.inflate(R.layout.receiver_message, parent, false);
        }


        return new MessageViewHolder(ItemView,  this, viewType);
    }

    @Override
    // this method sets the different fields of the object.
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {

        Message message = list.get(position);

        ((TextView)holder.Message).setText(message.getMessage());
        ((TextView)holder.MessageDate).setText(message.getMessageDate());

    }


    @Override
    public int getItemCount() {
        if(list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (!list.get(position).getFlagSent()) {
            return RECEIVE_MESSAGE;
        } else {
            return SENDING_MESSAGE;
        }
    }




    public static class MessageViewHolder extends RecyclerView.ViewHolder {

         private View Message;
         private View MessageDate;



        final MessageAdapter Adapter;

        public MessageViewHolder( @NonNull View itemView, MessageAdapter Adapter, int viewType) {
            super(itemView);
            this.Adapter = Adapter;
            if(viewType == RECEIVE_MESSAGE) {
                Message = itemView.findViewById(R.id.incoming_msg);
                MessageDate = itemView.findViewById(R.id.incoming_msg_time);

            }
            else {
                Message = itemView.findViewById(R.id.outgoing_msg);
                MessageDate = itemView.findViewById(R.id.outgoing_msg_time);
            }

        }
    }


}
