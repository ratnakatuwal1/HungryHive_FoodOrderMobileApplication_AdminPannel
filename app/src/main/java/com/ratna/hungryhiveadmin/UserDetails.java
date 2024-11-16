package com.ratna.hungryhiveadmin;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserDetails extends AppCompatActivity {
    ImageView imageUser;
    TextView textName;
    TextView textEmail;
    TextView textPhone;
    TextView textAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_details);

        imageUser = findViewById(R.id.imageUser);
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textPhone = findViewById(R.id.textPhone);
        textAddress = findViewById(R.id.textAddress);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String address = getIntent().getStringExtra("address");

        // Set data in TextViews
        textName.setText("Name: " + name);
        textEmail.setText("Email: " + email);
        textPhone.setText("Phone: " + phone);
        textAddress.setText("Address: " + address);
    }
}