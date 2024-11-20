package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhiveadmin.Adapter.OrderDetailAdapter;
import com.ratna.hungryhiveadmin.Model.OrderDetails;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView editTextName;
    private TextView editTextAddress;
    private TextView editTextPhone;
    private TextView editTextTotalPay;
    private ArrayList<String> foodName = new ArrayList<>();
    private ArrayList<String> foodQuantity = new ArrayList<>();
    private ArrayList<String> foodPrice = new ArrayList<>();
    private ArrayList<String> foodImages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_detail);

        initializeViews();
        getDataFromIntent();
    }

    private void initializeViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextTotalPay = findViewById(R.id.editTextTotalPay);
    }

    private void getDataFromIntent() {
       OrderDetails receivedOrderDetails = (OrderDetails) getIntent().getSerializableExtra("UserOrderDetails");
        if (receivedOrderDetails != null) {
            editTextName.setText(receivedOrderDetails.getUserName());
            editTextAddress.setText(receivedOrderDetails.getAddress());
            editTextPhone.setText(receivedOrderDetails.getPhoneNumber());
            editTextTotalPay.setText(receivedOrderDetails.getTotalPrices());

            foodName.addAll(receivedOrderDetails.getFoodNames());
            foodQuantity.addAll(receivedOrderDetails.getFoodQuantities());
            foodPrice.addAll(receivedOrderDetails.getFoodPrices());
            foodImages.addAll(receivedOrderDetails.getFoodImages());

            setAdapter();
        } else {
            Log.e("OrderDetailActivity", "OrderDetails data is null");
        }

    }

    private void setAdapter() {
        RecyclerView orderRecyclerView = findViewById(R.id.orderRecyclerView);
        OrderDetailAdapter adapter = new OrderDetailAdapter(this, foodName, foodQuantity, foodPrice, foodImages);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerView.setAdapter(adapter);
    }

//    private void setUserDetails() {
//        editTextName.setText("Name: " + userName);
//        editTextAddress.setText("Address: " + address);
//        editTextPhone.setText("Phone: " + phoneNumber);
//        editTextEmailAddress.setText("Email: " + email);
//        editTextTotalPay.setText("Total Pay: " + totalPrice);
//
//    }
}