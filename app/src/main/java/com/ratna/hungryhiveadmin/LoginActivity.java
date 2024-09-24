package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.hungryhiveadmin.Model.Admin;

public class LoginActivity extends AppCompatActivity {
    Button adminLoginButton;
    EditText editTextEmailAddress, editTextPassword;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        adminLoginButton = findViewById(R.id.adminLoginButton);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Admins");

        adminLoginButton.setOnClickListener(view -> {
            String email = editTextEmailAddress.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                adminLoginButton.setEnabled(false);
                Admin admin = new Admin("", email, password, "", "", "", "");
                loginAdmin(email, admin.password);
            } else {
                Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginAdmin(String email, String hashedPassword) {
        mAuth.signInWithEmailAndPassword(email, hashedPassword).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    databaseReference.child(userId).get().addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful()) {
                            Admin admin = dbTask.getResult().getValue(Admin.class);
                            if (admin == null) {
//                                Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
//                                startActivity(intent);
//                                finish();
                                saveAdminDetails(user);
                            } else {
                                Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                startActivity(intent);
                                finish();
//                                Toast.makeText(LoginActivity.this, "Admin data not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to retrieve admin data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
            }
            adminLoginButton.setEnabled(true);
        });
    }

    private void saveAdminDetails(FirebaseUser user) {
        String userId = user.getUid();
        String email = user.getEmail();

        // Creating Admin object with default values for now
        Admin admin = new Admin(userId, email, editTextPassword.getText().toString().trim(), "Admin Name", "Admin Address", "Admin Phone", "Profile Image URL");

        // Save admin to the Firebase Realtime Database
        databaseReference.child(userId).setValue(admin).addOnCompleteListener(saveTask -> {
            if (saveTask.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "Admin details saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Failed to save admin details", Toast.LENGTH_SHORT).show();
            }
        });
    }

}