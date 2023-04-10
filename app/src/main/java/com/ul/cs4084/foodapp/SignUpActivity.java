package com.ul.cs4084.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button signUpButton, showLogInButton;

    private static final String TAG = SignUpActivity.class.getName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        TextInputLayout emailInputLayout = findViewById(R.id.EmailInput);
        TextInputLayout passwordInputLayout = findViewById(R.id.PasswordInput);

        signUpButton = findViewById(R.id.Sign_up_btn);
        showLogInButton = findViewById(R.id.loginRedirect);

        signUpButton.setOnClickListener(view -> {
            signUp(Objects.requireNonNull(emailInputLayout.getEditText()).getText().toString(), Objects.requireNonNull(passwordInputLayout.getEditText()).getText().toString());
        });

        showLogInButton.setOnClickListener(view -> {
            showLogInActivity();
        });

    }

    public void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "User created successfully... ", Toast.LENGTH_SHORT).show();
                       Log.d(TAG, "User created successfully...");
                       showMenuActivity();
                    } else {
                        Log.d(TAG, "Failed to create user: "+ Objects.requireNonNull(task.getException()).getMessage());
                        Toast.makeText(this, "Failed to create user: "+ Objects.requireNonNull(task.getException()).getMessage() + ", please sign up first!", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void showMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void showLogInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}