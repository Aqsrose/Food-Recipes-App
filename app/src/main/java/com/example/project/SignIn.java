package com.example.project;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignIn extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth auth;
    Button btnSignup;
    Button btnSignIn;

    EditText etUsername;
    EditText etPassword;
    ProgressBar progressBarSignIn;

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

    //made a separate function bc it's being used twice
    private void moveToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //to ensure a fresh main activity (prevent any previous saved state from being loaded)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); //because we don't want to return here after login
    }

    private boolean validate() {
        AwesomeValidation validator = new AwesomeValidation(BASIC); //for displaying error msg under the input field
        validator.addValidation(etUsername, RegexTemplate.NOT_EMPTY, "This field is required");
        validator.addValidation(etUsername, Patterns.EMAIL_ADDRESS, "Please enter a valid email");
        validator.addValidation(etPassword, RegexTemplate.NOT_EMPTY, "This field is required");
        return validator.validate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignup = findViewById(R.id.btnSignUp);
        etUsername = findViewById(R.id.etEmailSignin);
        etPassword = findViewById(R.id.etPasswordSignin);
        progressBarSignIn = findViewById(R.id.progressBarSignIn);


        btnSignIn.setOnClickListener(view -> {
            String email = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (!validate()) {
                return;
            }

            btnSignIn.setVisibility(View.GONE);
            btnSignIn.setEnabled(false);
            progressBarSignIn.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            moveToMainActivity();
                        } else {
                            Snackbar.make(btnSignIn, "Authentication failed", Snackbar.LENGTH_LONG).show();
                            progressBarSignIn.setVisibility(View.GONE);
                            btnSignIn.setVisibility(View.VISIBLE);
                            btnSignIn.setEnabled(true);

                        }
                    });

        });
        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(SignIn.this, SignUp.class);
            startActivity(intent);
        });
    }
}