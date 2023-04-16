package com.ul.cs4084.foodapp;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.ul.cs4084.foodapp.databinding.ActivityEditProfileBinding;
import com.ul.cs4084.foodapp.databinding.ActivityProfilePageBinding;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends DrawerBaseActivity {
    ActivityEditProfileBinding binding;
    public static final String TAG = "TAG";
    public static final int Request_code = 100;
    EditText p_fullname, p_phone, p_email, p_address;
    ImageView profileImageView;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    FirebaseUser user;
    Button saveBtn;
    com.google.firebase.storage.StorageReference storageReference;
    ImageView image_View;

    private ActivityResultLauncher<String> uploadImageLauncher;
    private ActivityResultLauncher<Intent> takePictureLauncher;

    private static final int REQUEST_CAMERA_PERMISSION_CODE = 1;
    private ActivityResultLauncher<String> cameraPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Edit Profile");

        loadProfilePicture();

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
        saveBtn = findViewById(R.id.save_b);
        storageReference = FirebaseStorage.getInstance().getReference();

        if (storageReference != null) {
            Log.d(TAG, "Storage Reference is not null. Connected to Firebase Storage.");
        } else {
            Log.d(TAG, "Storage Reference is null. Failed to connect to Firebase Storage.");
        }

        profileImageView = findViewById(R.id.cam);
        image_View = findViewById(R.id.imageView3);

        cameraPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                takePicture();
            } else {
                Toast.makeText(this, "permission denied..", Toast.LENGTH_SHORT).show();
            }
        });

        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Bundle extras = result.getData().getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Uri imageUri = getImageUri(getApplicationContext(), imageBitmap);
                uploadImageToFirebaseStorage(imageUri);
                image_View.setImageBitmap(imageBitmap);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(500, // width
                        500 // height
                );
                params.setMargins(0, 50, 0, 0);
                params.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
                image_View.setLayoutParams(params);
            } else {
                Toast.makeText(this, "image path error", Toast.LENGTH_SHORT).show();
            }
        });

        uploadImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            uploadImageToFirebaseStorage(uri);
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p_fullname.getText().toString().isEmpty() || p_phone.getText().toString().isEmpty() || p_address.getText().toString().isEmpty()) {
                    Toast.makeText(EditProfile.this, "one of many field are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                String user_email = p_email.getText().toString();
                user.updateEmail(user_email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference docRef = fstore.collection("users").document(user.getUid());
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("Address", p_address.getText().toString());
                        edited.put("email", user_email);
                        edited.put("fullname", p_fullname.getText().toString());
                        edited.put("phoneNo", p_phone.getText().toString());

                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(EditProfile.this, "profile updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Profile_page.class));
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
                checkCameraPermission();

            }
        });


        p_fullname.setText(fullname);
        p_phone.setText(phone);
        p_email.setText(email);
        p_address.setText(address);

        Log.d(TAG, "onCreate:" + fullname + " " + email + " " + phone + " " + address);
    }


    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            return false;
        }
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        } else {
            Toast.makeText(this, "Error happened", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("users/" + fAuth.getCurrentUser().getUid() + "/images/" + imageUri.getLastPathSegment());

        imagesRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(this, "Image saved", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error happened", Toast.LENGTH_SHORT).show();
        });
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void loadProfilePicture() {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/images/");

        imagesRef.listAll().addOnSuccessListener(listResult -> {
            // Check if any items exist in the directory
            if (!listResult.getItems().isEmpty()) {
                // Get the first item in the list
                StorageReference firstImageRef = listResult.getItems().get(0);

                // Load the image into imageView3
                firstImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(this).load(uri).into(image_View);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                });
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load images", Toast.LENGTH_SHORT).show();
        });
    }
}