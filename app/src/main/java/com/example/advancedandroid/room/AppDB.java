package com.example.advancedandroid.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.advancedandroid.models.Contact;
import com.example.advancedandroid.models.Message;

@Database(entities = {Contact.class, Message.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract MessageDao messageDao();
}
