package com.example.rupizzeria;
/**
 * Meatzza pizza subclass, a speciality pizza with present toppings
 * @author Vy Nguyen, Shahnaz Khan
 */
public class Meatzza extends Pizza{

    /**
     * Default constructor for Meatzza Pizza
     */
    public Meatzza(){
        super();
        addTopping(Topping.SAUSAGE);
        addTopping(Topping.PEPPERONI);
        addTopping(Topping.BEEF);
        addTopping(Topping.HAM);
    }

    /**
     * Constructor for Meatzza Pizza
     *
     * @param crust The crust type for the pizza
     */
    public Meatzza(Crust crust) {
        super(crust);
        addTopping(Topping.SAUSAGE);
        addTopping(Topping.PEPPERONI);
        addTopping(Topping.BEEF);
        addTopping(Topping.HAM);
    }

    /**
     * Calculates the price of Meatzza pizza based on its size.
     * @return The price of the Meatzza pizza
     */
    @Override
    public double price() {
        switch(getSize()) {
            case SMALL:
                return 17.99;
            case MEDIUM:
                return 19.99;
            case LARGE:
                return 21.99;
            default: throw new IllegalStateException("Size not set");
        }
    }
}
