package com.example.rupizzeria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Custom adapter for displaying order details in a ListView.
 * Binds each order's details to a view and handles the view click for order details.
 * @author Shahnaz Khan, Vy Nguyen
 */
public class OrderDetailsAdapter extends BaseAdapter {
    private final List<Order> orders;  // List of orders to display
    private final LayoutInflater inflater;  // LayoutInflater to create views
    private final OrderClickListener orderClickListener;  // Listener to handle order click events

    /**
     * Interface to define a listener for order click events.
     * This interface is implemented in the activity to handle order click events.
     */
    public interface OrderClickListener {
        /**
         * This method is triggered when an order is clicked.
         * @param order The order that was clicked.
         */
        void onOrderClick(Order order);
    }

    /**
     * Constructor for initializing the adapter with context, list of orders, and click listener.
     *
     * @param context The context where the adapter is being used (usually an Activity or Fragment).
     * @param orders The list of orders to display in the ListView.
     * @param orderClickListener The listener to handle click events on orders.
     */
    public OrderDetailsAdapter(Context context, List<Order> orders, OrderClickListener orderClickListener) {
        this.orders = orders;
        this.orderClickListener = orderClickListener;
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * Returns the number of orders in the list.
     * @return The number of orders to display.
     */
    @Override
    public int getCount() {
        return orders.size();
    }

    /**
     * Returns the order at the specified position in the list.
     * @param position The position of the item within the adapter's data set.
     * @return The order object at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    /**
     * Returns the item ID for the specified position (used for identifying items in the ListView).
     * @param position The position of the item within the adapter's data set.
     * @return The item ID for the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Creates and populates the view for each order item.
     * If the view is not recycled, it creates a new one and binds order data to the view.
     * @param position The position of the item within the adapter's data set.
     * @param convertView The recycled view to reuse, or null if no view is available.
     * @param parent The parent view that this view will be attached to.
     * @return A view representing the order at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate a new view if convertView is null
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cardview_order_item, parent, false);
        }

        // Get the order object at the given position
        Order order = orders.get(position);

        // Bind the order data to the view
        TextView tvOrderId = convertView.findViewById(R.id.tv_order_id);
        TextView tvOrderItems = convertView.findViewById(R.id.tv_order_items);
        TextView tvOrderTotal = convertView.findViewById(R.id.tv_order_total);
        Button btnViewOrder = convertView.findViewById(R.id.btn_view_order);

        // Set the data for the views
        tvOrderId.setText("Order ID: #" + order.getOrderNumber());
        tvOrderItems.setText("Items: " + order.getOrderQuantity());
        tvOrderTotal.setText("Total: $" + String.format("%.2f", order.getTotalPrice()));

        // Set click listener for the button
        btnViewOrder.setOnClickListener(v -> orderClickListener.onOrderClick(order));

        return convertView;
    }
}