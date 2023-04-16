package com.ul.cs4084.foodapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ul.cs4084.foodapp.databinding.ActivityProfilePageBinding;

public class Profile_page extends DrawerBaseActivity {
    ActivityProfilePageBinding activityProfilePageBinding;
    private TextView phone,user_name,email,address;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;
    private String userId;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfilePageBinding = ActivityProfilePageBinding.inflate(getLayoutInflater());
        setContentView(activityProfilePageBinding.getRoot());
        allocateActivityTitle("Profile");
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        Button button = findViewById(R.id.button3);
        user_name = findViewById(R.id.textView4);
        phone = findViewById(R.id.textView);
        email = findViewById(R.id.textView1);
        address = findViewById(R.id.textView2);
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this,(documentSnapshot,e) -> {
            if(documentSnapshot.exists()){
                phone.setText(documentSnapshot.getString("phoneNo"));
                address.setText(documentSnapshot.getString("Address"));
                user_name.setText(documentSnapshot.getString("fullname"));
                email.setText(documentSnapshot.getString("email"));
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate to the second activity
                Intent intent1 = new Intent(Profile_page.this, EditProfile.class);
                intent1.putExtra("fullname", user_name.getText().toString());
                intent1.putExtra("phoneNo", phone.getText().toString());
                intent1.putExtra("email",  email.getText().toString());
                intent1.putExtra("Address",  address.getText().toString());
                startActivity(intent1);
            }
        });

        Button sign_out_button = findViewById(R.id.button2);
        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate to the second activity
                mAuth.signOut();
                Intent intent2 = new Intent(Profile_page.this, LoginActivity.class);
                startActivity(intent2);
            }
        });
    }
}
