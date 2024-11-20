package com.ratna.hungryhiveadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.hungryhiveadmin.PendingOrder;
import com.ratna.hungryhiveadmin.databinding.PendingOrderItemBinding;

import java.util.ArrayList;
import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder> {
    private static Context context;
    private List<String> customerNames;
    private List<String> textQuantities;
    private List<String> itemImages;
    private onItemClick itemClicked;
    private static List<Boolean> isAccepted;

    private DatabaseReference dispatchedOrdersRef;



    public PendingOrderAdapter(Context context, List<String> customerNames, List<String> textQuantities, List<String> itemImages) {
        this.context = context;
        this.customerNames = new ArrayList<>(customerNames);
        this.textQuantities = new ArrayList<>(textQuantities);
        this.itemImages = new ArrayList<>(itemImages);
        this.isAccepted = new ArrayList<>(customerNames.size());

        dispatchedOrdersRef = FirebaseDatabase.getInstance().getReference("DispatchedOrders");
        for (int i = 0; i < customerNames.size(); i++) {
            isAccepted.add(false); // All orders start as "Not Accepted"
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
    public PendingOrderAdapter.PendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PendingOrderItemBinding binding = PendingOrderItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PendingOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrderAdapter.PendingOrderViewHolder holder, int position) {
        String name = this.customerNames.get(position);
        String quantity = this.textQuantities.get(position);
        String image = this.itemImages.get(position);
        boolean accepted = this.isAccepted.get(position);
        holder.bind(name, quantity, image, accepted, position);
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }

    public class PendingOrderViewHolder extends RecyclerView.ViewHolder {
        private PendingOrderItemBinding binding;

        public PendingOrderViewHolder(@NonNull PendingOrderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String name, String quantity, String image, boolean accepted, int position) {
            binding.customerName.setText(name);
            binding.textQuantity.setText(quantity);

            Glide.with(binding.imageCartFood.getContext())
                    .load(image)
                    .into(binding.imageCartFood);

            binding.buttonPending.setText(accepted ? "Dispatch" : "Accept");

            binding.buttonPending.setOnClickListener(view -> {
                if (accepted) {
                    if (itemClicked != null) {
                        itemClicked.onItemDispatchClickListener(position);
                    }

                    String dispatchKey = dispatchedOrdersRef.push().getKey();

                    if (dispatchKey != null) {
                        dispatchedOrdersRef.child(dispatchKey).child("customerName").setValue(name);
                        dispatchedOrdersRef.child(dispatchKey).child("quantity").setValue(quantity);
                        dispatchedOrdersRef.child(dispatchKey).child("image").setValue(image);
                    }

                    customerNames.remove(position);
                    textQuantities.remove(position);
                    itemImages.remove(position);
                    isAccepted.remove(position);

                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                    Toast.makeText(context, "Order Dispatched and Removed", Toast.LENGTH_SHORT).show();

                } else {
                    binding.buttonPending.setText("Dispatch");
                    isAccepted.set(position, true);

                    if (itemClicked != null) {
                        itemClicked.onItemAcceptClickListener(position);
                    }
                    Toast.makeText(context, "Order Accepted", Toast.LENGTH_SHORT).show();
                }
            });

            binding.getRoot().setOnClickListener(view -> {
                if (itemClicked != null) {
                    itemClicked.onItemClickListener(position);
                }
            });
        }
    }
}

