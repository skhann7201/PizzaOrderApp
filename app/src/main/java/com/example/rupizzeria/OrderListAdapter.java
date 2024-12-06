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
 * Adapter class for displaying orders in a ListView.
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

    @Override
    public int getCount() {
        return ordersList.size();
    }

    @Override
    public Object getItem(int position) {
        return ordersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

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

        tvOrderId.setText("Order ID: #" + order.getOrderNumber());
        tvOrderItems.setText("Items: " + order.getOrderQuantity());
        tvOrderTotal.setText(String.format("Total: $%.2f", order.getTotalPrice()));

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
