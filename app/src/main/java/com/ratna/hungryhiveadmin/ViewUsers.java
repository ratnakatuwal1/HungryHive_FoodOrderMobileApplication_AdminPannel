// ViewUsers.java
package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratna.hungryhiveadmin.Adapter.UserAdapter;
import com.ratna.hungryhiveadmin.Model.User;

import java.util.ArrayList;
import java.util.List;

public class ViewUsers extends AppCompatActivity {
    private RecyclerView recycler_view_users;
    private UserAdapter userAdapter;
    private List<User> userList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        recycler_view_users = findViewById(R.id.recycler_view_users);
        recycler_view_users.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Fetch users from Firebase
        fetchUsersFromFirebase();
    }

    private void fetchUsersFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear(); // Clear the list to avoid duplication
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user); // Add user to list
                    }
                }
                // Initialize or update the adapter
                if (userAdapter == null) {
                    userAdapter = new UserAdapter(userList, user -> {
                        Intent intent = new Intent(ViewUsers.this, UserDetails.class);
                        intent.putExtra("name", user.getName());
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("phone", user.getPhone());
                        intent.putExtra("address", user.getAddress());
                        intent.putExtra("profileImageUrl", user.getProfileImageUrl());
                        startActivity(intent);
                    });
                    recycler_view_users.setAdapter(userAdapter);
                } else {
                    userAdapter.notifyDataSetChanged(); // Update the adapter if data changes
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ViewUsers", "Error: " + error.getMessage());
                Toast.makeText(ViewUsers.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
