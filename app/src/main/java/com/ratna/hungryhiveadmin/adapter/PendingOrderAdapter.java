package com.ratna.hungryhiveadmin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhiveadmin.databinding.PendingOrderItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {
    private List<String> customerNames;
    private List<String> textQuantities;
    private List<Integer> itemImages;

    public PendingOrderAdapter(List<String> customerNames, List<String> textQuantities, List<Integer> itemImages) {
        this.customerNames = new ArrayList<>(customerNames);
        this.textQuantities = new ArrayList<>(textQuantities);
        this.itemImages = new ArrayList<>(itemImages);
    }

    @NonNull
    @Override
    public PendingOrderAdapter.PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PendingOrderItemBinding binding = PendingOrderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PendingOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderAdapter.PendingOrderViewHolder holder, int position) {
        String name = this.customerNames.get(position);
        String quantity = this.textQuantities.get(position);
        int image = this.itemImages.get(position);
        holder.bind(name, quantity, image, position);
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }

    public static class PendingOrderViewHolder extends RecyclerView.ViewHolder {
        private PendingOrderItemBinding binding;

        public PendingOrderViewHolder(@NonNull PendingOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String name, String quantity, int image, int position) {
            binding.customerName.setText(name);
            binding.textQuantity.setText(quantity);
            binding.imageCartFood.setImageResource(image);
        }
    }
}
