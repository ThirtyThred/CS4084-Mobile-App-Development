package com.ul.cs4084.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ul.cs4084.foodapp.databinding.ActivityChangePasswordBinding;
import com.ul.cs4084.foodapp.databinding.ActivityProfilePageBinding;

import java.util.HashMap;
import java.util.Map;

public class Change_Password extends DrawerBaseActivity {

    ActivityChangePasswordBinding activityChangePasswordBinding;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    FirebaseUser user;

    EditText pass_word;
    Button pass_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChangePasswordBinding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_change_password);
        setContentView(activityChangePasswordBinding.getRoot());
        setTitle("Change Password");

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        pass_btn = findViewById(R.id.change_pass);

        pass_word = findViewById(R.id.pass_text);

        pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pass_word.getText().toString().isEmpty()) {
                    Toast.makeText(Change_Password.this, "cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                } else if (pass_word.getText().toString().length() < 6) {
                    Toast.makeText(Change_Password.this, "Password character cannot be less than 6", Toast.LENGTH_SHORT).show();
                    return;
                }

                String user_pass = pass_word.getText().toString();
                user.updatePassword(user_pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference docRef = fstore.collection("users").document(user.getUid());
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("password", pass_word.getText().toString());

                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Change_Password.this, "Password updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Profile_page.class));
                                finish();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Change_Password.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

        });
    }
}
