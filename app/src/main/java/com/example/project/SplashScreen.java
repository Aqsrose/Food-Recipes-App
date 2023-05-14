package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    private static  int SPLASH_SCREEN_TIME_OUT=4000;
//    Handler handler;
    Animation zoom;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        zoom= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom);
        image=findViewById(R.id.image);
        image.startAnimation(zoom);

        Handler h=new Handler();
        long delayMillis;
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN_TIME_OUT);



//        getSupportActionBar().hide();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent=new Intent(SplashScreen.this, SignIn.class);
//                startActivity(intent);
//                finish();
//            }
//        },SPLASH_SCREEN_TIME_OUT);
    }
}