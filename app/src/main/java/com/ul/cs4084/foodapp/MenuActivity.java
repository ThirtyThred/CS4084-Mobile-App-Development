package com.ul.cs4084.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ul.cs4084.foodapp.databinding.ActivityBasketBinding;
import com.ul.cs4084.foodapp.databinding.ActivityMenuBinding;

public class MenuActivity extends DrawerBaseActivity {
    ActivityMenuBinding activityMenuBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMenuBinding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(activityMenuBinding.getRoot());
        allocateActivityTitle("Menu");
    }
}