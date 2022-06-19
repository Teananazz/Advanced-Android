package com.example.advancedandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.advancedandroid.adapters.ContactAdapter;
import com.example.advancedandroid.models.Contact;
import com.example.advancedandroid.room.AppDB;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsFragment extends Fragment {
    private String MainUser;
    private String BearToken;
    private AppActivity Activity;
    RecyclerView RecyclerView;
    public ContactsFragment() {

        super(R.layout.contactsfragment);



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.contactsfragment, container, false);
        Activity = (AppActivity) getActivity();
        Button b = view.findViewById(R.id.settingsButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), SettingsActivity.class));
            }
        });
        if(Activity != null) {

            BearToken = Activity.Token_Bear;
            MainUser = Activity.user;
            Activity.RecyclerView = view.findViewById(R.id.chats_recyclerview);

            // make it visible only if no contacts
            Activity.EmptyIndicator = view.findViewById(R.id.tutorial);

            Activity.db = Room.databaseBuilder(Activity.getApplicationContext(), AppDB.class, "ContactDB")
                    .allowMainThreadQueries()
                    .build();

            Activity.Current_Contacts = Activity.db.contactDao().index(Activity.user);

            Activity.Current_Contacts = Activity.db.contactDao().index(MainUser);
            if( Activity.Current_Contacts == null) {
                Activity.Current_Contacts = new ArrayList<Contact>();
            }

            Activity.Adapter = new ContactAdapter(view.getContext(), Activity.Current_Contacts, Activity.Token_Bear, Activity.user, Activity.Users);
            Activity.RecyclerView.setAdapter(Activity.Adapter);
            Activity.RecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            // meanwhile.
            Activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getContacts(Activity.Token);
                    Activity.CheckUserList();
                }
            });
        }

        return view;

    }



    void getContacts(String Token) {

        Call<List<Contact>>  call = Activity.api.GetContacts(Activity.Token_Bear);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call , Response<List<Contact>> response) {

                List<Contact> ServerContacts = response.body();
                if(ServerContacts == null ) {
                    ServerContacts = new ArrayList<Contact>();
                }

                if(ServerContacts.size() > 0) {

                    for(int  i = 0 ; i< ServerContacts.size() ; i++) {
                        ServerContacts.get(i).setUsernameOfLooker(Activity.user);
                    }
                }

                if(Activity.Current_Contacts.isEmpty() && ServerContacts.size() == 1) {
                    int size = 0;
                    Activity.Current_Contacts.add(ServerContacts.get(0));
                    Activity.Adapter.notifyItemInserted(0);

                }
                // if we just added a contact
                else if(ServerContacts.size() == (Activity.Current_Contacts.size() + 1) ) {
                    int size = Activity.Current_Contacts.size();
                    int size2 = ServerContacts.size();
                    Activity.Current_Contacts.add(ServerContacts.get(size2 - 1));
                    Activity.Adapter.notifyItemInserted(size2 - 1);

                }
                else {
                    // this when we update potentially everyone if they changed info.
                    Activity.Current_Contacts.clear();
                    Activity.Current_Contacts.addAll(ServerContacts);
                    Activity.Adapter.notifyDataSetChanged();
                }

                // we use Insert all because replace policy is replace in case details were updated.
                if(!Activity.Current_Contacts.isEmpty()) {
                    Activity.db.contactDao().InsertAll(Activity.Current_Contacts);
                }

                if (Activity.Current_Contacts.size() > 0 && Activity.EmptyIndicator.getVisibility() == View.VISIBLE) {
                    Activity. EmptyIndicator.setVisibility(View.INVISIBLE);
                }

                if( Activity.Current_Contacts.isEmpty() && Activity.EmptyIndicator.getVisibility() == View.INVISIBLE) {
                    // if no contacts then we show hint to add contact.
                    Activity.EmptyIndicator.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Toast.makeText(Activity.getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();
                Log.d("Error222: ", t.toString());
            }

        });




    }




}
