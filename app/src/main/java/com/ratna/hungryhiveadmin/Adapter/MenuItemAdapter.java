package com.ratna.hungryhiveadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhiveadmin.Model.AllMenu;
import com.ratna.hungryhiveadmin.databinding.AllItemBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.AllItemViewHolder> {
    Context context;
    ArrayList<AllMenu> menuList;

    public MenuItemAdapter(List<String> itemNames, List<String> itemPrices, List<Integer> itemImages) {
        this.itemNames = new ArrayList<>(itemNames);
        this.itemPrices = new ArrayList<>(itemPrices);
        this.itemImages = new ArrayList<>(itemImages);
        this.itemQuantity = new int[itemNames.size()];
        Arrays.fill(itemQuantity, 1);
    }

    @NonNull
    @Override
    public MenuItemAdapter.AllItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllItemBinding binding = AllItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AllItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemAdapter.AllItemViewHolder holder, int position) {
String name = this.menuList.get(position);
        String price = this.menuList.get(position);
        int image = this.menuList.get(position);
        int quantity = this.menuList[position];
        holder.bind(name, price, image, quantity, position);
    }

    @Override
    public int getItemCount() {
        return menuList.size();
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
                if (menuList[position] > 1){
                    menuList[position]--;
                    binding.textQty.setText(String.valueOf(menuList[position]));
                }
            });

binding.buttonPlus.setOnClickListener(view -> {
    if (menuList[position] < 10) {
        menuList[position]++;
        binding.textQty.setText(String.valueOf(menuList[position]));
    }
});

binding.buttonDelete.setOnClickListener(view -> {
    removeItem(position);
});

        }
    }

    private void removeItem(int position) {
        menuList.remove(position);
        menuList.remove(position);
        menuList.remove(position);

        int[] newQuantities = new int[itemQuantity.length - 1];
        System.arraycopy(menuList, 0, newQuantities, 0, position);
        System.arraycopy(menuList, position + 1, newQuantities, position, menuList.length - position - 1);
        menuList = newQuantities;

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, menuList.size());
    }
}
