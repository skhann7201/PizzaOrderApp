package com.example.rupizzeria;

/**
 * Chicago factory class, implements the PizzaFactory interface to produce different types of pizzas.
 * This class will create Chicago-style pizza with specific crust.
 * @author Vy Nguyen, Shahnaz Khan
 */
public class ChicagoPizza implements PizzaFactory{

    /**
     * Creates a Chicago-style Deluxe pizza with a deep dish crust
     * @return A new Deluxe pizza with a Chicago-style deep dish crust
     */
    @Override
    public Pizza createDeluxe() {
        return new Deluxe(Crust.DEEP_DISH);
    }

    /**
     * Creates a Chicago-style Meatzza pizza with a stuffed crust
     * @return A new Meatzza pizza with a Chicago-style stuffed crust
     */
    @Override
    public Pizza createMeatzza() {
        return new Meatzza(Crust.STUFFED);
    }

    /**
     * Creates a Chicago-style BQQChicken pizza with a pan crust
     * @return A new BBQChicken pizza with a Chicago-style pan crust
     */
    @Override
    public Pizza createBBQChicken() {
        return new BBQChicken(Crust.PAN);
    }

    /**
     * Creates a customizable Chicago-style pizza with a pan crust
     * @return A new BuildYourOwn pizza with a Chicago-style pan crust
     */
    @Override
    public Pizza createBuildYourOwn() {
        return new BuildYourOwn(Crust.PAN);
    }
}
