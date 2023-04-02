package com.ul.cs4084.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ul.cs4084.foodapp.models.Food;

public class FoodDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);


        ImageView foodImage = findViewById(R.id.foodDetailImage);
        TextView foodName = findViewById(R.id.foodDetailName);
        TextView foodDescrition = findViewById(R.id.foodDetailDescrition);
        TextView foodPrice = findViewById(R.id.foodDetailPrice);

        Food food = (Food) getIntent().getSerializableExtra("FOOD");
        foodName.setText(food.getName());
        foodDescrition.setText(food.getDescription());
        foodPrice.setText(food.getPrice());
        Picasso.get()
                .load(food.getImageUrl())
                .placeholder(R.drawable.img)
                .into(foodImage);
    }
}