package com.example.avivmoyal.minsta.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Aviv Moyal on 25/11/2018.
 */

@IgnoreExtraProperties
public class Like {
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