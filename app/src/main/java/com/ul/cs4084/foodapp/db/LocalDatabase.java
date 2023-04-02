package com.ul.cs4084.foodapp.db;

import com.ul.cs4084.foodapp.models.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocalDatabase {

    ArrayList<Food> storage;
    private static LocalDatabase instance = null;

    private LocalDatabase(ArrayList<Food> storage) {
        this.storage = storage;
    }

    private Map<String, Food> uniqueFoods = new HashMap<>();

    public static LocalDatabase getInstance() {

        if (instance == null) {
            instance =  new LocalDatabase(new ArrayList<>());
        }

        return instance;
    }

    public boolean save(Food food) {
        String name = food.getName();

        Food uniqueFood = uniqueFoods.get(name);
        if (uniqueFood != null) {
            uniqueFood.setCount(uniqueFood.getCount() + 1);
            return true;
        } else {
            uniqueFoods.put(name, food);
            food.setCount(1);
            return storage.add(food);
        }
    }

    public boolean exist(Food food) {
        return storage.contains(food);
    }

    public boolean delete(Food food) {
        String name = food.getName();
        food.setCount(food.getCount() - 1);
        uniqueFoods.remove(name);
        if (food.getCount() == 0) {
            storage.remove(food);
        }
        return true;
    }

    public Food first() {

        if (storage.size() > 0) {
            return storage.get(0);
        }

        return null;
    }

    public Food last() {

        if (storage.size() > 0) {
            return storage.get(storage.size() - 1);
        }

        return null;
    }

    public Food get(int index) {

        if (index < storage.size()) {
            return storage.get(index);
        }
        return null;
    }

    public ArrayList<Food> allFoods() {
        return storage;
    }

    public boolean clear() {
        return this.storage.removeAll(storage);
    }

}
