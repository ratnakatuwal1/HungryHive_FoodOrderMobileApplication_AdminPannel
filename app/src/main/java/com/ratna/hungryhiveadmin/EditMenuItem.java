package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.hungryhiveadmin.Model.AllMenu;

public class EditMenuItem extends AppCompatActivity {

    private EditText editFoodName, editFoodPrice, description, ingredients;
    private ImageView selectImage;
    private Button buttonSaveItem, buttonCancel;

    private AllMenu menuItem; // Stores the item being edited
    private String itemKey; // Firebase key for the item
    private DatabaseReference databaseReference; // Reference to Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_item);

        // Initialize views
        editFoodName = findViewById(R.id.editFoodName);
        editFoodPrice = findViewById(R.id.editFoodPrice);
        description = findViewById(R.id.description);
        ingredients = findViewById(R.id.ingredients);
        selectImage = findViewById(R.id.selectImage);
        buttonSaveItem = findViewById(R.id.buttonAddItem);
        buttonCancel = findViewById(R.id.buttonCancel);

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Get the menu item and key from the intent
        Intent intent = getIntent();
        menuItem = (AllMenu) intent.getSerializableExtra("menuItem");
        itemKey = intent.getStringExtra("itemKey");

        // Populate the fields with existing item data
        if (menuItem != null) {
            editFoodName.setText(menuItem.getFoodName());
            editFoodPrice.setText(menuItem.getFoodPrice());
            description.setText(menuItem.getFoodDescription());
            ingredients.setText(menuItem.getFoodIngredients());

            // Load image using Glide
            if (menuItem.getFoodImage() != null) {
                Glide.with(this)
                        .load(menuItem.getFoodImage())
                        .into(selectImage);
            }
        }

        // Set listener for the Save button
        buttonSaveItem.setOnClickListener(v -> updateItem());

        // Set listener for the Cancel button
        buttonCancel.setOnClickListener(v -> cancelEdit());
    }

    /**
     * Method to handle updating the item.
     */
    private void updateItem() {
        // Fetch entered data
        String name = editFoodName.getText().toString().trim();
        String price = editFoodPrice.getText().toString().trim();
        String desc = description.getText().toString().trim();
        String ing = ingredients.getText().toString().trim();

        // Check if all fields are filled
        if (name.isEmpty() || price.isEmpty() || desc.isEmpty() || ing.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the item locally
        if (menuItem != null && itemKey != null) {
        menuItem.setFoodName(name);
        menuItem.setFoodPrice(price);
        menuItem.setFoodDescription(desc);
        menuItem.setFoodIngredients(ing);

        // Update the item in Firebase
            databaseReference.child("Menu").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("menuItems").child(itemKey).setValue(menuItem)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Item updated successfully!", Toast.LENGTH_SHORT).show();

                        // Pass the updated item back to the adapter
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("updatedMenuItem", menuItem);
                        returnIntent.putExtra("itemKey", itemKey);
                        setResult(RESULT_OK, returnIntent);

                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to update item in database", Toast.LENGTH_SHORT).show();
                    });
        }
    }
    private void cancelEdit() {
        // Show a confirmation toast or message
        Toast.makeText(this, "Edit canceled", Toast.LENGTH_SHORT).show();
        // Return to the previous screen without saving
        finish();
    }
}
