package com.example.rupizzeria;

import java.util.ArrayList;

/**
 * Order class contains unique order number and keeps the list of instances of Pizza class.
 * This class will generate a serial number for the order
 * with unique an integer whenever an order is created.
 * @author Vy Nguyen, Shahnaz Khan
 */
public class Order {
    private int number; //order number
    private ArrayList<Pizza> pizzas; // can use List<E> instead of ArrayList<E>

    /**
     * Default constructor for Order
     */
    public Order() {
        this.number = 0;
        this.pizzas = new ArrayList<>();
    }

    /**
     * Constructor for Order.
     * @param number Unique order number for the order.
     */
    public Order(int number) {
        this.number = number;
        this.pizzas = new ArrayList<>();
    }

    /**
     * Adds a pizza to the order
     * @param pizza The Pizza to add to the order.
     */
    public void addPizza(Pizza pizza) {
        pizzas.add(pizza);
    }

    /**
     * Remove a pizza from the order
     * @param pizza The Pizza to remove from the order.
     */
    public void removePizza(Pizza pizza) {
        pizzas.remove(pizza);
    }

    /**
     * Calculates the total amount of all pizzas in the order before tax.
     * @return The total amount before tax;
     */
    public double getSubtotal() {
        double total = 0;
        for(Pizza pizza: pizzas) {
            total += pizza.price();
        }
        return total;
    }

    /**
     * Calculate the sales tax based on the total amount before tax.
     * @return The sales tax amount of the order.
     */
    public double getSalesTax() {
        return getSubtotal() * Pizza.getNJSaleTax();
    }

    /**
     * Calculate the total price, including New Jersey sales tax
     * @return The total amount with tax.
     */
    public double getTotalPrice() {
        return getSubtotal() + getSalesTax();
    }

    /**
     * Gets the unique order number of order.
     * @return The order number.
     */
    public int getOrderNumber() {
        return number;
    }

    /**
     * Get the list of pizza in the order
     * @return list of pizzas.
     */
    public ArrayList<Pizza> getPizzaList() {
        return pizzas;
    }

    /**
     * Get the number of items in the order.
     * @return number of order in the order.
     */
    public int getOrderQuantity() {
        return getPizzaList().size();
    }

    /**
     * Provides a string representation of the order details
     * @return A string with order number, pizza details, total before tax, tax, and total after tax.
     */
    @Override
    public String toString() {
        StringBuilder orderDetails = new StringBuilder("Order #" + number + "\n");

        // Add details for each pizza in the order
        for(Pizza pizza: pizzas) {
            orderDetails.append(pizza.toString()).append("\n");
        }
        orderDetails.append("Total before tax: $").append(String.format("%.2f", getSubtotal())).append("\n");
        orderDetails.append("Sales tax: $").append(String.format("%.2f", getSalesTax())).append("\n");
        orderDetails.append("Total after tax: $").append(String.format("%.2f", getTotalPrice())).append("\n");

        return orderDetails.toString();
    }
}
