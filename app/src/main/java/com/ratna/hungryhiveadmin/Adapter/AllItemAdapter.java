package com.ratna.hungryhiveadmin.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhiveadmin.databinding.AllItemBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllItemAdapter extends RecyclerView.Adapter<AllItemAdapter.AllItemViewHolder> {
    private int[] itemQuantity;
    private List<String> itemNames;
    private List<String> itemPrices;
    private List<Integer> itemImages;

    public AllItemAdapter(List<String> itemNames, List<String> itemPrices, List<Integer> itemImages) {
        this.itemNames = new ArrayList<>(itemNames);
        this.itemPrices = new ArrayList<>(itemPrices);
        this.itemImages = new ArrayList<>(itemImages);
        this.itemQuantity = new int[itemNames.size()];
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
String name = this.itemNames.get(position);
        String price = this.itemPrices.get(position);
        int image = this.itemImages.get(position);
        int quantity = this.itemQuantity[position];
        holder.bind(name, price, image, quantity, position);
    }

    @Override
    public int getItemCount() {
        return itemNames.size();
    }

    public class AllItemViewHolder extends RecyclerView.ViewHolder {
        private AllItemBinding binding;

        public AllItemViewHolder(@NonNull AllItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String name, String price, int image, int quantity, int position) {
            binding.textFoodName.setText(name);
            binding.textFoodAmount.setText(price);
            binding.imageCartFood.setImageResource(image);
            binding.textQty.setText(String.valueOf(quantity));

            binding.buttonMinus.setOnClickListener(view -> {
                if (itemQuantity[position] > 1){
                    itemQuantity[position]--;
                    binding.textQty.setText(String.valueOf(itemQuantity[position]));
                }
            });

binding.buttonPlus.setOnClickListener(view -> {
    if (itemQuantity[position] < 10) {
        itemQuantity[position]++;
        binding.textQty.setText(String.valueOf(itemQuantity[position]));
    }
});

binding.buttonDelete.setOnClickListener(view -> {
    removeItem(position);
});

        }
    }

    private void removeItem(int position) {
        itemNames.remove(position);
        itemPrices.remove(position);
        itemImages.remove(position);

        int[] newQuantities = new int[itemQuantity.length - 1];
        System.arraycopy(itemQuantity, 0, newQuantities, 0, position);
        System.arraycopy(itemQuantity, position + 1, newQuantities, position, itemQuantity.length - position - 1);
        itemQuantity = newQuantities;

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemNames.size());
    }
}
