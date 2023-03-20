package com.example.appfoodie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appfoodie.Adapters.RandomRecipeAdapter;
import com.example.appfoodie.Listeners.RandomRecipeResponseListener;
import com.example.appfoodie.Listeners.RecipeClickListener;
import com.example.appfoodie.Models.RandomRecipeApiResponse;
import com.example.appfoodie.Models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;
    RequestManager manager;
    RandomRecipeAdapter randomRecipeAdapter;
    RecyclerView recyclerView;
    Spinner spinner;
    List<String> tags = new ArrayList<>();
    SearchView searchView;
    Button LogRegisterButton;
    Button ShowRecipeButton;
    FirebaseAuth firebaseAuth;
    DataBaseHelper DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();

        LogRegisterButton = (Button) findViewById(R.id.LogRegisterButton);
        LogRegisterButton.setOnClickListener(v -> RegisterLoginActivity());

        ShowRecipeButton = (Button) findViewById(R.id.show_recipe);
        ShowRecipeButton.setOnClickListener(v -> createNewRecipe());

        DB = new DataBaseHelper(this);

        if (userLoggedIn == null) {
            LogRegisterButton.setText("Sign in");
            ShowRecipeButton.setVisibility(View.INVISIBLE);
        } else {
            LogRegisterButton.setText("Sign out");
        }

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");

        //searching
        searchView = findViewById(R.id.searchView_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query);
                manager.getRandomRecipes(randomRecipeResponseListener, tags);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        spinner = findViewById(R.id.spinner_tags);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.tags,
                R.layout.spinner_text
        );

        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        manager = new RequestManager(this);

    }

    public void RegisterLoginActivity() {
        FirebaseUser userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();

        if (userLoggedIn == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            FirebaseAuth.getInstance().signOut();
            LogRegisterButton.setText("Sign in");
            ShowRecipeButton.setVisibility(View.INVISIBLE);
        }

    }

    public void createNewRecipe() {
        Intent intent = new Intent(this, ShowRecipeActivity.class);
        startActivity(intent);
    }


    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            dialog.dismiss();
            recyclerView = findViewById(R.id.recycler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
            ArrayList<Recipe> recipes = DB.getAllRecipe();
            recipes.addAll(response.recipes);
            randomRecipeAdapter = new RandomRecipeAdapter(MainActivity.this, recipes, recipeClickListener);
            recyclerView.setAdapter(randomRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);

        }
    };

    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            tags.clear();
            tags.add(parent.getSelectedItem().toString());
            manager.getRandomRecipes(randomRecipeResponseListener, tags);
            dialog.show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(MainActivity.this, RecipeDetailsActivity.class)
                    .putExtra("id", id));

        }
    };


}










