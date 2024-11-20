package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import com.ratna.hungryhiveadmin.Adapter.PendingOrderAdapter;
import com.ratna.hungryhiveadmin.Model.OrderDetails;

import java.util.ArrayList;
import java.util.List;

public class PendingOrder extends AppCompatActivity implements PendingOrderAdapter.onItemClick {
    List<String> listOfName = new ArrayList<>();
    List<String> listOfTotalPrice = new ArrayList<>();
    List<String> listOfImage = new ArrayList<>();
    ArrayList<OrderDetails> listOfOrderItem = new ArrayList<>();
    RecyclerView pendingOrderRecyclerView;
    PendingOrderAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference dispatchedOrdersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pending_order);

        pendingOrderRecyclerView = findViewById(R.id.pendingOrderRecyclerView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("OrderDetails");
        dispatchedOrdersReference = firebaseDatabase.getReference("OrderDispatch");

        // Fetch order details from the database
        getOrderDetails();

        // Set up RecyclerView after data is fetched
        pendingOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getOrderDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear lists to prevent appending duplicate data
                listOfOrderItem.clear();
                listOfName.clear();
                listOfTotalPrice.clear();
                listOfImage.clear();

                // Loop through all orders and add them to local lists
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderDetails orderDetails = orderSnapshot.getValue(OrderDetails.class);
                    if (orderDetails != null) {
                        listOfOrderItem.add(orderDetails);
                    }
                }
                addDataToListForRecyclerView(); // Populate data for RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PendingOrder", "Failed to read value.", error.toException());
            }
        });
    }

    private void addDataToListForRecyclerView() {
        // Populate name, price, and image lists for RecyclerView
        for (OrderDetails orderItem : listOfOrderItem) {
            listOfName.add(orderItem.getUserName());
            listOfTotalPrice.add(orderItem.getTotalPrices());

            if (orderItem.getFoodImages() != null && !orderItem.getFoodImages().isEmpty()) {
                listOfImage.add(orderItem.getFoodImages().get(0));
            }
        }

        // Now set the adapter after data is loaded
        setAdapter();
    }

    private void setAdapter() {
        // Initialize the adapter with the loaded data
        adapter = new PendingOrderAdapter(this, listOfName, listOfTotalPrice, listOfImage);
        adapter.setItemClicked(this);
        pendingOrderRecyclerView.setAdapter(adapter); // Set the adapter
    }

    @Override
    public void onItemClickListener(int position) {
        Log.d("PendingOrder", "Item clicked at position: " + position);
        Intent intent = new Intent(PendingOrder.this, OrderDetailActivity.class);
        OrderDetails userOrderDetails = listOfOrderItem.get(position);
        intent.putExtra("UserOrderDetails", userOrderDetails);
        startActivity(intent);
    }

    @Override
    public void onItemAcceptClickListener(int position) {
        Log.d("PendingOrder", "Order accepted at position: " + position);
        OrderDetails acceptedOrder = listOfOrderItem.get(position);

        // Show Toast
        Toast.makeText(this, "Order Accepted", Toast.LENGTH_SHORT).show();

        // Move the order to "OrderDispatch" node using the correct itemPushKey
        dispatchedOrdersReference.child(acceptedOrder.getItemPushKey()).setValue(acceptedOrder);

        // Remove the order from "OrderDetails"
        databaseReference.child(acceptedOrder.getItemPushKey()).removeValue();

        // Remove from local lists and update RecyclerView
        listOfOrderItem.remove(position);
        listOfName.remove(position);
        listOfTotalPrice.remove(position);
        listOfImage.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onItemDispatchClickListener(int position) {
        Log.d("PendingOrder", "Order dispatched at position: " + position);
        OrderDetails dispatchedOrder = listOfOrderItem.get(position);

        // Move the order to "OrderDispatch"
        dispatchedOrdersReference.child(dispatchedOrder.getItemPushKey()).setValue(dispatchedOrder);

        // Remove the order from "OrderDetails"
        databaseReference.child(dispatchedOrder.getItemPushKey()).removeValue();

        // Remove from local lists and update RecyclerView
        listOfOrderItem.remove(position);
        listOfName.remove(position);
        listOfTotalPrice.remove(position);
        listOfImage.remove(position);
        adapter.notifyItemRemoved(position);

        Toast.makeText(this, "Order Dispatched and Moved", Toast.LENGTH_SHORT).show();
    }
}
