package com.ul.cs4084.foodapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton, showSignUpButton;

    private static final String TAG = LoginActivity.class.getName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            showMenuActivity();
            return;
        }

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        signInButton = findViewById(R.id.Sign_up_btn);
        showSignUpButton = findViewById(R.id.sign_up);


        String email = emailEditText.getText().toString();
//        String password = passwordEditText.getText().toString();

        TextInputLayout emailInputLayout = findViewById(R.id.EmailInput);
        TextInputLayout passwordInputLayout = findViewById(R.id.PasswordInput);

        String password = Objects.requireNonNull(passwordInputLayout.getEditText()).getText().toString();


        signInButton.setOnClickListener(view -> {

            Log.d(TAG, Objects.requireNonNull(emailInputLayout.getEditText()).getText().toString());


            Log.d(TAG, Objects.requireNonNull(passwordInputLayout.getEditText()).getText().toString());

            // For some reason, this is the only way to retrieve input values that works
            signIn(Objects.requireNonNull(emailInputLayout.getEditText()).getText().toString(), Objects.requireNonNull(passwordInputLayout.getEditText()).getText().toString());
        });

        showSignUpButton.setOnClickListener(view -> {
            showSignupActivity();
        });

    }

    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "User signed in successfully... ", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "User signed in successfully...");
                        showMenuActivity();
                    } else {
                        Toast.makeText(this, "Failed to sign in User: "+ Objects.requireNonNull(task.getException()).getMessage() + ", please sign up first!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Failed to sign in User: "+ Objects.requireNonNull(task.getException()).getMessage());
                    }
                });

    }

    public void showMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void showSignupActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}