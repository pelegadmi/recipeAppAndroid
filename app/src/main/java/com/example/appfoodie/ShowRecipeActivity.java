package com.example.appfoodie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.appfoodie.Adapters.RandomRecipeAdapter;
import com.example.appfoodie.Listeners.RecipeClickListener;
import com.example.appfoodie.Models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ShowRecipeActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser userLoggedIn;
    DataBaseHelper DB;
    RandomRecipeAdapter randomRecipeAdapter;
    Button createRecipe;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipe);

        firebaseAuth = FirebaseAuth.getInstance();
        DB = new DataBaseHelper(this);


        userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();

        createRecipe = findViewById(R.id.create_recipe);

        createRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(ShowRecipeActivity.this, CreateRecipeActivity.class);
            startActivity(intent);
            finish();
        });
        ArrayList<Recipe> recipes =  DB.getUserRecipe(userLoggedIn.getUid());

        recyclerView = findViewById(R.id.recycler_random_show);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(ShowRecipeActivity.this, 1));

        randomRecipeAdapter = new RandomRecipeAdapter(ShowRecipeActivity.this, recipes, recipeClickListener);
        recyclerView.setAdapter(randomRecipeAdapter);

    }

    private final RecipeClickListener recipeClickListener = (recipe) -> startActivity(new Intent(ShowRecipeActivity.this, RecipeDetailsActivity.class)
            .putExtra("recipe", recipe));
}