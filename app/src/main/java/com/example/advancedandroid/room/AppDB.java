package com.example.advancedandroid.room;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.advancedandroid.models.Contact;
import com.example.advancedandroid.models.Message;
import com.example.advancedandroid.models.User;


@Database(
        entities = {Contact.class, Message.class, User.class},
        version = 4


)

public abstract class AppDB extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract MessageDao messageDao();
    public abstract UserDao UserDao();
}
