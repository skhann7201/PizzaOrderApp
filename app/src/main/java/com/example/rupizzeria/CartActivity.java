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

import java.util.List;


public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCartItems;
    private CartItemAdapter cartItemAdapter;
    private TextView tvOrderId, tvTotalItems, tvSubtotal, tvTax, tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize UI elements
        rvCartItems = findViewById(R.id.rv_cart_items);
        tvOrderId = findViewById(R.id.tv_order_id);
        tvTotalItems = findViewById(R.id.tv_total_items);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvTax = findViewById(R.id.tv_tax);
        tvTotal = findViewById(R.id.tv_total);

        setUpRecyclerView();
        updateOrderDetails();

        // Place order button logic
        findViewById(R.id.btn_placeOrder).setOnClickListener(v -> placeOrder());

        // Initialize back button
        ImageButton backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> navigateBackToHome());
    }
    // implement this method
    private void setUpRecyclerView() {
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        List<Pizza> cartItems = ShareResource.getInstance().getCartItems();
        cartItemAdapter = new CartItemAdapter(cartItems);
        rvCartItems.setAdapter(cartItemAdapter);
    }

    private void updateOrderDetails() {

    }

    private void placeOrder() {
        // Logic for placing the order (e.g., move to order list, clear cart)

        updateOrderDetails(); // Refresh details
    }


    /**
     * Navigates back to the MainActivity.
     */
    private void navigateBackToHome(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Optional: Clears other activities
        startActivity(intent);
        finish(); // End current activity
    }
}