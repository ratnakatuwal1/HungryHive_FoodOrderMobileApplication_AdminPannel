package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ratna.hungryhiveadmin.Model.Admin;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfileActivity extends AppCompatActivity {
    Button buttonEditProfile, buttonLogout, changePasswordButton, buttonAddPhoto;
    TextView textFullName, textEmailAddress, textPhone, textAddress, textName, textEmail;
    CircleImageView profileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
            imageUri = result.getData().getData();
            UploadPhotoToFirebase();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_profile);

        buttonEditProfile = findViewById(R.id.buttonEditProfile);
        buttonLogout = findViewById(R.id.buttonLogout);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        buttonAddPhoto = findViewById(R.id.buttonAddPhoto);
        textFullName = findViewById(R.id.textFullName);
        textEmailAddress = findViewById(R.id.textEmailAddress);
        textPhone = findViewById(R.id.textPhone);
        textAddress = findViewById(R.id.textAddress);
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        profileImage = findViewById(R.id.profileImage);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Admins");
        storageReference = FirebaseStorage.getInstance().getReference("Admin_profile_photos");

        setUserData();

        buttonAddPhoto.setOnClickListener(v -> {
            openGallery();
        });

        buttonEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(AdminProfileActivity.this, EditProfile.class);
            startActivity(intent);
        });

        changePasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminProfileActivity.this, ChangePassword.class);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(AdminProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            setUserData();  // Refresh profile data
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    private void UploadPhotoToFirebase() {
        if (imageUri != null) {
            String userId = mAuth.getCurrentUser().getUid();
            StorageReference fileRef = storageReference.child(userId + ".jpg");
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            databaseReference.child(userId).child("profileImageUrl").setValue(uri.toString());
                            Picasso.get().load(uri).into(profileImage);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // handle error
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // handle error
                }
            });
        }
    }

    private void setUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        if (userId != null) {
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Admin userProfile = snapshot.getValue(Admin.class);
                        if (userProfile != null) {
                            // Display data or default to empty if fields are missing
                            textName.setText(userProfile.getName() != null ? userProfile.getName() : "");
                            textEmail.setText(userProfile.getEmail() != null ? userProfile.getEmail() : "");
                            textFullName.setText("Name: " + (userProfile.getName() != null ? userProfile.getName() : ""));
                            textEmailAddress.setText("Email: " + (userProfile.getEmail() != null ? userProfile.getEmail() : ""));
                            textPhone.setText("Phone: " + (userProfile.getPhoneNo() != null ? userProfile.getPhoneNo() : ""));
                            textAddress.setText("Address: " + (userProfile.getAddress() != null ? userProfile.getAddress() : ""));

                            if (userProfile.getProfileImageUrl() != null) {
                                Picasso.get().load(userProfile.getProfileImageUrl()).into(profileImage);
                            }

                            initializeMissingFields(userId, userProfile);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void initializeMissingFields(String userId, Admin userProfile) {
        if (userProfile.getName() == null) {
            databaseReference.child(userId).child("name").setValue("");
        }
        if (userProfile.getEmail() == null) {
            databaseReference.child(userId).child("email").setValue("");
        }
        if (userProfile.getPhoneNo() == null) {
            databaseReference.child(userId).child("phoneNo").setValue("");
        }
        if (userProfile.getAddress() == null) {
            databaseReference.child(userId).child("address").setValue("");
        }
        if (userProfile.getProfileImageUrl() == null) {
            databaseReference.child(userId).child("profileImageUrl").setValue("");
        }
    }
}