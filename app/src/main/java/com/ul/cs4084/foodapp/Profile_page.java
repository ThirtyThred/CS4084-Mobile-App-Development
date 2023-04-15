package com.ul.cs4084.foodapp;


import android.os.Bundle;

import com.ul.cs4084.foodapp.databinding.ActivityProfilePageBinding;

public class Profile_page extends DrawerBaseActivity {
    ActivityProfilePageBinding activityProfilePageBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfilePageBinding = ActivityProfilePageBinding.inflate(getLayoutInflater());
        setContentView(activityProfilePageBinding.getRoot());
        allocateActivityTitle("Profile");
    }
}