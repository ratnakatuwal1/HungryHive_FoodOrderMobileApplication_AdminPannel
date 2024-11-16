package com.ratna.hungryhiveadmin;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    }
}