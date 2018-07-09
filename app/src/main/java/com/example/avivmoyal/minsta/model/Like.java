package com.example.avivmoyal.minsta.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Aviv Moyal on 25/11/2018.
 */

@IgnoreExtraProperties
public class Like implements Serializable {
    public String imageId;
    public String userId;

    public Like() {
        // Default constructor required for calls to DataSnapshot.getValue(Like.class)
    }

    public Like(String imageId, String userId) {
        this.imageId = imageId;
        this.userId = userId;
    }
}