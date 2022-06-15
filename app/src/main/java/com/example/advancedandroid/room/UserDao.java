package com.example.advancedandroid.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.advancedandroid.models.User;

import java.util.List;

    @Dao
    public interface UserDao {

        @Query("SELECT * FROM User")
        List<User> index();

        @Query("SELECT * FROM User WHERE userName = :user")
        User get(String user);

        @Insert
        void insert(User... users);

        @Insert (onConflict = OnConflictStrategy.IGNORE)
        void insertAll(List<User> list);

        @Update
        void update(User... users);

        @Delete
        void delete(User... users);
    }




