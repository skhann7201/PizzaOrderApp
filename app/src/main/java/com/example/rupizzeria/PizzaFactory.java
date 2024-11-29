package com.example.rupizzeria;

/**
 * Interface for creating different types of pizzas.
 * Implementations define methods for creating specific pizza types.
 * @author Vy Nguyen
 */
public interface PizzaFactory {

    /**
     * Creates a Deluxe Pizza.
     * @return A new Deluxe Pizza instance.
     */
    Pizza createDeluxe();

    /**
     * Creates a Meatzza Pizza.
     * @return A new Meatzza Pizza instance.
     */
    Pizza createMeatzza();

    /**
     * Creates a BBQ Chicken Pizza.
     * @return A new BBQ Chicken Pizza instance.
     */
    Pizza createBBQChicken();

    /**
     * Creates a Build Your Own Pizza.
     * @return A new Build Your Own Pizza instance.
     */
    Pizza createBuildYourOwn();
}
