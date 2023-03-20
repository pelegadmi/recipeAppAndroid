package com.example.appfoodie.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.io.Serializable;

public class Recipe implements Serializable {
    public int id;
    public String userId;
    public int aggregateLikes;
    public int readyInMinutes;
    public int servings;
    public String image;
    public String title;
    public String summary;
    public boolean pullData;

    public Recipe(int id, String userId, int aggregateLikes, int readyInMinutes, int servings, String image, String title, String summary) {
        this.id = id;
        this.userId = userId;
        this.aggregateLikes = aggregateLikes;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.image = image;
        this.title = title;
        this.pullData = true;
        this.summary = summary;
    }

    public Recipe(String userId, int readyInMinutes, int servings, String image, String title, String summary) {
        this.userId = userId;
        this.aggregateLikes = 0;
        this.readyInMinutes = readyInMinutes;
        this.servings = servings;
        this.image = image;
        this.title = title;
        this.pullData = true;
        this.summary = summary;
    }

    public Recipe(Cursor cursor) {
        try {

            this.id = cursor.getInt(0);
            this.userId = cursor.getString(1);
            this.aggregateLikes = cursor.getInt(2);
            this.readyInMinutes = cursor.getInt(3);
            this.servings = cursor.getInt(4);
            this.image = cursor.getString(5);
            this.title = cursor.getString(6);
            this.summary = cursor.getString(7);
            this.pullData = true;
        } catch (Exception e) {
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
        contentValues.put("summary", summary);
        return contentValues;
    }
}
