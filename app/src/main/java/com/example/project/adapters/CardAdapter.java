package com.example.project.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.custom_views.CardViewHolder;
import com.example.project.models.CardDataModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CardAdapter extends RecyclerView.Adapter<CardViewHolder> {
    private List<CardDataModel> data;


    public CardAdapter(List<CardDataModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        // which card we're currently on
        CardDataModel item = data.get(position);

        holder.titleTextView.setText(item.getTitle());
        holder.detailTextView.setText(item.getDetail());
        holder.timeTextView.setText(item.getTime());
        holder.chipCategoryView.setText(item.getCategories());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(item.getImageUrl());
        File localFile;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // The file download was successful, load the image into the ImageView using Picasso
                Picasso.get().load(localFile).into(holder.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors that occurred during the file download
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clearData() {
        data.clear();
        notifyDataSetChanged();
    }
}
