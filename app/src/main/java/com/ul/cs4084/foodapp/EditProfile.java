package com.ul.cs4084.foodapp;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    public static  final String TAG = "TAG";
    public static final int Request_code = 100;
    EditText p_fullname,p_phone,p_email,p_address;
    ImageView profileImageView;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    FirebaseUser user;
    Button saveBtn;
    com.google.firebase.storage.StorageReference storageReference;
    ImageView image_View;

    ActivityResultLauncher<String> galleryLauncher;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Handle the result from the activity here
                    Intent data = result.getData();
                    if(Request_code == 100){
                        if(Request_code == Activity.RESULT_OK){
                            Uri imageUri = data.getData();

                            Bitmap image = (Bitmap) data.getExtras().get("data");
                            image_View.setImageBitmap(image);

                            uploadImageToFirebase(imageUri);
                        }
                    }
                }
            });




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
        storageReference = FirebaseStorage.getInstance().getReference();
        image_View = findViewById(R.id.imageView3);


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

        StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(image_View);
        });


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();

            }
        });


        p_fullname.setText(fullname);
        p_phone.setText(phone);
        p_email.setText(email);
        p_address.setText(address);

        Log.d(TAG, "onCreate:" + fullname + " " + email + " " + phone + " " + address);
    }
/*
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1000){
            if(requestCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                uploadImageToFirebase(imageUri);
            }
        }

    }
*/
    private void uploadImageToFirebase(Uri imageUri) {
        final StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Picasso.get().load(uri).into(image_View);
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void askCameraPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, Request_code);
        }else{
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Request_code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission is Required to user camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private  void openCamera(){

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        someActivityResultLauncher.launch(cameraIntent);


    }
}
















