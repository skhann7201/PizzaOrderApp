package com.example.rupizzeria;
/**
 * BBQChicken pizza subclass, a speciality pizza with present toppings
 * @author Vy Nguyen, Shahnaz Khan
 */
public class BBQChicken extends Pizza {

    /**
     * Default constructor for BBQChicken
     */
    public BBQChicken(){
        super();
        addTopping(Topping.BBQ_CHICKEN);
        addTopping(Topping.GREEN_PEPPER);
        addTopping(Topping.PROVOLONE);
        addTopping(Topping.CHEDDAR);
    }

    /**
     * Constructor for BBQChicken Pizza
     *
     * @param crust The crust type for the pizza
     */
    public BBQChicken(Crust crust) {
        super(crust);
        addTopping(Topping.BBQ_CHICKEN);
        addTopping(Topping.GREEN_PEPPER);
        addTopping(Topping.PROVOLONE);
        addTopping(Topping.CHEDDAR);
    }

    /**
     * Calculates the price of BBQChicken pizza based on its size.
     * @return The price of the BBQChicken pizza
     */
    @Override
    public double price() {
        switch(getSize()) {
            case SMALL:
                return 14.99;
            case MEDIUM:
                return 16.99;
            case LARGE:
                return 19.99;
            default: throw new IllegalStateException("Size not set");
        }
    }
}
