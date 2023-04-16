package com.ul.cs4084.foodapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.ul.cs4084.foodapp.databinding.ActivityProfilePageBinding;

import java.io.File;

public class Profile_page extends DrawerBaseActivity {
    ActivityProfilePageBinding activityProfilePageBinding;
    private TextView phone, user_name, email, address;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fstore;
    private String userId;
    private FirebaseUser user;

    com.google.firebase.storage.StorageReference storageReference;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfilePageBinding = ActivityProfilePageBinding.inflate(getLayoutInflater());
        setContentView(activityProfilePageBinding.getRoot());
        allocateActivityTitle("Profile");
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        Button button = findViewById(R.id.button3);
        user_name = findViewById(R.id.textView4);
        phone = findViewById(R.id.textView);
        email = findViewById(R.id.textView1);
        address = findViewById(R.id.textView2);
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();
        Button c_button = findViewById(R.id.button7);
        imageView = findViewById(R.id.imageView4);

        loadProfilePicture();

        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (documentSnapshot, e) -> {
            if (documentSnapshot.exists()) {
                phone.setText(documentSnapshot.getString("phoneNo"));
                address.setText(documentSnapshot.getString("Address"));
                user_name.setText(documentSnapshot.getString("fullname"));
                email.setText(documentSnapshot.getString("email"));
            }
        });

        c_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate to the second activity
                Intent intent3 = new Intent(Profile_page.this, Change_Password.class);
                startActivity(intent3);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate to the second activity
                Intent intent1 = new Intent(Profile_page.this, EditProfile.class);
                intent1.putExtra("fullname", user_name.getText().toString());
                intent1.putExtra("phoneNo", phone.getText().toString());
                intent1.putExtra("email", email.getText().toString());
                intent1.putExtra("Address", address.getText().toString());
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

    private void loadProfilePicture() {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/images/");

        imagesRef.listAll().addOnSuccessListener(listResult -> {
            // Check if any items exist in the directory
            if (!listResult.getItems().isEmpty()) {
                StorageReference firstImageRef = listResult.getItems().get(0);

                firstImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(this)
                            .load(uri)
                            .into(imageView);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                });
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load images", Toast.LENGTH_SHORT).show();
        });
    }

}
