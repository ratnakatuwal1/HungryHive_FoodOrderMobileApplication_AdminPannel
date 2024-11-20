package com.ratna.hungryhiveadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ratna.hungryhiveadmin.R;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    private Context context;
    private List<String> foodNames;
    private List<String> foodQuantities;
    private List<String> foodPrices;
    private List<String> foodImages;

    public OrderDetailAdapter(Context context, List<String> foodNames, List<String> foodQuantities, List<String> foodPrices, List<String> foodImages) {
        this.context = context;
        this.foodNames = foodNames;
        this.foodQuantities = foodQuantities;
        this.foodPrices = foodPrices;
        this.foodImages = foodImages;
    }


    @NonNull
    @Override
    public OrderDetailAdapter.OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orderdetail_item, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.OrderDetailViewHolder holder, int position) {String name = foodNames.get(position);
        String quantity = foodQuantities.get(position);  // Ensure that food quantity is being passed correctly
        String price = foodPrices.get(position);
        String image = foodImages.get(position);
        holder.bind(name, quantity, price, image);

//        holder.bind(foodNames.get(position), foodQuantities.get(position), foodPrices.get(position), foodImages.get(position));
//        holder.foodName.setText(foodName.get(position));
//        holder.foodQuantity.setText("Quantity: " + foodQuantity.get(position));
//        holder.foodPrice.setText("Price: " + foodPrice.get(position));
//
//        Glide.with(context)
//                .load(foodImages.get(position))
//                .into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return foodNames.size();
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodQuantity, foodPrice;
        ImageView foodImage;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodName);
            foodQuantity = itemView.findViewById(R.id.foodQuantity);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            foodImage = itemView.findViewById(R.id.imageCartFood);
        }

        public void bind(String name, String quantity, String price, String imageUrl) {
            foodName.setText(name);
            foodQuantity.setText("Quantity: " + quantity);
            foodPrice.setText("Price: " + price);
            Glide.with(context).load(imageUrl).into(foodImage);
        }
    }
}
