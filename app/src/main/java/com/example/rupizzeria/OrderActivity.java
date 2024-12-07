package com.example.rupizzeria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * The OrderActivity class manages the display and interaction with a list of customer orders.
 * It includes functionality to view the details of an order and cancel an order.
 * This activity uses a ListView to show a list of orders and a RecyclerView to display
 * order items within the selected order's details.
 *
 * Author: Vy Nguyen, Shahnaz Khan
 */
public class OrderActivity extends AppCompatActivity {
    // UI Components
    private ListView lvOrders;
    private RecyclerView rvOrderItems;
    private View orderDetailsContainer;
    private TextView tvOrderId, tvSubtotal, tvTax, tvTotal;
    private Button btnCancelOrder;
    private ImageButton btnCloseDetails, backButton;

    // Data
    private  ShareResource shareResource = ShareResource.getInstance();
    private List<Order> ordersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        findID();
        backButton.setOnClickListener(v -> navigateBackToHome());
        btnCloseDetails.setOnClickListener(v -> hideOrderDetails());
        ordersList = shareResource.getOrdersList();

        OrderListAdapter orderAdapter = new OrderListAdapter(this, ordersList, this::showOrderDetails);
        lvOrders.setAdapter(orderAdapter);
    }

    /**
     * Finds and initializes all UI components by their IDs from the layout.
     */
    private void findID(){
        lvOrders = findViewById(R.id.lv_orders);
        rvOrderItems = findViewById(R.id.rv_order_items);
        orderDetailsContainer = findViewById(R.id.order_details_container);
        tvOrderId = findViewById(R.id.tv_order_id);
        tvSubtotal = findViewById(R.id.tv_subtotal);
        tvTax = findViewById(R.id.tv_tax);
        tvTotal = findViewById(R.id.tv_total);
        btnCancelOrder = findViewById(R.id.btn_cancel_order);
        btnCloseDetails = findViewById(R.id.btn_close_details);
        backButton = findViewById(R.id.btn_back);
    }


    /**
     * Hides the order details view.
     */
    private void hideOrderDetails() {
        orderDetailsContainer.setVisibility(View.GONE);
    }

    /**
     * Shows order details in the details container.
     * @param order The selected order to display details for.
     */
    private void showOrderDetails(Order order) {
        orderDetailsContainer.setVisibility(View.VISIBLE);

        tvOrderId.setText(getString(R.string.order_id, order.getOrderNumber()));
        tvSubtotal.setText(getString(R.string.subtotal, order.getSubtotal()));
        tvTax.setText(getString(R.string.sale_tax, order.getSalesTax()));
        tvTotal.setText(getString(R.string.total_price, order.getTotalPrice()));

        // set up Recycler RecyclerView for order items
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        OrderItemDetailsAdapter adapter = new OrderItemDetailsAdapter(order.getPizzaList());
        rvOrderItems.setAdapter(adapter);

        // handle cancel order button
        btnCancelOrder.setOnClickListener(v -> {
            shareResource.cancelOrder(order);
            hideOrderDetails();
            ((OrderListAdapter) lvOrders.getAdapter()).notifyDataSetChanged();
        });
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
}