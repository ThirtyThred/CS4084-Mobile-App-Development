package com.ul.cs4084.foodapp.db;

import android.content.Context;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ul.cs4084.foodapp.models.Food;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FoodStore {

    public static List<Food> getAll(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("foods.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Food>>(){}.getType();
        List<Food> foods =  gson.fromJson(json, type);

        return foods;
    }

}
