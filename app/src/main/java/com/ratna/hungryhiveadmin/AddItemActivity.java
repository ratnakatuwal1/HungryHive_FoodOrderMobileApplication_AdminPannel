package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

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
            foodName = ((EditText) findViewById(R.id.foodName)).getText().toString();
            foodPrice = ((EditText) findViewById(R.id.foodPrice)).getText().toString();
            foodDescription = ((EditText) findViewById(R.id.description)).getText().toString();
            foodIngredients = ((EditText) findViewById(R.id.ingredients)).getText().toString();

            if (foodName.isEmpty() || foodPrice.isEmpty() || foodDescription.isEmpty() || foodIngredients.isEmpty()) {

            }
        });


        pickImage = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        selectImage.setImageURI(uri);
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
}