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
 * Adapter class for displaying each order item in a ListView.
 * This adapter uses a card layout (cardview_order_item) for displaying each order.
 * @author Vy Nguyen, Shahnaz Khan
 */
public class OrderListAdapter extends BaseAdapter {
    private final List<Order> ordersList;
    private final Context context;
    private final OnOrderClickListener onOrderClickListener;

    /**
     * Constructor for OrderListAdapter.
     *
     * @param context               The context of the calling activity.
     * @param ordersList            The list of orders to display.
     * @param onOrderClickListener  Listener for handling clicks on "View Order" buttons.
     */
    public OrderListAdapter(Context context, List<Order> ordersList, OnOrderClickListener onOrderClickListener) {
        this.context = context;
        this.ordersList = ordersList;
        this.onOrderClickListener = onOrderClickListener;
    }

    /**
     * Gets the number of orders in the list.
     *
     * @return The number of orders.
     */
    @Override
    public int getCount() {
        return ordersList.size();
    }

    /**
     * Gets the order at a specific position.
     *
     * @param position The position of the order in the list.
     * @return The order at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return ordersList.get(position);
    }

    /**
     * Gets the ID of the item at the specified position.
     * In this case, it returns the position itself as the ID.
     *
     * @param position The position of the item in the list.
     * @return The position of the item as its ID.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Creates and populates the view for an order in the ListView.
     *
     * @param position    The position of the order in the list.
     * @param convertView The recycled view to populate (if available).
     * @param parent      The parent ViewGroup that this view will be attached to.
     * @return The populated view for the order at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.cardview_order_item, parent, false);
        }

        Order order = ordersList.get(position);

        TextView tvOrderId = convertView.findViewById(R.id.tv_order_id);
        TextView tvOrderItems = convertView.findViewById(R.id.tv_order_items);
        TextView tvOrderTotal = convertView.findViewById(R.id.tv_order_total);
        Button btnViewOrder = convertView.findViewById(R.id.btn_view_order);

        // Use string resources to set text
        tvOrderId.setText(context.getString(R.string.order_id, order.getOrderNumber()));
        tvOrderItems.setText(context.getString(R.string.order_quantity, order.getOrderQuantity()));
        tvOrderTotal.setText(context.getString(R.string.total_price, order.getTotalPrice()));
        btnViewOrder.setOnClickListener(v -> onOrderClickListener.onViewOrder(order));

        return convertView;
    }

    /**
     * Interface for handling "View Order" button clicks.
     */
    public interface OnOrderClickListener {
        void onViewOrder(Order order);
    }
}
