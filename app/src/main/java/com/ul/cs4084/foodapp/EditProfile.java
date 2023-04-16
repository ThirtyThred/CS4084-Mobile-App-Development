package com.ul.cs4084.foodapp;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    public static  final String TAG = "TAG";
    EditText p_fullname,p_phone,p_email,p_address;
    ImageView profileImageView;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    FirebaseUser user;
    Button saveBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit Profile");

        Intent data = getIntent();
        String fullname = data.getStringExtra("fullname");
        String phone = data.getStringExtra("phoneNo");
        String email = data.getStringExtra("email");
        String address = data.getStringExtra("Address");

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();

        p_fullname = findViewById(R.id.fullname);
        p_phone = findViewById(R.id.p_phoneNo);
        p_email = findViewById(R.id.p_email);
        p_address = findViewById(R.id.p_address);
        profileImageView = findViewById(R.id.cam);
        saveBtn = findViewById(R.id.save_b);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p_fullname.getText().toString().isEmpty() || p_phone.getText().toString().isEmpty() || p_address.getText().toString().isEmpty()) {
                    Toast.makeText(EditProfile.this, "one of many field are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                String user_email =p_email.getText().toString();
                user.updateEmail(user_email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference docRef = fstore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("Address",p_address.getText().toString() );
                        edited.put("email",user_email);
                        edited.put("fullname",p_fullname.getText().toString());
                        edited.put("phoneNo",p_phone.getText().toString());

                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditProfile.this, "profile updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Profile_page.class));
                                finish();
                            }
                        });
                        Toast.makeText(EditProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        
                    }
                });

            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfile.this, "Profile Image Clicked.", Toast.LENGTH_SHORT).show();
            }
        });

        p_fullname.setText(fullname);
        p_phone.setText(phone);
        p_email.setText(email);
        p_address.setText(address);



        Log.d(TAG, "onCreate:" + fullname + " " + email + " " + phone + " " + address);


    }
}