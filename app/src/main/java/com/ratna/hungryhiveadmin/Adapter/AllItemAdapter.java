package com.ratna.hungryhiveadmin.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhiveadmin.databinding.AllItemBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllItemAdapter extends RecyclerView.Adapter<AllItemAdapter.AllItemViewHolder> {
    private int[] itemQuantity;
    private List<String> itemName;
    private List<String> itemPrices;
    private List<Integer> itemImages;

    public AllItemAdapter(List<String> itemName, List<String> itemPrices, List<Integer> itemImages) {
        this.itemName = new ArrayList<>(itemName);
        this.itemPrices = new ArrayList<>(itemPrices);
        this.itemImages = new ArrayList<>(itemImages);
        this.itemQuantity = new int[itemQuantity.size()];
        Arrays.fill(itemQuantity, 1);
    }

    @NonNull
    @Override
    public AllItemAdapter.AllItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllItemBinding binding = AllItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AllItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AllItemAdapter.AllItemViewHolder holder, int position) {
String itemName = this.itemName.get(position);
        String itemPrice = this.itemPrices.get(position);
        int itemImage = this.itemImages.get(position);
        int itemQuantity = this.itemQuantity[position];
        holder.bind(itemName, itemPrice, itemImage, itemQuantity, position);
    }

    @Override
    public int getItemCount() {
        return itemName.size();
    }

    public class AllItemViewHolder extends RecyclerView.ViewHolder {
        private AllItemBinding binding;

        public AllItemViewHolder(@NonNull View itemView) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String itemName, String itemPrice, int itemImage, int itemQuantity, int position) {
            binding.textFoodName.setText(itemName);
            binding.textFoodAmount.setText(itemPrice);
            binding.imageCartFood.setImageResource(itemImage);
            binding.textQty.setText(String.valueOf(itemQuantity));

            binding.buttonMinus.setOnClickListener(view -> {
                if (itemQuantity[position] > 1){
                    itemQuantity[position]--;
                    binding.textQty.setText(String.valueOf(itemQuantity[position]));
                }
            });



        }
    }
}
