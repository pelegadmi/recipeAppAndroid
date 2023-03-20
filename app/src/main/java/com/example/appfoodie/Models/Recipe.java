package com.example.appfoodie.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Recipe {
    public int id;
    public String userId;
    public int aggregateLikes;
    public int readyInMinutes;
    public int servings;
    public String image;
    public String title;

    public Recipe(int id, String userId, int aggregateLikes, int readyInMinutes, int servings, String image, String title) {
        this.id = id;
        this.userId = userId;
        this.aggregateLikes = aggregateLikes;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.image = image;
        this.title = title;
    }

    public Recipe(String userId, int readyInMinutes, int servings, String image, String title) {
        this.userId = userId;
        this.aggregateLikes = 0;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.image = image;
        this.title = title;
    }

    public Recipe(Cursor cursor) {
        try {

            this.id = cursor.getInt(0);
            this.userId = cursor.getString(1);
            this.aggregateLikes = cursor.getInt(2);;
            this.readyInMinutes = cursor.getInt(3);;
            this.servings = cursor.getInt(4);
            this.image = cursor.getString(5);
            this.title = cursor.getString(6);
        }
        catch (Exception e){
            Log.e("Recipe", "failed to presses data");
        }
    }

    public ContentValues generateContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", userId);
        contentValues.put("aggregateLikes", aggregateLikes);
        contentValues.put("title", title);
        contentValues.put("readyInMinutes", readyInMinutes);
        contentValues.put("servings", servings);
        contentValues.put("image", image);
        return contentValues;
    }
}
