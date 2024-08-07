package com.ratna.hungryhiveadmin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhiveadmin.adapter.AllItemAdapter;

import java.util.Arrays;
import java.util.List;

public class AllItemActivity extends AppCompatActivity {
RecyclerView allItemRecyclerView;
AllItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_item);

        allItemRecyclerView = findViewById(R.id.allItemRecyclerView);

        List<String> itemNames = Arrays.asList("Burger", "Pizza");
        List<String> itemPrices = Arrays.asList("100", "200");
        List<Integer> itemImages = Arrays.asList(R.drawable.burger, R.drawable.pizza);

        adapter = new AllItemAdapter(itemNames, itemPrices, itemImages);
        allItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allItemRecyclerView.setAdapter(adapter);
    }
}