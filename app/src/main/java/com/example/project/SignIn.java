package com.example.project;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;
import static com.basgeekball.awesomevalidation.ValidationStyle.COLORATION;
import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.project.controllers.SignInController;
import com.example.project.session.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class SignIn extends AppCompatActivity {

    Button btnSignup;
    Button btnSignIn;

    EditText etUsername;
    EditText etPassword;

    @Override
    protected void onStart() {
        super.onStart();

        //check for user session through the stored email
        SessionManager sessionManager = new SessionManager(SignIn.this);
        String email = sessionManager.getSession();
        if(email != null)
            moveToMainActivity();
    }

    //made a separate function bc it's being used twice
    private void moveToMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //to ensure a fresh main activity (prevent any previous saved state from being loaded)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); //because we don't want to return here after login
    }

    private boolean validate(){
        //String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";

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

                if(!validate()){
                    return;
                }

                SignInController signInController = new SignInController();
                String json = signInController.buildJson(username, password);

                //to hold the reference to response, so it can be modified
                final AtomicReference<SignInController> responses = new AtomicReference<>(null);

                new Thread(()->{
                    try {
                        SignInController responseObject = signInController.post("http://10.0.2.2:3000/api/users/login", json);
                        responses.set(responseObject);
                    }
                    catch(IOException ex){
                        Log.d("POST EXCEPTION", ex.toString());
                    }

                    //parse the response json
                    JsonObject jsonObject = JsonParser.parseString(responses.get().getResponseBody()).getAsJsonObject();

                    if(responses.get().getResponseCode() == 404){
                        Snackbar.make(btnSignIn, "No user found", Snackbar.LENGTH_LONG).show();
                    }
                    else if(responses.get().getResponseCode() == 401){
                        Snackbar.make(btnSignIn, "Wrong password", Snackbar.LENGTH_LONG).show();
                    }
                    else if(responses.get().getResponseCode() == 200){

                        if(jsonObject.get("user") != null){
                            JsonObject user = jsonObject.getAsJsonObject("user");
                            //passing the current context
                            SessionManager sessionManager = new SessionManager(SignIn.this);
                            sessionManager.saveSession(user.get("email").toString());
                            moveToMainActivity();
                        }
                    }
                    else if(responses.get().getResponseCode() == -1){
                        Snackbar.make(btnSignIn, "Error connecting to server", Snackbar.LENGTH_LONG).show();
                    }
                    else{
                        Snackbar.make(btnSignIn, "Server error. Please try again.", Snackbar.LENGTH_LONG).show();
                    }

                }).start();
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }
}