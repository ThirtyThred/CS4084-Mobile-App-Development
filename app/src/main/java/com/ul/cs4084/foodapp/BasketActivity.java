package com.ul.cs4084.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ul.cs4084.foodapp.databinding.ActivityBasketBinding;

public class BasketActivity extends DrawerBaseActivity {

    ActivityBasketBinding activityBasketBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBasketBinding = ActivityBasketBinding.inflate(getLayoutInflater());
        setContentView(activityBasketBinding.getRoot());
        allocateActivityTitle("Basket");
    }
}