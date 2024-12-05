package com.example.rupizzeria;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/**
 * OrderActivity is responsible for displaying a list of orders, showing details of a selected order,
 * and handling actions like canceling an order or closing the order details.
 * @author Vy Nguyen, Shahnaz Khan
 */
public class OrderActivity extends AppCompatActivity {
    private ShareResource shareResource = ShareResource.getInstance();  // Shared resource instance for accessing order data

    private ListView lvOrders, lvOrderItems;  // ListView for orders and order items
    private View orderDetailsContainer;  // View to display order details
    private TextView tvOrderId, tvSubtotal, tvTax, tvTotal;  // TextViews for displaying order details

    private Button btnCancelOrder;  // Button to cancel the order
    private ImageButton btnCloseDetails;  // Button to close order details view

    private List<Order> ordersList;  // List to store orders

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Initialize views
        lvOrders = findViewById(R.id.lv_orders);

        // Initialize order details views
        orderDetailsContainer = findViewById(R.id.order_details_container);
        lvOrderItems = findViewById(R.id.lv_order_items);
        tvOrderId = findViewById(R.id.tv_order_id);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvTax = findViewById(R.id.tv_tax);
        tvTotal = findViewById(R.id.tv_total);
        btnCancelOrder = findViewById(R.id.btn_cancel_order);
        btnCloseDetails = findViewById(R.id.btn_close_details);

        ImageButton backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> navigateBackToHome());

        btnCloseDetails.setOnClickListener(v -> hideOrderDetails());

        ordersList = shareResource.getOrdersList();

        OrderDetailsAdapter orderAdapter = new OrderDetailsAdapter(this, ordersList, this::showOrderDetails);
        lvOrders.setAdapter(orderAdapter);
    }

    /**
     * Navigates back to the MainActivity.
     */
    private void navigateBackToHome() {
        Intent intent = new Intent(this, MainActivity.class);  // Create an intent to navigate to MainActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Clear activity stack to avoid going back to this activity
        startActivity(intent);  // Start the MainActivity
        finish();  // Finish the current activity
    }

    /**
     * Hides the order details view.
     */
    private void hideOrderDetails() {
        orderDetailsContainer.setVisibility(View.GONE);  // Hide the order details view container
    }

    /**
     * Shows order details in the details container.
     * @param order The order whose details are to be shown.
     */
    private void showOrderDetails(Order order) {
        Log.d("OrderActivity", "Showing order details for Order ID: " + order.getOrderNumber());

        if (order.getPizzaList() == null || order.getPizzaList().isEmpty()) {
            Log.e("OrderActivity", "Pizza list is empty for this order.");
            return;
        }

        orderDetailsContainer.setVisibility(View.VISIBLE);

        tvOrderId.setText("Order ID: #" + order.getOrderNumber());
        tvSubtotal.setText(String.format("Subtotal: $%.2f", order.getSubtotal()));
        tvTax.setText(String.format("Tax: $%.2f", order.getSalesTax()));
        tvTotal.setText(String.format("Total: $%.2f", order.getTotalPrice()));

        OrderDetailsAdapter adapter = new OrderDetailsAdapter(this, ordersList, this::showOrderDetails);
        lvOrderItems.setAdapter(adapter);

        btnCancelOrder.setOnClickListener(v -> {
            shareResource.cancelOrder(order);
            hideOrderDetails();
            ((OrderDetailsAdapter) lvOrders.getAdapter()).notifyDataSetChanged();
        });
    }
}