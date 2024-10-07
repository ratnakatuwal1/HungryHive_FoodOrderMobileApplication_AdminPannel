package com.ratna.hungryhiveadmin;

import android.os.Bundle;

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
    ArrayList<AllMenu> menuList;
    RecyclerView allItemRecyclerView;
    MenuItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_item);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        retrieveMenuItem();

        allItemRecyclerView = findViewById(R.id.allItemRecyclerView);

//        ArrayList<AllMenu> menuList = new ArrayList<>();

//        adapter = new MenuItemAdapter(this, menuList);
//        allItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        allItemRecyclerView.setAdapter(adapter);
    }

    private void retrieveMenuItem() {
//        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference foodRef = firebaseDatabase.getReference().child("Menu");
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AllMenu menuItem = dataSnapshot.getValue(AllMenu.class);
                    if (menuItem != null) {
                        menuList.add(menuItem);
                    }
                }
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setAdapter () {
        adapter = new MenuItemAdapter(this, menuList);
        allItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allItemRecyclerView.setAdapter(adapter);
    }
}