package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashboardActivity extends AppCompatActivity {
CardView cardViewAddItem, cardViewAllItem, cardViewAdminProfile, cardViewCreateNewAdmin, CardViewOrderDispatch, CardViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        cardViewAddItem = findViewById(R.id.cardViewAddItem);
        cardViewAllItem = findViewById(R.id.cardViewAllItem);
        cardViewAdminProfile = findViewById(R.id.cardViewAdminProfile);
        cardViewCreateNewAdmin = findViewById(R.id.cardViewCreateNewAdmin);
        CardViewOrderDispatch = findViewById(R.id.CardViewOrderDispatch);
        CardViewUsers = findViewById(R.id.CardViewUsers);

        cardViewAddItem.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        cardViewAllItem.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AllItemActivity.class);
            startActivity(intent);

        });

        cardViewAdminProfile.setOnClickListener(view -> {
Intent intent = new Intent(AdminDashboardActivity.this, AdminProfileActivity.class);
startActivity(intent);
        });

        cardViewCreateNewAdmin.setOnClickListener(view -> {
Intent intent = new Intent(AdminDashboardActivity.this, AddNewAdmin.class);
startActivity(intent);
        });

        CardViewOrderDispatch.setOnClickListener(view -> {
Intent intent = new Intent(AdminDashboardActivity.this, PendingOrder.class);
startActivity(intent);
        });

        CardViewUsers.setOnClickListener(view -> {

        });
    }
}