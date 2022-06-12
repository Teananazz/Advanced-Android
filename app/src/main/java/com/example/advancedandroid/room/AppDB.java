package com.example.advancedandroid.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.advancedandroid.models.Contact;

@Database(entities = {Contact.class}, version = 1)
public abstract class AppDB extends RoomDatabase {
    public abstract ContactDao contactDao();
}
