package com.example.project.custom_views;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

public class CardViewHolder extends RecyclerView.ViewHolder {
    public MaterialCardView cardView;
    public ImageView imageView;
    public TextView titleTextView;
    public TextView detailTextView;
    public TextView timeTextView;
    public Chip chipCategoryView;
    public Button btnBookmarkView;

    public CardViewHolder(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.recipeCard);
        imageView = itemView.findViewById(R.id.recipeImage);
        titleTextView = itemView.findViewById(R.id.recipeTitle);
        detailTextView = itemView.findViewById(R.id.recipeDetail);
        timeTextView = itemView.findViewById(R.id.recipeTime);
        chipCategoryView = itemView.findViewById(R.id.chipCategory);
        btnBookmarkView = itemView.findViewById(R.id.btnBookmark);
    }
}

//this class only serves to set the instance of every card view