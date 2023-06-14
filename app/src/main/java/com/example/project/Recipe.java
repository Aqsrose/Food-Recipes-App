package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Recipe extends MainActivity {

    ImageView imgRecipeImage;
    TextView tvRecipeTitle, tvRecipeCategory, tvRecipeTime, tvRecipeDetail;
    Button btnOpenChatGPT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Recipe Activity", "Running");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe);
        imgRecipeImage =  findViewById(R.id.imgRecipeImage);
        tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        tvRecipeCategory = findViewById(R.id.tvRecipeCategory);
        tvRecipeTime = findViewById(R.id.tvRecipeTime);
        tvRecipeDetail = findViewById(R.id.tvRecipeDetail);

        Intent intent = getIntent();
        String recipeTitle = intent.getStringExtra("recipeTitle");
        String recipeCategory = intent.getStringExtra("recipeCategory");
        String recipeTime = intent.getStringExtra("recipeTime");
        String recipeDetail = intent.getStringExtra("recipeDetail");
        byte[] recipeImage = intent.getByteArrayExtra("recipeImage");

        Bitmap bitmap = BitmapFactory.decodeByteArray(recipeImage, 0, recipeImage.length);
        imgRecipeImage.setImageBitmap(bitmap);
        tvRecipeTitle.setText(recipeTitle);
        tvRecipeCategory.setText(recipeCategory);
        tvRecipeTime.setText(recipeTime);
        tvRecipeDetail.setText(recipeDetail);
    }
}
