package com.example.advancedandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.advancedandroid.R;
import com.example.advancedandroid.adapters.MessageAdapter;
import com.example.advancedandroid.api.Api;
import com.example.advancedandroid.api.RetrofitClient;
import com.example.advancedandroid.models.Message;
import com.example.advancedandroid.room.AppDB;
import com.example.advancedandroid.room.MessageDao;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagingFragment extends Fragment implements View.OnClickListener {
      private String MainUser;
      private String BearToken;
      private RecyclerView recycler;
    private AppActivity Activity;
    private View[] views;
    private View view;

    public MessagingFragment() {

        super(R.layout.activity_messaging);

    }

    private void setReceiver() {

       if(Activity.NotificationGetter == null) {
           Activity.NotificationGetter = new BroadcastReceiver() {
               @Override
               public void onReceive(Context context, Intent intent) {
                   try {
                       String   value= Activity.messageSend;
                       getMessages(Activity.Token_Bear, Activity.UserChoosen, 1);


                   } catch (Exception e) {
                       e.printStackTrace();
                   }

               }
           };
       }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.activity_messaging, container, false);
        Activity = (AppActivity) getActivity();

        setReceiver(); // this is setting the receiver that will get the intent from firebase.

        // this is registering the intent
        LocalBroadcastManager.getInstance((AppActivity)getActivity()).registerReceiver((Activity.NotificationGetter),
                new IntentFilter(FireBaseService.REQUEST_RECEIVE)
        );

       View btn = view.findViewById(R.id.send_msg_btn);
       btn.setOnClickListener(this);

       View btn2 = view.findViewById(R.id.back_btn);
       if(Activity.Orientation.equals("PORTRAIT")) {
           btn2.setVisibility(View.VISIBLE);
           btn2.setOnClickListener(this);
       }
       else {
           btn2.setVisibility(View.INVISIBLE);
       }

        MessageDao messageDao = Activity.dbMess.messageDao();
        Activity.RecyclerViewMessages = view.findViewById(R.id.msg_recyclerview);

        Activity.messageList = messageDao.index(); // didn't do?
        if(Activity.messageList == null) {
            Activity.messageList = new ArrayList<Message>();
        }
        if(Activity.UserChoosen != null) {
            getMessages(Activity.Token_Bear,Activity.UserChoosen, 0);
        }

        Activity.AdapterMess = new MessageAdapter(view.getContext(),  Activity.messageList);
        Activity.RecyclerViewMessages.setAdapter(Activity.AdapterMess);
        Activity.RecyclerViewMessages.setLayoutManager(new LinearLayoutManager(view.getContext()));

        if(Activity != null) {
            View views[] = new View[]{view.findViewById(R.id.receiver_name), view.findViewById(R.id.profile_pic_imageview), view.findViewById(R.id.msg_recyclerview)};
            TextView name = (TextView) views[0];
            name.setText(Activity.NickNameChoose);
            RecyclerView messages = (RecyclerView) views[2];

            ImageView img = (ImageView)views[1];
            img.setImageBitmap(Activity.imgContact);

            if(Activity.UserChoosen != null) {
                getMessages(Activity.Token_Bear, Activity.UserChoosen, 0);
            }
            if( Activity.messageSend!=null) {
                if(Activity.UserChoosen != null) {
                    Activity.messageSend = null;
                    getMessages(Activity.Token_Bear, Activity.UserChoosen, 1);
                }
            }
        }
        return view;
    }



    void getMessages(String Token, String UserNameContact, int flagChanged) {

        Call<List<Message>> call = Activity.api.getMessages(Token, UserNameContact);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {

                List<Message> entry = response.body();

                // for now we get all the fields from database.
                if(flagChanged == 1) {
                    Activity.AdapterMess.list.add(entry.get(entry.size() - 1));
                    Activity.AdapterMess.notifyItemInserted(Activity.messageList.size() - 1);
                    Activity.RecyclerViewMessages.scrollToPosition(Activity.messageList.size() - 1);

                }
                else {

                    List<Message> list = response.body();

                    if (list == null) {
                        list = new ArrayList<Message>();
                    }

                    int size_1 = list.size();
                    int size_2 = Activity.messageList.size();

                    if (Activity.messageList.size() < list.size()) {

                        Activity.messageList.clear();
                        Activity.messageList.addAll(list);
                        Activity.AdapterMess.notifyItemRangeInserted(size_2, size_1 - 1);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(view.getContext(), "An error has occured", Toast.LENGTH_LONG).show();
                Log.d("ErrorMessageSending: ", t.toString());
            }

        });


    }


    public void SendMessage(View viewing)  {

        TextView f = view.findViewById(R.id.typing_space);
        String message = f.getText().toString();

        Call <Void>  call = Activity.api.PostMessages(Activity.Token_Bear, Activity.UserChoosen,  message);
        call.enqueue(new Callback <Void>() {
            @Override
            public void onResponse(Call <Void> call, Response <Void> response) {

                f.setText("");

                getMessages(Activity.Token_Bear, Activity.UserChoosen, 1);

                SendTransferRequest(Activity.user , Activity.UserChoosen, message, Activity.ServerChosen);

            }

            @Override
            public void onFailure(Call <Void> call, Throwable t) {
                Toast.makeText(view.getContext(), "An error has occured", Toast.LENGTH_LONG).show();
                Log.d("Error222: ", t.toString());
            }

        });


    }


    public void SendTransferRequest(String from, String to, String message, String server) {

        String serv = server;
        Api custom = null;

        if(server.contains("localhost")) {
            server = server.replace("localhost", "10.0.0.2");

        }
        // if it is not equal to Base url, we have to create another retrofit object.
        if(!server.equals("10.0.0.2:7179") ) {

            RetrofitClient.getInstance().AddClient("https://".concat(server).concat("/api/"));
            custom =  RetrofitClient.getInstance().getCustom_Api();


        }

        String arr[] ={ from, to, message};
        Call<Void> call;
        // will only enter custom if different server address than user.
        if( custom != null) {
            call = custom.SendTransfer(arr);
        }
        else {
            call = Activity.api.SendTransfer(arr);
        }

        call.enqueue(new Callback <Void>() {
            @Override
            public void onResponse(Call <Void> call, Response <Void> response) {

                Response k = response;

            }

            @Override
            public void onFailure(Call <Void> call, Throwable t) {
                Toast.makeText(view.getContext(), "An error has occured", Toast.LENGTH_LONG).show();
                Log.d("Error222: ", t.toString());
            }

        });


    }

    @Override
    public void onClick(View view) {

        if( view.getId() == R.id.send_msg_btn) {

            SendMessage(view);
        }
        else if(view.getId() == R.id.back_btn) {
            if(Activity.Orientation.equals("PORTRAIT")) {
               Activity.getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, ContactsFragment.class, null)
                        .commit();
            }

        }


    }
}
