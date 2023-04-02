package com.ul.cs4084.foodapp;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ul.cs4084.foodapp.databinding.ActivityMenuBinding;
import com.ul.cs4084.foodapp.db.FoodStore;
import com.ul.cs4084.foodapp.db.LocalDatabase;
import com.ul.cs4084.foodapp.models.Food;

import java.util.List;

public class MenuActivity extends DrawerBaseActivity implements FoodAdapter.InteractionInterface {
    private static final String TAG = MenuActivity.class.getName();
    ActivityMenuBinding activityMenuBinding;
    private RecyclerView foodListView;
    private ProgressBar loadingIndicator;
    private List<Food> foods;
    private RelativeLayout menuContainer;
    private FoodAdapter foodAdapter;

    private LocalDatabase localDatabase = LocalDatabase.getInstance();

    FirebaseFirestore remoteDatabase = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMenuBinding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(activityMenuBinding.getRoot());
        allocateActivityTitle("Menu");

        foodListView = findViewById(R.id.foodListView);
        loadingIndicator = findViewById(R.id.progressView);
        menuContainer = findViewById(R.id.menu_container);

        foods = FoodStore.getAll(this);

        foodAdapter = new FoodAdapter(foods, this, "Add to basket", this);

        foodListView.setLayoutManager(new LinearLayoutManager(this) );
        foodListView.setAdapter(foodAdapter);

        loadingIndicator.setVisibility(View.INVISIBLE);



    }

    @Override
    public void onFoodClicked(int position) {
        Food food = foods.get(position);
        Intent intent = new Intent(this, FoodDetailActivity.class);
        intent.putExtra("FOOD", food);
        startActivity(intent);
    }

    @Override
    public void addToBasket(int position) {
        Food food = foods.get(position);
        localDatabase.save(food);
    }
}