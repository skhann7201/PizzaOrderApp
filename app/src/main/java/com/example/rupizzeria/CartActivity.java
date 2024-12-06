package com.example.rupizzeria;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * CartActivity manages the shopping cart screen. It displays a list of pizzas in the cart with their details (style, size, toppings, price).
 * The activity also calculates and shows the total cost, subtotal, tax, and number of items.
 * Users can remove pizzas from the cart and place an order, which clears the cart and initiates a new order.
 * @author Shahnaz Khan, Vy Nguyen
 */
public class CartActivity extends AppCompatActivity {
    private RecyclerView rvCartItems;
    private TextView tvOrderId, tvTotalItems, tvSubtotal, tvTax, tvTotal;
    private ImageButton backButton;
    private Button placeOrderButton;

    private ShareResource shareResource; //reference to the shared resource

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        shareResource = ShareResource.getInstance();

        findID();

        setUpRecyclerView();

        updateOrderDetails();

        findViewById(R.id.btn_placeOrder).setOnClickListener(v -> placeOrder());

        backButton.setOnClickListener(v -> navigateBackToHome());
    }

    private void findID(){
        // Initialize UI elements
        rvCartItems = findViewById(R.id.rv_cart_items);
        tvOrderId = findViewById(R.id.tv_order_id);
        tvTotalItems = findViewById(R.id.tv_total_items);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvTax = findViewById(R.id.tv_tax);
        tvTotal = findViewById(R.id.tv_total);
        backButton = findViewById(R.id.btn_back);
        placeOrderButton = findViewById(R.id.btn_placeOrder);
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
        Order currentOrder = shareResource.getCurrentOrder();

        int orderID = currentOrder.getOrderNumber();
        int orderQuantity = currentOrder.getOrderQuantity();
        double subtotal = currentOrder.getSubtotal();
        double salesTax = currentOrder.getSalesTax();
        double totalPrice = currentOrder.getTotalPrice();

        // Update UI components with the order details
        tvOrderId.setText(getString(R.string.order_id, orderID)); // Order ID
        tvTotalItems.setText(getString(R.string.order_quantity, orderQuantity)); // Total items
        tvSubtotal.setText(getString(R.string.subtotal, subtotal)); // Subtotal
        tvTax.setText(getString(R.string.sale_tax, salesTax)); // Sales tax
        tvTotal.setText(getString(R.string.total_price, totalPrice)); // Total price
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

        // check if the cart is empty
        if (shareResource.getCartItems().isEmpty()) {
            shareResource.showAlertDialog(this, "Empty Cart", "Your cart is empty. Please add items to place an order.");
            return;
        }

        Order currentOrder = shareResource.getCurrentOrder();
        shareResource.addOrder(currentOrder);

        // start new order
        shareResource.startNewOrder();

        // update order details
        updateOrderDetails();

        //show order activity right away
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