package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.example.project.adapters.CardAdapter;
import com.example.project.models.CardDataModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    CardAdapter cardAdapter;
    RecyclerView recipeRecyclerView;

    FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.drawerMenu);
        toolbar = findViewById(R.id.topAppBar);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        recipeRecyclerView = findViewById(R.id.recipeRecyclerView);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //setting up toolbar and navigation drawer
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.option_logout:
                    firebaseAuth.signOut();
                    Intent intent = new Intent(getApplicationContext(), SignIn.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        //getting the recipes from database
        CollectionReference recipes = db.collection("recipe");
        recipes.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle any errors that occur while fetching data
                    return;
                }

                //to hold the cards data
                List<CardDataModel> cardList = new ArrayList<>();
                // Iterate over the documents in the query snapshot
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    // Extract the necessary data from the document
                    String title = document.getString("title");
                    String imageUrl = document.getString("image");
                    //List<String> category = (List<String>) document.get("category");
                    String category = document.getString("category");
                    String detail = document.getString("detail");
                    String time = document.getString("time");

                    Log.d("Recipe Category", category);
                    // Create a CardDataModel object with the extracted data
                    CardDataModel cardDataModel = new CardDataModel(title, imageUrl, category, detail, time);
                    // Add the CardDataModel object to the adapter
                    cardList.add(cardDataModel);
                }
                cardAdapter = new CardAdapter(cardList);
                recipeRecyclerView.setAdapter(cardAdapter);

                // Notify the adapter that the data has changed
                cardAdapter.notifyDataSetChanged();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddRecipeActivity.class);
                startActivity(intent);
            }
        });
    }


}