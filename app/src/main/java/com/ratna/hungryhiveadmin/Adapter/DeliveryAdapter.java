package com.ratna.hungryhiveadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ratna.hungryhiveadmin.OrderDispatch;
import com.ratna.hungryhiveadmin.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder> {
    private Context context;
    ArrayList<String> customerNames = new ArrayList<>();
    ArrayList<String> moneyStatuses = new ArrayList<>();

    private final Map<String, Integer> colorMap = Map.of(
            "received", android.R.color.holo_green_dark,
            "notReceived", android.R.color.holo_red_dark,
            "pending", android.R.color.darker_gray
    );

    public DeliveryAdapter(Context context, ArrayList<String> customerNames, ArrayList<String> moneyStatuses) {
        this.context = context;
        this.customerNames = customerNames;
        this.moneyStatuses = moneyStatuses;
    }


    @NonNull
    @Override
    public DeliveryAdapter.DeliveryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_dispatch, parent, false);
        return new DeliveryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryAdapter.DeliveryViewHolder holder, int position) {
        holder.textCustomerName.setText(customerNames.get(position));
        holder.textPayment.setText(moneyStatuses.get(position));
        holder.textDeliveryStatus.setText("Not Received");

        Integer colorResId = colorMap.getOrDefault(moneyStatuses.get(position), android.R.color.black);
        holder.textPayment.setTextColor(ContextCompat.getColor(context, colorResId));
        holder.statusCard.setCardBackgroundColor(ContextCompat.getColor(context, colorResId));
    }

    @Override
    public int getItemCount() {
        return customerNames.size();
    }

    public class DeliveryViewHolder extends RecyclerView.ViewHolder {
        TextView textCustomerName, textPayment, textDeliveryStatus;
        CardView statusCard;
        public DeliveryViewHolder(@NonNull View itemView) {
            super(itemView);
            textCustomerName = itemView.findViewById(R.id.customerName);
            textPayment = itemView.findViewById(R.id.textPayment);
            textDeliveryStatus = itemView.findViewById(R.id.textView4);
            statusCard = itemView.findViewById(R.id.status);
        }
    }
}
