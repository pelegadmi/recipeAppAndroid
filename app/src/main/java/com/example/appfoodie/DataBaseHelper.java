package com.example.appfoodie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.appfoodie.Models.Recipe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "recipe.db";

    public DataBaseHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table recipe(id INTEGER primary key, userId VARCHAR(255), aggregateLikes INTEGER, readyInMinutes INTEGER, servings INTEGER, image VARCHAR(255),title VARCHAR(255))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists recipe");
    }

    public Boolean insertData(Recipe recipe) {
        SQLiteDatabase MyDB = this.getWritableDatabase();

        long result = MyDB.insert("recipe", null, recipe.generateContentValues());
        return result != -1;
    }

    public ArrayList<Recipe> getUserRecipe(String userId) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from recipe where userId =?", new String[]{userId});
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                recipes.add(new Recipe(cursor));
                cursor.moveToNext();
            }
        }
        return recipes;
    }

    public ArrayList<Recipe> getAllRecipe() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from recipe", new String[]{});
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                recipes.add(new Recipe(cursor));
                cursor.moveToNext();
            }
        }
        return recipes;
    }


}
