package com.ratna.hungryhiveadmin;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratna.hungryhiveadmin.Adapter.MenuItemAdapter;
import com.ratna.hungryhiveadmin.Model.AllMenu;

import java.util.ArrayList;

public class AllItemActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ArrayList<AllMenu> menuItem;
    RecyclerView allItemRecyclerView;
    MenuItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_item);

        allItemRecyclerView = findViewById(R.id.allItemRecyclerView);
        allItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        menuItem = new ArrayList<>();
//        adapter = new MenuItemAdapter(this, menuItem);
//        allItemRecyclerView.setAdapter(adapter);

        retrieveMenuItem();
    }

    private void retrieveMenuItem() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference foodRef = firebaseDatabase.getReference().child("Menu");
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuItem.clear();
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    AllMenu menuItems = foodSnapshot.getValue(AllMenu.class);
                    if (menuItems != null) {
                        menuItem.add(menuItems);
                    }
                }
                setAdapter();
//                if (!menuItem.isEmpty()){
//                    adapter.notifyDataSetChanged();
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AllItemActivity", "Database error: " + error.getMessage());
            }
        });
    }
    private void setAdapter () {
        adapter = new MenuItemAdapter(this, menuItem, databaseReference);
        allItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allItemRecyclerView.setAdapter(adapter);
    }
}