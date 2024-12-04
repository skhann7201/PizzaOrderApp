package com.example.rupizzeria;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private  ShareResource shareResource = ShareResource.getInstance();

    private RecyclerView rvOrders, rvOrderItems;
    private View orderDetailsContainer;
    private TextView tvOrderId, tvSubtotal, tvTax, tvTotal;

    private Button btnCancelOrder;
    private ImageButton btnCloseDetails;

    private List<Order> ordersList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Initialize views
        rvOrders = findViewById(R.id.rv_orders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));

        // Initialize order details views
        orderDetailsContainer = findViewById(R.id.order_details_container);
        rvOrderItems = findViewById(R.id.rv_order_items);
        tvOrderId = findViewById(R.id.tv_order_id);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvTax = findViewById(R.id.tv_tax);
        tvTotal = findViewById(R.id.tv_total);
        btnCancelOrder = findViewById(R.id.btn_cancel_order);
        btnCloseDetails = findViewById(R.id.btn_close_details);

        // Handle back button
        ImageButton backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> navigateBackToHome());

        // Close Order Details
        btnCloseDetails.setOnClickListener(v -> hideOrderDetails());

        //Load data from ShareResource
        ordersList = shareResource.getOrdersList();

        OrderAdapter orderAdapter = new OrderAdapter(ordersList, this::showOrderDetails);
        rvOrders.setAdapter(orderAdapter);

    }

    /**
     * Navigates back to the MainActivity.
     */
    private void navigateBackToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /**
     * Hides the order details view.
     */
    private void hideOrderDetails() {
        orderDetailsContainer.setVisibility(View.GONE);
    }

    /**
     * Shows order details in the details container.
     */
    private void showOrderDetails(Order order) {
        // Log for debugging
        Log.d("OrderActivity", "Showing order details for Order ID: " + order.getOrderNumber());

        if (order.getPizzaList() == null || order.getPizzaList().isEmpty()) {
            Log.e("OrderActivity", "Pizza list is empty for this order.");
            return;
        }

        orderDetailsContainer.setVisibility(View.VISIBLE);

        // Populate order details
        tvOrderId.setText("Order ID: #" + order.getOrderNumber());
        tvSubtotal.setText(String.format("Subtotal: $%.2f", order.getSubtotal()));
        tvTax.setText(String.format("Tax: $%.2f", order.getSalesTax()));
        tvTotal.setText(String.format("Total: $%.2f", order.getTotalPrice()));

        // Set up items RecyclerView
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        OrderItemDetailsAdapter adapter = new OrderItemDetailsAdapter(order.getPizzaList());
        rvOrderItems.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        // Handle cancel order button
        btnCancelOrder.setOnClickListener(v -> {
            shareResource.cancelOrder(order);
            hideOrderDetails();
            rvOrders.getAdapter().notifyDataSetChanged();
        });
    }


}