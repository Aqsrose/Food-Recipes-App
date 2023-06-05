package com.example.project.models;

public class User {


    public User(String userId, String username, String imageUrl) {
        this.userId = userId;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    String userId;
    String username;
    String imageUrl;

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
