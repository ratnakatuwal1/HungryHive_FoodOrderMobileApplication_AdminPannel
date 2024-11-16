package com.ratna.hungryhiveadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class EditMenuItem extends AppCompatActivity {

    private EditText editFoodName;
    private EditText editFoodPrice;
    private EditText description;
    private EditText ingredients;
    private ImageView selectImage;
    private Button buttonAddItem;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_menu_item);

        // Initialize the views
        editFoodName = findViewById(R.id.editFoodName);
        editFoodPrice = findViewById(R.id.editFoodPrice);
        description = findViewById(R.id.description);
        ingredients = findViewById(R.id.ingredients);
        selectImage = findViewById(R.id.selectImage);
        buttonAddItem = findViewById(R.id.buttonAddItem);
        buttonCancel = findViewById(R.id.buttonCancel);

        // Set listeners for Update and Cancel buttons
        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEdit();
            }
        });
    }

    /**
     * Method to handle updating the item.
     */
    private void updateItem() {
        // Fetch the entered data
        String name = editFoodName.getText().toString();
        String price = editFoodPrice.getText().toString();
        String desc = description.getText().toString();
        String ing = ingredients.getText().toString();

        // Check if all fields are filled
        if (name.isEmpty() || price.isEmpty() || desc.isEmpty() || ing.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save updated data logic goes here (e.g., update database)
        Toast.makeText(this, "Item updated successfully!", Toast.LENGTH_SHORT).show();

        // Optionally finish activity and return to previous screen
        finish();
    }

    /**
     * Method to handle cancel action.
     */
    private void cancelEdit() {
        // Show a confirmation toast or message
        Toast.makeText(this, "Edit canceled", Toast.LENGTH_SHORT).show();

        // Return to the previous screen
        finish();
    }
}
