package com.example.advancedandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advancedandroid.R;
import com.example.advancedandroid.models.Contact;

import java.util.LinkedList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> list;
    private LayoutInflater ContactInflater;

    public ContactAdapter(Context context,
                          List<Contact> List) {
       ContactInflater = LayoutInflater.from(context);
       list = List;
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View ItemView = ContactInflater.inflate(R.layout.group_list_item, parent, false);
         return new ContactViewHolder(ItemView, this);
    }

    @Override
    // this method sets the different fields of the object.
    public void onBindViewHolder(@NonNull ContactAdapter.ContactViewHolder holder, int position) {

        Contact contact = list.get(position);
        holder.item_chatname.setText(contact.getUserName());
        holder.item_last_message.setText(contact.getLastMessage());
        holder.item_lastm_time.setText(contact.getLastDate());





    }

    @Override
    public int getItemCount() {
       if(list != null) {
           return list.size();
       }
       return 0;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        public final TextView item_chatname;
        public final TextView item_last_message;
        public final TextView item_lastm_time;
        public final CardView card;
         public final ImageView img;
        final ContactAdapter mAdapter;

        // this method makes it so that we access all  these fields in onbindViewholder method.
        public ContactViewHolder(View itemView, ContactAdapter adapter) {
            super(itemView);
            item_chatname = itemView.findViewById(R.id.chat_name);
            item_last_message = itemView.findViewById(R.id.recent_message);
            item_lastm_time = itemView.findViewById(R.id.recent_time);
            card = itemView.findViewById(R.id.parent_image_view);
            img = itemView.findViewById(R.id.profile_pic_imageview);
            this.mAdapter = adapter;
        }

    }


}







