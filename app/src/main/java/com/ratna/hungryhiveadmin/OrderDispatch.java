package com.ratna.hungryhiveadmin;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratna.hungryhiveadmin.Adapter.DeliveryAdapter;
import com.ratna.hungryhiveadmin.Model.OrderDetails;

import java.util.ArrayList;

public class OrderDispatch extends AppCompatActivity {
    private DatabaseReference dispatchedOrdersRef;
    private DeliveryAdapter adapter;
    private ArrayList<String> customerNames = new ArrayList<>();
    private ArrayList<String> moneyStatuses = new ArrayList<>(); // Assume statuses like "Pending", "Received"


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_dispatch);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DeliveryAdapter(this, customerNames, moneyStatuses);
        recyclerView.setAdapter(adapter);

        dispatchedOrdersRef = FirebaseDatabase.getInstance().getReference("DispatchedOrders");
        loadDispatchedOrders();

//        ArrayList<String> customerNames = new ArrayList<>();
//        ArrayList<String> moneyStatuses = new ArrayList<>();

//        customerNames.add("John Doe");
//        customerNames.add("Jane Smith");
//        customerNames.add("Bob Johnson");
//
//        moneyStatuses.add("received");
//        moneyStatuses.add("notReceived");
//        moneyStatuses.add("Pending");

//        DeliveryAdapter adapter = new DeliveryAdapter(this, customerNames, moneyStatuses);
//        recyclerView.setAdapter(adapter);

    }

    private void loadDispatchedOrders() {
        dispatchedOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                customerNames.clear();
                moneyStatuses.clear();

                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String customerName = orderSnapshot.child("userName").getValue(String.class); // Assuming 'userName' holds the name
                    Boolean paymentReceived = orderSnapshot.child("paymentReceived").getValue(Boolean.class); // true/false

                    // Set a default payment status based on the database value
                    String paymentStatus = paymentReceived != null && paymentReceived ? "Received" : "Not Received";

                    if (customerName != null) {
                        customerNames.add(customerName);
                        moneyStatuses.add(paymentStatus);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle possible errors
            }
        });
    }

}