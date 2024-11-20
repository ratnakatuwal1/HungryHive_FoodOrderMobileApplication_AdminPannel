package com.ratna.hungryhiveadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ratna.hungryhiveadmin.databinding.PendingOrderItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {
    private final Context context;
    private final List<String> customerNames;
    private final List<String> textQuantities;
    private final List<String> itemImages;
    private final List<Boolean> isAccepted;
    private onItemClick itemClicked;

    public PendingOrderAdapter(Context context, List<String> customerNames, List<String> textQuantities, List<String> itemImages) {
        this.context = context;
        this.customerNames = new ArrayList<>(customerNames);
        this.textQuantities = new ArrayList<>(textQuantities);
        this.itemImages = new ArrayList<>(itemImages);
        this.isAccepted = new ArrayList<>();

        // Initialize the isAccepted list
        for (int i = 0; i < customerNames.size(); i++) {
            isAccepted.add(false); // Default: All orders are not accepted
        }
    }

    public void setItemClicked(onItemClick itemClicked) {
        this.itemClicked = itemClicked;
    }

    public interface onItemClick {
        void onItemClickListener(int position);
        void onItemAcceptClickListener(int position);
        void onItemDispatchClickListener(int position);
    }

    @NonNull
    @Override
    public PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PendingOrderItemBinding binding = PendingOrderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PendingOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderViewHolder holder, int position) {
        holder.bind(
                customerNames.get(position),
                textQuantities.get(position),
                itemImages.get(position),
                isAccepted.get(position),
                position
        );
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }

    public class PendingOrderViewHolder extends RecyclerView.ViewHolder {
        private final PendingOrderItemBinding binding;

        public PendingOrderViewHolder(@NonNull PendingOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String name, String quantity, String image, boolean accepted, int position) {
            // Bind data to the views
            binding.customerName.setText(name);
            binding.textQuantity.setText(quantity);

            Glide.with(binding.imageCartFood.getContext())
                    .load(image)
                    .into(binding.imageCartFood);

            // Set the button text based on the acceptance state
            binding.buttonPending.setText(accepted ? "Dispatch" : "Accept");

            // Button click logic for Accept and Dispatch
            binding.buttonPending.setOnClickListener(view -> {
                if (accepted) {
                    // Dispatch the order
                    if (itemClicked != null) {
                        itemClicked.onItemDispatchClickListener(position);
                    }

                    // Remove the item from all lists
                    customerNames.remove(position);
                    textQuantities.remove(position);
                    itemImages.remove(position);
                    isAccepted.remove(position);

                    // Notify adapter about the removal
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    Toast.makeText(context, "Order Dispatched and Removed", Toast.LENGTH_SHORT).show();
                } else {
                    // Accept the order
                    binding.buttonPending.setText("Dispatch");
                    isAccepted.set(position, true);

                    if (itemClicked != null) {
                        itemClicked.onItemAcceptClickListener(position);
                    }
                    Toast.makeText(context, "Order Accepted", Toast.LENGTH_SHORT).show();
                }
            });

            // Item click listener for viewing order details
            binding.getRoot().setOnClickListener(view -> {
                if (itemClicked != null) {
                    itemClicked.onItemClickListener(position);
                }
            });
        }
    }
}
