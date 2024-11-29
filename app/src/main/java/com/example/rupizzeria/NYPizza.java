package com.example.rupizzeria;

/**
 * NYPizza factory class, implements the PizzaFactory interface to produce different types of pizzas.
 * This class will create NY-style pizza with specific crust.
 * @author Vy Nguyen, Shahnaz Khan
 */
public class NYPizza implements PizzaFactory {

    /**
     * Creates a NY-style Deluxe pizza with a Brooklyn crust
     * @return A new Deluxe pizza with a NY-style Brooklyn crust
     */
    @Override
    public Pizza createDeluxe() {
        return new Deluxe(Crust.BROOKLYN);
    }

    /**
     * Creates a NY-style Deluxe pizza with a hand-tossed crust
     * @return A new Meatzza pizza with a NY-style hand-tossed crust
     */
    @Override
    public Pizza createMeatzza() {
        return new Meatzza(Crust.HAND_TOSSED);
    }

    /**
     * Creates a NY-style BBQChicken pizza with a thin crust
     * @return A new BBQChicken pizza with a NY-style thin crust
     */
    @Override
    public Pizza createBBQChicken() {
        return new BBQChicken(Crust.THIN);
    }

    /**
     * Creates a customizable NY-style  pizza with a hand-tossed crust
     * @return A new BuildYourOwn pizza with a NY-style hand-tossed crust
     */
    @Override
    public Pizza createBuildYourOwn() {
        return new BuildYourOwn(Crust.HAND_TOSSED);
    }
}
