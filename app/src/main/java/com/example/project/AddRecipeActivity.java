package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.example.project.models.Recipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.IllegalFormatPrecisionException;
import java.util.UUID;

public class AddRecipeActivity extends AppCompatActivity {

    public static final int PICK_IMAGE_INTENT_GALLERY = 1;
    public static final int PICK_IMAGE_INTENT_CAMERA = 1;
    Button timeDurationPickerButton, btnUploadRecipeImage, btnUploadRecipe;
    TextView tvTimeDuration, tvRecipeImageLabel;
    EditText etRecipeTitle, etRecipeDetail;
    Spinner spinnerCategory;
    ImageView imgRecipeImage;
    ProgressBar progressBarRecipe;
    FirebaseStorage storage;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;

    private void showOptionsPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnUploadRecipeImage);
        popupMenu.getMenuInflater().inflate(R.menu.upload_options_menu, popupMenu.getMenu());

        // Set click listener for menu items
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_gallery:
                    Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intentGallery, PICK_IMAGE_INTENT_GALLERY);
                    return true;
                case R.id.menu_camera:
                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentCamera, PICK_IMAGE_INTENT_CAMERA);
                    return true;
                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    void uploadImageToFireStorage(){
        Bitmap bitmap = ((BitmapDrawable) imgRecipeImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageData = baos.toByteArray();
        String imageName = UUID.randomUUID().toString() + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/recipes/" + imageName);

        // Create an UploadTask to upload the byte array to Firebase Storage
        UploadTask uploadTask = storageRef.putBytes(imageData);

        // Monitor the progress of the upload and handle any errors
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Image upload successful
                // You can now retrieve the download URL or perform any other operations
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {

                    String userId = user.getUid();
                    String title = etRecipeTitle.getText().toString().trim();
                    String detail = etRecipeDetail.getText().toString().trim();
                    String category = spinnerCategory.getSelectedItem().toString();
                    String time = tvTimeDuration.getText().toString().trim();
                    String imageUrl = uri.toString();

                    Log.d("User ID", userId);

                    Recipe recipe = new Recipe(userId, title, detail, category, time, imageUrl);
                    CollectionReference recipeCollection = db.collection("recipe");
                    recipeCollection.add(recipe)
                            .addOnSuccessListener(aVoid -> {
                                Snackbar.make(btnUploadRecipe, "Recipe Uploaded", Snackbar.LENGTH_LONG).show();
                                progressBarRecipe.setVisibility(View.GONE);
                                btnUploadRecipe.setVisibility(View.VISIBLE);
                                btnUploadRecipe.setEnabled(true);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                // Failed to save additional user information
                                Snackbar.make(btnUploadRecipe, "Error uploading recipe", Snackbar.LENGTH_LONG).show();
                                progressBarRecipe.setVisibility(View.GONE);
                                btnUploadRecipe.setVisibility(View.VISIBLE);
                                btnUploadRecipe.setEnabled(true);
                            });

                }).addOnFailureListener(e -> {
                    progressBarRecipe.setVisibility(View.GONE);
                    btnUploadRecipe.setVisibility(View.VISIBLE);
                    btnUploadRecipe.setEnabled(true);
                });
            } else {
                // Image upload failed
                progressBarRecipe.setVisibility(View.GONE);
                btnUploadRecipe.setVisibility(View.VISIBLE);
                btnUploadRecipe.setEnabled(true);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        etRecipeTitle = findViewById(R.id.etRecipeTitle);
        etRecipeDetail = findViewById(R.id.etRecipeDetail);
        timeDurationPickerButton = findViewById(R.id.timeDurationPickerButton);
        tvTimeDuration = findViewById(R.id.tvTimeDuration);
        tvRecipeImageLabel = findViewById(R.id.tvRecipeImageLabel);
        btnUploadRecipeImage = findViewById(R.id.btnUploadRecipeImage);
        btnUploadRecipe = findViewById(R.id.btnUploadRecipe);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imgRecipeImage = findViewById(R.id.imgRecipeImage);
        progressBarRecipe = findViewById(R.id.progressBarRecipe);
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        timeDurationPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker numberPicker = new NumberPicker(AddRecipeActivity.this);
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(7);
                numberPicker.setDisplayedValues(new String[]{"10 minutes", "20 minutes", "30 minutes", "45 minutes", "1 hr", "2 hr", "3 hr", "4 hr"});

                AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipeActivity.this);
                builder.setTitle("Select Time Duration");
                builder.setView(numberPicker);
                builder.setPositiveButton("OK", (dialog, which) -> {
                    String selectedDuration = numberPicker.getDisplayedValues()[numberPicker.getValue()];
                    tvTimeDuration.setText(selectedDuration);
                });
                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        btnUploadRecipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptionsPopupMenu();
            }
        });
        btnUploadRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateData()){
                    return;
                }
                btnUploadRecipe.setEnabled(false);
                btnUploadRecipe.setVisibility(View.GONE);
                progressBarRecipe.setVisibility(View.VISIBLE);
                uploadImageToFireStorage();
            }
        });
    }

    //for now the return is true, will add validation later
    boolean validateData() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT_CAMERA || requestCode == PICK_IMAGE_INTENT_GALLERY) {
                Uri imageUri = data.getData();
                tvRecipeImageLabel.setText("Image selected");
                Picasso.get().load(imageUri).into(imgRecipeImage);
            }
        }
    }

}
