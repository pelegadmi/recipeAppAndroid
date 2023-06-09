package com.example.appfoodie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appfoodie.Adapters.IngredientsAdapter;
import com.example.appfoodie.Adapters.InstructionsAdapter;
import com.example.appfoodie.Listeners.InstructionsListener;
import com.example.appfoodie.Listeners.RecipeDetailsListener;
import com.example.appfoodie.Models.Ingredient;
import com.example.appfoodie.Models.InstructionsResponse;
import com.example.appfoodie.Models.Recipe;
import com.example.appfoodie.Models.RecipeDetailsResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    Recipe recipe;
    boolean edit;
    Button recipeEditBtn;
    TextView textView_meal_name, textView_meal_source, textView_meal_summary, textView_ing;
    ImageView imageView_meal_image;
    RecyclerView recycler_meal_ingredients, recycler_meal_instructions;
    RequestManager manager;
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;
    InstructionsAdapter instructionsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        findViews();

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        edit = getIntent().getBooleanExtra("edit", false);

        if (!recipe.pullData) {
            manager = new RequestManager(this);
            manager.getRecipeDetails(recipeDetailsListener, recipe.id);
            manager.getInstructions(instructionsListener, recipe.id);
            dialog = new ProgressDialog(this);
            dialog.setTitle("Loading Details...");
            dialog.show();
        } else {
            textView_meal_name.setText(recipe.title);
            Picasso.get().load(recipe.image).into(imageView_meal_image);

            textView_meal_summary.setText(recipe.summary);
            textView_ing.setVisibility(View.INVISIBLE);

            if (edit) {
                recipeEditBtn.setVisibility(View.VISIBLE);

                recipeEditBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(RecipeDetailsActivity.this, CreateRecipeActivity.class);
                    intent.putExtra("recipe", recipe);
                    startActivity(intent);
                    finish();
                });

            }
        }
    }

    private void findViews() {
        textView_meal_name = findViewById(R.id.textView_meal_name);
        textView_meal_source = findViewById(R.id.textView_meal_source);
        textView_meal_summary = findViewById(R.id.textView_meal_summary);
        textView_ing = findViewById(R.id.show_ing);
        imageView_meal_image = findViewById(R.id.imageView_meal_image);
        recycler_meal_ingredients = findViewById(R.id.recycler_meal_ingredients);
        recycler_meal_instructions = findViewById(R.id.recycler_meal_instructions);
        recipeEditBtn = findViewById(R.id.recipe_edit_btn);

    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            textView_meal_name.setText(response.title);
            textView_meal_source.setText(response.sourceName);
            textView_meal_summary.setText(response.summary);
            Picasso.get().load(response.image).into(imageView_meal_image);

            recycler_meal_ingredients.setHasFixedSize(true);
            recycler_meal_ingredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));

            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            recycler_meal_ingredients.setAdapter(ingredientsAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    };
    private final InstructionsListener instructionsListener = new InstructionsListener() {
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recycler_meal_instructions.setHasFixedSize(true);
            recycler_meal_instructions.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
            instructionsAdapter = new InstructionsAdapter(RecipeDetailsActivity.this, response);
            recycler_meal_instructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {

        }
    };

}