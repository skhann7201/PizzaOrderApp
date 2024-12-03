package com.example.rupizzeria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private final List<Order> orders;
    private final OrderClickListener orderClickListener;

    public interface OrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderAdapter(List<Order> orders, OrderClickListener orderClickListener) {
        this.orders = orders;
        this.orderClickListener = orderClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.tvOrderId.setText("Order ID: #" + order.getOrderNumber());
        holder.tvOrderItems.setText("Items: " + order.getOrderQuantity());
        holder.tvOrderTotal.setText("Total: $" + String.format("%.2f", order.getTotalPrice()));

        holder.btnViewOrder.setOnClickListener(v -> orderClickListener.onOrderClick(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderItems, tvOrderTotal;
        Button btnViewOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvOrderItems = itemView.findViewById(R.id.tv_order_items);
            tvOrderTotal = itemView.findViewById(R.id.tv_order_total);
            btnViewOrder = itemView.findViewById(R.id.btn_view_order);
        }
    }
}
