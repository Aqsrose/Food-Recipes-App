package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.project.models.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class SignUp extends AppCompatActivity {
    public static final int PICK_IMAGE_INTENT_GALLERY = 1;
    public static final int PICK_IMAGE_INTENT_CAMERA = 2;
    Button btnSignIn, btnSignUp, btnUploadImage;
    ProgressBar progressBar;
    EditText etUsername, etEmail, etPassword, etConfirmPassword;
    ImageView uploadedImage;
    TextView tvImageLabel;
    FirebaseFirestore db;
    FirebaseAuth auth;


    private void showOptionsPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnUploadImage);
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

    void createUser(String userId, String username, String imageUrl) {
        User user = new User(username, imageUrl);
        //get the collection reference
        CollectionReference userReference = db.collection("users");
        userReference.document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    // Additional user information saved successfully
                    Snackbar.make(btnSignIn, "User added to collection", Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    btnSignUp.setVisibility(View.VISIBLE);
                    btnSignUp.setEnabled(true);
                    Intent intent = new Intent(getApplicationContext(), SignIn.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Failed to save additional user information
                    Snackbar.make(btnSignIn, "Error adding user to collection", Snackbar.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    btnSignUp.setVisibility(View.VISIBLE);
                    btnSignUp.setEnabled(true);
                });
    }

    void uploadImageToFireBaseStorage(String userId) {
        Bitmap bitmap = ((BitmapDrawable) uploadedImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageData = baos.toByteArray();
        String imageName = UUID.randomUUID().toString() + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + imageName);

        // Create an UploadTask to upload the byte array to Firebase Storage
        UploadTask uploadTask = storageRef.putBytes(imageData);

        // Monitor the progress of the upload and handle any errors
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Image upload successful
                // You can now retrieve the download URL or perform any other operations
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // The download URL is available in the uri variable
                    String imageUrl = uri.toString();
                    String username = etUsername.getText().toString().trim();
                    createUser(userId, username, imageUrl);
                    // Perform any further operations with the download URL
                }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnSignUp.setVisibility(View.VISIBLE);
                    btnSignUp.setEnabled(true);
                });
            } else {
                // Image upload failed
                progressBar.setVisibility(View.GONE);
                btnSignUp.setVisibility(View.VISIBLE);
                btnSignUp.setEnabled(true);
            }
        });
    }

    void createUserInAuth() {
        btnSignUp.setVisibility(View.GONE);
        btnSignUp.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        //create the user in firebase auth
        auth.createUserWithEmailAndPassword(etEmail.getText().toString().trim(), etPassword.getText().toString().trim())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //get the current user, and get the id
                        FirebaseUser user = auth.getCurrentUser();
                        String userId = user.getUid();
                        uploadImageToFireBaseStorage(userId);
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Snackbar.make(btnSignUp, "Email already exists", Snackbar.LENGTH_LONG).show();
                        } else if (task.getException() instanceof FirebaseAuthEmailException) {
                            Snackbar.make(btnSignUp, "Enter valid email and try again", Snackbar.LENGTH_LONG).show();
                        } else {
                            // Failed to create user
                            Snackbar.make(btnSignUp, "Failed registering user", Snackbar.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            btnSignUp.setVisibility(View.VISIBLE);
                            btnSignUp.setEnabled(true);
                        }
                        progressBar.setVisibility(View.GONE);
                        btnSignUp.setVisibility(View.VISIBLE);
                        btnSignUp.setEnabled(true);
                    }
                });
    }

    //check if user is logged in
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    boolean validateData() {
        String email = etEmail.getText().toString().trim();
        if (!email.matches("^[A-Za-z\\d+_.-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,}$")) {
            // Invalid email format
            etEmail.setError("Invalid email address");
            return false;
        }

        String username = etUsername.getText().toString().trim();
        if (!username.matches("^(?=.*[a-zA-Z])[a-zA-Z\\d_\\s]{3,20}$")) {
            // Invalid username format
            etUsername.setError("Username must be 3-20 characters long and contain only alphanumeric characters and underscore");
            return false;
        }

        String password = etPassword.getText().toString().trim();
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            // Invalid password format
            etPassword.setError("Password must be at least 8 characters long and contain at least one lowercase letter, one uppercase letter, one digit, and one special character");
            return false;
        }

        String confirmPassword = etConfirmPassword.getText().toString().trim();
        if (!confirmPassword.equals(password)) {
            // Password and confirm password do not match
            etConfirmPassword.setError("Passwords do not match");
            return false;
        }
        if (uploadedImage.getDrawable() == null) {
            // Image is not loaded in the ImageView
            // Display an error message or handle accordingly
            Snackbar.make(btnSignIn, "Please select an image", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        uploadedImage = findViewById(R.id.uploadedImage);
        tvImageLabel = findViewById(R.id.tvImageLabel);
        progressBar = findViewById(R.id.progressBar);

        //input fields
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnUploadImage.setOnClickListener(v -> showOptionsPopupMenu());

        btnSignUp.setOnClickListener(view -> {
            if (!validateData()) return;
            createUserInAuth();
        });
        btnSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT_CAMERA || requestCode == PICK_IMAGE_INTENT_GALLERY) {
                Uri imageUri = data.getData();
                tvImageLabel.setText("Image selected");
                Picasso.get().load(imageUri).into(uploadedImage);
            }
        }
    }
}