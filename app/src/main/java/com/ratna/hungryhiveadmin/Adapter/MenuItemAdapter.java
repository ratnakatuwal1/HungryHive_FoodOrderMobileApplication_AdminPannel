package com.ratna.hungryhiveadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ratna.hungryhiveadmin.Model.AllMenu;
import com.ratna.hungryhiveadmin.databinding.AllItemBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.AllItemViewHolder> {
    Context context;
    ArrayList<AllMenu> menuList;
    private int[] itemQuantity;

    public MenuItemAdapter(Context context, ArrayList<AllMenu> menuList) {
        this.context = context;
        this.menuList = menuList;
        this.itemQuantity = new int[menuList.size()];
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
        AllMenu menuItem = menuList.get(position);
        holder.bind(menuItem, itemQuantity[position], position);
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

        public void bind(AllMenu menuItem, int quantity, int position) {
            binding.textFoodName.setText(menuItem.getFoodName());
            binding.textFoodAmount.setText(menuItem.getFoodPrice());
//            binding.imageCartFood.setImageResource(Integer.parseInt(menuItem.getFoodImage()));
            binding.textQty.setText(String.valueOf(quantity));

            Glide.with(context)
                            .load(menuItem.getFoodImage())
                                    .into(binding.imageCartFood);


            binding.buttonMinus.setOnClickListener(view -> {
                if (itemQuantity[position] > 1) {
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
        menuList.remove(position);

        int[] newQuantities = new int[itemQuantity.length - 1];
        System.arraycopy(itemQuantity, 0, newQuantities, 0, position);
        System.arraycopy(itemQuantity, position + 1, newQuantities, position, itemQuantity.length - position - 1);
        itemQuantity = newQuantities;

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, menuList.size());
    }
}
