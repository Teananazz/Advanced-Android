package com.example.advancedandroid.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.advancedandroid.models.Message;

import java.util.List;


@Dao
public interface MessageDao {

    @Query("SELECT * FROM Message")
    List<Message> index();

    @Query("SELECT * FROM Message WHERE message = :message")
    Message get(String message);

    @Insert
    void insert(Message... messages);


    // same message we don't care.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Message> list);

    @Update
    void update(Message... messages);

    @Delete
    void delete(Message... messages);
}
