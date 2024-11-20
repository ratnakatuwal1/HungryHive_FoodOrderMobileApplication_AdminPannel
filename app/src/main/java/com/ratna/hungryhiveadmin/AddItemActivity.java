package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ratna.hungryhiveadmin.Model.AllMenu;

public class AddItemActivity extends AppCompatActivity {
    ImageView selectImage;
    TextView textSelectImage;
    Button buttonAddItem;

    String foodName;
    String foodPrice;
    String foodDescription;
    String foodIngredients;
    Uri foodImage = null;

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    ActivityResultLauncher<PickVisualMediaRequest> pickImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        selectImage = findViewById(R.id.selectImage);
        textSelectImage = findViewById(R.id.textSelectImage);
        buttonAddItem = findViewById(R.id.buttonAddItem);

        buttonAddItem.setOnClickListener(view -> {
            foodName = ((EditText) findViewById(R.id.foodName)).getText().toString().trim();
            foodPrice = ((EditText) findViewById(R.id.foodPrice)).getText().toString().trim();
            foodDescription = ((EditText) findViewById(R.id.description)).getText().toString().trim();
            foodIngredients = ((EditText) findViewById(R.id.ingredients)).getText().toString().trim();

            if (!foodName.isEmpty() && !foodPrice.isEmpty() && !foodDescription.isEmpty() && !foodIngredients.isEmpty()) {
                uploadData();
                Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Fill all the details!", Toast.LENGTH_SHORT).show();
            }
        });

        selectImage.setOnClickListener(view -> {
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build();
            pickImage.launch(request);
        });

        pickImage = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        selectImage.setImageURI(uri);
                        foodImage = uri;
                    }
                }
        );

        textSelectImage.setOnClickListener(view -> {
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                    .build();
            pickImage.launch(request);
        });
    }

    private void uploadData() {
        DatabaseReference menuRef = database.getReference("Menu");
        String newItemKey = menuRef.push().getKey();

        if (foodImage != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("menu_images/" + newItemKey + ".jpg");
            UploadTask uploadTask = imageRef.putFile(foodImage);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            AllMenu newItem = new AllMenu(
                                    newItemKey,
                                    foodName,
                                    foodPrice,
                                    foodDescription,
                                    foodIngredients,
                                    uri.toString()
                            );

                            if (newItemKey != null) {
                                menuRef.child(newItemKey).setValue(newItem)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(AddItemActivity.this, "Data uploaded successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Show failure toast
                                                Toast.makeText(AddItemActivity.this, "Failed to upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                // Handle the case when newItemKey is null
                                Toast.makeText(AddItemActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle any failure in image upload
                    Toast.makeText(AddItemActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(AddItemActivity.this, "Please select an image.", Toast.LENGTH_SHORT).show();
        }
    }
}
