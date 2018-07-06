package com.example.avivmoyal.minsta.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Aviv Moyal on 25/11/2018.
 */
@IgnoreExtraProperties
public class Post {
    public String key;
    public String userId;
    public String downloadUrl;
    public String text;

    // these properties will not be saved to the database
    @Exclude
    public User user;

    @Exclude
    public int likes = 0;

    @Exclude
    public boolean hasLiked = false;

    @Exclude
    public String userLike;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Post(String key, String userId, String downloadUrl) {
        this.key = key;
        this.userId = userId;
        this.downloadUrl = downloadUrl;
    }

    public void addLike() {
        this.likes++;
    }

    public void removeLike() {
        this.likes--;
    }
}