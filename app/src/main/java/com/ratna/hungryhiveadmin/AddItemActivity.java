package com.ratna.hungryhiveadmin;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {
  ImageView selectImage;
  TextView textSelectImage;

  ActivityResultLauncher<PickVisualMediaRequest> pickImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);

        selectImage = findViewById(R.id.selectImage);
        textSelectImage = findViewById(R.id.textSelectImage);

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