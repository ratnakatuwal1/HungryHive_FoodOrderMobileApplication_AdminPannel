package com.ratna.hungryhiveadmin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhiveadmin.adapter.PendingOrderAdapter;

import java.util.Arrays;
import java.util.List;

public class PendingOrder extends AppCompatActivity {
    RecyclerView pendingOrderRecyclerView;
    PendingOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pending_order);

        pendingOrderRecyclerView = findViewById(R.id.pendingOrderRecyclerView);

        List<String> customerNames = Arrays.asList("Ratna", "Sanju", "Anjali", "Amar");
        List<String> quantity = Arrays.asList("1", "2", "3", "4");
        List<Integer> itemImages = Arrays.asList(R.drawable.prabesh, R.drawable.parbesh_sec, R.drawable.prabesh, R.drawable.parbesh_sec);

        adapter = new PendingOrderAdapter(customerNames, quantity, itemImages);
        pendingOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pendingOrderRecyclerView.setAdapter(adapter);
    }
}