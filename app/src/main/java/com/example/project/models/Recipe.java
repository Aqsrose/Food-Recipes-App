package com.example.project.models;

public class Recipe {
    String userId;
    String title;
    String detail;
    String category;
    String time;
    String image;

    public Recipe(String userId, String title, String detail, String category, String time, String image) {
        this.title = title;
        this.detail = detail;
        this.category = category;
        this.time = time;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
