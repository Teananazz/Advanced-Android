package com.example.advancedandroid.adapters;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.advancedandroid.MessagingActivity;
import com.example.advancedandroid.R;
import com.example.advancedandroid.models.Contact;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> list;
    private LayoutInflater ContactInflater;
    private Context AppContext;
    private String Token_bear;
    public ContactAdapter(Context context,
                          List<Contact> List, String Token) {
       ContactInflater = LayoutInflater.from(context);
       list = List;
       AppContext = context;
       Token_bear = Token; // identifier of user who is seeing messaging screen.
    }

    public ContactAdapter(List<Contact> list) {
        this.list = list;
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
        holder.item_chatname.setText(contact.getNickName());
        holder.item_last_message.setText(contact.getLastMessage());
        holder.item_lastm_time.setText(contact.getLastDate());
        holder.UserName = contact.getUserName();

    }

    @Override
    public int getItemCount() {
       if(list != null) {
           return list.size();
       }
       return 0;
    }
    // we implement on click so we can click contacts.
    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView item_chatname;
        public final TextView item_last_message;
        public final TextView item_lastm_time;
        public final CardView card;
         public final ImageView img;
        final ContactAdapter mAdapter;
        private String UserName;

        // this method makes it so that we access all  these fields in onbindViewholder method.
        public ContactViewHolder(View itemView, ContactAdapter adapter) {
            super(itemView);
            item_chatname = itemView.findViewById(R.id.chat_name);
            item_last_message = itemView.findViewById(R.id.recent_message);
            item_lastm_time = itemView.findViewById(R.id.recent_time);
            card = itemView.findViewById(R.id.parent_image_view);
            img = itemView.findViewById(R.id.profile_pic_imageview);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {


            Intent TransferApp = new Intent(AppContext.getApplicationContext(), MessagingActivity.class);
            TransferApp.addFlags(FLAG_ACTIVITY_NEW_TASK); // must be added else you can't start activity from non-activity.
            TransferApp.putExtra("Token", mAdapter.Token_bear);
            TransferApp.putExtra("Nickname", item_chatname.getText().toString());
            TransferApp.putExtra("UserName", UserName);
            AppContext.getApplicationContext().startActivity(TransferApp);

        }
    }


}







