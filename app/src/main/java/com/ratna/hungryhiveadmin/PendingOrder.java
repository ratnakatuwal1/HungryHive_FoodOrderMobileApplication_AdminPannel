package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
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
import java.util.Arrays;
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
        dispatchedOrdersReference = firebaseDatabase.getReference("DispatchedOrders");

        getOrderDetails();

//        List<String> customerNames = Arrays.asList("Ratna", "Sanju", "Anjali", "Amar");
//        List<String> quantity = Arrays.asList("1", "2", "3", "4");
//        List<Integer> itemImages = Arrays.asList(R.drawable.prabesh, R.drawable.parbesh_sec, R.drawable.prabesh, R.drawable.parbesh_sec);

        // adapter = new PendingOrderAdapter(customerNames, quantity, itemImages);
        pendingOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pendingOrderRecyclerView.setAdapter(adapter);
    }

    private void getOrderDetails() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    OrderDetails orderDetails = orderSnapshot.getValue(OrderDetails.class);
                    if (orderDetails != null) {
                        listOfOrderItem.add(orderDetails);
                    }
                }
                addDataToListForRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addDataToListForRecyclerView() {
        for (OrderDetails orderItem : listOfOrderItem) {
            listOfName.add(orderItem.getUserName());
            listOfTotalPrice.add(orderItem.getTotalPrices());

            if (orderItem.getFoodImages() != null && !orderItem.getFoodImages().isEmpty()) {
                listOfImage.add(orderItem.getFoodImages().get(0));
            }

            if (orderItem.getFoodQuantities() != null && !orderItem.getFoodQuantities().isEmpty()) {
                // Just make sure the list is not empty or null
                for (String quantity : orderItem.getFoodQuantities()) {
                    Log.d("PendingOrder", "Food quantity: " + quantity); // Debugging the food quantity
                }
            }
        }

        setAdapter();
    }

    private void setAdapter() {
        adapter = new PendingOrderAdapter(this, listOfName, listOfTotalPrice, listOfImage);
        adapter.setItemClicked(this);
        pendingOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pendingOrderRecyclerView.setAdapter(adapter);
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
        String childItemPushKey = listOfOrderItem.get(position).getItemPushKey();
        DatabaseReference clickItemOrderReference = (childItemPushKey != null)
                ? databaseReference.child(childItemPushKey)
                : null;

        if (clickItemOrderReference != null) {
            clickItemOrderReference.child("AcceptedOrder").setValue(true);
        }
    }

    @Override
    public void onItemDispatchClickListener(int position) {
        String itemPushKey = listOfOrderItem.get(position).getItemPushKey();
        DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference("OrderDetails").child(itemPushKey);

        // Remove the order from the database
        orderReference.removeValue().addOnSuccessListener(aVoid -> {
            // After removal, add it to dispatched orders
            OrderDetails orderToDispatch = listOfOrderItem.get(position);
            dispatchedOrdersReference.push().setValue(orderToDispatch)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Successfully dispatched, remove from list and update the adapter
                            listOfOrderItem.remove(position);
                            listOfName.remove(position);
                            listOfTotalPrice.remove(position);
                            listOfImage.remove(position);

                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());

                            Toast.makeText(PendingOrder.this, "Order Dispatched and Removed from Pending", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PendingOrder.this, "Failed to dispatch order", Toast.LENGTH_SHORT).show();
                        }
                    });
        }).addOnFailureListener(e -> {
            Toast.makeText(PendingOrder.this, "Failed to remove order from pending", Toast.LENGTH_SHORT).show();
        });
}

}