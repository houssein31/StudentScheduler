package com.example.studentschedualerc868.db.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.studentschedualerc868.db.Entities.User;

@Dao
public interface UserDAO {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM user WHERE user_name = :username")
    User getUserByUsername(String username);


    @Query("SELECT full_name FROM user WHERE user_name = :username")
    String getFullName(String username);

}

