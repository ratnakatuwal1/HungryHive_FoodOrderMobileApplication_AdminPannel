package com.ratna.hungryhiveadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.ratna.hungryhiveadmin.EditMenuItem;
import com.ratna.hungryhiveadmin.Model.AllMenu;
import com.ratna.hungryhiveadmin.R;

import java.util.ArrayList;
import java.util.Arrays;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.AllItemViewHolder> {
    Context context;
    ArrayList<AllMenu> menuList;
    private int[] itemQuantity;
    private DatabaseReference databaseReference;
    private String userId;

    public MenuItemAdapter(Context context, ArrayList<AllMenu> menuList, DatabaseReference databaseReference) {
        this.context = context;
        this.menuList = menuList;
        this.databaseReference = databaseReference;

        // Get user ID from Firebase Authentication
        this.userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        this.itemQuantity = new int[menuList.size()];
        Arrays.fill(itemQuantity, 1);
    }

    @NonNull
    @Override
    public MenuItemAdapter.AllItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_item, parent, false);
        return new AllItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemAdapter.AllItemViewHolder holder, int position) {
        AllMenu menuItem = menuList.get(position);
        holder.bind(menuItem, itemQuantity[position], position);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class AllItemViewHolder extends RecyclerView.ViewHolder {
        TextView textFoodName, textFoodAmount, textQty;
        ImageView imageCartFood;
        ImageButton buttonMinus, buttonPlus, buttonDelete, imageButtonEdit;

        public AllItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textFoodName = itemView.findViewById(R.id.textFoodName);
            textFoodAmount = itemView.findViewById(R.id.textFoodAmount);
            textQty = itemView.findViewById(R.id.textQty);
            imageCartFood = itemView.findViewById(R.id.imageCartFood);
            imageButtonEdit = itemView.findViewById(R.id.imageButtonEdit);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        public void bind(AllMenu menuItem, int quantity, int position) {
            textFoodName.setText(menuItem.getFoodName());
            textFoodAmount.setText(menuItem.getFoodPrice());
            textQty.setText(String.valueOf(quantity));

            Glide.with(context)
                    .load(menuItem.getFoodImage())
                    .into(imageCartFood);

            buttonMinus.setOnClickListener(view -> {
                if (itemQuantity[position] > 1) {
                    itemQuantity[position]--;
                    textQty.setText(String.valueOf(itemQuantity[position]));
                }
            });

            buttonPlus.setOnClickListener(view -> {
                if (itemQuantity[position] < 10) {
                    itemQuantity[position]++;
                    textQty.setText(String.valueOf(itemQuantity[position]));
                }
            });

            buttonDelete.setOnClickListener(view -> removeItem(position, menuItem));

            imageButtonEdit.setOnClickListener(view -> editItem(menuItem));
        }
    }

    private void removeItem(int position, AllMenu menuItem) {
        // Debug log to check if ID is correct
        Log.d("DeleteItem", "User ID: " + userId);
        String itemKey = null;
        Log.d("DeleteItem", "Item Key: " + itemKey);
        itemKey = menuItem.getId();

        if (itemKey != null && !itemKey.isEmpty() && userId != null) {
            // Use userId to reference user-specific items in the database
            databaseReference.child("Menu").child(userId).child(itemKey).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        menuList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, menuList.size());
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Unable to remove item", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "Invalid item key or user ID. Cannot delete.", Toast.LENGTH_SHORT).show();
        }
    }

    private void editItem(AllMenu menuItem) {
        Intent intent = new Intent(context, EditMenuItem.class);
        intent.putExtra("menuItem", menuItem);
        intent.putExtra("itemKey", menuItem.getId());
        context.startActivity(intent);
    }
}
