package com.example.rupizzeria;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton class to manage shared resources across activities.
 * This includes cart items, order lists, and associated data.
 * @author Vy Nguyen
 */
public class ShareResource {
    private static ShareResource instance; // reference the main home controller

    //Shared data
    private List<Pizza> cartItems;
    private List<Order> ordersList;
    private Map<Pizza, String> pizzaStyles;
    private int orderNumberCounter;
    private Order currentOrder;

    /**
     * Private constructor to prevent instantiation.
     */
    private ShareResource() {
        cartItems = new ArrayList<>();
        ordersList = new ArrayList<>();
        pizzaStyles = new HashMap<>();
        orderNumberCounter = 1;
        currentOrder = new Order(orderNumberCounter);
    }

    /**
     * Returns the single instance of ShareResource.
     * Synchronized to ensure thread safety.
     * @return The ShareResource instance.
     */
    public static synchronized ShareResource getInstance() {
        if (instance == null) {
            instance = new ShareResource();
        }
        return instance;
    }

    // --- Shared Data Management Methods ---
    /**
     * Returns the list of items in the cart.
     * @return A List of Pizza objects.
     */
    public List<Pizza> getCartItems() {
        return cartItems;
    }

    /**
     * Adds a Pizza to the cart and maps its style.
     * @param pizza The Pizza object to add.
     * @param style The style of the pizza (e.g., Chicago, NY).
     */
    public void addPizzaToCart(Pizza pizza, String style) {
        currentOrder.addPizza(pizza);
        cartItems.add(pizza);
        pizzaStyles.put(pizza, style);
    }

    /**
     * Removes a Pizza from the cart and updates its style mapping.
     * @param pizza The Pizza object to remove.
     */
    public void removePizzaFromCart(Pizza pizza) {
        currentOrder.removePizza(pizza);
        cartItems.remove(pizza);
    }

    /**
     * Returns the list of placed orders.
     * @return A List of Order objects.
     */
    public List<Order> getOrdersList() {
        return ordersList;
    }

    /**
     * Adds an order to the order list.
     * @param order The Order object to add.
     */
    public void addOrder(Order order) {
        ordersList.add(order);
    }

    /**
     * Cancels and removes an order from the order list.
     * @param order The Order object to remove.
     */
    public void cancelOrder(Order order) {
        ordersList.remove(order);
    }

    /**
     * Returns the current order being built.
     * @return The current Order object.
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Starts a new order by clearing the cart and incrementing the order number.
     */
    public void startNewOrder() {
        cartItems.clear();
        currentOrder = new Order(++orderNumberCounter);
    }

    /**
     * Returns the style of a specific pizza.
     * @param pizza The Pizza object.
     * @return The style of the pizza (e.g., Chicago, NY), or "Unknown Style" if not found.
     */
    public String getPizzaStyle(Pizza pizza) {
        return pizzaStyles.getOrDefault(pizza, "Unknown Style");
    }


    /**
     * Displays an alert dialog with the given title and message.
     *
     * @param context The context of the activity or application
     * @param title   The title of the dialog
     * @param message The message of the dialog
     */
    public void showAlertDialog(Context context, String title, String message) {
        new androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Formats a topping name for display.
     *
     * @param topping The topping to format
     * @return The formatted topping name
     */
    public String formatToppingName(Topping topping) {
        String name = topping.name().toLowerCase().replace('_', ' ');

        // Special case handling BBQ Chicken
        if (name.equalsIgnoreCase("bbq chicken")) {
            return "BBQ Chicken";
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

}
