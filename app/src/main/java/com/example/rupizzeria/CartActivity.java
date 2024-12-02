package com.example.rupizzeria;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * CartActivity manages the shopping cart screen. It displays a list of pizzas in the cart with their details (style, size, toppings, price).
 * The activity also calculates and shows the total cost, subtotal, tax, and number of items.
 * Users can remove pizzas from the cart and place an order, which clears the cart and initiates a new order.
 * @author Shahnaz Khan, Vy Nguyen
 */
public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCartItems;
    private TextView tvOrderId, tvTotalItems, tvSubtotal, tvTax, tvTotal;

    private ShareResource shareResource; //reference to the shared resource

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        shareResource = ShareResource.getInstance();

        // Initialize UI elements
        rvCartItems = findViewById(R.id.rv_cart_items);
        tvOrderId = findViewById(R.id.tv_order_id);
        tvTotalItems = findViewById(R.id.tv_total_items);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvTax = findViewById(R.id.tv_tax);
        tvTotal = findViewById(R.id.tv_total);

        setUpRecyclerView();

        updateOrderDetails();

        findViewById(R.id.btn_placeOrder).setOnClickListener(v -> placeOrder());

        ImageButton backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> navigateBackToHome());
    }

    /**
     * Method to set up RecyclerView and bind data.
     */
    private void setUpRecyclerView() {
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));

        // Set up adapter for the RecyclerView
        CartAdapter cartAdapter = new CartAdapter(shareResource.getCartItems(), this::onRemovePizza);
        rvCartItems.setAdapter(cartAdapter);
    }

    /**
     * Method to update the order details (total items, subtotal, tax, total).
     */
    private void updateOrderDetails() {
        // Get the current order details
        int totalItems = shareResource.getCartItems().size();
        double subtotal = 0;
        double tax = 0;

        // Calculate subtotal
        for (Pizza pizza : shareResource.getCartItems()) {
            subtotal += pizza.price();  // Make sure the price method is defined in Pizza
        }

        // Assuming tax rate is 8%
        tax = subtotal * 0.06625;

        double total = subtotal + tax;

        // Update the UI with the calculated values
        tvOrderId.setText("Order ID: " + shareResource.getCurrentOrder().getOrderNumber());
        tvTotalItems.setText("Total Items: " + totalItems);
        tvSubtotal.setText("Subtotal: $" + String.format("%.2f", subtotal));
        tvTax.setText("Sales Tax: $" + String.format("%.2f", tax));
        tvTotal.setText("Total: $" + String.format("%.2f", total));
    }

    /**
     * Method to handle removing a pizza from the cart.
     */
    private void onRemovePizza(Pizza pizza) {
        shareResource.removePizzaFromCart(pizza);
        updateOrderDetails();

        // Notify the adapter that the dataset has changed
        ((CartAdapter) rvCartItems.getAdapter()).notifyDataSetChanged();
    }

    /**
     * Place the order (e.g., move it to the orders list, clear cart).
     */
    private void placeOrder() {
        shareResource.addOrder(shareResource.getCurrentOrder());

        shareResource.startNewOrder();

        updateOrderDetails();

        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Navigates back to the MainActivity.
     */
    private void navigateBackToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Optional: Clears other activities
        startActivity(intent);
        finish(); // End current activity
    }
}