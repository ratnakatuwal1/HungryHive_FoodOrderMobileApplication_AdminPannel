package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {
    CardView cardViewAddItem, cardViewAllItem, cardViewAdminProfile, CardViewUsers;
    TextView textPendingOrder, textCompletedOrder, textEarning;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        cardViewAddItem = findViewById(R.id.cardViewAddItem);
        cardViewAllItem = findViewById(R.id.cardViewAllItem);
        cardViewAdminProfile = findViewById(R.id.cardViewAdminProfile);
        CardViewUsers = findViewById(R.id.CardViewUsers);
        textPendingOrder = findViewById(R.id.textPendingOrder);
        textCompletedOrder = findViewById(R.id.textCompletedOrder);
        textEarning = findViewById(R.id.textEarning);

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

        CardViewUsers.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ViewUsers.class);
            startActivity(intent);
        });

        textPendingOrder.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, PendingOrder.class);
            startActivity(intent);
        });

        pendingOrder();
        completedOrder();
        totalEarning();

    }

    private void pendingOrder() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference orderRef = database.getReference("OrderDetails");

        // Listener to get the pending orders count
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int pendingOrderCount = 0;

                // Loop through all orders to count the ones that are pending
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    // Assuming "orderAccepted" field indicates whether the order is pending
                    Boolean orderAccepted = orderSnapshot.child("orderAccepted").getValue(Boolean.class);

                    if (orderAccepted != null && !orderAccepted) {  // Order is pending if orderAccepted is false
                        pendingOrderCount++;
                    }
                }

                // Update the TextView with the pending orders count
                TextView pendingValue = findViewById(R.id.pendingValue);
                pendingValue.setText(String.valueOf(pendingOrderCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any database errors here (e.g., no internet connection)
                Toast.makeText(AdminDashboardActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void completedOrder() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference orderRef = database.getReference("OrderDispatch");

        // Listener to get the completed orders count
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int completedOrderCount = 0;

                // Loop through all orders to count the ones that are completed
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    // Assuming "orderAccepted" field indicates whether the order is completed
                    Boolean orderAccepted = orderSnapshot.child("orderAccepted").getValue(Boolean.class);

                    if (orderAccepted != null && orderAccepted) {  // Order is completed if orderAccepted is true
                        completedOrderCount++;
                    }
                }

                // Update the TextView with the completed orders count
                TextView completedValue = findViewById(R.id.textCompletedOrderValue);
                completedValue.setText(String.valueOf(completedOrderCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any database errors here (e.g., no internet connection)
                Toast.makeText(AdminDashboardActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void totalEarning() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("OrderDispatch");

        // Listener to get the completed orders' total earnings
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalEarnings = 0.0;

                // Loop through all orders to calculate the earnings of completed ones
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    // Check if order is completed based on "orderAccepted" field
                    Boolean orderAccepted = orderSnapshot.child("orderAccepted").getValue(Boolean.class);

                    if (orderAccepted != null && orderAccepted) {  // Order is completed if orderAccepted is true
                        // Assuming "totalPrices" contains the price of the order
                        String totalPriceString = orderSnapshot.child("totalPrices").getValue(String.class);

                        if (totalPriceString != null) {
                            // Remove the "Rs." part and parse the numeric value
                            try {
                                double price = Double.parseDouble(totalPriceString.replace("Rs. ", "").trim());
                                totalEarnings += price;
                            } catch (NumberFormatException e) {
                                // Handle parsing error (in case of malformed data)
                                e.printStackTrace();
                            }
                        }
                    }
                }

                // Update the TextView with the total earnings
                TextView earningValue = findViewById(R.id.EarningValue);
                earningValue.setText("Rs: " + totalEarnings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any database errors here (e.g., no internet connection)
                Toast.makeText(AdminDashboardActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

}