package com.example.rupizzeria;
/**
 * Deluxe pizza subclass, a speciality pizza with present toppings
 * @author Vy Nguyen, Shahnaz Khan
 */
public class Deluxe extends Pizza {

    /**
     * Default constructor for Deluxe pizza
     */
    public Deluxe() {
        super();
        addTopping(Topping.SAUSAGE);
        addTopping(Topping.PEPPERONI);
        addTopping(Topping.GREEN_PEPPER);
        addTopping(Topping.ONION);
        addTopping(Topping.MUSHROOM);
    }

    /**
     * Constructor for Deluxe Pizza
     *
     * @param crust The crust type for the pizza
     */
    public Deluxe(Crust crust) {
        super(crust);
        addTopping(Topping.SAUSAGE);
        addTopping(Topping.PEPPERONI);
        addTopping(Topping.GREEN_PEPPER);
        addTopping(Topping.ONION);
        addTopping(Topping.MUSHROOM);
    }

    /**
     * Calculates the price of Deluxe pizza based on its size.
     * @return The price of the Deluxe pizza
     */
    @Override
    public double price() {
        switch(getSize()) {
            case SMALL:
                return 16.99;
            case MEDIUM:
                return 18.99;
            case LARGE:
                return 20.99;
            default: throw new IllegalStateException("Size not set");
        }
    }

}

