package com.example.avivmoyal.minsta.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Aviv Moyal on 25/11/2018.
 */
@IgnoreExtraProperties
public class User implements Serializable{
    public String uid;
    public String displayName;
    public String token;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String displayName, String token) {
        this.uid = uid;
        this.displayName = displayName;
        this.token = token;
    }
}