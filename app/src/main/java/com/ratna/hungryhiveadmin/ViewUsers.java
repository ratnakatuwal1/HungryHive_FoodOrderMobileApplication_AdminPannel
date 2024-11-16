package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhiveadmin.Adapter.UserAdapter;
import com.ratna.hungryhiveadmin.Model.User;

import java.util.ArrayList;
import java.util.List;

public class ViewUsers extends AppCompatActivity {
    private RecyclerView recycler_view_users;
    private UserAdapter userAdapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_users);

        recycler_view_users = findViewById(R.id.recycler_view_users);
        recycler_view_users.setLayoutManager(new LinearLayoutManager(this));

        userList = getUsers();
        userAdapter = new UserAdapter(userList, user -> {
            // On user item click, navigate to UserDetails activity
            Intent intent = new Intent(ViewUsers.this, UserDetails.class);
            intent.putExtra("name", user.getName());
            intent.putExtra("email", user.getEmail());
            intent.putExtra("phone", user.getPhone());
            intent.putExtra("address", user.getAddress());
            startActivity(intent);
        });
        recycler_view_users.setAdapter(userAdapter);
    }

    private List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("Alice Smith", "alice@example.com", "123456789", "123 Main St, City"));
        users.add(new User("Bob Johnson", "bob@example.com", "987654321", "456 Elm St, City"));
        // Add more users as needed
        return users;
    }
}