package com.example.studentschedualerc868.db.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    @ColumnInfo(name = "user_name")
    @NonNull
    private String username;

//    @ColumnInfo(name = "term_ids")
//    private List<Integer> termIds;

    @ColumnInfo(name = "full_name")
    private String fullname;
    @ColumnInfo(name = "password")
    private String password;

    public User(String fullname, String username, String password) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
//        this.termIds = termIds;
    }


    public String getFullname() { return fullname; }

    public void setFullname(String fullname) { this.fullname = fullname; }

//    public List<Integer> getTermIds() {
//        return termIds;
//    }
//
//    public void setTermIds(List<Integer> termIds) {
//        this.termIds = termIds;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "user{" +
                ", fullName" + fullname +
                ", userName='" + username + '\'' +
                ", password=" + password +
                '}';
    }

}


