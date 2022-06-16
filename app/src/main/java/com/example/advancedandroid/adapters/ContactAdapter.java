package com.example.advancedandroid.adapters;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
import com.example.advancedandroid.models.User;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<Contact> list;
    private List<User> listUser;
    private LayoutInflater ContactInflater;
    private Context AppContext;
    private String Token_bear;
    private String UserHost;


    public ContactAdapter(Context context,
                          List<Contact> List, String Token, String UserHost, List<User> listUser) {
       ContactInflater = LayoutInflater.from(context);
       list = List;
       AppContext = context;
       Token_bear = Token; // identifier of user who is seeing messaging screen.
       this.UserHost = UserHost;
       this.listUser = listUser;
    }

    public ContactAdapter(List<Contact> list, List<User> listUser) {
        this.listUser = listUser;
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


        // TODO: get the img from Users so that we can decode it and put in the picture.




        Contact contact = list.get(position);
        String images = null;
        User acc = null;
        if(listUser!=null) {

            for(int i = 0 ; i < listUser.size() ; i++) {
                String username_1 = listUser.get(i).getUserName();
                String username_2 = contact.getUsername();
                if(username_1.equals(username_2)) {
                    acc = listUser.get(i);
                    break;
                }

            }

        }



        if(listUser!=null && acc!=null) {
            images = acc.getImg();
            if(images != null) {
                byte[] decodedString = Base64.decode(images, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                holder.img.setImageBitmap(decodedByte);
            }

        }



        holder.item_chatname.setText(contact.getNickName());
        holder.item_last_message.setText(contact.getLastMessage());
        holder.item_lastm_time.setText(contact.getLastDate());
        holder.UserName = contact.getUserName();
        holder.UserNameHost = UserHost;
        holder.Server = contact.getServer();


    }

    @Override
    public int getItemCount() {
       if(list != null) {
           return list.size();
       }
       return 0;
    }
    // we implement on click so we can click contacts.
    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView item_chatname;
        public final TextView item_last_message;
        public final TextView item_lastm_time;
        public final CardView card;
        public final ImageView img;
        final ContactAdapter mAdapter;
        private String UserName;
        private String Server;
        private String UserNameHost;
        public Context AppContext;


        // this method makes it so that we access all these fields in onbindViewholder method.
        public ContactViewHolder(View itemView, ContactAdapter adapter) {
            super(itemView);
            item_chatname = itemView.findViewById(R.id.chat_name);
            item_last_message = itemView.findViewById(R.id.recent_message);
            item_lastm_time = itemView.findViewById(R.id.recent_time);
            card = itemView.findViewById(R.id.parent_image_view);
            img = itemView.findViewById(R.id.profile_pic_imageview);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
            this.AppContext = adapter.AppContext;
        }

        @Override
        public void onClick(View view) {


            Intent TransferApp = new Intent(AppContext.getApplicationContext(), MessagingActivity.class);
            TransferApp.addFlags(FLAG_ACTIVITY_NEW_TASK); // must be added else you can't start activity from non-activity.
            TransferApp.putExtra("Token", mAdapter.Token_bear);
            TransferApp.putExtra("Nickname", item_chatname.getText().toString());
            TransferApp.putExtra("UserName", UserName);
            TransferApp.putExtra("HostUser", UserNameHost);
            TransferApp.putExtra("Server", Server);
            AppContext.getApplicationContext().startActivity(TransferApp);

        }
    }


}







