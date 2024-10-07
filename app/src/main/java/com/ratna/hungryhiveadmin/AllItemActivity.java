package com.ratna.hungryhiveadmin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhiveadmin.Adapter.MenuItemAdapter;
import com.ratna.hungryhiveadmin.Model.AllMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllItemActivity extends AppCompatActivity {
RecyclerView allItemRecyclerView;
MenuItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_item);

        allItemRecyclerView = findViewById(R.id.allItemRecyclerView);

        ArrayList<AllMenu> menuList = new ArrayList<>();
        menuList.add(new AllMenu("Burger", "100", "Delicious burger", "Beef, Lettuce, Cheese", String.valueOf(R.drawable.burger)));
        menuList.add(new AllMenu("Pizza", "200", "Cheesy pizza", "Cheese, Tomato, Olives", String.valueOf(R.drawable.pizza)));

        adapter = new MenuItemAdapter(this, menuList);
        allItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allItemRecyclerView.setAdapter(adapter);
    }
}