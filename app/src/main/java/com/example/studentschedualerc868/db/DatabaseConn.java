package com.example.studentschedualerc868.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.studentschedualerc868.db.Entities.User;
import com.example.studentschedualerc868.db.DAOs.UserDAO;

@Database(entities = {User.class}, version = 1)
public abstract class DatabaseConn extends RoomDatabase {

    public abstract UserDAO getUserDAO();

    private static DatabaseConn INSTANCE;


    public static DatabaseConn getDBInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseConn.class, "DB_NAME")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;

    }
}
