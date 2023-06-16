package com.example.project.models;

import java.util.ArrayList;
import java.util.List;

public class CardDataModel {
    private String recipeId;
    private String title;
    private String imageUrl;
    //private List<String> categories;
    private String categories;
    private String detail;
    private String time;

    public CardDataModel(String recipeId, String title, String imageUrl, String categories, String detail, String time) {
        this.recipeId = recipeId;
        this.title = title;
        this.imageUrl = imageUrl;
        //this.categories = new ArrayList<String>(); //change this back to array maybe?
        this.categories = categories;
        this.detail = detail;
        this.time = time;
    }



    public void setId(String recipeId) {
        this.recipeId = recipeId;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getRecipeId() {
        return recipeId;
    }
    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategories() {
        return categories;
    }

    public String getDetail() {
        return detail;
    }

    public String getTime() {
        return time;
    }
}
