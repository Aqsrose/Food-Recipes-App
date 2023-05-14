package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project.controllers.SignInController;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class SignIn extends AppCompatActivity {

    Button btnSignup;
    Button btnSignIn;

    EditText etUsername;
    EditText etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignup = findViewById(R.id.btnSignUp);
        etUsername = findViewById(R.id.etUsernameSignin);
        etPassword = findViewById(R.id.etPasswordSignin);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                SignInController signInController = new SignInController();
                String json = signInController.buildJson(username, password);

                //to hold the reference to response, so it can be modified
                final AtomicReference<String> responseRef = new AtomicReference<>("");

                new Thread(()->{
                    try {
                        String response = signInController.post("http://10.0.2.2:3000/api/users/login", json);
                        responseRef.set(response);
                    }
                    catch(IOException ex){
                        Log.d("POST EXCEPTION", ex.toString());
                    }
                    Log.d("SERVER RESPONSE", responseRef.get());
                }).start();
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });
    }
}