package com.example.advancedandroid.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.advancedandroid.models.Contact;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM Contact WHERE  usernameOfLooker = :userName")
    List<Contact> index(String userName);

    @Query("SELECT * FROM Contact WHERE username = :userName")
    Contact get(String userName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contact... contacts);

    // same contact we care because server and last message could be changed.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void InsertAll(List<Contact> contactList);

    @Update
    void update(Contact... contacts);

    @Delete
    void delete(Contact... contacts);
}
