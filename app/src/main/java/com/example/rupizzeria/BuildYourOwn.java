package com.example.rupizzeria;

import java.text.DecimalFormat;

/**
 * BuildYourOwn pizza subclass, allows customization of toppings
 * @author Vy Nguyen, Shahnaz Khan
 */
public class BuildYourOwn extends Pizza {

    /**
     * Default constructor for BuildYourOwn pizza.
     */
    public BuildYourOwn() {
        super();
    }

    /**
     * Constructor for BuildYourOwn pizza.
     * @param crust The crust type for the customizable pizza
     */
    public BuildYourOwn(Crust crust) {
        super(crust);
    }

    /**
     * Adds a topping to the pizza, enforcing a maximum of 7 toppings;
     * @param topping The topping to add.
     * @throws IllegalStateException if attempt to add more than 7 toppings.
     */
    @Override
    public void addTopping(Topping topping){
        if(getToppings().size() <= 7) {
            super.addTopping(topping);
        } else {
            throw new IllegalStateException("Cannot add more than 7 toppings to Build Your Own pizza.");
        }
    }

    /**
     * Calculates the price of BuildYourOwn pizza based on its size and number of toppings
     * @return The price of the BuildYourOwn pizza.
     */
    @Override
    public double price() {
        double basePrice;
        double totalPrice;
        final double PRICE_PER_TOPPING = 1.69;
        switch(getSize()) {
            case SMALL: basePrice = 8.99;
                break;
            case MEDIUM: basePrice = 10.99;
                break;
            case LARGE: basePrice = 12.99;
                break;
            default: throw new IllegalStateException("Size not set");
        }
        totalPrice = basePrice + (getToppings().size() * PRICE_PER_TOPPING);
        // Format to two decimal places
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.parseDouble(df.format(totalPrice));
    }
}
