package com.example.project.models;

import java.sql.Time;

public class Bookmark {
    String userId;
    String recipeId;
    String createdAt;

    public Bookmark(String userId, String recipeId, String createdAt) {
        this.userId = userId;
        this.recipeId = recipeId;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
