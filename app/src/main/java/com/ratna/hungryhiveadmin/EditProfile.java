package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ratna.hungryhiveadmin.Model.Admin;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    CircleImageView profileImageChange;
    Button buttonChangeProfileImage, buttonSaveChanges;
    EditText editTextName, editTextEmailAddress, editTextPhone, editTextAddress;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private final ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            imageUri = result.getData().getData();
            // Handle the selected image URI
            if (imageUri  != null){
                Picasso.get().load(imageUri).into(profileImageChange);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        profileImageChange = findViewById(R.id.profileImageChange);
        buttonChangeProfileImage = findViewById(R.id.buttonChangeProfileImage);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        editTextName = findViewById(R.id.editTextName);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAddress = findViewById(R.id.editTextAddress);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Admins");

        loadUserData();
        buttonChangeProfileImage.setOnClickListener(v -> {
            openGallery();
        });

        buttonSaveChanges.setOnClickListener(v -> {
            saveUserData();
        });
    }

    private void loadUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Admin userProfile = snapshot.getValue(Admin.class);
                    if (userProfile != null) {
                        // Load user data
                        editTextName.setText(userProfile.getName() != null ? userProfile.getName() : "");
                        editTextEmailAddress.setText(userProfile.getEmail() != null ? userProfile.getEmail() : "");
                        editTextPhone.setText(userProfile.getPhoneNo() != null ? userProfile.getPhoneNo() : "");
                        editTextAddress.setText(userProfile.getAddress() != null ? userProfile.getAddress() : "");

                        // Load profile image or use default
                        if (userProfile.getProfileImageUrl() != null) {
                            Picasso.get().load(userProfile.getProfileImageUrl()).into(profileImageChange);
                        } else {
                            profileImageChange.setImageResource(R.drawable.person);  // Placeholder for missing image
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfile.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    private void saveUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmailAddress.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save updated data to Firebase
        databaseReference.child(userId).child("name").setValue(name);
        databaseReference.child(userId).child("address").setValue(address);
        databaseReference.child(userId).child("phoneNo").setValue(phone);
        databaseReference.child(userId).child("email").setValue(email);

        if (!isValidEmail(email)) {
            editTextEmailAddress.setError("Please enter a valid email address");
            return;
        }

        if (!isValidPhoneNumber(phone)) {
            editTextPhone.setError("Please enter a valid phone number");
            return;
        }

        databaseReference.child(userId).child("name").setValue(name);
        databaseReference.child(userId).child("address").setValue(address);
        databaseReference.child(userId).child("phoneNo").setValue(phone);
        databaseReference.child(userId).child("email").setValue(email);

        // If there's an image selected, upload it to Firebase
        if (imageUri != null) {
            uploadProfileImage(userId);
        } else {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish();  // Go back to the previous activity
        }
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return email.matches(emailPattern);
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{10}");
    }

    private void uploadProfileImage(String userId) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReference("profile_photos").child(userId + ".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                databaseReference.child(userId).child("profileImageUrl").setValue(uri.toString());
                Toast.makeText(EditProfile.this, "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                finish();  // Go back to the previous activity
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(EditProfile.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
        });
    }
}