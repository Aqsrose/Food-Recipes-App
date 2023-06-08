package com.example.project.models;

public class User {


    public User(String username, String imageUrl) {
        this.username = username;
        this.imageUrl = imageUrl;
    }

    String username;
    String imageUrl;

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
