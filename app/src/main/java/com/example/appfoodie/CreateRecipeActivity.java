package com.example.appfoodie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appfoodie.Models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateRecipeActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser userLoggedIn;
    Button create;
    DataBaseHelper DB;
    EditText titleEt, servingsEt, readyInMinutesEt, urlEt, summaryEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        firebaseAuth = FirebaseAuth.getInstance();
        DB = new DataBaseHelper(this);

        create = (Button) findViewById(R.id.create_recipe);
        titleEt = (EditText) findViewById(R.id.create_title);
        servingsEt = (EditText) findViewById(R.id.create_servings);
        readyInMinutesEt = (EditText) findViewById(R.id.create_readyInMinutes);
        urlEt = (EditText) findViewById(R.id.create_URL);
        summaryEt = (EditText) findViewById(R.id.create_summary);

        userLoggedIn = FirebaseAuth.getInstance().getCurrentUser();


        create.setOnClickListener(v -> {
            titleEt.setTextColor(Color.BLACK);
            titleEt.setHintTextColor(Color.BLACK);

            servingsEt.setTextColor(Color.BLACK);
            servingsEt.setHintTextColor(Color.BLACK);

            readyInMinutesEt.setTextColor(Color.BLACK);
            readyInMinutesEt.setHintTextColor(Color.BLACK);

            urlEt.setTextColor(Color.BLACK);
            urlEt.setHintTextColor(Color.BLACK);

            summaryEt.setTextColor(Color.BLACK);
            summaryEt.setHintTextColor(Color.BLACK);


            String title = String.valueOf(titleEt.getText()),
                    url = String.valueOf(urlEt.getText()),
                    readyInMinutesStr = String.valueOf(readyInMinutesEt.getText()),
                    servingsStr = String.valueOf(servingsEt.getText()),
                    summary = String.valueOf(summaryEt.getText());

            if (title.length() > 0 && url.length() > 0 && summary.length() > 0 && tryParse(readyInMinutesStr) &&
                    tryParse(servingsStr)) {
                Recipe recipe = new Recipe(userLoggedIn.getUid(), Integer.parseInt(readyInMinutesStr), Integer.parseInt(servingsStr), url, title, summary);
                DB.insertData(recipe);
                Intent intent = new Intent(CreateRecipeActivity.this, ShowRecipeActivity.class);

                startActivity(intent);
                finish(); // Call once you redirect to another activity
            } else {
                Toast.makeText(this, "you must enter params", Toast.LENGTH_SHORT).show();

                if (title.length() == 0) {
                    titleEt.setTextColor(Color.RED);
                    titleEt.setHintTextColor(Color.RED);
                }

                if (url.length() == 0) {
                    urlEt.setTextColor(Color.RED);
                    urlEt.setHintTextColor(Color.RED);
                }

                if (!tryParse(readyInMinutesStr)) {
                    readyInMinutesEt.setTextColor(Color.RED);
                    readyInMinutesEt.setHintTextColor(Color.RED);
                }

                if (!tryParse(servingsStr)) {
                    servingsEt.setTextColor(Color.RED);
                    servingsEt.setHintTextColor(Color.RED);
                }

                if (summary.length() == 0) {
                    summaryEt.setTextColor(Color.RED);
                    summaryEt.setHintTextColor(Color.RED);
                }
            }
        });

    }

    Boolean tryParse(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }
}