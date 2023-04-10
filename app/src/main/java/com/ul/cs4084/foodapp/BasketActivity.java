package com.ul.cs4084.foodapp;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ul.cs4084.foodapp.databinding.ActivityBasketBinding;
import com.ul.cs4084.foodapp.db.LocalDatabase;
import com.ul.cs4084.foodapp.models.Food;

import java.util.ArrayList;

public class BasketActivity extends DrawerBaseActivity implements BasketListAdapter.InteractionInterface {

    ActivityBasketBinding activityBasketBinding;
    private RecyclerView foodListView;
    private BasketListAdapter foodAdapter;

    private ArrayList<Food> foods;

    private FloatingActionButton buyButton;

    private final LocalDatabase database = LocalDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBasketBinding = ActivityBasketBinding.inflate(getLayoutInflater());
        setContentView(activityBasketBinding.getRoot());
        allocateActivityTitle("Basket");

        foodListView = findViewById(R.id.basketFoodListView);

        foods = database.allFoods();

        foodAdapter = new BasketListAdapter(foods, this, "Remove from basket", this);

        foodListView.setLayoutManager(new LinearLayoutManager(this) );
        foodListView.setAdapter(foodAdapter);

        buyButton = findViewById(R.id.buyButton);

        buyButton.setOnClickListener(view -> {
            Toast.makeText(this, "You have just bought these items", Toast.LENGTH_LONG).show();

            // Wait for 2 seconds and then run the function
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean success = database.clear();
                    if (success) {
                        foodAdapter.notifyDataSetChanged();
                    }
                }
            }, 2000); // Delay in milliseconds

        });
    }

    @Override
    public void onFoodClicked(int position) {
        Food food = foods.get(position);
        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra("FOOD", food);
        startActivity(intent);
    }

    @Override
    public void removeFromBasket(Food food) {
        int index = foods.indexOf(food);
        database.delete(food);
        foodAdapter.notifyItemRemoved(index);
    }
}