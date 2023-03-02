package com.ul.cs4084.foodapp;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ul.cs4084.foodapp.databinding.ActivityMenuBinding;
import com.ul.cs4084.foodapp.models.Food;

import java.util.ArrayList;

public class MenuActivity extends DrawerBaseActivity implements FoodAdapter.InteractionInterface {
    ActivityMenuBinding activityMenuBinding;

    private RecyclerView foodListView;
    private ProgressBar loadingIndicator;
    private ArrayList<Food> foods;
    private RelativeLayout menuContainer;
    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMenuBinding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(activityMenuBinding.getRoot());
        allocateActivityTitle("Menu");


        foodListView = findViewById(R.id.foodListView);
        loadingIndicator = findViewById(R.id.progressView);
        menuContainer = findViewById(R.id.menu_container);

        foods = new ArrayList<Food>();
        foods.add(new Food("Apple Frangipan Tart", "https://www.themealdb.com/images/media/meals/wxywrq1468235067.jpg", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ", 9.99));
        foods.add(new Food("Apam balik", "https://www.themealdb.com/images/media/meals/adxcbq1619787919.jpg","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ", 7.50));
        foods.add(new Food("Ayam Percik", "https://www.themealdb.com/images/media/meals/020z181619788503.jpg","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", 7.50));

        foodAdapter = new FoodAdapter(foods, this, this);

        foodListView.setLayoutManager(new LinearLayoutManager(this) );
        foodListView.setAdapter(foodAdapter);

        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFoodClicked(int position) {
        Food food = foods.get(position);
        System.out.println("Selected food "+food.getName());
        Toast.makeText(this, food.getName() + "selected", Toast.LENGTH_LONG);
    }
}